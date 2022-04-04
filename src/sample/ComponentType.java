package sample;

import javafx.geometry.Point2D;
import javafx.scene.shape.Polygon;

public interface ComponentType {
    void move();
    void turnLeft();
    void turnRight();
    void accelerate();
    boolean isAlive();
    Point2D getMovement();
    Polygon getComponent();
}
