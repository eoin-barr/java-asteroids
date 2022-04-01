package sample;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class Ship extends Character{

    public Ship(int x, int y){
        // Creates a new ship
        super(new Polygon(-5,-5,10,0,-5,5),x,y, Color.HOTPINK);
    }
}
