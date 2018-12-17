package net.nuttle.maze;

import static net.nuttle.maze.Direction.E;
import static net.nuttle.maze.Direction.N;
import static net.nuttle.maze.Direction.S;
import static net.nuttle.maze.Direction.W;
import static net.nuttle.maze.Orientation.HORIZONTAL;
import static net.nuttle.maze.Orientation.VERTICAL;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.nuttle.maze.GameTree.Node;

public class Mover {

  private static final Logger LOG = LoggerFactory.getLogger(Mover.class);
  List<Node> winners;
  List<List<Board>> winningBoards;
  List<Board> boards;
  List<Direction> directions;
  GameTree tree;
  
  public Mover(Board start) {
    directions = new ArrayList<>(4);
    directions.add(N);
    directions.add(E);
    directions.add(S);
    directions.add(W);
    winners = new ArrayList<>();
    winningBoards = new ArrayList<>();
    boards = new ArrayList<>();
    boards.add(start);
    tree = new GameTree(start);
    LOG.info("Starting board");
    LOG.info(start.paint());
  }
  
  public void run() {
    makeMove(tree.getRoot());
    boolean first = true;
    List<Board> winner = winningBoards.get(0);
    int step = 1;
    for (Board b : winner) {
      LOG.info(step + ". " + b.getMove() + b.paint());
    }
    /*
    for (Node winner : winners) {
      int steps = 0;
      Node curr = winner;
      if (first) {
        LOG.info(tree.getRoot().getBoard().paint());
      }
      while (curr != null) {
        steps++;
        if (first && curr.getBoard().getMove() != null) {
          LOG.info(steps + ". " + curr.getBoard().getMove().toString()
              + curr.getBoard().paint());
        }
        curr = curr.getParent();
      }
      LOG.info("{} steps", steps);
      first = false;
    }
    LOG.debug("Found {} winners", winners.size());
    */
  }
  
  private void addWinner(Node leaf) {
    List<Board> sequence = new ArrayList<>();
    Node curr = leaf;
    while ((curr = curr.getParent()) != null) {
      Board b = curr.getBoard().cloneBoard();
      b.setMove(curr.getBoard().getMove());
      sequence.add(0, b);
    }
    sequence.get(0).setMove(new Move());
    winningBoards.add(sequence);
    //winners.add(winner);
  }
  
  public void makeMove(Node parent) {
    Board b = parent.getBoard();
    for (Vehicle piece : b.getPieces()) {
      for (Direction d : directions) {
        int moves = b.getSpacesOpen(b, piece, d);
        for (int i = moves; i > 0; i--) {
          Board child = checkBoard(b, piece, d, i);
          if (child != null) {
            debugMove(b, child);
            boards.add(child);
            Node childNode = parent.addChild(child);
            if (child.isSolved()) {
              //winners.add(childNode);
              addWinner(childNode);
            }
            makeMove(childNode);
          }
        }
      }
    }
  }
  
  
  private void debugMove(Board start, Board end) {
    LOG.trace("Move\n" + start.paint() + "\n" + end.paint());
  }
  
  /**
   * Returns true if the proposed move does not result in a board that already exists
   * (i.e. is a duplicate of previous moves from the starting point)
   * @param b
   * @param piece
   * @param dir
   * @param spacesMoved
   * @return
   */
  private Board checkBoard(Board b, Vehicle piece, Direction dir, int spacesMoved) {
    if (piece.getOrientation() == VERTICAL && (dir == E || dir == W)) return null;
    if (piece.getOrientation() == HORIZONTAL && (dir == N || dir ==S)) return null;
    //LOG.trace(piece.getName() + " " + dir + " " + spacesMoved);
    Board clone = b.cloneBoard();
    Vehicle pieceToMove = clone.getPieceById(piece.getId());
    pieceToMove.move(dir,  spacesMoved);
    Move move = new Move(piece, pieceToMove);
    clone.setMove(move);
    clone.finalize();
    
    if (isBoardExists(clone)) {
      return null;
    }
    
    LOG.trace("Moving {} {} spaces {}, pos now {}", pieceToMove.getName(), spacesMoved, dir, pieceToMove.getPosition());
    LOG.trace(">" + b.getMovesHash());
    LOG.trace(clone.getMovesHash());
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
}
