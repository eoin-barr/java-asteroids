package sample;

import javafx.scene.text.Text;

import java.util.concurrent.atomic.AtomicInteger;

public class PointsCounter {
    private AtomicInteger points;
    private Text text;

    public PointsCounter(Text text){
        this.points = new AtomicInteger();
        this.text = text;
    }

    public void increasePoints(int points_to_add){
        text.setText("Points: " + points.addAndGet(points_to_add));
    }
}