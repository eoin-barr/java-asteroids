package sample.scenes;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.collections.FXCollections;

import java.net.URL;
import java.util.List;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.math.BigInteger;
import java.util.Collections;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class HighScoresController implements Initializable{
    @FXML private Button mainMenuButton;
    @FXML private ListView fxmlList;
    @Override
    public void initialize(URL url, ResourceBundle rb){
        List<String> myList;
        try{
            myList = Files.lines(Paths.get("scores.txt")).collect(Collectors.toList());
//            fxmlList.setStyle("-fx-control-inner-background: black; -fx-background-radius: 20;");
            fxmlList.setItems(FXCollections.observableArrayList(sortData(myList)));

        }catch (IOException e){
            System.out.println("Couldn't find file");
        }
    }

    public void navigateMainMenu() throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("mainmenu.fxml"));
        Stage window = (Stage) mainMenuButton.getScene().getWindow();
        window.setScene(new Scene(root, 700, 500));
    }

    public static List<String> sortData(List<String> data) {
        List<BigInteger> convertData = new ArrayList<BigInteger>();
        for (String s : data){
            convertData.add(new BigInteger(s));
        }
        Collections.sort(convertData, Collections.reverseOrder());
        List<String>sortedData=new ArrayList<String>();

        for (BigInteger b : convertData){
            sortedData.add(String.valueOf(b));
        }
        return sortedData;
    }
}
