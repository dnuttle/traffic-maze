package net.nuttle.maze;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.nuttle.maze.Orientation.HORIZONTAL;
import static net.nuttle.maze.Orientation.VERTICAL;
import static net.nuttle.maze.Direction.N;
import static net.nuttle.maze.Direction.S;
import static net.nuttle.maze.Direction.E;
import static net.nuttle.maze.Direction.W;

public class Board {

  private static final Logger LOG = LoggerFactory.getLogger(Board.class);
  
  private List<Vehicle> pieces;
  private Move move;
  private byte[] spaces;
  
  public Board() {
    pieces = new ArrayList<>();
  }
  
  public void addPiece(Vehicle v) {
    pieces.add(v);
  }
  
  public void sortPieces() {
    Collections.sort(pieces);
  }
  
  public List<Vehicle> getPieces() {
    return pieces;
  }
  
  public void setMove(Move move) {
    this.move = move;
  }
  
  public Move getMove() {
    return move;
  }
  
  public byte[] getSpaces() {
    finalize();
    return spaces;
  }
  
  public void finalize() {
    if (spaces == null) {
      spaces = new byte[6];
      for (Vehicle piece : this.getPieces()) {
        int size = piece.getType() == Type.TRUCK ? 3 : 2;
        if (piece.getOrientation() == Orientation.HORIZONTAL) {
          for (int i = piece.getPosition().getCol(); i < piece.getPosition().getCol() + size; i++) {
            spaces[piece.getPosition().getRow()] |= (1 << i);
          }
        } else {
          for (int i = piece.getPosition().getRow(); i < piece.getPosition().getRow() + size; i++) {
            spaces[i] |= (1 << piece.getPosition().getCol());
          }
        }
      }
      /*
      for (int i = 0; i < 6; i++) {
        LOG.debug("Row " + i + ": " + spaces[i]);
      }
      */
      sortPieces();
    }
  }
  
  public boolean isSpaceOpen(Vehicle v, Direction dir, int offset) {
    Orientation orient = v.getOrientation();
    if (orient == HORIZONTAL && (dir == N || dir == S)) return false;
    if (orient == VERTICAL && (dir == E || dir == W)) return false;
    finalize();
    int row = v.getPosition().getRow();
    int col = v.getPosition().getCol();
    int size = v.getType() == Type.TRUCK ? 3 : 2;
    switch (dir) {
    case N:
      if (row + offset <= 0 || orient == HORIZONTAL) return false;
      return (spaces[row - 1 + offset] & (1 << col)) == 0;
    case S:
      if (row + size + offset >= 6 || orient == HORIZONTAL) return false;
      return (spaces[row + size] & (1 << col)) == 0;
    case E:
      if (col + size + offset >= 6 || orient == VERTICAL) return false;
      return (spaces[row] & (1 << col + size + offset )) == 0;
    case W:
      if (col + offset <= 0 || orient == VERTICAL) return false;
      return (spaces[row] & (1 << col - 1 + offset)) == 0;
    }
    throw new RuntimeException("Invalid Neighbors instance");
  }
  
  public static boolean isBoardSame(Board b1, Board b2) {
    if (b1.getPieces().size() != b2.getPieces().size()) {
      throw new RuntimeException("Boards have different sizes");
    }
    List<Vehicle> p1 = b1.getPieces();
    List<Vehicle> p2 = b2.getPieces();
    for (int i = 0; i < p1.size(); i++) {
      if (p2.get(i).getId() != p1.get(i).getId()) {
        throw new RuntimeException("Mismatched IDs in boards, were the pieces sorted in both?");
      }
      if (!p2.get(i).getPosition().equals(p1.get(i).getPosition())) {
        return false;
      }
    }
    return true;
  }
  
  public Vehicle getPieceById(int id) {
    for (Vehicle piece : pieces) {
      if (piece.getId() == id) {
        return piece;
      }
    }
    return null;
  }
  
  public Board cloneBoard() {
    Board b = new Board();
    for (Vehicle v : pieces) {
      b.addPiece(v.cloneVehicle());
    }
    return b;
  }
  
  public boolean isSolved() {
    Vehicle player = this.getPieceById(0);
    int col = player.getPosition().getCol();
    int spacesToCheck = 4 - col;
    for (int i = 0; i < spacesToCheck; i++) {
      if (!this.isSpaceOpen(player, Direction.E, i)) {
        return false;
      }
    }
    return true;
  }
}
