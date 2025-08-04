package io.javagame.snake.model;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;

public class Snake {
    private static Snake INSTANCE = null;

    private final List<Point> coordinates;
    private final int size;
    private Direction direction;
    private float speed;

    public static Snake getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Snake();
        }
        return INSTANCE;
    }

    private Snake() {
        // For fast first and last removal
        coordinates = new LinkedList<>();
        size = 15;
        direction = Direction.UP;
        speed = 0.1f;
    }

    public Point getHead() {
        return this.coordinates.getFirst();
    }

    public void addCoordinate(Point p) {
        coordinates.add(p);
    }

    public List<Point> getCoordinates() {
        return this.coordinates;
    }

    public int getSize() {
        return this.size;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
}
