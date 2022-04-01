package sample;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

enum ProjectileType {
    SHIP,
    ALIEN
}

public class Projectile extends Character{
    public Projectile(int x, int y, ProjectileType type){
        // Creates a new projectile
        super(new Polygon(2, -2, 2, 2, -2, 2, -2, -2), x, y, Color.BLACK);

        // Setting the projectile type
        super.setProjectileType(type);
    }

    @Override
    public void move() {
        super.getCharacter().setTranslateX(super.getCharacter().getTranslateX() + super.getMovement().getX());
        super.getCharacter().setTranslateY(super.getCharacter().getTranslateY() + super.getMovement().getY());

        // Removes projectile if leaves right side of the game screen
        if(super.getCharacter().getTranslateX()>GameView.gameScreenWidth){
            setAlive(false);
        }

        // Removes projectile if leaves left side of the game screen
        if(super.getCharacter().getTranslateX()<0){
            setAlive(false);
        }

        // Removes projectile if leaves top of the game screen
        if(super.getCharacter().getTranslateY()>GameView.gameScreenHeight){
            setAlive(false);
        }

        // Removes projectile if leaves bottom of the game screen
        if(super.getCharacter().getTranslateY()<0){
            setAlive(false);
        }

    }
}