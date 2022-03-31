package sample;

import javafx.scene.text.Text;

import java.util.concurrent.atomic.AtomicInteger;

public class ValueCounter {
    private Text text;
    private AtomicInteger value;

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

    public void decreaseLives(){
        text.setText("Lives: " + value.decrementAndGet());
    }

    public void increaseLives(int livesToAdd){
        text.setText("Lives: " + value.addAndGet(livesToAdd));
    }

    public AtomicInteger getText(){
        return value;
    }
}