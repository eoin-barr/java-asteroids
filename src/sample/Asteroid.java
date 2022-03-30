package sample;

import java.util.Random;

enum AsteroidSize {
    SMALL,
    MEDIUM,
    LARGE
}

public class Asteroid extends Character {

    private double rotationalMovement;

    public Asteroid(int x, int y, AsteroidSize size) {
        super(PolygonFactory.createPolygon(size), x, y);

        Random rand = new Random();
        int base;

        if (size == AsteroidSize.LARGE){
            base = 10;
        }else if (size == AsteroidSize.MEDIUM){
            base = 20;
        }else {
            base = 30;
        }

        super.setSize(size);


        // Adjust Acceleration based on size
        super.getCharacter().setRotate(rand.nextInt(360));
        int accelerationValue = base + rand.nextInt(5);
        for(int i = 0; i<accelerationValue;i++){
            accelerate();
        }

        this.rotationalMovement = 0.5-rand.nextDouble();

    }


    public void move(){
        super.move();
        super.getCharacter().setRotate(super.getCharacter().getRotate()+rotationalMovement);
    }


}
