package com.example.pacman;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Klassen representer alt man kan collecte på mappet
 */
public class Food implements Collectible {
    private Circle visual; // visuell representasjon av food items

    /**
     * setter et food objekt ut på mappet på spesifisert location i map.txt
     * @param x x koordinaten av collectible
     * @param y y koordinaten av collectible
     */
    public Food(double x, double y) {
        visual = new Circle(x, y, 3);
        visual.setFill(Color.YELLOW);
    }

    /**
     * handler collection av food itemet for pacman, metoden blir kalt hver gang pacman kolliderer med ett collectible objekt
     * @param pacMan pacman karakteren som collecter
     */
    @Override
    public void collect(PacMan pacMan) {
        visual.setVisible(false); // fjernes etter kollisjon
        pacMan.addScore(10); // adder poeng til score
    }

    @Override
    public Node getVisual() {
        return visual;
    }

}
