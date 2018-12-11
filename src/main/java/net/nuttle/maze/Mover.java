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
  List<Board> boards;
  GameTree tree;
  
  public Mover(Board start) {
    winners = new ArrayList<>();
    boards = new ArrayList<>();
    boards.add(start);
    tree = new GameTree(start);
    makeMove(tree.getRoot());
    boolean first = true;
    for (Node winner : winners) {
      int steps = 0;
      Node curr = winner;
      while (curr != null) {
        steps++;
        if (first && curr.getBoard().getMove() != null) {
          LOG.debug((steps + 1) + ". " + curr.getBoard().getMove().toString());
        }
        curr = curr.getParent();
      }
      LOG.debug("{} steps", steps);
      first = false;
    }
    LOG.debug("Found {} winners", winners.size());
  }
  
  public void makeMove(Node n) {
    Board b = n.getBoard();
    for (Vehicle piece : b.getPieces()) {
      int leftMoves = 0;
      int rightMoves = 0;
      int upMoves = 0;
      int downMoves = 0;
      while (b.isSpaceOpen(piece, Direction.W, -leftMoves)) leftMoves++;
      while (b.isSpaceOpen(piece, Direction.E, rightMoves)) rightMoves++;
      while (b.isSpaceOpen(piece, Direction.S, downMoves)) downMoves++;
      while (b.isSpaceOpen(piece, Direction.N, upMoves)) upMoves++;
      for (int i = 1; i <= leftMoves; i++) {
        Board newBoard = checkBoard(b, piece, Direction.W, i);
        if (newBoard != null) {
          boards.add(newBoard);
          Node newNode = n.addChild(newBoard);
          if (newBoard.isSolved()) {
            winners.add(newNode);
          }
          makeMove(newNode);
        } else break;
      }
      for (int i = 1; i <= rightMoves; i++) {
        Board newBoard = checkBoard(b, piece, Direction.E, i);
        if (newBoard != null) {
          boards.add(newBoard);
          Node newNode = n.addChild(newBoard);
          if (newBoard.isSolved()) {
            winners.add(newNode);
          }
          makeMove(newNode);
        } else break;
      }
      for (int i = 1; i <= upMoves; i++) {
        Board newBoard = checkBoard(b, piece, Direction.N, i);
        if (newBoard != null) {
          boards.add(newBoard);
          Node newNode = n.addChild(newBoard);
          if (newBoard.isSolved()) {
            winners.add(newNode);
          }
          makeMove(newNode);
        } else break;
      }
      for (int i = 1; i <= downMoves; i++) {
        Board newBoard = checkBoard(b, piece, Direction.S, i);
        if (newBoard != null) {
          boards.add(newBoard);
          Node newNode = n.addChild(newBoard);
          if (newBoard.isSolved()) {
            winners.add(newNode);
          }
          makeMove(newNode);
        } else break;
      }
    }
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
    Move move = new Move(piece, pieceToMove);
    clone.setMove(move);
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
