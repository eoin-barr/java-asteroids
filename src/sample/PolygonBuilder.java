package sample;

import javafx.scene.shape.Polygon;

import java.util.Random;

public class PolygonBuilder {

    public static Polygon createPolygon(AsteroidSize asteroidSize) {

        // base variable provides a base size to each polygon depending on its size
        int base;
        if (asteroidSize == AsteroidSize.LARGE){
            base = 30;
        } else if (asteroidSize == AsteroidSize.MEDIUM){
            base = 20;
        }else {
            base = 10;
        }

        // rnd variable adds a random size to each polygon
        Random rnd = new Random();
        double size = base + rnd.nextInt(5);

        // Assigning polygon to a new Polygon
        Polygon polygon = new Polygon();
        double c1 = Math.cos(Math.PI * 2 / 5);
        double c2 = Math.cos(Math.PI / 5);
        double s1 = Math.sin(Math.PI * 2 / 5);
        double s2 = Math.sin(Math.PI * 4 / 5);

        // Adding the points to the Polygon
        polygon.getPoints().addAll(
                size, 0.0,
                size * c1, -1 * size * s1,
                -1 * size * c2, -1 * size * s2,
                -1 * size * c2, size * s2,
                size * c1, size * s1);

        for (int i = 0; i < polygon.getPoints().size(); i++) {
            // Adding random component to the polygon points
            int change = rnd.nextInt(5) - 2;
            polygon.getPoints().set(i, polygon.getPoints().get(i) + change);
        }

        return polygon;
    }
}
