package net.nuttle.maze;

import net.nuttle.maze.GameTree.Node;

public class Result {

  private Node node;
  private boolean isSolved;
  
  public Result(Node node, boolean isSolved) {
    this.node = node;
    this.isSolved = isSolved;
  }
  
  public Node getNode() {
    return node;
  }
  
  public boolean isSolved() {
    return isSolved;
  }
}
