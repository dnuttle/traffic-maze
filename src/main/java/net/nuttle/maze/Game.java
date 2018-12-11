package net.nuttle.maze;

import static net.nuttle.maze.Orientation.HORIZONTAL;
import static net.nuttle.maze.Orientation.VERTICAL;
import static net.nuttle.maze.Direction.E;
import static net.nuttle.maze.Direction.W;
import static net.nuttle.maze.Direction.S;
import static net.nuttle.maze.Direction.N;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.nuttle.maze.GameTree.Node;

public class Game {
  
  private static final Logger LOG = LoggerFactory.getLogger(Game.class);
  
  private Set<Board> boards;
  private Board start;
  private GameTree tree;
  
  public static void main(String[] args) {
    Game g = new Game(1);
    //g.run();
  }
  
  public Game(int challengeId) {
    boards = new HashSet<Board>();
    start = new Board();
    loadAndSetup(start, challengeId);
    start.finalize();
    boards.add(start);
    Mover m = new Mover(start);
    /*
    for (int i = 0; i < start.getPieces().size(); i++) {
      LOG.debug(start.getPieces().get(i).toString());
      LOG.debug("Open N: " + start.isSpaceOpen(start.getPieces().get(i), Neighbors.N));
      LOG.debug("Open S: " + start.isSpaceOpen(start.getPieces().get(i), Neighbors.S));
      LOG.debug("Open E: " + start.isSpaceOpen(start.getPieces().get(i), Neighbors.E));
      LOG.debug("Open W: " + start.isSpaceOpen(start.getPieces().get(i), Neighbors.W));
    }
    */
  }
  
  public void run() {
    
    tree = new GameTree(start);
    Node root = tree.getRoot();
    Result r = makeMove(root);
    LOG.debug("Solved: " + r.isSolved());
    Node n = r.getNode();
    while (n != null) {
      LOG.debug("Found node with {} children", n.getChildNodes().size());
      if (n.getChildNodes().size() == 0) {
        return;
      }
      for (Node child : n.getChildNodes()) {
        n = child;
      }
    }
  }
  
  public Result makeMove(Node node) {
    Board newBoard = findMove(node.getBoard());
    if (newBoard == null) {
      return new Result(node, false);
    }
    Node child = node.addChild(newBoard);
    boards.add(newBoard);
    if (newBoard.isSolved()) {
      return new Result(child, true);
    }
    LOG.debug("Boards: " + boards.size());
    return makeMove(child);
  }
  
  private void debug(Node root) {
    Board newBoard = findMove(start);
    boards.add(newBoard);
    Node childNode = root.addChild(newBoard);
    newBoard = findMove(newBoard);
    childNode = childNode.addChild(newBoard);
    LOG.debug("Pieces in root: " + childNode.getParent().getParent().getBoard().getPieces().size());
    LOG.debug("equal: " + (root.getBoard() == childNode.getParent().getParent().getBoard()));
    LOG.debug("done");
    
  }
  
  private Board checkBoard(Board b, Vehicle piece, Direction dir, int spacesMoved) {
    if (piece.getOrientation() == VERTICAL && (dir == E || dir == W)) return null;
    if (piece.getOrientation() == HORIZONTAL && (dir == N || dir ==S)) return null;
    Board clone = b.cloneBoard();
    Vehicle pieceToMove = clone.getPieceById(piece.getId());
    pieceToMove.move(dir,  spacesMoved);
    clone.finalize();
    if (isBoardExists(clone)) {
      return null;
    }
    LOG.debug("Moving {} {} spaces {}, pos now {}", pieceToMove.getName(), spacesMoved, dir, pieceToMove.getPosition());
    return clone;
  }
  
  private boolean isBoardExists(Board b) {
    for (Board curr : boards) {
      if (Board.isBoardSame(b, curr)) {
        return true;
      }
    }
    return false;
  }
  
  /*
   * Algorithm: Go through pieces in order, try to move N, E, S or W in that order, 
   * 1 space or 2 or 3 etc. until a new move is found.
   */
  private Board findMove(Board b) {
    Board newBoard = null;
    Map<Vehicle, PossibleMove> possibleMoves = new HashMap<>(12);
    for (Vehicle piece : b.getPieces()) {
      Orientation orient = piece.getOrientation();
      PossibleMove pm = new PossibleMove();
      possibleMoves.put(piece, pm);
      if (orient == HORIZONTAL) {
        int validMoves = 0;
        while (b.isSpaceOpen(piece, Direction.E, validMoves)) {
          validMoves++;
        }
        pm.setRight(validMoves);
        validMoves = 0;
        while (b.isSpaceOpen(piece, Direction.W, -validMoves)) {
          validMoves++;
        }
        pm.setLeft(validMoves);
      } else {
        int validMoves = 0;
        while (b.isSpaceOpen(piece, Direction.N, -validMoves)) {
          validMoves++;
        }
        pm.setUp(validMoves);
        validMoves = 0;
        while (b.isSpaceOpen(piece, Direction.S, validMoves)) {
          validMoves++;
        }
        pm.setDown(validMoves);
      }
    }
    for (Vehicle piece : b.getPieces()) {
      for (Vehicle piece2 : b.getPieces()) {
        if (piece.getId() == piece2.getId()) continue;
        PossibleMove pm = possibleMoves.get(piece);
        PossibleMove pm2 = possibleMoves.get(piece2);
        for (int i = 0; i <= pm.getUp(); i++) {
          if ((newBoard = checkBoard(b, piece, Direction.N, i)) != null) {
            return newBoard;
          }
          for (int j = 0; j <= pm2.getUp(); j++) {
            if ((newBoard = checkBoard(b, piece2, Direction.N, j)) != null) {
              return newBoard;
            }
          }
          for (int j = 0; j <= pm2.getRight(); j++) {
            if ((newBoard = checkBoard(b, piece2, Direction.E, j)) != null) {
              return newBoard;
            }
          }
          for (int j = 0; j <= pm2.getDown(); j++) {
            if ((newBoard = checkBoard(b, piece2, Direction.S, j)) != null) {
              return newBoard;
            }
          }
          for (int j = 0; j <= pm2.getLeft(); j++) {
            if ((newBoard = checkBoard(b, piece2, Direction.W, j)) != null) {
              return newBoard;
            }
          }
            
        }
        for (int i = 0; i <= pm.getRight(); i++) {
          if ((newBoard = checkBoard(b, piece, Direction.E, i)) != null) {
            return newBoard;
          }
          for (int j = 0; j <= pm2.getUp(); j++) {
            if ((newBoard = checkBoard(b, piece2, Direction.N, j)) != null) {
              return newBoard;
            }
          }
          for (int j = 0; j <= pm2.getRight(); j++) {
            if ((newBoard = checkBoard(b, piece2, Direction.E, j)) != null) {
              return newBoard;
            }
          }
          for (int j = 0; j <= pm2.getDown(); j++) {
            if ((newBoard = checkBoard(b, piece2, Direction.S, j)) != null) {
              return newBoard;
            }
          }
          for (int j = 0; j <= pm2.getLeft(); j++) {
            if ((newBoard = checkBoard(b, piece2, Direction.W, j)) != null) {
              return newBoard;
            }
          }
        }
        for (int i = 0; i <= pm.getDown(); i++) {
          if ((newBoard = checkBoard(b, piece, Direction.S, i)) != null) {
            return newBoard;
          }
          for (int j = 0; j <= pm2.getUp(); j++) {
            if ((newBoard = checkBoard(b, piece2, Direction.N, j)) != null) {
              return newBoard;
            }
          }
          for (int j = 0; j <= pm2.getRight(); j++) {
            if ((newBoard = checkBoard(b, piece2, Direction.E, j)) != null) {
              return newBoard;
            }
          }
          for (int j = 0; j <= pm2.getDown(); j++) {
            if ((newBoard = checkBoard(b, piece2, Direction.S, j)) != null) {
              return newBoard;
            }
          }
          for (int j = 0; j <= pm2.getLeft(); j++) {
            if ((newBoard = checkBoard(b, piece2, Direction.W, j)) != null) {
              return newBoard;
            }
          }
        }
        for (int i = 0; i <= pm.getLeft(); i++) {
          if ((newBoard = checkBoard(b, piece, Direction.W, i)) != null) {
            return newBoard;
          }
          for (int j = 0; j <= pm2.getUp(); j++) {
            if ((newBoard = checkBoard(b, piece2, Direction.N, j)) != null) {
              return newBoard;
            }
          }
          for (int j = 0; j <= pm2.getRight(); j++) {
            if ((newBoard = checkBoard(b, piece2, Direction.E, j)) != null) {
              return newBoard;
            }
          }
          for (int j = 0; j <= pm2.getDown(); j++) {
            if ((newBoard = checkBoard(b, piece2, Direction.S, j)) != null) {
              return newBoard;
            }
          }
          for (int j = 0; j <= pm2.getLeft(); j++) {
            if ((newBoard = checkBoard(b, piece2, Direction.W, j)) != null) {
              return newBoard;
            }
          }
        }
      }
    }
    return null;
  }
  
  private void loadAndSetup(Board b, int challengeId) {
    ObjectMapper mapper = new ObjectMapper();
    boolean found = false;
    try {
      ArrayNode root = (ArrayNode) mapper.readTree(new File(System.getProperty("user.dir") + "/src/main/resources/setups.json"));
      LOG.debug("Setups found: " + root.size());
      for (JsonNode node : root) {
        if (node.get("id").asInt() == challengeId) {
          found = true;
          ArrayNode pieces = (ArrayNode) node.get("pieces");
          LOG.debug("Found {} pieces", pieces.size());
          for (JsonNode piece : pieces) {
            Vehicle v = Vehicles.valueOf(Vehicles.class, piece.get("type").asText()).get();
            Orientation o = Orientation.valueOf(piece.get("orientation").asText());
            v.setOrientation(o);
            v.getPosition().setRow(piece.get("row").asInt());
            v.getPosition().setCol(piece.get("col").asInt());
            b.getPieces().add(v);
          }
          b.sortPieces();
          for (Vehicle piece : b.getPieces()) {
            LOG.debug(piece.getType() + ", " + piece.getOrientation() + ", " + piece.getPosition());
          }
        }
      }
    } catch (IOException e) {
      //TODO
      LOG.error("Failure", e);
    }
    if (!found) {
      throw new IllegalArgumentException("Challenge " + challengeId + " not found");
    }
  }
  
}
