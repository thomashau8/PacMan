package com.example.pacman;

import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.util.List;
import static javafx.scene.paint.Color.CYAN;
import static javafx.scene.paint.Color.ORANGE;

/**
 * Denne klassen representerer ghostet Clyde, som har en unik oppførsel i spillet med å springe vekk fra pacman når han er
 * innen en spesifikk rekkevidde
 */
public class Clyde extends Ghosts {
    /**
     * // Clyde løper vekk fra pacman når han er 8 fliser i rekkevidde
     */
    private final double scatterRange = 8 * 32;

    /**
     * lager ett clyde ghost med samme gamepane og liste med alle veggene i spillet for kollisjon
     * @param gamePane
     * @param walls
     */
    public Clyde(Pane gamePane, List<Rectangle> walls) {
        super(gamePane, walls);
        createVisual();
    }

    /**
     * lager visuelle representasjonen for Clyde
     */
    @Override
    protected void createVisual() {
        super.createVisual();
        ghostVisual.setFill(ORANGE);
    }

    /**
     * Clyde sin "chase" logikk, clyde vil springe vekk fra pacman når han er for nærme
     * @param pacManX x koordinatene til pacman
     * @param pacManY y koordinatene til pacman
     * @param pacManDirection retningen til pacman (brukes ikke av clyde)
     */
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

    /**
     * Beveger seg til ett hjørne
     */
    private void moveToHome() {
        // sender clyde hjem
        double homeX = 0;
        double homeY = gamePane.getHeight();
        moveTowards(homeX, homeY);
    }

    /**
     * kunne potensielt lagt denne være i superklassen, men bare i tilfelle den ødelegger for Blinky og Pinky som ikke skal bruke den lar jeg bare den ligge her
     * og evt duplikere den i Inky
     *
     * Clyde beveger seg til sitt spesifikk target location
     *
     * @param x koordinatene til target location
     * @param y koordinater til target location
     */
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