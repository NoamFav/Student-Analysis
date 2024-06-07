package com.umproject;

import com.umproject.MainPage.Menu;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;


public class Launcher extends Application {

    public static final Rectangle2D bounds = Screen.getPrimary().getBounds();
    public static final double screenWidth = bounds.getWidth();
    public static final double screenHeight = bounds.getHeight();

    public static double Distance(double originalSize, double originalDistance, double newSize) {
        //make the original distance change based on the new size
        return originalDistance * newSize / originalSize;
    }

    @Override
    public void start(Stage stage) {
        //create the main Pane
        Pane root = new Pane();
        root.setPadding(new javafx.geometry.Insets(0));
        root.setStyle("-fx-padding: 0; -fx-background-color: #F8F6F3");

        //create the scene
        Scene scene = new Scene(root, screenWidth, screenHeight);

        //add styling to the scene
        URL cssUrl =  getClass().getResource("/Styling.css");
        if (cssUrl != null) {
            scene.getStylesheets().add(cssUrl.toExternalForm());
        } else {
            System.err.println("Styling.css not found");
        }

        //create the menu
        Menu menu = new Menu();
        menu.createScene(stage,root);

        //change the full-screen exit hint
        stage.setFullScreenExitHint("press -");
        stage.setFullScreenExitKeyCombination(KeyCombination.keyCombination("-"));

        stage.setResizable(false);

        // Remove the title bar
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setFullScreen(true);

        stage.setTitle("ProjectPhase2");

        //show the stage
        stage.setScene(scene);
        stage.show();

        //remove the focus from the search bar text field to the root
        Platform.runLater(root::requestFocus);
    }
}