package sample;

import javafx.stage.Stage;
import javafx.application.Application;

public class AsteroidsApp extends Application {
    // Creating the stage
    static Stage classStage = new Stage();

    @Override
    public void start(Stage window) {
        // Creating the game view
        GameView gameView = new GameView(700, 500);
        // Initialising the game view
        gameView.setupGameView();
        // Setting the window title
        window.setTitle("Asteroids");
        // Setting the scene to the window
        window.setScene(gameView.getGameScene());
        window.show();
    }

    public static void main(String[] args) {
        launch(AsteroidsApp.class);
    }
}
