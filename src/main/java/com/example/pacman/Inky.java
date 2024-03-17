package com.example.pacman;

import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Pair;

import java.util.List;

import static javafx.scene.paint.Color.CYAN;
import static javafx.scene.paint.Color.RED;

public class Inky extends Ghosts {
    // denne blir litt mer kompleks siden vi må bruke blinky sin posisjon og pacman's orientasjon for å gjøre Inky unpredictable
    private Ghosts blinky; // referer til blinky for å finne Inky sitt target

    public Inky(Pane gamePane, List<Rectangle> walls, Ghosts blinky) {
        super(gamePane, walls);
        this.blinky = blinky; // gir Inky tilgang til Blinky
        createVisual();
    }

    @Override protected void createVisual() {
        super.createVisual();
        ghostVisual.setFill(CYAN);
    }

    @Override
    protected void chaseImp(double pacManX, double pacManY, KeyCode pacManDirection) {
        // gir oss vektoren fra Blinky til 2 fliser foran PacMan
        double vectorX = pacManX + 2 * getDirectionOffset(pacManDirection).getKey() - blinky.getGhostVisual().getLayoutX();
        double vectorY = pacManY + 2 * getDirectionOffset(pacManDirection).getValue() - blinky.getGhostVisual().getLayoutY();

        // dobbler lengen av vektoren for Inky
        double targetX = blinky.getGhostVisual().getLayoutX() + 2 * vectorX;
        double targetY = blinky.getGhostVisual().getLayoutY() + 2 * vectorY;

        moveTowards(targetX, targetY);

    }

    private Pair<Double, Double> getDirectionOffset(KeyCode direction) {
        switch (direction) {
            case UP:
                return new Pair<>(0.0, -1.0);
            case DOWN:
                return new Pair<>(0.0, 1.0);
            case LEFT:
                return new Pair<>(-1.0, 0.0);
            case RIGHT:
                return new Pair<>(1.0, 0.0);
            default:
                return new Pair<>(0.0, 0.0);
        }
    }
    // kunne potensielt lagt denne være i superklassen, men bare i tilfelle den ødelegger for Blinky og Pinky som ikke skal bruke den lar jeg bare den ligge her
    // og evt duplikere den i Clyde
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
        ghostVisual.setFill(CYAN);
        ghostVisual.setOpacity(1.0);
    }
}
