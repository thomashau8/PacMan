package com.example.pacman;

import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import java.util.List;
import static javafx.scene.paint.Color.PINK;

/** Pinky er en av ghostene, klassen extender superklassen "Ghosts" {@link Ghosts}
 * Pinky had unik oppførsel fra de andre klassene, det går ut på å predicte movementen til pacman til 4 fliser (grid bokser)
 * foran pacman
 */

public class Pinky extends Ghosts {
    /**
     * Vi forsøker å tracke siste brukbar move for å ikke bli stuck
     */
    private KeyCode lastSuccessfulMove = KeyCode.UP; // vi initaliserer med en random direction

    /**
     *
     * @param gamePane panet som blir brukt til å visualisere elementene
     * @param walls list av vegger for collision detection
     */
    public Pinky(Pane gamePane, List<Rectangle> walls) {
        super(gamePane, walls);
        createVisual();
    }

    @Override
    protected void createVisual() {
        super.createVisual();
        ghostVisual.setFill(PINK); // setter pinky til pink
    }

    /**
     * definerer chase logikken til Pinky som spesifisert på toppen av klassen
     *
     * @param pacManX X coordinatene av Pacman's posisjon
     * @param pacManY Y coordinatene av pacman's posisjon
     * @param pacManDirection current direction pacman beveger seg i
     */
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

        /**
         * beveger pinky til forutsitt posisjon
         */
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
