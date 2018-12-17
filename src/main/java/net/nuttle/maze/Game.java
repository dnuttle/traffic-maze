package net.nuttle.maze;

import static net.nuttle.maze.Orientation.HORIZONTAL;
import static net.nuttle.maze.Orientation.VERTICAL;
import static net.nuttle.maze.Direction.E;
import static net.nuttle.maze.Direction.W;
import static net.nuttle.maze.Direction.S;
import static net.nuttle.maze.Direction.N;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.nuttle.maze.GameTree.Node;

public class Game {
  
  private static final Logger LOG = LoggerFactory.getLogger(Game.class);
  
  
  public static void main(String[] args) {
    Game g = new Game(1);

    /*
     * Experiment on hashing
    Set<Integer> codes = new HashSet<>();
    String patt = "%01d";
    int codeCount = 0;
    for (int i = 0; i < 6; i++) {
      for (int j = 0; j < 6; j++) {
        for (int k = 0; k < 6; k++) {
          StringBuilder sb = new StringBuilder();
          sb.append(String.format(patt, i)).append(j).append(k).append(j).append(k+1);
          int code = sb.toString().hashCode();
          codes.add(code);
          codeCount++;
          sb = new StringBuilder();
          sb.append(String.format(patt, i)).append(j).append(k).append(j+1).append(k);
          code = sb.toString().hashCode();
          codes.add(code);
          codeCount++;
        }
      }
    }
    LOG.debug("" + codeCount + " " + codes.size());
    */
  }
  
  public Game(int challengeId) {
    Board start = new Board();
    loadAndSetup(start, challengeId);
    start.finalize();
    Mover m = new Mover(start);
    m.run();
  }
  
  private void loadAndSetup(Board b, int challengeId) {
    ObjectMapper mapper = new ObjectMapper();
    boolean found = false;
    try {
      ArrayNode root = (ArrayNode) mapper.readTree(new File(System.getProperty("user.dir") + "/src/main/resources/setups.json"));
      LOG.debug("Setups found: " + root.size());
      for (JsonNode node : root) {
        if (node.get("id").asInt() == challengeId) {
          found = true;
          ArrayNode pieces = (ArrayNode) node.get("pieces");
          LOG.debug("Found {} pieces", pieces.size());
          for (JsonNode piece : pieces) {
            Vehicle v = Vehicles.valueOf(Vehicles.class, piece.get("type").asText()).get();
            Orientation o = Orientation.valueOf(piece.get("orientation").asText());
            v.setOrientation(o);
            v.getPosition().setRow(piece.get("row").asInt());
            v.getPosition().setCol(piece.get("col").asInt());
            b.getPieces().add(v);
          }
          b.sortPieces();
          for (Vehicle piece : b.getPieces()) {
            LOG.debug(piece.getType() + ", " + piece.getOrientation() + ", " + piece.getPosition());
          }
        }
      }
    } catch (IOException e) {
      //TODO
      LOG.error("Failure", e);
    }
    if (!found) {
      throw new IllegalArgumentException("Challenge " + challengeId + " not found");
    }
  }
  
}
