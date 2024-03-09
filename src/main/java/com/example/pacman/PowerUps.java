package com.example.pacman;

import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;


public class PowerUps implements Collectible {
    private Circle visual;

    public PowerUps (double x, double y) {
        visual = new Circle(x, y, 7);
        visual.setFill(Color.PURPLE);

        // glow effect
        DropShadow glow = new DropShadow();
        glow.setColor(Color.BLUE);
        glow.setSpread(0.6);

        visual.setEffect(glow);
    }
    @Override
    public void collect (PacMan pacMan) {
        pacMan.addScore(500);
       // pacMan.enablePowerMode(); // må implementeres
        visual.setVisible(false); // fjernes etter kollisjon
    }

    @Override
    public Node getVisual() {
        return visual;
    }
}
