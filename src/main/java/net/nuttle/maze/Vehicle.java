package net.nuttle.maze;

public class Vehicle implements Comparable<Vehicle> {

  private int id;
  private Type type;
  private Orientation orientation;
  private Position pos;
  
  public Vehicle(int id, Type type) {
    this.id = id;
    this.type = type;
  }
  
  public void setOrientation(Orientation orientation) {
    this.orientation = orientation;
  }
  public void setPosition(Position pos) {
    this.pos = pos;
  }
  public int getId() {
    return id;
  }
  public Position getPosition() {
    return pos;
  }
  public Type getType() {
    return type;
  }
  public Orientation getOrientation() {
    return orientation;
  }
  @Override
  public int compareTo(Vehicle v) {
    if (id == v.getId()) {
      return 0;
    }
    return id - v.getId();
  }
  public Vehicle cloneVehicle() {
    Vehicle v = new Vehicle(id, type);
    v.setOrientation(orientation);
    v.setPosition(pos);
    return v;
  }
}
