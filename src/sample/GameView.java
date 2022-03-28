package sample;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

public class GameView {
    private Scene gameScene;
    static int gameScreenWidth;
    static int gameScreenHeight;

    public GameView(int width, int height){
        gameScreenWidth = width;
        gameScreenHeight = height;
    }

    @FXML Button gameButton;


    public void setupGameView() {
        BorderPane gameView = new BorderPane();
        gameScene = new Scene(gameView);
        GameMechanic game = new GameMechanic(gameScene,this);
        gameView.setPrefSize(gameScreenWidth,gameScreenHeight);


        Text text2 = new Text(10, 40, "Main Menu");
        text2.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try (FileWriter fw = new FileWriter("scores.txt", true);
                     BufferedWriter bw = new BufferedWriter(fw);
                     PrintWriter out = new PrintWriter(bw))
                {
                    String score = "10,000";
                    System.out.println(score);
                    out.println(score);
                } catch (Exception e){
                    System.out.println(e);
                }

                Window stage = text2.getScene().getWindow();
                stage.hide();
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("sample.fxml"));
                    /*
                     * if "fx:controller" is not set in fxml
                     * fxmlLoader.setController(NewWindowController);
                     */
                    Scene scene = new Scene(fxmlLoader.load(), 700, 500);
                    Stage stage2 = new Stage();
                    stage2.setTitle("Asteroids");
                    stage2.setScene(scene);
                    stage2.show();
                } catch (IOException e) {
                    System.out.println("Error");
                }

            }
        });
        Text text = new Text(10, 20, "Points: 0");
        PointsCounter pointsCounter = new PointsCounter(text);

        game.setupGameComponents(pointsCounter);

        gameView.getChildren().add(game.getGamePane());
        gameView.getChildren().add(text);
        gameView.getChildren().add(text2);

    }

    public int getGameScreenWidth(){
        return gameScreenWidth;
    }
    public int getGameScreenHeight(){
        return gameScreenHeight;
    }


    public Scene getGameScene(){
        return gameScene;
    }
}
