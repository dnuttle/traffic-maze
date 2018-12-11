package net.nuttle.maze;

public class Position {

  private int row;
  private int col;
  private static int[] primes = {11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53};
  
  public Position(int row, int col) {
    this.row = row;
    this.col = col;
  }
  
  public int getRow() {
    return row;
  }
  public int getCol() {
    return col;
  }
  public void setRow(int row) {
    if (row < 0 || row > 5) {
      throw new IllegalArgumentException("Row must be 0-5");
    }
    this.row = row;
  }
  public void setCol(int col) {
    if (col < 0 || col > 5) {
      throw new IllegalArgumentException("Col must be 0-5");
    }
    this.col = col;
  }
  public Position clonePosition() {
    return new Position(row, col);
  }
  
  @Override
  public int hashCode() {
    return primes[row] * primes[col + 6];
  }
  
  @Override
  public boolean equals(Object o) {
    if (o == null) return false;
    if (! (o instanceof Position)) return false;
    Position p = (Position) o;
    return (p.getCol() == col && p.getRow() == row);
  }
  
  @Override
  public String toString() {
    return row + ", " + col;
  }
  
}
