package sample;

import javafx.animation.AnimationTimer;
import javafx.animation.PathTransition;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class GameMechanic {
    private GameView gameSettings;
    private Pane gamePane;
    private Scene gameScene;
    private List<Character> asteroids;
    private List<Character> projectiles;
    private Character ship;
    private PointsCounter pointsCounter;

    public GameMechanic(Scene scene, GameView gameView) {
        this.gameSettings = gameView;
        asteroids = new ArrayList<>();
        projectiles = new ArrayList<>();
        this.gamePane = new Pane();
        gamePane.setPrefSize(gameSettings.getGameScreenWidth(), gameSettings.getGameScreenHeight());
        this.gameScene = scene;
    }


    public void setupGameComponents(PointsCounter pointsCounter) {
        this.pointsCounter = pointsCounter;

        spawnInitialAsteroids();

        ship = new Ship(gameSettings.getGameScreenWidth() / 2, gameSettings.getGameScreenHeight() / 2);
        asteroids.forEach(asteroid -> gamePane.getChildren().add(asteroid.getCharacter()));
        gamePane.getChildren().add(ship.getCharacter());

        Map<KeyCode, Boolean> pressedKeys = readKeyboardInput();

        startGame(pressedKeys);


    }

    public void startGame(Map<KeyCode, Boolean> pressedKeys) {
        new AnimationTimer() {
            public void handle(long now) {
                if (pressedKeys.getOrDefault(KeyCode.LEFT, false)) {
                    ship.turnLeft();
                }
                if (pressedKeys.getOrDefault(KeyCode.RIGHT, false)) {
                    ship.turnRight();
                }
                if (pressedKeys.getOrDefault(KeyCode.UP, false)) {
                    ship.accelerate();
                }
                if (pressedKeys.getOrDefault(KeyCode.DOWN, false)) {
                    ship.decelerate();
                }
                if (pressedKeys.getOrDefault(KeyCode.H, false)){
                    ship.hyperJump(asteroids);
                    pressedKeys.put(KeyCode.H, false);
                }



                if (pressedKeys.getOrDefault(KeyCode.SPACE, false) && projectiles.size() < 5) {
                    Projectile projectile = new Projectile((int) ship.getCharacter().getTranslateX(), (int) ship.getCharacter().getTranslateY());
                    projectile.getCharacter().setRotate(ship.getCharacter().getRotate());
                    projectile.setAlive(true);
                    projectiles.add(projectile);

                    setProjectileSpeed(projectile);

                    gamePane.getChildren().add(projectile.getCharacter());
                    pressedKeys.put(KeyCode.SPACE, false);
                }

                ensureCharactersMovement();

                if(checkForShipCollision()){
                    stop();
                }

                checkForProjectileCollision();

                deleteDeadCharacters(projectiles);
                deleteDeadCharacters(asteroids);
//                spawnAdditionalAsteroid();

            }
        }.start();
    }

    public void deleteDeadCharacters(List<Character> listOfCharacters) {
        listOfCharacters.stream()
                .filter(character -> !character.isAlive())
                .forEach(character -> {
                    gamePane.getChildren().remove(character.getCharacter());
                });
        listOfCharacters.removeAll(listOfCharacters.stream()
                .filter(character -> !character.isAlive())
                .collect(Collectors.toList()));
    }

    public Map<KeyCode, Boolean> readKeyboardInput() {
        Map<KeyCode, Boolean> pressedKeys = new HashMap<>();
        gameScene.setOnKeyPressed(keyEvent -> {
            pressedKeys.put(keyEvent.getCode(), true);
        });

        gameScene.setOnKeyReleased(keyEvent -> {
            pressedKeys.put(keyEvent.getCode(), false);
        });
        return pressedKeys;
    }

    public void spawnAdditionalAsteroid() {
        if (Math.random() < 0.005) {
            Random rand = new Random();
            Asteroid asteroid = new Asteroid(rand.nextInt(gameSettings.getGameScreenWidth()), rand.nextInt(gameSettings.getGameScreenHeight()), AsteroidSize.LARGE);
            if (!asteroid.collide(ship)) {
                asteroids.add(asteroid);
                asteroid.setAlive(true);
                gamePane.getChildren().add(asteroid.getCharacter());
            }
        }
    }

    public void spawnInitialAsteroids() {
        asteroids = new ArrayList<>();
        for (int i = 0; i <= 3; i++) {
            Random rand = new Random();
            Asteroid asteroid = new Asteroid(rand.nextInt(gameSettings.getGameScreenWidth()), rand.nextInt(gameSettings.getGameScreenHeight()), AsteroidSize.SMALL);
            asteroids.add(asteroid);
            asteroid.setAlive(true);
        }
    }

    public void setProjectileSpeed(Character projectile) {
        projectile.accelerate();
        projectile.setMovement(projectile.getMovement().normalize().multiply(4));
    }

    public void ensureCharactersMovement() {
        ship.move();
        asteroids.forEach(asteroid -> asteroid.move());
        projectiles.forEach(projectile -> projectile.move());
    }

    public boolean checkForShipCollision(){
        AtomicBoolean ifTrue = new AtomicBoolean(false);
        asteroids.forEach(asteroid -> {
            if (asteroid.collide(ship)) {
                ifTrue.set(true);
            }
        });
        return ifTrue.get();
    }

    public void checkForProjectileCollision(){
        projectiles.forEach(projectile -> {
            asteroids.forEach(asteroid -> {
                if (projectile.collide(asteroid)) {
                    projectile.setAlive(false);
                    asteroid.setAlive(false);
                    pointsCounter.increasePoints(getScore(asteroid.getSize()));
                }
            });
        });
    }

    public int getScore(AsteroidSize size){
        System.out.println(size);
        if (size == AsteroidSize.LARGE){
            return 200;
        }else if (size == AsteroidSize.MEDIUM){
            return 500;
        }else {
            return 1000;
        }
    }

    public Pane getGamePane() {
        return gamePane;
    }
}
