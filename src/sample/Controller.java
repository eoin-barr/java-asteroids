package sample;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import sample.AsteroidsApplication;

public class Controller extends AsteroidsApplication {
    @FXML public Button gameButton, highScoresButton, instructionsButton;

    public void navigateGame() throws Exception {
        gameButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                AsteroidsApplication aa = new AsteroidsApplication();
                aa.start(AsteroidsApplication.classStage);
                Window stage = gameButton.getScene().getWindow();
                stage.hide();
            }
        });
    }

    public void navigateHighScores() throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("scenes/HighScores.fxml"));
        Stage window = (Stage) highScoresButton.getScene().getWindow();
        window.setScene(new Scene(root, 700, 500));
    }

    public void navigateInstructions() throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("scenes/Instructions.fxml"));
        Stage window = (Stage) instructionsButton.getScene().getWindow();
        window.setScene(new Scene(root, 700, 500));
    }

}
