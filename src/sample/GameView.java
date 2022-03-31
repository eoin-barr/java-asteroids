package sample;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class GameView {
    private Scene gameScene;
    static int gameScreenWidth;
    static int gameScreenHeight;

    public GameView(int width, int height){
        gameScreenWidth = width;
        gameScreenHeight = height;
    }



    public void setupGameView() {
        BorderPane gameView = new BorderPane();
        gameScene = new Scene(gameView);
        GameMechanic game = new GameMechanic(gameScene,this);
        gameView.setPrefSize(gameScreenWidth,gameScreenHeight);



        Text mainMenu = new Text(10, 80, "Main Menu");
        mainMenu.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Window stage = mainMenu.getScene().getWindow();
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
        Text pointText = new Text(10, 20, "Points: 0");
        ValueCounter pointsCounter = new ValueCounter(pointText);

        Text levelsText = new Text(10, 40, "Level: 1");
        ValueCounter levelsCounter = new ValueCounter(levelsText);

        Text livesText = new Text(10, 60, "Lives: 0");
        ValueCounter livesCounter = new ValueCounter(livesText);

        game.setupGameComponents(pointsCounter, levelsCounter, livesCounter);

        gameView.getChildren().add(game.getGamePane());
        gameView.getChildren().add(pointText);
        gameView.getChildren().add(levelsText);
        gameView.getChildren().add(livesText);
        gameView.getChildren().add(mainMenu);

    }

    public void navigateToGameOver(){
        Window stage = gameScene.getWindow();
        stage.hide();
        try{
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("scenes/gameOver.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 700, 500);
            Stage stage2 = new Stage();
            stage2.setTitle("Asteroids");
            stage2.setScene(scene);
            stage2.show();
        }catch (Exception e){
            System.out.println(e);
        }
    }


    public void addScoreToFile(String score){
        try (FileWriter fw = new FileWriter("scores.txt", true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw))
        {
            out.println(score);
        } catch (Exception e){
            System.out.println(e);
        }
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
