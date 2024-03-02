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
    private Pane root;

    public gameLoop(PacMan pacMan, List<Collectible> collectibles, Pane root) {
        this.pacMan = pacMan;
        this.collectibles = collectibles;
        this.root = root;
    }



    @Override
    public void handle(long now) {
        pacMan.move();
        collectibleCollision(collectibles);
    }

    public void collectibleCollision(List<Collectible> collectibles) {
        Iterator<Collectible> iterator = collectibles.iterator();
        while (iterator.hasNext()) {
            Collectible collectible = iterator.next();
            Node visual = collectible.getVisual();
            if (pacMan.getBounds().intersects(visual.getBoundsInParent())) {
                collectible.collect(pacMan); // "this" referer til current pacman instanse
                root.getChildren().remove(collectible.getVisual()); // remover visuel representasjon i spillet
                iterator.remove(); // remover "collectible" som pacman kom i kontakt med fra listen.
            }
        }
    }

}


