package net.nuttle.maze;

import java.util.ArrayList;
import java.util.List;

public class GameTree {

  private Node root;
  
  public GameTree(Board startingBoard) {
    root = new Node(null, startingBoard);
  }
  
  public Node getRoot() {
    return root;
  }
  
  public static class Node {
    private Node parent;
    private List<Node> childNodes;
    private Board board;
    
    public Node(Node parent, Board b) {
      childNodes = new ArrayList<>();
      this.board = b;
      this.parent = parent;
    }
    public Node addChild(Board childBoard) {
      Node childNode = new Node(this, childBoard);
      childNodes.add(childNode);
      return childNode;
    }
    public Node getParent() {
      return parent;
    }
    public Board getBoard() {
      return board;
    }
    public List<Node> getChildNodes() {
      return childNodes;
    }
  }
}
