package sample;

import javafx.scene.Scene;

public interface Game {
    void setupGameView();
    Scene getGameScene();
    int getGameViewWidth();
    int getGameViewHeight();
    void navigateToGameOver();
}
