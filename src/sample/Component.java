package sample;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Shape;
import javafx.scene.shape.Polygon;

import java.util.List;
import java.util.Random;

public abstract class Component implements ComponentType {

    private boolean alive;
    private Point2D movement;
    private AsteroidSize size;
    private Polygon component;
    private ProjectileType projectileType;

    public Component(Polygon polygon, int x, int y, Paint color) {
        // Assigning the specific component variables
        this.component = polygon;
        this.component.setTranslateY(y);
        this.component.setTranslateX(x);
        this.component.setFill(Color.WHITE);
        this.component.setStroke(color);
        this.movement = new Point2D(0, 0);
    }

    // component getter method
    public Polygon getComponent() {
        return component;
    }

    // Enable component to turn left
    public void turnLeft() {
        this.component.setRotate(this.component.getRotate() - 5);
    }

    // Enable component to turn left
    public void turnRight() {
        this.component.setRotate(this.component.getRotate() + 5);
    }

    // Enable component to hyperJump
    public void hyperJump(List<Component> asteroids){
        // Choosing a random location on the gameView
        Random rand = new Random();
        double newX = rand.nextDouble() * GameView.gameViewWidth;
        double newY = rand.nextDouble() * GameView.gameViewHeight;

        // Ensuring that the location is empty
        asteroids.forEach(asteroid -> {
            if (
                    (asteroid.getComponent().getTranslateX() > newX - 25
                    && asteroid.getComponent().getTranslateX() < newX + 25)
                    || (asteroid.getComponent().getTranslateY() > newY - 25
                    && asteroid.getComponent().getTranslateY() < newY + 25)
            ){
                this.hyperJump(asteroids);
            }
        });
        // Placing the component in the new location
        this.component.setTranslateX(newX);
        this.component.setTranslateY(newY);
        // Stopping the component from moving once it has been placed
        // in the new location
        this.stopMovement(this.movement);
    }


    public void move() {
        this.component.setTranslateX(this.component.getTranslateX() + this.movement.getX());
        this.component.setTranslateY(this.component.getTranslateY() + this.movement.getY());

        // If the component reaches the left border of the gameView they are translated to the right side of the screen
        if (this.component.getTranslateX() < 0) {
            this.component.setTranslateX(this.component.getTranslateX() + GameView.gameViewWidth);
        }

        // If the component reaches the right border of the gameView they are translated to the left side of the screen
        if (this.component.getTranslateX() > GameView.gameViewWidth) {
            this.component.setTranslateX(this.component.getTranslateX() % GameView.gameViewWidth);
        }

        // If the component reaches the bottom border of the gameView they are translated to the top of the screen
        if (this.component.getTranslateY() < 0) {
            this.component.setTranslateY(this.component.getTranslateY() + GameView.gameViewHeight);
        }

        // If the component reaches the top border of the gameView they are translated to the bottom of the screen
        if (this.component.getTranslateY() > GameView.gameViewHeight) {
            this.component.setTranslateY(this.component.getTranslateY() % GameView.gameViewHeight);
        }
    }

    // Method to accelerate a component
    public void accelerate() {
        double changeX = Math.cos(Math.toRadians(this.component.getRotate()));
        double changeY = Math.sin(Math.toRadians(this.component.getRotate()));

        changeX *= 0.05;
        changeY *= 0.05;

        this.movement = this.movement.add(changeX, changeY);
    }

    // Method which checks if a component collides with another component
    public boolean collide(Component other) {
        Shape collisionArea = Shape.intersect(this.component, other.getComponent());
        if (collisionArea.getBoundsInLocal().getWidth() > 0) {
            return true;
        }
        return false;
    }

    // Method which returns the movement of a component
    public Point2D getMovement(){
        return movement;
    }

    // Method which sets the movement of a component
    public void setMovement(Point2D movement){
        this.movement = this.movement.add(movement.getX(),movement.getY());
    }

    // Method which stops the movement of a component
    public void stopMovement(Point2D movement){
        this.movement = this.movement.subtract(movement.getX(), movement.getY());
    }

    // Method which sets if a component is alive or not
    public void setAlive(boolean ifAlive){
        this.alive = ifAlive;
    }

    // Method which checks if a component is alive
    public boolean isAlive(){
        return this.alive;
    }

    // Method which sets the size of a component
    public void setSize(AsteroidSize size){
        this.size = size;
    }

    // Method which gets the size of a component
    public AsteroidSize getSize(){
        return this.size;
    }

    // Method which sets the type of the component
    public void setProjectileType(ProjectileType type){
        this.projectileType = type;
    }

    // Method which gets the type of the component
    public ProjectileType getProjectileType(){
        return this.projectileType;
    }
}
