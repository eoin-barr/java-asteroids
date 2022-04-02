package sample;

import javafx.scene.Scene;

public interface Game {
    void setupGameView();
    Scene getGameScene();
    int getGameScreenWidth();
    int getGameScreenHeight();
    void navigateToGameOver();
}
