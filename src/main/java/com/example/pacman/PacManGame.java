package com.example.pacman;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
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
  // hvis tid  private Text highScoreText = new Text("High Score: 0");
    private Text scoreText = new Text("Score: 0");
    private Text livesText = new Text("Lives: 3");


    @Override
    public void start(Stage primaryStage) {
        // Hbox for top layer
        HBox hud = new HBox(10);
        hud.setPadding(new Insets(15, 12, 15, 12));
        hud.setAlignment(Pos.CENTER);
        hud.getChildren().addAll(scoreText, livesText);
        hud.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));


        // Styler HUD
     //   highScoreText.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        scoreText.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        livesText.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        // gamePane holder game elementene
        Pane gamePane = new Pane();

        // initierer mappet fra tekst fil
        loadMap(gamePane, "map.txt");

        // Byttet til VBox for å kunne stacke HUD og gamePane
        VBox root = new VBox();
        root.getChildren().addAll(hud, gamePane);

        Scene scene = new Scene(root, 735, 880, Color.BLACK);
        scene.setOnKeyPressed(e -> pacMan.setCurrentDirection(e.getCode()));

        gameLoop loop = new gameLoop(pacMan, collectibles, root);
        loop.start();

        primaryStage.setTitle("Pac-Man");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public void loadMap(Pane gamePane, String mapFileName) {
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
                            gamePane.getChildren().add(wall);
                            walls.add(wall); // setter alle veggene på mappet
                            break;
                        case '.':
                            Food food  = new Food(x * 32 + 16, y * 32 + 16);
                            gamePane.getChildren().add(food.getVisual());
                            collectibles.add(food); // addes til listen
                            break;
                        case '0':
                            PowerUps powerUp = new PowerUps(x * 32 + 16, y * 32 + 16);
                            gamePane.getChildren().add(powerUp.getVisual());
                            collectibles.add(powerUp); // addes de til listen
                            break;
                        case 'P':
                            // initierer pacman her
                            pacMan = new PacMan(gamePane, scoreText, livesText);
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
