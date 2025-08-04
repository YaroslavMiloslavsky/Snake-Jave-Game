package io.javagame.snake.model;

import java.awt.Point;

public record Food(Point point, int size) {
    public Food(Point point) {
        this(point, 15);
    }
}
