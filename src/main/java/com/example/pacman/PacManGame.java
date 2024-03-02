package com.example.pacman;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;


public class PacManGame extends Application {

    private PacMan pacMan;
    private List<Rectangle> walls = new ArrayList<>(); // skal holde alle veggene i listen
    private List<Collectible> collectibles = new ArrayList<>(); // holder alle collectibles
    private static final Logger LOGGER = Logger.getLogger(PacManGame.class.getName());


    @Override
    public void start(Stage primaryStage) {
        Pane root = new Pane();
        Scene scene = new Scene(root, 960, 640, Color.BLACK);

        // initierer mappet fra tekst fil
        loadMap(root, "map.txt");

        scene.setOnKeyPressed(e -> pacMan.setCurrentDirection(e.getCode()));

        gameLoop loop = new gameLoop(pacMan, collectibles, root);
        loop.start();

        primaryStage.setTitle("Pac-Man");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public void loadMap(Pane root, String mapFileName) {
        // åpner map filen
        InputStream is = getClass().getClassLoader().getResourceAsStream(mapFileName);
        // hvis den ikke finner filen, log feil melding
        if (is == null) {
            LOGGER.log(Level.SEVERE, "Could not find resource file: {0}", mapFileName);
            return;
        }

        // wrapper inputstream med bufferedReader for å lese tekst fra inputstream
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line; // for å holde linjene
            int y = 0; // for å holde track på linje nummer
            // leser linje til slutten av filen
            while ((line = br.readLine()) != null) {
                for (int x = 0; x < line.length(); x++) {
                    char ch = line.charAt(x);
                    switch (ch) {
                        case 'X':
                            Rectangle wall = new Rectangle(x * 32, y * 32, 32, 32);
                            wall.setFill(Color.BLUE);
                            wall.setStroke(Color.DARKBLUE);
                            root.getChildren().add(wall);
                            walls.add(wall); // setter alle veggene på mappet
                            break;
                        case '.':
                            Food food  = new Food(x * 32 + 16, y * 32 + 16);
                            root.getChildren().add(food.getVisual());
                            collectibles.add(food); // addes til listen
                            break;
                        case '0':
                            PowerUps powerUp = new PowerUps(x * 32 + 16, y * 32 + 16);
                            root.getChildren().add(powerUp.getVisual());
                            collectibles.add(powerUp); // addes de til listen
                            break;
                        case 'P':
                            // initierer pacman her
                            pacMan = new PacMan(root);
                            pacMan.setPosition(x * 32 + 16, y * 32 + 16);
                            pacMan.setWalls(walls);
                            break;

                    }
                }
                y++;
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error reading file: " + mapFileName, e);
        }
    }



    public static void main(String[] args) {
        launch(args);
    }
}
