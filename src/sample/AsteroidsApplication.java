package sample;

import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.application.Application;

public class AsteroidsApplication extends Application {

    static Stage classStage = new Stage();

    @Override
    public void start(Stage window) {
        GameView gameView = new GameView(700, 500, Color.BLACK);
        gameView.setupGameView();

        window.setTitle("Asteroids");
        window.setScene(gameView.getGameScene());
        window.show();
    }

    public static void main(String[] args) {
        launch(AsteroidsApplication.class);
    }
}
