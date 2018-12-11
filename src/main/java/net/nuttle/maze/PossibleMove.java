package net.nuttle.maze;

/**
 * Used to keep track of the possible number of moves (left or right, or up or down) a piece might make
 * @author dnuttle
 *
 */
public class PossibleMove {

  private int down;
  private int up;
  private int left;
  private int right;
  
  public PossibleMove() {
  }
  
  public void setDown(int down) {
    this.down = down;
  }
  
  public void setUp(int up) {
    this.up = up;
  }
  
  public void setLeft(int left) {
    this.left = left;
  }
  
  public void setRight(int right) {
    this.right = right;
  }
  
  public int getDown() {
    //Funkay!
    return down;
  }
  
  public int getUp() {
    //Git on up!
    return up;
  }
  
  public int getLeft() {
    return left;
  }
  
  public int getRight() {
    return right;
  }
}
