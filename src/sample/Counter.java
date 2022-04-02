package sample;

import java.util.concurrent.atomic.AtomicInteger;

public interface Counter {
    void increaseLevel();
    void decreaseLives();
    AtomicInteger getText();
}
