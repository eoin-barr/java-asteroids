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

    public void increasePoints(){
        text.setText("Points: " + points.addAndGet(1000));
    }
}