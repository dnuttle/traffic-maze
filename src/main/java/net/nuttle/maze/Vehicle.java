package net.nuttle.maze;

public class Vehicle implements Comparable<Vehicle> {

  private int id;
  private String name;
  private Type type;
  private Orientation orientation;
  private Position pos;
  
  public Vehicle(int id, String name, Type type) {
    this.id = id;
    this.name = name;
    this.type = type;
    this.pos = new Position(0, 0);
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
  public void move(Direction dir, int spaces) {
    switch (dir) {
    case N:
      pos.setRow(pos.getRow() - spaces);
      break;
    case E:
      pos.setCol(pos.getCol() + spaces);
      break;
    case S:
      pos.setRow(pos.getRow() + spaces);
      break;
    case W:
      pos.setCol(pos.getCol() - spaces);
      break;
    default:
      throw new IllegalArgumentException("Unexpected Direction " + dir);
    }
  }
  public Orientation getOrientation() {
    return orientation;
  }
  public String getName() {
    return name;
  }
  @Override
  public int compareTo(Vehicle v) {
    if (id == v.getId()) {
      return 0;
    }
    return id - v.getId();
  }
  public Vehicle cloneVehicle() {
    Vehicle v = new Vehicle(id, name, type);
    v.setOrientation(orientation);
    v.setPosition(pos.clonePosition());
    return v;
  }
  @Override
  public String toString() {
    return "id:" + id + ", name:" + name + ", " + "type:" + type + ", orient:" + orientation + ", " + "pos:" + pos;
  }
}
