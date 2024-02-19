package com.example.pacman;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;


public class PacManGame extends Application {

    private PacMan pacMan;
    private static final Logger LOGGER = Logger.getLogger(PacManGame.class.getName());



    @Override
    public void start(Stage primaryStage) {
        Pane root = new Pane();
        Scene scene = new Scene(root, 1200, 800);

        // initierer mappet fra tekst fil
        loadMap(root, "map.txt");

        scene.setOnKeyPressed(e -> pacMan.move(e.getCode()));

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
                            Rectangle wall = new Rectangle(x * 20, y * 20, 20, 20);
                            root.getChildren().add(wall);
                            break;
                        case '.':
                            Circle dot = new Circle(x * 20 + 10, y * 20 + 10, 5);
                            root.getChildren().add(dot);
                            break;
                        case 'P':
                            // initierer pacman her
                            pacMan = new PacMan(root);
                            pacMan.setPosition(x * 20 + 10, y * 20 + 10);
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
