package com.umproject.Utils;

import com.umproject.Launcher;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

public class PdfSaver {
    public PdfSaver(Pane root, double x, double y) {
        //create PDF button
        Button save = new Button("+ Save as PDF");
        save.getStyleClass().add("SaveButton");
        save.setLayoutX(x);
        save.setLayoutY(y);
        save.setPrefWidth(Launcher.Distance(1920,273,root.getWidth()));
        save.setPrefHeight(Launcher.Distance(1080,71,root.getHeight()));

        root.getChildren().add(save);
    }

    private void createPDF(int input) {
        //initialize logic pdf`
        //not available without external libraries
    }
}
