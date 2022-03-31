package sample.scenes;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;


public class InstructionsController {
    @FXML private Button mainMenuButton;

    public void navigateMainMenu() throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("mainmenu.fxml"));
        Stage window = (Stage) mainMenuButton.getScene().getWindow();
        window.setScene(new Scene(root, 700, 500));
    }

}
