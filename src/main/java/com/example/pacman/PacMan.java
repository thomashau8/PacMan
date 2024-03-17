package com.example.pacman;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Text;
import javafx.util.Duration;
import java.util.List;
import static javafx.scene.paint.Color.*;

public class PacMan {
    // byttet fra circle til arc for å animere pacman, men tror ikke jeg rekker det
    private List<Ghosts> ghosts;
    private Arc pacManFigure;
    private int lives = 3;
    private int score = 0;
    private Text scoreText;
    private Text livesText;
    private boolean powerMode = false;
    private List<Rectangle> walls;
    private KeyCode currentDirection = KeyCode.RIGHT; // må visst ha en variabel for å kunne ha pacman gående
    private KeyCode desiredDirection = KeyCode.RIGHT;
    private double movementAssistance = 2; // det er veldig vanskelig å navigere så vi implementerer en slags "lookahead" -
    // metode for å hjelpe spiller å turne selv om tasten blir trykket ved en vegg
    private Timeline powerModeTimer;


    public PacMan(Pane gamePane, Text scoreText, Text livesText, List<Ghosts> ghosts) {
        this.scoreText = scoreText;
        this.livesText = livesText;
        this.ghosts = ghosts;
        // Byttet om fra circle til arc for å kunne animere munn åpning
        pacManFigure = new Arc();
        pacManFigure.setRadiusX(14.5);
        pacManFigure.setRadiusY(14.5);
        pacManFigure.setLength(360);
        pacManFigure.setType(ArcType.ROUND);
        pacManFigure.setFill(Color.YELLOW);

        gamePane.getChildren().add(pacManFigure);
        updateScoreDisplay();
        updateLivesDisplay();
    }

    // gir PacMan klassen tilgang til alle veggene på mappet for å sjekke collision
    public void setWalls(List<Rectangle> walls) {
        this.walls = walls;
    }

    // sjekker om pacman's bounding box intersects med bounding boxen til veggene
    public boolean wallCollision(double nextX, double nextY) {
        Bounds potentialBounds = new BoundingBox(
                nextX - pacManFigure.getRadiusX(),
                nextY - pacManFigure.getRadiusY(),
                pacManFigure.getRadiusX() * 2,
                pacManFigure.getRadiusY() * 2
        );

        for (Rectangle wall : walls) {
            if (potentialBounds.intersects(wall.getBoundsInParent())) {
                return true;
            }
        }
        return false;
    }

    // metode for å sette score
    public void addScore(int points) {
        this.score += points;
        updateScoreDisplay();
    }

    // metode for å tracke liv
    public void loseLife() {
        this.lives--;
        updateLivesDisplay();
    }

    private void updateScoreDisplay() {
        Platform.runLater(() -> scoreText.setText("Score: " + score));
    }

    private void updateLivesDisplay() {
        Platform.runLater(() -> livesText.setText("Lives: " + lives));
    }

    // metode for powermode
    public void enablePowerMode() {
        this.powerMode = true;
        pacManFigure.setFill(PURPLE);
        for (Ghosts ghost : ghosts) {
            ghost.scared();

        if ( powerModeTimer != null) {
            powerModeTimer.stop();
        }
        powerModeTimer = new Timeline(new KeyFrame(Duration.seconds(5), e -> disablePowerMode()));
        powerModeTimer.play();
        // handle ghost state
        }
    }

    public void disablePowerMode() {
        this.powerMode = false;
        pacManFigure.setFill(YELLOW);
        for (Ghosts ghost : ghosts) {
            ghost.normal();
        }
    }

    // holder pacman's orginal posisjon, og etter move forsøk sjekker om man koliderer med en vegg
    public void move(double currentSpeed) {
        // sjekker om vi kan snu til valgt direction
        if (canTurn(desiredDirection)) {
            currentDirection = desiredDirection;
        }

        double offsetX = 0, offsetY = 0;
        switch (currentDirection) {
            case UP:
                offsetY = -currentSpeed;
                break;
            case DOWN:
                offsetY = currentSpeed;
                break;
            case LEFT:
                offsetX = -currentSpeed;
                break;
            case RIGHT:
                offsetX = currentSpeed;
                break;
        }
        // gjør det faktiske movementet
        pacManFigure.setCenterX(pacManFigure.getCenterX() + offsetX);
        pacManFigure.setCenterY(pacManFigure.getCenterY() + offsetY);

        // sjekker for collision og undoer movet.
        if (wallCollision(pacManFigure.getCenterX(), pacManFigure.getCenterY())) {
            pacManFigure.setCenterX(pacManFigure.getCenterX() - offsetX);
            pacManFigure.setCenterY(pacManFigure.getCenterY() - offsetY);
        }
    }

    public boolean canTurn(KeyCode direction) {
        //kalkulerer lookahead posisjon basert på desired direction
        double lookaheadX = pacManFigure.getCenterX();
        double lookaheadY = pacManFigure.getCenterY();

        switch (direction) {
            case UP: lookaheadY -= movementAssistance;
            break;
            case DOWN: lookaheadY += movementAssistance;
            break;
            case LEFT: lookaheadX -= movementAssistance;
            break;
            case RIGHT: lookaheadX += movementAssistance;
            break;
        }

        // sjekk om det er en vegg på lookahead posisjon
        return !wallCollision(lookaheadX, lookaheadY);
    }

    // setter ønsket direksjon for å turne pacman
    public void setCurrentDirection(KeyCode newDirection) {
        this.desiredDirection = newDirection;
    }
    // gir bounds til gameLoop for sjekk
    public Bounds getBounds() {
        return pacManFigure.getBoundsInParent();
    }

    public void setPosition(double x, double y) {
        pacManFigure.setCenterX(x);
        pacManFigure.setCenterY(y);
    }

    public double getPositionX() {
        return pacManFigure.getCenterX();
    }

    public double getPositionY() {
        return pacManFigure.getCenterY();
    }

    // getter for liv
    public int getLives() {
        return lives;
    }

    // getter for direction for Pinky
    public KeyCode getCurrentDirection() {
        return currentDirection;
    }

    public Arc getPacManFigure() {
        return pacManFigure;
    }
}
