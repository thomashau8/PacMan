package com.example.pacman;

import javafx.application.Platform;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static javafx.scene.paint.Color.BLUE;

public abstract class Ghosts {
    // attributter her---
    protected Arc ghostVisual;
    protected double speed;
    protected Pane gamePane;
    protected KeyCode lastSuccessfulMove = KeyCode.RIGHT; // default value for å unngå nullpointerexception

    // ghost states
    protected boolean isScared = false;
    protected List<Rectangle> walls;
    public Ghosts(Pane gamePane, List<Rectangle> walls) {
        this.gamePane = gamePane;
        this.walls = walls;
        createVisual();
        normal();
    }

    protected void createVisual() {
        ghostVisual = new Arc();
        ghostVisual.setRadiusX(14.5);
        ghostVisual.setRadiusY(14.5);
        ghostVisual.setLength(360);
        ghostVisual.setType(ArcType.ROUND);
    }


    public void scared() {
        isScared = true;
        // visuelle changes her
        ghostVisual.setOpacity(0.5);
        ghostVisual.setFill(BLUE);
       // speed = 1.0; //denne ødelegger han visst, selv om vi setter den tilbake til 1.0, jeg gir opp på å fikse denne i tide
    }

    public void normal() {
        isScared = false;
        speed = 1.0;
    }

    public void respawn() {
        setPosition(432, 368);
    }

    protected void setPosition(double x, double y) {
        if (ghostVisual != null) {
            ghostVisual.setLayoutX(x);
            ghostVisual.setLayoutY(y);
        }
    }

    public void chase (double pacManX, double pacManY, KeyCode pacManDirection) {
        if (isScared) {
            runAway(pacManX, pacManY);
        } else {
            chaseImp(pacManX, pacManY, pacManDirection);
        }
    }

    protected abstract void chaseImp(double pacManX, double pacManY, KeyCode pacManDirection);

    protected void runAway(double pacManX, double pacManY) {
        double deltaX = ghostVisual.getLayoutX() - pacManX;
        double deltaY = ghostVisual.getLayoutY() - pacManY;
        KeyCode escapeDirection = getDesiredDirection(deltaX, deltaY);

        if (!tryMove(escapeDirection) && !tryMove(lastSuccessfulMove)) {
            fallbackMove(escapeDirection);
        }
    }

    // henter motsatt vei til current vei
    public KeyCode getOppositeDirection(KeyCode direction) {
        switch (direction) {
            case UP: return KeyCode.DOWN;
            case DOWN: return KeyCode.UP;
            case LEFT: return KeyCode.RIGHT;
            case RIGHT: return KeyCode.LEFT;
            default: return direction; // burde aldri komme hit i switch setningen
        }
    }
    // velger random vei som ikke er direkt inni en vegg eller motsatt fra current vei
    public void fallbackMove(KeyCode blockedDirection) {
        List<KeyCode> directions = Arrays.asList(KeyCode.UP, KeyCode.DOWN, KeyCode.LEFT, KeyCode.RIGHT);
        Collections.shuffle(directions); // randomizer direksjon listen

        for (KeyCode direction : directions) {
            if (direction != blockedDirection && direction != getOppositeDirection(blockedDirection)) {
                if (tryMove(direction)) {
                    lastSuccessfulMove = direction;
                    break;
                }
            }
        }
    }

    public KeyCode getDesiredDirection(double deltaX, double deltaY) {
        // simpel logikk for å bestemme direksjon
        if (Math.abs(deltaX) > Math.abs(deltaY)) {
            return deltaX > 0 ? KeyCode.RIGHT : KeyCode.LEFT;
        } else {
            return deltaY > 0 ? KeyCode.DOWN : KeyCode.UP;
        }
    }

    public Bounds getBoundsInParent() {
        return ghostVisual.getBoundsInParent();
    }

    protected boolean wallCollision(double newX, double newY) {
        Bounds potentialBounds = new BoundingBox(
                newX - ghostVisual.getRadiusX(),
                newY - ghostVisual.getRadiusY(),
                ghostVisual.getRadiusX() * 2,
                ghostVisual.getRadiusY() * 2);

        for (Rectangle wall : walls) {
            if (potentialBounds.intersects(wall.getBoundsInParent())) {
                return true;
            }
        }
        return false;
    }

    public boolean collidesWithPacMan(PacMan pacMan) {
        return this.getBoundsInParent().intersects(pacMan.getBounds());
    }

    protected boolean tryMove(KeyCode direction) {
        double newX = ghostVisual.getLayoutX();
        double newY = ghostVisual.getLayoutY();

        switch (direction) {
            case UP:
                newY -= speed;
                break;
            case DOWN:
                newY += speed;
                break;
            case LEFT:
                newX -= speed;
                break;
            case RIGHT:
                newX += speed;
                break;
            default:
                return false;
        }

        if (!wallCollision(newX, newY)) {
            ghostVisual.setLayoutX(newX);
            ghostVisual.setLayoutY(newY);
            return true;
        }
        return false;
    }

    public Arc getGhostVisual() {
        return ghostVisual;
    }
}
