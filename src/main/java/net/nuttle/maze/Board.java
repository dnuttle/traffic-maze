package net.nuttle.maze;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Board {

  private List<Vehicle> pieces;
  
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
  
  public Board cloneBoard() {
    Board b = new Board();
    for (Vehicle v : pieces) {
      b.addPiece(v.cloneVehicle());
    }
    return b;
  }
}
