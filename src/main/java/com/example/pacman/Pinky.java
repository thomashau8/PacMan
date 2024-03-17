package com.example.pacman;

import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.List;

import static javafx.scene.paint.Color.PINK;
import static javafx.scene.paint.Color.RED;

public class Pinky extends Ghosts {
    private KeyCode lastSuccessfulMove = KeyCode.UP; // vi initaliserer med en random direction

    public Pinky(Pane gamePane, List<Rectangle> walls) {
        super(gamePane, walls);
        createVisual();
    }

    @Override
    protected void createVisual() {
        super.createVisual();
        ghostVisual.setFill(PINK); // setter pinky til pink
    }

    // chase logikken til pinky,
    @Override
    public void chaseImp(double pacManX, double pacManY, KeyCode pacManDirection) { // målet er å sette pinky foran pacman
        // forutse pacman's bevegelse
        double targetX = pacManX, targetY = pacManY;
        int gridTilesAhead = 4; // nummer av "bokser" eller "fliser" pinky aimer for forann pacman
        double tileSize = 32; // en flis er 32x32 pixels

        switch (pacManDirection) {
            case UP:
                targetY -= gridTilesAhead * tileSize;
                break;
            case DOWN:
                targetY += gridTilesAhead * tileSize;
                break;
            case LEFT:
                targetX -= gridTilesAhead * tileSize;
                break;
            case RIGHT:
                targetX += gridTilesAhead * tileSize;
                break;
        }

        // beveger pinky til forutsitt posisjon
        double deltaX = targetX - ghostVisual.getLayoutX();
        double deltaY = targetY - ghostVisual.getLayoutY();
        KeyCode desiredDirection = getDesiredDirection(deltaX, deltaY);



        if (!tryMove(desiredDirection)) {
            // hvis direksjon er blokkert, prøv ny
            KeyCode[] directions = {KeyCode.UP, KeyCode.DOWN, KeyCode.LEFT, KeyCode.RIGHT};
            for (KeyCode direction : directions) {
                if (tryMove(direction)) {
                    lastSuccessfulMove = direction; // oppdaterer lastsuccesful move
                    break;
                }
            }
        }
    }

    @Override
    public void normal() {
        super.normal();
        ghostVisual.setFill(PINK);
        ghostVisual.setOpacity(1.0);
     //   speed = 1.0;
    }
}
