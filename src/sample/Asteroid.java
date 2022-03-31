package sample;

import java.util.Random;

enum AsteroidSize {
    SMALL,
    MEDIUM,
    LARGE
}

public class Asteroid extends Character {

    private double rotationalMovement;

    public Asteroid(int x, int y, AsteroidSize size, int level) {
        super(PolygonFactory.createPolygon(size), x, y);
        super.setSize(size);

        Random rand = new Random();
        int base;
        level *= 2;
        if (size == AsteroidSize.LARGE){
            base = 5;
        }else if (size == AsteroidSize.MEDIUM){
            base = 10;
        }else {
            base = 15;
        }

        super.getCharacter().setRotate(rand.nextInt(360));
        int accelerationValue = level + base + rand.nextInt(5);
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
