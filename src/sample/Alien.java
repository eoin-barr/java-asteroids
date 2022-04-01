package sample;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

import java.util.Random;

public class Alien extends Character{

    public Alien(int x, int y, int level){
        // Create new Alien
        super(new Polygon(12,12,-12,12,-18,2,-8,2,0,-12,8,2,18,2),x,y, Color.LIMEGREEN);

        // Ensure alien has no rotation
        super.getCharacter().setRotate(0);

        // Provide random component to the aliens acceleration
        Random rand = new Random();

        // base is used to ensure minimum speed of alien
        int base = 10;

        // level increases the acceleration of the alien based on the level
        int accelerationValue = level + base + rand.nextInt(5);
        for (int i = 0; i < accelerationValue; i++){
            accelerate();
        }

    }
}
