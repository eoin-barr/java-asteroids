package sample;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class Projectile extends Component{
    public Projectile(int x, int y, ProjectileType type){
        // Creates a new projectile
        super(new Polygon(2, -2, 2, 2, -2, 2, -2, -2), x, y, Color.BLACK);

        // Setting the projectile type
        super.setProjectileType(type);
    }

    @Override
    public void move() {
        super.getComponent().setTranslateX(super.getComponent().getTranslateX() + super.getMovement().getX());
        super.getComponent().setTranslateY(super.getComponent().getTranslateY() + super.getMovement().getY());

        // Removes projectile if leaves right side of the game screen
        if(super.getComponent().getTranslateX() > GameView.gameViewWidth){
            setAlive(false);
        }

        // Removes projectile if leaves left side of the game screen
        if(super.getComponent().getTranslateX() < 0){
            setAlive(false);
        }

        // Removes projectile if leaves top of the game screen
        if(super.getComponent().getTranslateY() > GameView.gameViewHeight){
            setAlive(false);
        }

        // Removes projectile if leaves bottom of the game screen
        if(super.getComponent().getTranslateY() < 0){
            setAlive(false);
        }

    }
}