package com.example.pacman;

import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static javafx.scene.paint.Color.RED;

public class Blinky extends Ghosts {

    private boolean cornered = false;


    public Blinky(Pane gamePane, List<Rectangle> walls) {
        super(gamePane, walls);
        createVisual();
    }
    // lager visuell representasjon av Blinky
    @Override
    protected void createVisual() {
        super.createVisual();
        ghostVisual.setFill(RED);
    }
    // setter posisjonen til blinky på gamePane
    public void setPosition(double x, double y) {
        this.ghostVisual.setLayoutX(x);
        this.ghostVisual.setLayoutY(y);
    }

    /**
     * main algoritme logikken for å følge etter pacman, Blinky er ganske straight forward, bare chase til han får tak i Pacman
     * @param pacManX x koordinatene til pacman
     * @param pacManY y koordinatene til pacman
     * @param pacManDirection retningen til pacman
     */
    @Override
    public void chaseImp(double pacManX, double pacManY, KeyCode pacManDirection) {

            double deltaX = pacManX - ghostVisual.getLayoutX();
            double deltaY = pacManY - ghostVisual.getLayoutY();
            KeyCode desiredDirection = getDesiredDirection(deltaX, deltaY);

            // hvis blinky er cornered må han velge en ny path
            if (cornered) {
                // forsøker å bevege seg til forgie vinkelrett bevegelse
                KeyCode[] directions = getPerpendicularDirections(lastSuccessfulMove);
                for (KeyCode direction : directions) {
                    if (tryMove(direction)) {
                        cornered = false;
                        lastSuccessfulMove = direction;
                        return;
                    }
                }
            }
            // normal chase oppførsel
            if (!tryMove(desiredDirection)) {
                // hvis neste move er blokkert revert til forgie move
                if (!tryMove(lastSuccessfulMove)) {
                    // // hvis blinky er stuck, sett cornered true og velg ny direction
                    cornered = true;
                    fallbackMove(desiredDirection);
                }
            } else {
                lastSuccessfulMove = desiredDirection;
                cornered = false;
            }
        }


    // gir alternativ veier å ta når blinky's main path er blokkert
    private KeyCode[] getPerpendicularDirections(KeyCode direction) {
        if (direction == KeyCode.UP || direction == KeyCode.DOWN) {
            return new KeyCode[]{KeyCode.LEFT, KeyCode.RIGHT};
        } else {
            return new KeyCode[]{KeyCode.UP, KeyCode.DOWN};
        }
    }


    @Override
    public void normal() {
        super.normal();
        ghostVisual.setFill(RED);
        ghostVisual.setOpacity(1.0);
        // speed = 1; prøvd mye rart med speed men finner i
    }
    // fikk bugs med hvordan han er påvirket av en reset, så forsøker dette for å restarte han til en bedre state
    public void respawn() {
        super.respawn(); // superklasse sin respawn logikk
        cornered = false; // resetter cornered state

    }
}