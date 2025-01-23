package com.StudentAnalysis.AI;

import com.StudentAnalysis.Launcher;
import com.StudentAnalysis.Utils.SearchBar;

import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class Predict {

    private TextField course;
    private TextField Id;
    private final ContextMenu contextMenu;

    public Predict(Pane root, Stage stage) {

        contextMenu = new ContextMenu();

        Button button = new Button("Predict Grades");
        button.getStyleClass().add("SaveButton");
        button.setFont(new Font("Georgia", 30));
        button.setLayoutX(Launcher.Distance(1920, 523.5, root.getWidth()));
        button.setLayoutY(Launcher.Distance(1080, 71, root.getHeight()));
        button.setPrefWidth(Launcher.Distance(1920, 273, root.getWidth()));
        button.setPrefHeight(Launcher.Distance(1080, 71, root.getHeight()));
        button.setOnAction(
                event -> {
                    try {
                        predictGrades(stage, root);
                    } catch (IOException e) {
                        e.printStackTrace(new java.io.PrintWriter(System.err));
                    }
                });

        root.getChildren().add(button);
    }

    private void predictGrades(Stage stage, Pane root) throws IOException {
        Popup popup = new Popup();
        Pane popupPane = new Pane();
        popupPane.setPrefHeight(Launcher.Distance(1080, 500, root.getHeight()));
        popupPane.setPrefWidth(Launcher.Distance(1920, 600, root.getWidth()));
        popupPane.getStyleClass().add("graph-pane");

        RegressionTreePoissonDeviance tree = new RegressionTreePoissonDeviance();

        VBox vbox = new VBox(20);
        Id = new TextField();
        course = new TextField();

        Id.setPromptText("Student ID");
        course.setPromptText("Course");

        Id.setPrefWidth(Launcher.Distance(1920, 500, root.getWidth()));
        course.setPrefWidth(Launcher.Distance(1920, 500, root.getWidth()));
        Id.setPrefHeight(Launcher.Distance(1080, 50, root.getHeight()));
        course.setPrefHeight(Launcher.Distance(1080, 50, root.getHeight()));
        Id.getStyleClass().add("Search-bar");
        course.getStyleClass().add("Search-bar");

        contextMenu.getStyleClass().add("context-menu");

        CheckBox checkBox = new CheckBox("With random forest");
        checkBox.setFont(new Font("Georgia", 40));
        checkBox.getStyleClass().add("CheckBox");

        setupTextFieldListener();

        Label label = new Label("Predict Grades");
        label.setFont(new Font("Georgia", 50));

        Button button = new Button("Predict");
        button.setFont(new Font("Georgia", 50));
        button.setPrefHeight(Launcher.Distance(1080, 50, root.getHeight()));
        button.setPrefWidth(Launcher.Distance(1920, 200, root.getWidth()));
        button.getStyleClass().add("Predict");

        Label result2 = new Label();
        result2.setFont(new Font("Georgia", 30));

        button.setOnAction(
                event -> {
                    String courseInput = course.getText();
                    int idInput = Integer.parseInt(Id.getText());
                    if (courseInput.isEmpty()
                            || Id.getText().isEmpty()
                            || !SearchBar.studentIds.contains(Id.getText())
                            || !SearchBar.courses.contains(courseInput)) {
                        result2.setText("Please enter a valid course and ID");
                    } else {
                        try {
                            tree.buttonPrediction(
                                    checkBox.isSelected(), courseInput, idInput, result2);
                        } catch (IOException e) {
                            e.printStackTrace(new java.io.PrintWriter(System.err));
                        }
                    }
                });

        vbox.getChildren().addAll(label, Id, course, checkBox, button, result2);
        vbox.setAlignment(javafx.geometry.Pos.CENTER);

        vbox.setPrefWidth(
                popupPane.getPrefWidth()
                        - Launcher.Distance(1920, 100, root.getWidth())); // Adjust width as needed
        vbox.setPrefHeight(
                popupPane.getPrefHeight()
                        - Launcher.Distance(
                                1080, 100, root.getHeight())); // Adjust height as needed

        // Center the VBox in the PopupPane
        vbox.setLayoutX((popupPane.getPrefWidth() - vbox.getPrefWidth()) / 2);
        vbox.setLayoutY((popupPane.getPrefHeight() - vbox.getPrefHeight()) / 2);

        popup.getContent().add(popupPane);

        popupPane.getChildren().add(vbox);
        popup.setAutoHide(true);

        // create the popup
        popup.setX(Launcher.screenWidth / 2 - popupPane.getPrefWidth() / 2);
        popup.setY(Launcher.Distance(1080, 300, root.getHeight()));
        popup.setWidth(Launcher.Distance(1920, 700, root.getWidth()));
        popup.setHeight(Launcher.Distance(1080, 700, root.getHeight()));

        // show the popup
        popup.show(stage);
    }

    private void setupTextFieldListener() {
        Id.textProperty()
                .addListener(
                        (observable, oldValue, newValue) -> updateSuggestions(newValue, Id, false));
        course.textProperty()
                .addListener(
                        (observable, oldValue, newValue) ->
                                updateSuggestions(newValue, course, true));
    } // add a listener to the text fields

    private void updateSuggestions(String input, TextField textField, boolean isCourse) {
        // update the list of suggestions based on the input
        contextMenu.getItems().clear();

        if (!input.isEmpty()) {
            List<String> suggestions = getSuggestions(input, isCourse);
            suggestions.forEach(
                    suggestion -> {
                        MenuItem menuItem = new MenuItem(suggestion);
                        menuItem.setOnAction(event -> textField.setText(suggestion));
                        menuItem.getStyleClass().add("menu-item");
                        contextMenu.getItems().add(menuItem);
                    });

            if (!contextMenu.getItems().isEmpty()) {
                Point2D point2D = textField.localToScreen(0, textField.getHeight());
                contextMenu.show(textField, point2D.getX(), point2D.getY());
            } else {
                contextMenu.hide();
            }
        } else {
            contextMenu.hide();
        }
    }

    private List<String> getSuggestions(String input, boolean isCourse) {
        // return a list of suggestions based on the input in the context menu
        if (isCourse) {
            return SearchBar.courses.stream()
                    .filter(item -> item.toLowerCase().startsWith(input.toLowerCase()))
                    .limit(4)
                    .collect(Collectors.toList());
        } else {
            return SearchBar.studentIds.stream()
                    .filter(item -> item.toLowerCase().startsWith(input.toLowerCase()))
                    .limit(4)
                    .collect(Collectors.toList());
        }
    }
}
