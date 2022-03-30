package sample;

import javafx.scene.text.Text;

import java.util.concurrent.atomic.AtomicInteger;

public class ValueCounter {
    private AtomicInteger value;
    private Text text;

    public ValueCounter(Text text){
        this.value = new AtomicInteger();
        this.text = text;
    }

    public void increasePoints(int points_to_add){
        text.setText("Points: " + value.addAndGet(points_to_add));
    }

    public void increaseLevel(){
        text.setText("Level: " + value.addAndGet(1));
    }
}