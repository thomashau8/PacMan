package com.example.pacman;
import javafx.animation.AnimationTimer;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

import java.util.Iterator;
import java.util.List;

public class gameLoop extends AnimationTimer {
    private PacMan pacMan;
    private List<Collectible> collectibles;
    private Pane gamePane;

    public gameLoop(PacMan pacMan, List<Collectible> collectibles, Pane gamePane) {
        this.pacMan = pacMan;
        this.collectibles = collectibles;
        this.gamePane = gamePane;
    }


    @Override
    public void handle(long now) {
        pacMan.move();
        collectibleCollision(collectibles);
    }

    // Metode som kaller collect hver gang pacman kommer i kontakt med fysiske ting på mappet
    public void collectibleCollision(List<Collectible> collectibles) {
        Iterator<Collectible> iterator = collectibles.iterator();
        while (iterator.hasNext()) {
            Collectible collectible = iterator.next();
            Node visual = collectible.getVisual();
            if (pacMan.getBounds().intersects(visual.getBoundsInParent())) {
                collectible.collect(pacMan); // "this" referer til current pacman instanse
                gamePane.getChildren().remove(collectible.getVisual()); // remover visuel representasjon i spillet
                iterator.remove(); // remover "collectible" som pacman kom i kontakt med fra listen.
            }
        }
    }

}


