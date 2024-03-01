package com.example.pacman;

import javafx.scene.Node;
// bare en interface fil for items pacman kan interacte med.
public interface Collectible {
    void collect(PacMan pacMan);
    Node getVisual();
}