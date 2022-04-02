package sample;

import javafx.geometry.Point2D;
import javafx.scene.shape.Polygon;

public interface Char {
    void move();
    void turnLeft();
    void turnRight();
    void accelerate();
    boolean isAlive();
    Point2D getMovement();
    Polygon getCharacter();
}
