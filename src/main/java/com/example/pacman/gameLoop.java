package com.example.pacman;
import javafx.animation.AnimationTimer;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class gameLoop extends AnimationTimer {
    private PacMan pacMan;
    private List<Collectible> collectibles;
    private Pane gamePane;
    private double speed = 1.0;
    private PacManGame pacManGame;


    public gameLoop(PacManGame pacManGame, PacMan pacMan, List<Collectible> collectibles, Pane gamePane) {
        this.pacManGame = pacManGame;
        this.pacMan = pacMan;
        this.collectibles = collectibles;
        this.gamePane = gamePane;
    }


    @Override
    public void handle(long now) {
        pacMan.move(speed);
        collectibleCollision(collectibles);
        checkForLevelCompletion();
    }

    private void checkForLevelCompletion() {
        if (collectibles.isEmpty()) {
            resetGame();
        }
    }

    // speeder opp gamet for hver completion
    public void increaseSpeed() {
        this.speed += 0.2;
    }

    private void resetGame() {
        // Sletter spesifikk noder for å restarte gamet, (brukte 5 timer på å finne ut hvorfor pacman ikke var visuelt representert lol)
        List<Node> nodesToRemove = new ArrayList<>();
        for (Node child : gamePane.getChildren()) {
            if (child instanceof Rectangle || child instanceof Collectible) {
                nodesToRemove.add(child);
            }
        }
        increaseSpeed();
        gamePane.getChildren().removeAll(nodesToRemove);

        pacManGame.loadMap(gamePane, "map.txt");



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


