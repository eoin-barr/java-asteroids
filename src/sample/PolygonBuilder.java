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
        Random random = new Random();
        double size = base + random.nextInt(5);

        // Assigning polygon to a new Polygon
        Polygon polygon = new Polygon();
        double a1 = Math.cos(Math.PI * 2 / 5);
        double a2 = Math.cos(Math.PI / 5);
        double b1 = Math.sin(Math.PI * 2 / 5);
        double b2 = Math.sin(Math.PI * 4 / 5);

        // Adding the points to the Polygon
        polygon.getPoints().addAll(
                size, 0.0,
                size * a1, -1 * size * b1,
                -1 * size * a2, -1 * size * b2,
                -1 * size * a2, size * b2,
                size * a1, size * b1);

        for (int i = 0; i < polygon.getPoints().size(); i++) {
            // Adding random component to the polygon points
            int change = random.nextInt(5) - 2;
            polygon.getPoints().set(i, polygon.getPoints().get(i) + change);
        }

        return polygon;
    }
}
