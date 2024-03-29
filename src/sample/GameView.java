package sample;

import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.fxml.FXMLLoader;
import javafx.scene.text.Text;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedWriter;

public class GameView implements Game {
    private Scene gameScene;
    static int gameViewWidth;
    static int gameViewHeight;

    public GameView(int width, int height){
        gameViewWidth = width;
        gameViewHeight = height;
    }
    
    public void setupGameView() {
        BorderPane gameView = new BorderPane();
        gameScene = new Scene(gameView);
        GameFunctionality game = new GameFunctionality(gameScene,this);
        gameView.setPrefSize(gameViewWidth, gameViewHeight);
        Text mainMenu = new Text(10, 80, "Main Menu");

        mainMenu.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Window stage = mainMenu.getScene().getWindow();
                stage.hide();
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("scenes/MainMenu.fxml"));
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

        game.configureGameComponents(pointsCounter, levelsCounter, livesCounter);

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
            fxmlLoader.setLocation(getClass().getResource("scenes/GameOver.fxml"));
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

    public int getGameViewWidth(){
        return gameViewWidth;
    }

    public int getGameViewHeight(){
        return gameViewHeight;
    }

    public Scene getGameScene(){
        return gameScene;
    }
}
