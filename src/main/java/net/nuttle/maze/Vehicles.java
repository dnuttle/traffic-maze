package net.nuttle.maze;

import static net.nuttle.maze.Type.CAR;
import static net.nuttle.maze.Type.PLAYER;
import static net.nuttle.maze.Type.TRUCK;

public enum Vehicles {

  RED_CAR(0, "RED_CAR", PLAYER), LGN_CAR(1, "LGN_CAR", CAR), ORG_CAR(2, "ORG_CAR", CAR), 
  PNK_CAR(3, "PNK_CAR", CAR), BRN_CAR(4, "BRN_CAR", CAR), DGN_CAR(5, "DGN_CAR", CAR),
  BLU_CAR(6, "BLU_CAR", CAR), CRM_CAR(7, "CRM_CAR", CAR), WHT_CAR(8, "WHT_CAR", CAR), 
  PRP_CAR(9, "PRP_CAR", CAR), YEL_CAR(10, "YEL_CAR", CAR), MGR_CAR(11, "MGR_CAR", CAR),
  PRP_TRUCK(12, "PRP_TRUCK", TRUCK), BLU_TRUCK(13, "BLU_TRUCK", TRUCK), 
  YEL_TRUCK(14, "YEL_TRUCK", TRUCK), GRN_TRUCK(15, "GRN_TRUCK", TRUCK);
  
  private Vehicle vehicle;
  
  private Vehicles(int id, String name, Type type) {
    vehicle = new Vehicle(id, name, type);
  }
  
  public Vehicle get() {
    return vehicle;
  }
}
