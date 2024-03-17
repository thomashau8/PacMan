package com.example.pacman;

import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.List;

import static javafx.scene.paint.Color.CYAN;
import static javafx.scene.paint.Color.ORANGE;

public class Clyde extends Ghosts {
    private final double scatterRange = 8 * 32; // Clyde løper vekk fra pacman når han er 8 fliser i rekkevidde

    public Clyde(Pane gamePane, List<Rectangle> walls) {
        super(gamePane, walls);
        createVisual();
    }

    @Override
    protected void createVisual() {
        super.createVisual();
        ghostVisual.setFill(ORANGE);
    }

    @Override
    protected void chaseImp(double pacManX, double pacManY, KeyCode pacManDirection) {
        double distance = Math.sqrt(Math.pow(ghostVisual.getLayoutX() - pacManX, 2) + Math.pow(ghostVisual.getLayoutY() - pacManY, 2));

        if (distance > scatterRange) {
            // løper etter pacman
            double deltaX = pacManX - ghostVisual.getLayoutY();
            double deltaY = pacManY - ghostVisual.getLayoutY();
            KeyCode desiredDirection = getDesiredDirection(deltaX, deltaY);
            if (!tryMove(desiredDirection) && !tryMove(lastSuccessfulMove)) {
                fallbackMove(desiredDirection);
            }
        } else {
            // løp "hjem"
            moveToHome();
        }
    }

    private void moveToHome() {
        // vi setter hjem til nedre venstre side
        double homeX = 0;
        double homeY = gamePane.getHeight();
        moveTowards(homeX, homeY);
    }

    // kunne potensielt lagt denne være i superklassen, men bare i tilfelle den ødelegger for Blinky og Pinky som ikke skal bruke den lar jeg bare den ligge her
    // og evt duplikere den i Inky
    protected void moveTowards(double x, double y) {
        double deltaX = x - ghostVisual.getLayoutX();
        double deltaY = y - ghostVisual.getLayoutY();
        KeyCode desiredDirection = getDesiredDirection(deltaX, deltaY);

        if (!tryMove(desiredDirection) && !tryMove(lastSuccessfulMove)) {
            fallbackMove(desiredDirection);
        }
    }
    public void normal() {
        super.normal();
        ghostVisual.setFill(ORANGE);
        ghostVisual.setOpacity(1.0);
    }
}