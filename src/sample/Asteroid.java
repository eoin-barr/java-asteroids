package sample;

import javafx.scene.paint.Color;

import java.util.Random;

public class Asteroid extends Character {

    private double rotationalMovement;

    public Asteroid(int x, int y, AsteroidSize size, int level) {
        // Creates a new asteroid
        super(PolygonBuilder.createPolygon(size), x, y, Color.GREY);

        // Setting the size
        super.setSize(size);

        // Provide random component to the asteroids acceleration
        Random rand = new Random();

        // level increases the acceleration of the asteroid based on the level
        level *= 2;

        // base is used to ensure minimum speed of the asteroid and
        // is adjusted based on the size of the asteroid
        int base;
        if (size == AsteroidSize.LARGE){
            base = 5;
        }else if (size == AsteroidSize.MEDIUM){
            base = 10;
        }else {
            base = 15;
        }

        // Giving the asteroid a random rotation
        super.getCharacter().setRotate(rand.nextInt(360));
        int accelerationValue = level + base + rand.nextInt(5);
        for(int i = 0; i<accelerationValue;i++){
            accelerate();
        }
        // Giving the asteroid a random rotational movement
        this.rotationalMovement = 0.5-rand.nextDouble();
    }

    public void move(){
        super.move();
        super.getCharacter().setRotate(super.getCharacter().getRotate()+rotationalMovement);
    }
}
