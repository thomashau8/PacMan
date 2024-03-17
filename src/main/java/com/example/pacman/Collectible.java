package com.example.pacman;

import javafx.scene.Node;

/**
 * bare en interface fil for items pacman kan interacte med.
 * collect er for å kunne interacte med ting på mappet
 * Node getVisual brukes til å implementere ting visuelt
 */
public interface Collectible {
    // collect er for å kunne interacte med ting på mappet
    void collect(PacMan pacMan);

    // Node getVisual brukes til å implementere ting visuelt
    Node getVisual();
}