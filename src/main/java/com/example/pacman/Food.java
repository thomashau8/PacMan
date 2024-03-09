package com.example.pacman;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Food implements Collectible {
    private Circle visual;

    public Food(double x, double y) {
        visual = new Circle(x, y, 3);
        visual.setFill(Color.YELLOW);
    }

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
