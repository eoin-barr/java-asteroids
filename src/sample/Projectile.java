package sample;

import javafx.scene.shape.Polygon;

enum ProjectileType {
    SHIP,
    ALIEN
}

public class Projectile extends Character{
    public Projectile(int x, int y, ProjectileType type){
        super(new Polygon(2, -2, 2, 2, -2, 2, -2, -2), x, y);
        super.setAsteroidType(type);
    }


    @Override
    public void move() {
        super.getCharacter().setTranslateX(super.getCharacter().getTranslateX() + super.getMovement().getX());
        super.getCharacter().setTranslateY(super.getCharacter().getTranslateY() + super.getMovement().getY());

        // Removes projectiles once it leaves the bounds bounds of the window
        if(super.getCharacter().getTranslateX()>GameView.gameScreenWidth){
            setAlive(false);
        }

        if(super.getCharacter().getTranslateX()<0){
            setAlive(false);
        }

        if(super.getCharacter().getTranslateY()>GameView.gameScreenHeight){
            setAlive(false);
        }

        if(super.getCharacter().getTranslateY()<0){
            setAlive(false);
        }

    }
}