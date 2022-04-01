package sample;

import javafx.geometry.Point2D;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Shape;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

import java.util.List;
import java.util.Random;

public abstract class Character {

    private boolean alive;
    private Point2D movement;
    private AsteroidSize size;
    private Polygon character;
    private ProjectileType projectileType;

    public Character(Polygon polygon, int x, int y, Paint color) {
        // Assigning the specific character variables
        this.character = polygon;
        this.character.setTranslateY(y);
        this.character.setTranslateX(x);
        this.character.setFill(color);
        this.character.setStroke(color);
        this.movement = new Point2D(0, 0);
    }

    // Character getter method
    public Polygon getCharacter() {
        return character;
    }

    // Enable character to turn left
    public void turnLeft() {
        this.character.setRotate(this.character.getRotate() - 5);
    }

    // Enable character to turn left
    public void turnRight() {
        this.character.setRotate(this.character.getRotate() + 5);
    }

    // Enable character to hyperJump
    public void hyperJump(List<Character> asteroids){
        // Choosing a random location on the gameView
        Random rand = new Random();
        double newX = rand.nextDouble() * GameView.gameScreenWidth;
        double newY = rand.nextDouble() * GameView.gameScreenHeight;

        // Ensuring that the location is empty
        asteroids.forEach(asteroid -> {
            if (
                    (asteroid.getCharacter().getTranslateX() > newX - 25
                    && asteroid.getCharacter().getTranslateX() < newX + 25)
                    || (asteroid.getCharacter().getTranslateY() > newY - 25
                    && asteroid.getCharacter().getTranslateY() < newY + 25)
            ){
                this.hyperJump(asteroids);
            }
        });
        // Placing the character in the new location
        this.character.setTranslateX(newX);
        this.character.setTranslateY(newY);
    }

    public void move() {
        this.character.setTranslateX(this.character.getTranslateX() + this.movement.getX());
        this.character.setTranslateY(this.character.getTranslateY() + this.movement.getY());

        // If the character reaches the left border of the gameView they are translated to the right side of the screen
        if (this.character.getTranslateX() < 0) {
            this.character.setTranslateX(this.character.getTranslateX() + GameView.gameScreenWidth);
        }

        // If the character reaches the right border of the gameView they are translated to the left side of the screen
        if (this.character.getTranslateX() > GameView.gameScreenWidth) {
            this.character.setTranslateX(this.character.getTranslateX() % GameView.gameScreenWidth);
        }

        // If the character reaches the bottom border of the gameView they are translated to the top of the screen
        if (this.character.getTranslateY() < 0) {
            this.character.setTranslateY(this.character.getTranslateY() + GameView.gameScreenHeight);
        }

        // If the character reaches the top border of the gameView they are translated to the bottom of the screen
        if (this.character.getTranslateY() > GameView.gameScreenHeight) {
            this.character.setTranslateY(this.character.getTranslateY() % GameView.gameScreenHeight);
        }
    }

    // Method to accelerate a character
    public void accelerate() {
        double changeX = Math.cos(Math.toRadians(this.character.getRotate()));
        double changeY = Math.sin(Math.toRadians(this.character.getRotate()));

        changeX *= 0.05;
        changeY *= 0.05;

        this.movement = this.movement.add(changeX, changeY);
    }

    // Method which checks if a character collides with another character
    public boolean collide(Character other) {
        Shape collisionArea = Shape.intersect(this.character, other.getCharacter());
        if (collisionArea.getBoundsInLocal().getWidth() > 0) {
            return true;
        }
        return false;
    }

    // Method which returns the movement of a character
    public Point2D getMovement(){
        return movement;
    }

    // Method which sets the movement of a character
    public void setMovement(Point2D movement){
        this.movement = this.movement.add(movement.getX(),movement.getY());
    }

    // Method which sets if a character is alive or not
    public void setAlive(boolean ifAlive){
        this.alive = ifAlive;
    }

    // Method which checks if a character is alive
    public boolean isAlive(){
        return alive;
    }

    // Method which sets the size of a character
    public void setSize(AsteroidSize size){
        this.size = size;
    }

    // Method which gets the size of a character
    public AsteroidSize getSize(){
        return this.size;
    }

    // Method which sets the type of the character
    public void setProjectileType(ProjectileType type){
        this.projectileType = type;
    }

    // Method which gets the type of the character
    public ProjectileType getProjectileType(){
        return this.projectileType;
    }
}
