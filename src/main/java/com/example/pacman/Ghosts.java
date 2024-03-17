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
    protected double speed; // hastighet for ghosts, ville brukt den til å speede opp gamet per win, men er bugged
    protected Pane gamePane;
    protected KeyCode lastSuccessfulMove = KeyCode.RIGHT; // default value for å unngå nullpointerexception

    // ghost states
    protected boolean isScared = false;
    protected List<Rectangle> walls;

    /**
     * konstruktør som lager ett ghost objekt med referanse til spillets pane og vegger
     * @param gamePane panet hvor ghostet blir addet
     * @param walls alle veggene i spillet
     */
    public Ghosts(Pane gamePane, List<Rectangle> walls) {
        this.gamePane = gamePane;
        this.walls = walls;
        createVisual();
        normal();
    }

    /**
     * lager visuals til alle ghostene, midlertidig circles (egentlig kopiert av pacman)
     */
    protected void createVisual() {
        ghostVisual = new Arc();
        ghostVisual.setRadiusX(14.5);
        ghostVisual.setRadiusY(14.5);
        ghostVisual.setLength(360);
        ghostVisual.setType(ArcType.ROUND);
    }

    /**
     * blir enabled når pacman tar en powerup
     */
    public void scared() {
        isScared = true;
        // visuelle changes her
        ghostVisual.setOpacity(0.5);
        ghostVisual.setFill(BLUE);
       // speed = 1.0; denne ødelegger han visst, selv om vi setter den tilbake til 1.0, jeg gir opp på å fikse denne i tide
    }

    /**
     * returnerer til normal state
     */
    public void normal() {
        isScared = false;
        speed = 1.0;
    }

    public void respawn() {
        setPosition(432, 368);
    }

    /**
     * setter koordinatene til ghostene
     * @param x setter X koordinaten
     * @param y setter Y koordinaten
     */
    protected void setPosition(double x, double y) {
        if (ghostVisual != null) {
            ghostVisual.setLayoutX(x);
            ghostVisual.setLayoutY(y);
        }
    }

    /**
     * hvis ghost er scared, spring vekk, ellers fortsett med chase logikken til subklasser
     * @param pacManX X koordinaten til pacman
     * @param pacManY Y koordinaten til pacman
     * @param pacManDirection retningen pacman går i
     */
    public void chase (double pacManX, double pacManY, KeyCode pacManDirection) {
        if (isScared) {
            runAway(pacManX, pacManY);
        } else {
            chaseImp(pacManX, pacManY, pacManDirection);
        }
    }

    /**
     * implementerer selve logikken for jakt fra subklasser
     * @param pacManX x koordinatene til pacman
     * @param pacManY y koordinatene til pacman
     * @param pacManDirection retningen til pacman
     */
    protected abstract void chaseImp(double pacManX, double pacManY, KeyCode pacManDirection);

    /**
     * springer vekk fra pacman
     * @param pacManX koordinater
     * @param pacManY koordinater
     */
    protected void runAway(double pacManX, double pacManY) {
        double deltaX = ghostVisual.getLayoutX() - pacManX;
        double deltaY = ghostVisual.getLayoutY() - pacManY;
        KeyCode escapeDirection = getDesiredDirection(deltaX, deltaY);

        if (!tryMove(escapeDirection) && !tryMove(lastSuccessfulMove)) {
            fallbackMove(escapeDirection);
        }
    }

    /**
     * henter motsatt vei av current vei
     * @param direction current vei til pacman
     * @return returnerer motsatt vei
     */
    public KeyCode getOppositeDirection(KeyCode direction) {
        switch (direction) {
            case UP: return KeyCode.DOWN;
            case DOWN: return KeyCode.UP;
            case LEFT: return KeyCode.RIGHT;
            case RIGHT: return KeyCode.LEFT;
            default: return direction; // burde aldri komme hit i switch setningen
        }
    }

    /**
     * velger annen vei hvis veien er blokkert
     * @param blockedDirection direksjonen som er currently blokkert
     */
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

    /**
     * beregner desired direksjon for ghostene å bevege seg basert på deltaX og deltaY verdier
     * velger den axis'en med høyest differanse for å prioritere bevegelse
     * @param deltaX differansen i X koordinaten mellom ghost og pacman
     * @param deltaY differansen i Y koordinaten mellom ghost og pacman
     * @return
     */
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

    /**
     *  sjekker om neste bevegelse vil resultere i en kollisjon
     * @param newX den neste foreslåtte koordinaten for ghostet
     * @param newY den neste foreslåtte koordinaten for ghostet
     * @return
     */
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

    /**
     * Forsøker å bevege seg i en direksjon, sjekker for kollisjon
     * @param direction ønsket retning å bevege ghostet i
     * @return true hvis movet er succesful ( ingen kollisjon )
     */
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
