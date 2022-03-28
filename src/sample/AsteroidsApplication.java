package sample;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class AsteroidsApplication extends Application {

    static Stage classStage = new Stage();

    @Override
    public void start(Stage window) {
        GameView gameView = new GameView(700, 500);
        gameView.setupGameView();

        window.setTitle("Asteroids");
        window.setScene(gameView.getGameScene());
        window.show();
    }

    public static void main(String[] args) {
        launch(AsteroidsApplication.class);
    }


}
