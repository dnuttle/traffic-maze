package net.nuttle.maze;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class Game {
  
  private static final Logger LOG = LoggerFactory.getLogger(Game.class);
  
  public static void main(String[] args) {
    Game g = new Game(1);
  }
  
  public Game(int challenge) {
    Board b = new Board();
    ObjectMapper mapper = new ObjectMapper();
    try {
      ArrayNode root = (ArrayNode) mapper.readTree(new File(System.getProperty("user.dir") + "/src/main/resources/setups.json"));
      LOG.debug("Setups found: " + root.size());
    } catch (IOException e) {
      //TODO
      LOG.error("Failure", e);
    }
    b.getPieces().add(getPiece(0, Type.CAR, Orientation.HORIZONTAL, 0, 0));
    b.sortPieces();
  }
  
  private static Vehicle getPiece(int id, Type type, Orientation orientation, int row, int col) {
    Vehicle v = new Vehicle(id, type);
    v.setOrientation(orientation);
    v.getPosition().setRow(row);
    v.getPosition().setCol(col);
    return v;
  }

}
