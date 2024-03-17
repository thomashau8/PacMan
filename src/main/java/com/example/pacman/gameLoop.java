package com.example.pacman;
import javafx.animation.AnimationTimer;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class gameLoop extends AnimationTimer {
    private PacMan pacMan;
    private List<Collectible> collectibles;
    private List<Ghosts> ghosts = new ArrayList<>();
    private Pane gamePane;
    private double speed = 1.2;
    private PacManGame pacManGame;
    private static final double TUNNEL_LEFT = 0;
    private static final double TUNNEL_RIGHT = 735; // samme some game width


    public gameLoop(PacManGame pacManGame, PacMan pacMan, List<Collectible> collectibles, List<Ghosts> ghosts, Pane gamePane) {
        this.pacManGame = pacManGame;
        this.pacMan = pacMan;
        this.collectibles = collectibles;
        this.ghosts = ghosts;
        this.gamePane = gamePane;
    }


    @Override
    public void handle(long now) {
        pacMan.move(speed);
        collectibleCollision(collectibles);
        handleTunnelTeleportation(pacMan.getPacManFigure()); // teleport for pacman
        GhostCollision();
        checkForLevelCompletion();
        KeyCode pacManDirection = pacMan.getCurrentDirection(); // returnerer pacmans direction
        for (Ghosts ghost: ghosts) {
            ghost.chase(pacMan.getPositionX(), pacMan.getPositionY(), pacManDirection);
            handleTunnelTeleportation(ghost.getGhostVisual()); // teleport for ghostene
        }
    }


    private void checkForLevelCompletion() {
        if (collectibles.isEmpty()) {
            resetGame();
        }
    }

    public void handleTunnelTeleportation(Node entity) {
        // Handle LayoutX for ghosts
        if (entity.getLayoutX() < TUNNEL_LEFT) {
            entity.setLayoutX(TUNNEL_RIGHT);
        } else if (entity.getLayoutX() > TUNNEL_RIGHT) {
            entity.setLayoutX(TUNNEL_LEFT);
        }

        // lager enda en for pacman, litt upraktisk men dårlig tid så her rusher vi
        if (entity instanceof Arc) {
            Arc arc = (Arc) entity;
            if (arc.getCenterX() < TUNNEL_LEFT) {
                arc.setCenterX(TUNNEL_RIGHT);
            } else if (arc.getCenterX() > TUNNEL_RIGHT) {
                arc.setCenterX(TUNNEL_LEFT);
            }
        }
    }


    // speeder opp gamet for hver completion, temp ute - skaper mer bugs, noe ødelegges med ghost om de får justert speed opp eller ned
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
      //  increaseSpeed(); skaper problemer med spillet så jeg ikke klarer å fikse
        gamePane.getChildren().removeAll(nodesToRemove);

        pacManGame.loadMap(gamePane, "map.txt");
    }

    private void GhostCollision() {
        for (Ghosts ghost : ghosts) {
            if (ghost.collidesWithPacMan(pacMan)) {
                if (ghost.isScared) {
                    ghost.respawn(); // resetter ghost til starter posisjon
                    pacMan.addScore(500);
                } else {
                    pacManDeath();
                    break;
                }
            }
        }
    }

    private void pacManDeath() {
        pacMan.loseLife();

        if (pacMan.getLives() > 0) {
            resetGame();
        } else {
            gameOver();
        }
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

    private void gameOver() {
        this.stop(); // stopper gamet

        gamePane.setEffect(new ColorAdjust(0, 0, 0.5, 0)); // grayer ut gamet

        // setter tekst på midten av sjermen
        Text gameOverText = new Text("LOSER LOL!");
        gameOverText.setFont(Font.font("Verdana", FontWeight.BOLD, 70));
        gameOverText.setFill(Color.RED);
        gameOverText.setX(gamePane.getWidth() / 2 - gameOverText.getBoundsInLocal().getWidth() / 2);
        gameOverText.setY(gamePane.getHeight() / 2 - gameOverText.getBoundsInLocal().getHeight() / 2);

        gamePane.getChildren().add(gameOverText);
    }
}


