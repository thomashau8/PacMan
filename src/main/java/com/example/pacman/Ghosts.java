package com.example.pacman;

import javafx.geometry.Bounds;
import javafx.scene.Node;

public abstract class Ghosts {
    // attributter her---
    protected Node ghostVisual;

    public Ghosts() {
    }

    public Bounds getBoundsInParent() {
        return ghostVisual.getBoundsInParent();
    }
    public boolean collidesWithPacMan(PacMan pacMan) {
        return this.getBoundsInParent().intersects(pacMan.getBounds());
    }
}
