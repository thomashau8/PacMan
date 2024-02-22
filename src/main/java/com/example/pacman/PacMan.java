package com.example.pacman;

import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.List;

public class PacMan {
    // PacMan's sine attributes
    private Circle pacManFigure;
    private int lives = 3;
    private int score = 0;
    private List<Rectangle> walls;

    public PacMan(Pane root) {
        pacManFigure = new Circle(15);
        root.getChildren().add(pacManFigure);
    }

    // gir PacMan klassen tilgang til alle veggene på mappet for å sjekke collision
    public void setWalls(List<Rectangle> walls) {
        this.walls = walls;
    }

    // sjekker om pacman's bounding box intersects med bounding boxen til veggene
    public boolean wallCollision() {
        for (Rectangle wall : walls) {
            if (pacManFigure.getBoundsInParent().intersects(wall.getBoundsInParent())) {
                return true;
            }
        }
        return false;
    }

    // holder pacman's orginal posisjon, og etter move forsøk sjekker om man koliderer med en vegg
    public void move(KeyCode code) {
        double originalX = pacManFigure.getCenterX();
        double originalY = pacManFigure.getCenterY();

        switch (code) {
            case UP:
                pacManFigure.setCenterY(originalY - 5);
                break;
            case DOWN:
                pacManFigure.setCenterY(originalY + 5);
                break;
            case LEFT:
                pacManFigure.setCenterX(originalX - 5);
                break;
            case RIGHT:
                pacManFigure.setCenterX(originalX + 5);
                break;
        }

        // sjekker for collision og undo movet.
        if (wallCollision()) {
            pacManFigure.setCenterX(originalX);
            pacManFigure.setCenterY(originalY);
        }
    }

    public void setPosition(double x, double y) {
        pacManFigure.setCenterX(x);
        pacManFigure.setCenterY(y);
    }

}
