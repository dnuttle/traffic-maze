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
  private String movesHash;
  private int hashCode;
  
  public Board() {
    this.movesHash = "";
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
      sortPieces();
      StringBuilder sb = new StringBuilder();
      for (Vehicle piece : pieces) {
        sb.append(String.format("%02d", piece.getId())).append(":");
        sb.append(piece.getPosition().getRow()).append(",");
        sb.append(piece.getPosition().getCol()).append(";");
      }
      hashCode = sb.toString().hashCode();
    }
  }
  

  public int getSpacesOpen(Board b, Vehicle piece, Direction dir) {
    int col = piece.getPosition().getCol();
    int row = piece.getPosition().getRow();
    int size = piece.getType() == Type.TRUCK ? 3 : 2;
    if (piece.getOrientation() == HORIZONTAL && (dir == N || dir == S)) return 0;
    if (piece.getOrientation() == VERTICAL && (dir == E || dir == W)) return 0;
    int moves = 0;
    switch (dir) {
    case N:
      while (row + moves > 0) {
        if ((spaces[row + moves - 1] & (1 << col)) == 0) {
          moves--;
        } else break;
      }
      return -moves;
    case E:
      while (col + size + moves < 6) {
        if ((spaces[row] & (1 << col + size + moves)) == 0) {
          moves++;
        } else break;
      }
      return moves;
    case S:
      while (row + size + moves < 6) {
        if ((spaces[row + size + moves] & (1 << col)) == 0) {
          moves++;
        } else break;
      }
      return moves;
    case W:
      while (col + moves > 0) {
        if ((spaces[row] & (1 << col + moves - 1)) == 0) {
          moves--;
        } else break;
      }
      return -moves;
    }
    throw new RuntimeException("Invalid direction " + dir);
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
        if (b1.hashCode() == b2.hashCode()) {
          throw new RuntimeException("Board hashcodes are equal but should differ");
        }
        return false;
      }
    }
    if(b1.hashCode() != b2.hashCode()) {
      throw new RuntimeException("Board hashcodes differ but should be the same");
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
    int spacesOpen = this.getSpacesOpen(this, player, E);
    return col + spacesOpen == 4;
  }
  
  public String paint() {
    String[][] map = new String[][]{
      {"-","-","-","-","-","-"},
      {"-","-","-","-","-","-"},
      {"-","-","-","-","-","-"},
      {"-","-","-","-","-","-"},
      {"-","-","-","-","-","-"},
      {"-","-","-","-","-","-"}
    };
    for (Vehicle piece : pieces) {
      int size = piece.getType() == Type.TRUCK ? 3 : 2;
      int row = piece.getPosition().getRow();
      int col = piece.getPosition().getCol();
      String s = piece.getName().substring(0, 1);
      map[row][col] = s;
      if (piece.getOrientation() == Orientation.HORIZONTAL) {
        map[row][col+1] = s;
        if (size == 3) {
          map[row][col+2] = s;
        }
      }
      if (piece.getOrientation() == Orientation.VERTICAL) {
        map[row+1][col] = s;
        if (size == 3) {
          map[row+2][col] = s;
        }
      }
    }
    StringBuilder sb = new StringBuilder();
    sb.append("\n");
    for (int i = 0; i < 6; i++) {
      for (int j = 0; j < 6; j++) {
        sb.append(map[i][j]);
      }
      sb.append("\n");
    }
    return sb.toString();
  }
  
  @Override
  public boolean equals(Object o) {
    if (o == null || !(o instanceof Board)) {
      return false;
    }
    Board b = (Board) o;
    return hashCode() == o.hashCode();
  }
  
  public String getMovesHash() {
    return movesHash;
  }
  
  @Override
  public int hashCode() {
    return hashCode;
    //return movesHash.hashCode();
  }
}
