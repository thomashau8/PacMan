package com.example.pacman;

import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Denne klassen representerer powerups i spillet blir visuelt displayed på mappet og gir pacman evner til å jakte på ghostene
 */

public class PowerUps implements Collectible {
    /**
     * Visuel representasjon av powerupsene {@link Circle} shape
     */
    private Circle visual;

    /**
     * lager powerups på spesifiserte locations
     * @param x coordinates for hvor powerups skal bli placed
     * @param y coordinates for hvor powerups skal bli placed
     */
    public PowerUps (double x, double y) {
        visual = new Circle(x, y, 7);
        visual.setFill(Color.PURPLE);

        // glow effect til powerups
        DropShadow glow = new DropShadow();
        glow.setColor(Color.BLUE);
        glow.setSpread(0.6);

        visual.setEffect(glow);
    }

    /**
     * Metoden for å gi pacman evnen til å collecte powerups, gi poeng, despawne de etter collected, og enabler powermode
     * @param pacMan Instansen av pacman som collecter powerupsene
     */
    @Override
    public void collect (PacMan pacMan) {
        pacMan.addScore(500);
        visual.setVisible(false); // fjernes etter kollisjon
        pacMan.enablePowerMode();
    }


    @Override
    public Node getVisual() {
        return visual;
    }
}