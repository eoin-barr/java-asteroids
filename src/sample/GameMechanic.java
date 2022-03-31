package sample;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.input.KeyCode;
import javafx.animation.AnimationTimer;

import java.util.*;
import java.util.stream.Collectors;
import java.util.concurrent.atomic.AtomicBoolean;

public class GameMechanic {
    private int level;
    private int lives;
    private Pane gamePane;
    private Character ship;
    private Character alien;
    private Scene gameScene;
    private GameView gameSettings;
    private List<Character> asteroids;
    private ValueCounter livesCounter;
    private ValueCounter pointsCounter;
    private ValueCounter levelsCounter;
    private List<Character> projectiles;

    public GameMechanic(Scene scene, GameView gameView) {
        this.gameSettings = gameView;
        asteroids = new ArrayList<>();
        projectiles = new ArrayList<>();
        this.gamePane = new Pane();
        gamePane.setPrefSize(gameSettings.getGameScreenWidth(), gameSettings.getGameScreenHeight());
        this.gameScene = scene;
    }

    public void setupGameComponents(ValueCounter pointsCounter, ValueCounter levelsCounter, ValueCounter livesCounter) {
        this.pointsCounter = pointsCounter;
        this.levelsCounter = levelsCounter;
        this.livesCounter = livesCounter;
        levelsCounter.increaseLevel();
        livesCounter.increaseLives(2);
        this.level = 1;
        this.lives = 2;

        Random rand = new Random();
        spawnInitialAsteroids();

        ship = new Ship(gameSettings.getGameScreenWidth() / 2, gameSettings.getGameScreenHeight() / 2);
        alien = new Alien(rand.nextInt(gameSettings.getGameScreenWidth()), rand.nextInt(gameSettings.getGameScreenHeight()), level);
        alien.setAlive(false);
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
                    Projectile projectile = new Projectile((int) ship.getCharacter().getTranslateX(), (int) ship.getCharacter().getTranslateY(), ProjectileType.SHIP);
                    projectile.getCharacter().setRotate(ship.getCharacter().getRotate());
                    projectile.setAlive(true);
                    projectiles.add(projectile);
                    setProjectileSpeed(projectile);
                    gamePane.getChildren().add(projectile.getCharacter());
                    pressedKeys.put(KeyCode.SPACE, false);
                }
                if (lives < 1){
                    gameSettings.addScoreToFile(pointsCounter.getText().toString());
                    stop();
                    gameSettings.navigateToGameOver();
                }

                ensureCharactersMovement();
                handleShipCollision();
                levelUp();
                handleAlienCreation();
                deleteDeadCharacters(projectiles);
                deleteDeadCharacters(asteroids);
                checkForProjectileCollision();
            }
        }.start();
    }

    public void ensureCharactersMovement() {
        if (alien.isAlive()){
            alien.move();
        }
        ship.move();
        asteroids.forEach(asteroid -> asteroid.move());
        projectiles.forEach(projectile -> projectile.move());
    }

    public void handleShipCollision(){
        if(checkForShipCollision()){
            lives -= 1;
            livesCounter.decreaseLives();
            ship.hyperJump(asteroids);
        }
    }

    public void levelUp(){
        if (asteroids.size() < 1){
            if (alien.isAlive()){
                alien.setAlive(false);
                gamePane.getChildren().remove(alien.getCharacter());
            }
            levelsCounter.increaseLevel();
            level += 1;
            for (int i = 0; i < level; i++){
                Random rand = new Random();
                Asteroid newAsteroid = new Asteroid(rand.nextInt(gameSettings.getGameScreenWidth()), rand.nextInt(gameSettings.getGameScreenHeight()), AsteroidSize.LARGE, level);
                if (!newAsteroid.collide(ship)) {
                    asteroids.add(newAsteroid);
                    newAsteroid.setAlive(true);
                    gamePane.getChildren().add(newAsteroid.getCharacter());
                }
            }
        }
    }

    public void handleAlienCreation(){
        if (!alien.isAlive()){
            spawnAlien();
        } else {
            if (Math.random() < 0.007){
                createAlienProjectile();
            }
        }
    }

    public void createAlienProjectile(){
        Projectile projectile = new Projectile((int) alien.getCharacter().getTranslateX(), (int) alien.getCharacter().getTranslateY(), ProjectileType.ALIEN);
        double nextX = ship.getCharacter().getTranslateX();
        double currentX = alien.getCharacter().getTranslateX();
        double nextY = ship.getCharacter().getTranslateY();
        double currentY = alien.getCharacter().getTranslateY();
        double deltaX = nextX - currentX;
        double deltaY = nextY - currentY;
        double theta = Math.atan2(deltaY, deltaX);
        projectile.getCharacter().setRotate(theta*180/Math.PI);
        projectile.setAlive(true);
        projectiles.add(projectile);
        setProjectileSpeed(projectile);
        gamePane.getChildren().add(projectile.getCharacter());
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
            Asteroid asteroid = new Asteroid(rand.nextInt(gameSettings.getGameScreenWidth()), rand.nextInt(gameSettings.getGameScreenHeight()), AsteroidSize.LARGE, level);
            if (!asteroid.collide(ship)) {
                asteroids.add(asteroid);
                asteroid.setAlive(true);
                gamePane.getChildren().add(asteroid.getCharacter());
            }
        }
    }

    public void spawnAlien(){
        if (Math.random() < 0.003){
            Random rand = new Random();
            alien = new Alien(rand.nextInt(gameSettings.getGameScreenWidth()), rand.nextInt(gameSettings.getGameScreenHeight()), level);
            alien.setAlive(true);
            gamePane.getChildren().add(alien.getCharacter());
        }
    }

    public void spawnInitialAsteroids() {
        asteroids = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            Random rand = new Random();
            Asteroid asteroid = new Asteroid(rand.nextInt(gameSettings.getGameScreenWidth()), rand.nextInt(gameSettings.getGameScreenHeight()), AsteroidSize.LARGE, level);
            asteroids.add(asteroid);
            asteroid.setAlive(true);
        }
    }

    public void setProjectileSpeed(Character projectile) {
        projectile.accelerate();
        projectile.setMovement(projectile.getMovement().normalize().multiply(4));
    }

    public boolean checkForShipCollision(){
        AtomicBoolean ifTrue = new AtomicBoolean(false);
        if (alien.isAlive()){
            if (alien.collide(ship)){
                ifTrue.set(true);
            }
        }
        asteroids.forEach(asteroid -> {
            if (asteroid.collide(ship)) {
                ifTrue.set(true);
            }
        });
        projectiles.forEach(projectile -> {
            if (projectile.getProjectileType() == ProjectileType.ALIEN && projectile.collide(ship)){
                projectile.setAlive(false);
                ifTrue.set(true);
            }
        });
        return ifTrue.get();
    }

    public void checkForProjectileCollision(){
        projectiles.forEach(projectile -> {
            if (alien.isAlive()){
                if (projectile.getProjectileType() == ProjectileType.SHIP && projectile.collide(alien)){
                    alien.setAlive(false);
                    projectile.setAlive(false);
                    pointsCounter.increasePoints(1500);
                    gamePane.getChildren().remove(alien.getCharacter());
                }
            }
            asteroids.forEach(asteroid -> {
                if (projectile.getProjectileType() == ProjectileType.SHIP && projectile.collide(asteroid)) {
                    projectile.setAlive(false);
                    int x = (int) asteroid.getCharacter().getTranslateX();
                    int y = (int) asteroid.getCharacter().getTranslateY();
                    AsteroidSize asteroidSize = asteroid.getSize();
                    asteroid.setAlive(false);
                    pointsCounter.increasePoints(getScore(asteroid.getSize()));
                    AsteroidSize size;
                    for (int i = 0; i < 2; i++){
                        if (asteroidSize == AsteroidSize.LARGE){
                            size = AsteroidSize.MEDIUM;
                        }else if (asteroidSize == AsteroidSize.MEDIUM){
                            size = AsteroidSize.SMALL;
                        }else {
                            break;
                        }
                        Asteroid newAsteroid = new Asteroid(x, y, size, level);
                        if (!newAsteroid.collide(ship)) {
                            asteroids.add(newAsteroid);
                            newAsteroid.setAlive(true);
                            gamePane.getChildren().add(newAsteroid.getCharacter());
                        }
                    }
                }
                if (projectile.getProjectileType() == ProjectileType.ALIEN && projectile.collide(asteroid)){
                    projectile.setAlive(false);
                }
            });
        });
    }

    public int getScore(AsteroidSize size){
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
