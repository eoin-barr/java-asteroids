package sample;

import javafx.scene.shape.Polygon;

import java.util.Random;

public class Alien extends Character{

    public Alien(int x, int y, int level){
        super(new Polygon(12,12,-12,12,-18,2,-8,2,0,-12,8,2,18,2),x,y);
        super.getCharacter().setRotate(0);

        Random rand = new Random();
        int accelerationValue = level + 10 + rand.nextInt(5);
        for (int i = 0; i < accelerationValue; i++){
            accelerate();
        }

    }
}
