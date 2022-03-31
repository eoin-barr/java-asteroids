package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Window;
import sample.AsteroidsApplication;

public class Controller extends AsteroidsApplication {
    @FXML public Button gameButton;

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

}
