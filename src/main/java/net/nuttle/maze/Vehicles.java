package net.nuttle.maze;

import static net.nuttle.maze.Type.CAR;
import static net.nuttle.maze.Type.PLAYER;
import static net.nuttle.maze.Type.TRUCK;

public enum Vehicles {

  RED_CAR(0, PLAYER), LGN_CAR(1, CAR), ORG_CAR(2, CAR), PNK_CAR(3, CAR), BRN_CAR(4, CAR), DGN_CAR(5, CAR),
  BLU_CAR(6, CAR), CRM_CAR(7, CAR), WHT_CAR(8, CAR), PRP_CAR(9, CAR), YEL_CAR(10, CAR), MGR_CAR(11, CAR),
  PRP_TRUCK(12, TRUCK), BLU_TRUCK(13, TRUCK), YEL_TRUCK(14, TRUCK), GRN_TRUCK(15, TRUCK);
  
  private Vehicle vehicle;
  
  private Vehicles(int id, Type type) {
    vehicle = new Vehicle(id, type);
  }
  
  public Vehicle get() {
    return vehicle;
  }
}
