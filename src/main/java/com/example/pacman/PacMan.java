package com.example.pacman;

import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

public class PacMan {
    // PacMan's sine attributes
    private Circle pacManFigure;
    private int lives = 3;
    private int score = 0;

    public PacMan(Pane root) {
        pacManFigure = new Circle(15);
        root.getChildren().add(pacManFigure);
    }

    public void move(KeyCode code) {
        switch (code) {
            case UP:
                pacManFigure.setCenterY(pacManFigure.getCenterY() - 5);
                break;
            case DOWN:
                pacManFigure.setCenterY(pacManFigure.getCenterY() + 5);
                break;
            case LEFT:
                pacManFigure.setCenterX(pacManFigure.getCenterX() - 5);
                break;
            case RIGHT:
                pacManFigure.setCenterX(pacManFigure.getCenterX() + 5);
                break;
        }
    }

    public void setPosition(double x, double y) {
        pacManFigure.setCenterX(x);
        pacManFigure.setCenterY(y);
    }

}
