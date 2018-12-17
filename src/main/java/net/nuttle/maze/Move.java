package net.nuttle.maze;

public class Move {
  
  private static final String ID_PATT = "%02d";

  private Vehicle piece;
  private Vehicle piece2;
  private int startRow;
  private int startCol;
  private int endRow;
  private int endCol;
  
  public Move(Vehicle piece, Vehicle piece2) {
    this.piece = piece;
    this.piece2 = piece2;
    this.startCol = piece.getPosition().getCol();
    this.endCol = piece2.getPosition().getCol();
    this.startRow = piece.getPosition().getRow();
    this.endRow = piece2.getPosition().getRow();
  }
  
  public Move() {
    
  }
  
  public int getStartRow() {
    return startRow;
  }
  public int getStartCol() {
    return startCol;
  }
  public int getEndRow() {
    return endRow;
  }
  public int getEndCol() {
    return endCol;
  }
  /*
  public String getMoveHash() {
    StringBuilder sb = new StringBuilder();
    sb.append(String.format(ID_PATT, piece.getId()));
    sb.append(piece.getPosition().getRow())
      .append(piece.getPosition().getCol())
      .append(piece2.getPosition().getRow())
      .append(piece2.getPosition().getCol())
      .append(":");
    return sb.toString();
  }
  */
  
  @Override
  public String toString() {
    if (piece == null) {
      return "Starting point";
    }
    StringBuilder sb = new StringBuilder();
    sb.append(piece.getName()).append(" ");
    if (startCol != endCol && startRow != endRow) {
      int x= 0;
    }
    if (startCol > endCol) {
      sb.append(startCol - endCol).append(" spaces left");
    }
    if (startCol < endCol) {
      sb.append(endCol - startCol).append(" spaces right");
    }
    if (startRow > endRow) {
      sb.append(startRow - endRow).append(" spaces up");
    }
    if (startRow < endRow) {
      sb.append(endRow - startRow).append(" spaces down");
    }
    return sb.toString();
    
  }
  
}
