package sample;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.input.KeyCode;
import javafx.animation.AnimationTimer;

import java.util.*;
import java.util.stream.Collectors;
import java.util.concurrent.atomic.AtomicBoolean;

public class GameFunctionality {
    // Initialise game instance variables
    private int level;
    private int lives;
    private Pane gamePane;
    private Component ship;
    private Component alien;
    private Scene gameScene;
    private GameView gameSettings;
    private List<Component> asteroids;
    private ValueCounter livesCounter;
    private ValueCounter pointsCounter;
    private ValueCounter levelsCounter;
    private List<Component> projectiles;

    public GameFunctionality(Scene scene, GameView gameView) {
        // Assign game variables
        this.gameSettings = gameView;
        asteroids = new ArrayList<>();
        projectiles = new ArrayList<>();
        this.gamePane = new Pane();
        gamePane.setPrefSize(gameSettings.getGameViewWidth(), gameSettings.getGameViewHeight());
        this.gameScene = scene;
    }

    public void configureGameComponents(ValueCounter pointsCounter, ValueCounter levelsCounter, ValueCounter livesCounter) {
        // Assign game variables
        this.pointsCounter = pointsCounter;
        this.levelsCounter = levelsCounter;
        this.livesCounter = livesCounter;
        levelsCounter.increaseLevel();
        livesCounter.increaseLives(2);
        this.level = 1;
        this.lives = 2;

        Random rand = new Random();
        createInitialAsteroids();

        ship = new Ship(gameSettings.getGameViewWidth() / 2, gameSettings.getGameViewHeight() / 2);
        alien = new Alien(rand.nextInt(gameSettings.getGameViewWidth()), rand.nextInt(gameSettings.getGameViewHeight()), level);
        alien.setAlive(false);
        asteroids.forEach(asteroid -> gamePane.getChildren().add(asteroid.getComponent()));
        gamePane.getChildren().add(ship.getComponent());

        //Capture keyboard input
        Map<KeyCode, Boolean> pressedKeys = captureKeyboardInput();
        startGame(pressedKeys);
    }

    // Primary method where all the other game methods are run
    public void startGame(Map<KeyCode, Boolean> pressedKeys) {
        new AnimationTimer() {
            public void handle(long now) {
                // If left arrow key is pressed the ship turns left
                if (pressedKeys.getOrDefault(KeyCode.LEFT, false)) {
                    ship.turnLeft();
                }
                // If right arrow key is pressed the ship turns right
                if (pressedKeys.getOrDefault(KeyCode.RIGHT, false)) {
                    ship.turnRight();
                }
                // If up arrow key is pressed the ship accelerates
                if (pressedKeys.getOrDefault(KeyCode.UP, false)) {
                    ship.accelerate();
                }
                // If h key is pressed the ship hyperSpace jumps
                if (pressedKeys.getOrDefault(KeyCode.H, false)){
                    ship.hyperJump(asteroids);
                    pressedKeys.put(KeyCode.H, false);
                }
                // If q is pressed the game is terminated (score is not appended to score file)
                if (pressedKeys.getOrDefault(KeyCode.Q, false)){
                    // Game loop is terminated
                    stop();
                    // The Game over screen is opened
                    gameSettings.navigateToGameOver();
                }
                // If space bar is pressed and there are less than 5 projectiles on the screen a projectile is created
                if (pressedKeys.getOrDefault(KeyCode.SPACE, false) && projectiles.size() < 5) {
                    createProjectile(pressedKeys);
                }

                // Triggered when user has no lives left
                if (lives < 1){
                    // Score is appended to the scores.txt file
                    gameSettings.addScoreToFile(pointsCounter.getText().toString());
                    // The game loop is stopped
                    stop();
                    // The Game over screen is opened
                    gameSettings.navigateToGameOver();
                }

                guaranteeComponentsMovement();
                handleShipCollision();
                levelUp();
                handleAlienProjectileCreation();
                deleteDeadComponents(projectiles);
                deleteDeadComponents(asteroids);
//                deleteComponentsProjectiles();
//                deleteComponentsAsteroids();

                
                // Put try catch here to catch the error
                try {
                    checkForProjectileCollision();

                }catch (Exception e){
                    System.out.println(e);
                }

            }
        }.start();
    }

    // Method creates a ship projectile when called
    public void createProjectile(Map<KeyCode, Boolean> pressedKeys){
        // Creates new projectile of type ship
        Projectile projectile = new Projectile((int) ship.getComponent().getTranslateX(), (int) ship.getComponent().getTranslateY(), ProjectileType.SHIP);
        // Sets the rotation of the projectile
        projectile.getComponent().setRotate(ship.getComponent().getRotate());
        // Sets the projectile as alive
        projectile.setAlive(true);
        // Adds the projectile to the projectile array
        projectiles.add(projectile);
        // Sets the speed of the projectile
        setProjectileSpeed(projectile);
        // Adds the projectile to the game pane
        gamePane.getChildren().add(projectile.getComponent());
        // Prevents a user from holding down space and spamming projectiles
        pressedKeys.put(KeyCode.SPACE, false);
    }

    // Method ensures that alien, ship, asteroids and projectiles move correctly
    public void guaranteeComponentsMovement() {
        if (alien.isAlive()){
            alien.move();
        }
        ship.move();
        asteroids.forEach(asteroid -> asteroid.move());
        projectiles.forEach(projectile -> projectile.move());
    }

    // Method handles logic of a ship collision
    public void handleShipCollision(){
        if(checkForShipCollision()){
            lives -= 1;
            livesCounter.decreaseLives();
            // Ship is moves to a new safe location whenever it is hit
            // with an alien projectile or collides with an asteroid
            ship.hyperJump(asteroids);
        }
    }

    // Method handles the logic when a user levels up
    public void levelUp(){
        // Checks if all asteroids have been removed
        if (asteroids.size() < 1){
            if (alien.isAlive()){
                // Removes alien at end of level if alien is present
                alien.setAlive(false);
                gamePane.getChildren().remove(alien.getComponent());
            }
            // Increments the levels counters
            levelsCounter.increaseLevel();
            level += 1;
            // Creates a number of new asteroids based on the level the user is on
            for (int i = 0; i < level; i++){
                Random rand = new Random();
                Asteroid newAsteroid = new Asteroid(rand.nextInt(gameSettings.getGameViewWidth()), rand.nextInt(gameSettings.getGameViewHeight()), AsteroidSize.LARGE, level);
                if (!newAsteroid.collide(ship)) {
                    asteroids.add(newAsteroid);
                    newAsteroid.setAlive(true);
                    gamePane.getChildren().add(newAsteroid.getComponent());
                }
            }
        }
    }

    // Method handles alien creation
    public void handleAlienProjectileCreation(){
        if (!alien.isAlive()){
            createAlien();
        } else {
            // If an alien is currently present and a random number is
            // less than 0.007 a new alien projectile is created
            if (Math.random() < 0.007){
                createAlienProjectile();
            }
        }
    }

    // Method creates an alien projectile
    public void createAlienProjectile(){
        // Creates a projectile of type alien
        Projectile projectile = new Projectile((int) alien.getComponent().getTranslateX(), (int) alien.getComponent().getTranslateY(), ProjectileType.ALIEN);

        // Gather the positions of the ship and alien
        double nextX = ship.getComponent().getTranslateX();
        double currentX = alien.getComponent().getTranslateX();
        double nextY = ship.getComponent().getTranslateY();
        double currentY = alien.getComponent().getTranslateY();
        double deltaX = nextX - currentX;
        double deltaY = nextY - currentY;

        // Calculation that aims the alien projectile at the ship
        double theta = Math.atan2(deltaY, deltaX);
        projectile.getComponent().setRotate(theta*180/Math.PI);

        // Setting the projectile as alive, giving it speed
        // and adding it to the gamePane
        projectile.setAlive(true);
        projectiles.add(projectile);
        setProjectileSpeed(projectile);
        gamePane.getChildren().add(projectile.getComponent());
    }

    // Method removes dead components from the gamePane
    public void deleteDeadComponents(List<Component> ListOfComponents) {
        // Using stream to iterate through each component in the
        // theListOfComponents and if its .isAlive is false it is removed
        // from the gamePane
        ListOfComponents.stream()
                .filter(component -> !component.isAlive())
                .forEach(component -> {
                    gamePane.getChildren().remove(component.getComponent());
                });
        ListOfComponents.removeAll(ListOfComponents.stream()
                .filter(component -> !component.isAlive())
                .collect(Collectors.toList()));
    }

    public void deleteComponentsProjectiles(){
        for (Iterator<Component> p = projectiles.iterator(); p.hasNext();){
            Component projectile = p.next();
            if (!projectile.isAlive()){
                p.remove();
                gamePane.getChildren().remove(projectile.getComponent());
            }
        }
    }

    public void deleteComponentsAsteroids(){
        for (Iterator<Component> a = asteroids.iterator(); a.hasNext();){
            Component asteroid = a.next();
            if (!asteroid.isAlive()){
                a.remove();
                gamePane.getChildren().remove(asteroid.getComponent());
            }
        }
    }

    // Method captures a users input
    public Map<KeyCode, Boolean> captureKeyboardInput() {
        // Creating the pressedKeys hashMap
        Map<KeyCode, Boolean> pressedKeys = new HashMap<>();
        gameScene.setOnKeyPressed(keyEvent -> {
            pressedKeys.put(keyEvent.getCode(), true);
        });

        gameScene.setOnKeyReleased(keyEvent -> {
            pressedKeys.put(keyEvent.getCode(), false);
        });
        return pressedKeys;
    }

    // Method creates an alien
    public void createAlien(){
        // A random number is less than 0.0025 a new alien is created
        if (Math.random() < 0.0025){
            Random rand = new Random();
            alien = new Alien(rand.nextInt(gameSettings.getGameViewWidth()), rand.nextInt(gameSettings.getGameViewHeight()), level);
            alien.setAlive(true);
            gamePane.getChildren().add(alien.getComponent());
        }
    }

    // Method adds initial asteroids to the screen
    public void createInitialAsteroids() {
        asteroids = new ArrayList<>();
            Random rand = new Random();
            Asteroid asteroid = new Asteroid(rand.nextInt(gameSettings.getGameViewWidth()), rand.nextInt(gameSettings.getGameViewHeight()), AsteroidSize.LARGE, level);
            asteroids.add(asteroid);
            asteroid.setAlive(true);
    }

    // Method sets speed of a projectile
    public void setProjectileSpeed(Component projectile) {
        projectile.accelerate();
        projectile.setMovement(projectile.getMovement().normalize().multiply(4));
    }

    // Method checks for a ship collision
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

    // Method returns a score depending on the size of the asteroid
    public int getScore(AsteroidSize size){
        if (size == AsteroidSize.LARGE){
            return 200;
        }else if (size == AsteroidSize.MEDIUM){
            return 500;
        }else {
            return 1000;
        }
    }

    // Method returns the gamePane
    public Pane getGamePane() {
        return gamePane;
    }

    // Method checks for an asteroid collision
    public void checkForProjectileCollision(){
        projectiles.forEach(projectile -> {
            if (alien.isAlive()){
                if (projectile.getProjectileType() == ProjectileType.SHIP && projectile.collide(alien)){
                    alien.setAlive(false);
                    projectile.setAlive(false);
                    pointsCounter.increasePoints(1500);
                    gamePane.getChildren().remove(alien.getComponent());
                }
            }
            asteroids.forEach(asteroid -> {
                if (projectile.getProjectileType() == ProjectileType.SHIP && projectile.collide(asteroid)) {
                    projectile.setAlive(false);
                    int x = (int) asteroid.getComponent().getTranslateX();
                    int y = (int) asteroid.getComponent().getTranslateY();
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
                            gamePane.getChildren().add(newAsteroid.getComponent());
                        }
                    }
                }
                if (projectile.getProjectileType() == ProjectileType.ALIEN && projectile.collide(asteroid)){
                    projectile.setAlive(false);
                }
            });
        });
    }

    //// Attempt to use Iterator to fix warnings in console when an asteroid is broken up
    public void checkForProjectileCollision2(){
        for (Iterator<Component> p = projectiles.iterator(); p.hasNext();){
            Component projectile = p.next();
            if (alien.isAlive()){
                if (projectile.getProjectileType() == ProjectileType.SHIP && projectile.collide(alien)){
                    alien.setAlive(false);
                    projectile.setAlive(false);
                    pointsCounter.increasePoints(1500);
                    gamePane.getChildren().remove(alien.getComponent());
                }
            }
            for (Iterator<Component> a = asteroids.iterator(); a.hasNext();){
                Component asteroid = a.next();
                if (projectile.getProjectileType() == ProjectileType.SHIP && projectile.collide(asteroid)){
                    projectile.setAlive(false);
                    ////

                    try{
                        p.remove();

                        gamePane.getChildren().remove(projectile.getComponent());

                    } catch(Exception e){
                        System.out.println("1" + e);
                    }
                    //////
                    int x = (int) asteroid.getComponent().getTranslateX();
                    int y = (int) asteroid.getComponent().getTranslateY();
                    AsteroidSize asteroidSize = asteroid.getSize();
                    asteroid.setAlive(false);
                    ////
                    try{
                        a.remove();

                        gamePane.getChildren().remove(asteroid.getComponent());

                    }catch (Exception e){
                        System.out.println("2" + e);
                    }
                    ////
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
                        if (!newAsteroid.collide(ship)){
                            asteroids.add(newAsteroid);
                            newAsteroid.setAlive(true);
                            gamePane.getChildren().add(newAsteroid.getComponent());
                        }
                    }
                }
                if (projectile.getProjectileType() == ProjectileType.ALIEN && projectile.collide(asteroid)){
                    projectile.setAlive(false);
                }
            }
        }
    }

    // Attempt to use streams to fix warnings in console when an asteroid is broken up
    public void checkForProjectileCollision3(List<Component> asteroids, List <Component> projectiles){
        projectiles.stream().forEach(projectile -> {
            if (alien.isAlive()){
                if (projectile.getProjectileType() == ProjectileType.SHIP && projectile.collide(alien)){
                    alien.setAlive(false);
                    projectile.setAlive(false);
                    pointsCounter.increasePoints(1500);
                    gamePane.getChildren().remove(alien.getComponent());
                }
            }
            asteroids.stream()
                    .forEach(asteroid -> {
                        if (projectile.getProjectileType() == ProjectileType.SHIP && projectile.collide(asteroid)) {
                            projectile.setAlive(false);
                            int x = (int) asteroid.getComponent().getTranslateX();
                            int y = (int) asteroid.getComponent().getTranslateY();
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
                                    gamePane.getChildren().add(newAsteroid.getComponent());
                                }
                            }
                        }
                        if (projectile.getProjectileType() == ProjectileType.ALIEN && projectile.collide(asteroid)){
                            projectile.setAlive(false);
                        }
                    });
        });
    }

}
