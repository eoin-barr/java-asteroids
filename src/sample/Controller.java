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

public class Controller extends AsteroidsApp {
    @FXML public Button gameButton, highScoresButton, instructionsButton;

    // Method which handles gameButton clicks and runs the AsteroidsApp
    public void navigateGame() throws Exception {
        gameButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                AsteroidsApp aa = new AsteroidsApp();
                aa.start(AsteroidsApp.classStage);
                Window stage = gameButton.getScene().getWindow();
                stage.hide();
            }
        });
    }

    // Method which handles highScoresButton clicks and navigates to the HighScore screen
    public void navigateHighScores() throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("scenes/HighScores.fxml"));
        Stage window = (Stage) highScoresButton.getScene().getWindow();
        window.setScene(new Scene(root, 700, 500));
    }

    // Method which handles instructionsButton clicks and navigates to the Instructions screen
    public void navigateInstructions() throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("scenes/Instructions.fxml"));
        Stage window = (Stage) instructionsButton.getScene().getWindow();
        window.setScene(new Scene(root, 700, 500));
    }

}
