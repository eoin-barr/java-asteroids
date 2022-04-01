package sample;

import javafx.scene.text.Text;

import java.util.concurrent.atomic.AtomicInteger;

public class ValueCounter {
    private Text text;
    private AtomicInteger value;

    // Assigns variables
    public ValueCounter(Text text){
        this.value = new AtomicInteger();
        this.text = text;
    }

    // Method increases points by the integer argument
    public void increasePoints(int points_to_add){
        text.setText("Points: " + value.addAndGet(points_to_add));
    }

    // Method increases the level by 1
    public void increaseLevel(){
        text.setText("Level: " + value.addAndGet(1));
    }

    // Method decreases the lives by 1
    public void decreaseLives(){
        text.setText("Lives: " + value.decrementAndGet());
    }

    // Method increases the lives by the integer argument provided
    public void increaseLives(int livesToAdd){
        text.setText("Lives: " + value.addAndGet(livesToAdd));
    }

    // Method returns the value of the text
    public AtomicInteger getText(){
        return value;
    }
}