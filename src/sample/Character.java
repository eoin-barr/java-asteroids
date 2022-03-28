package sample;

import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

import java.util.Random;

public abstract class Character {

    private Polygon character;
    private Point2D movement;
    private boolean alive;

    public Character(Polygon polygon, int x, int y) {
        this.character = polygon;
        this.character.setTranslateX(x);
        this.character.setTranslateY(y);
        this.movement = new Point2D(0, 0);
    }

    public Polygon getCharacter() {
        return character;
    }

    public void turnLeft() {
        this.character.setRotate(this.character.getRotate() - 5);
    }

    public void turnRight() {
        this.character.setRotate(this.character.getRotate() + 5);
    }
    
    public void hyperJump(){
        Random rand = new Random();
        this.character.setTranslateX(rand.nextDouble() * GameView.gameScreenWidth);
        this.character.setTranslateY(rand.nextDouble() * GameView.gameScreenHeight);
    }


    public void move() {
        this.character.setTranslateX(this.character.getTranslateX() + this.movement.getX());
        this.character.setTranslateY(this.character.getTranslateY() + this.movement.getY());

        if (this.character.getTranslateX() < 0) {
            this.character.setTranslateX(this.character.getTranslateX() + GameView.gameScreenWidth);
        }

        if (this.character.getTranslateX() > GameView.gameScreenWidth) {
            this.character.setTranslateX(this.character.getTranslateX() % GameView.gameScreenWidth);
        }

        if (this.character.getTranslateY() < 0) {
            this.character.setTranslateY(this.character.getTranslateY() + GameView.gameScreenHeight);
        }

        if (this.character.getTranslateY() > GameView.gameScreenHeight) {
            this.character.setTranslateY(this.character.getTranslateY() % GameView.gameScreenHeight);
        }
    }


    public void accelerate() {
        double changeX = Math.cos(Math.toRadians(this.character.getRotate()));
        double changeY = Math.sin(Math.toRadians(this.character.getRotate()));

        changeX *= 0.05;
        changeY *= 0.05;

        this.movement = this.movement.add(changeX, changeY);
    }

    public void decelerate(){
        double changeX = Math.cos(Math.toRadians(this.character.getRotate()));
        double changeY = Math.sin(Math.toRadians(this.character.getRotate()));

        changeX *= -0.05;
        changeY *= -0.05;

        this.movement = this.movement.add(changeX, changeY);
    }
    public boolean collide(Character other) {
        Shape collisionArea = Shape.intersect(this.character, other.getCharacter());
        if (collisionArea.getBoundsInLocal().getWidth() > 0) {
            return true;
        }
        return false;
    }

    public Point2D getMovement(){
        return movement;
    }

    public void setMovement(Point2D movement){
        this.movement = this.movement.add(movement.getX(),movement.getY());
    }

    public void setAlive(boolean ifAlive){
        this.alive = ifAlive;
    }

    public boolean isAlive(){
        return alive;
    }
}
