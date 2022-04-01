package sample.scenes;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;


public class InstructionsController {
    @FXML private Button mainMenuButton;

    // Method which handles mainMenuButton clicks and navigates to the MainMenu screen
    public void navigateMainMenu() throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
        Stage window = (Stage) mainMenuButton.getScene().getWindow();
        window.setScene(new Scene(root, 700, 500));
    }

}
