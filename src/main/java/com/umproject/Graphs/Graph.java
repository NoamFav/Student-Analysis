package com.umproject.Graphs;

import com.umproject.Launcher;
import com.umproject.Utils.Widget;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.Objects;

public class Graph {
    private final Pane root;
    private final Stage stage;
    private final Button lineChart;
    private final Button radioChart;
    private final Button barChart;
    public Graph(Pane root, Stage stage) {
        //initialize the root, the stage and the buttons
        this.root = root;
        this.stage = stage;

        lineChart = new Button();
        radioChart = new Button();
        barChart = new Button();

        lineChart.getStyleClass().add("Button");
        radioChart.getStyleClass().add("Button");
        barChart.getStyleClass().add("Button");
    }

    public void createGraphButton() {
        //change the info label
        Widget.info.setText("Compare");

        //load the icons for the buttons
        Image lineChartIcon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/umproject/images/lineChart.png")));
        Image polygonIcon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/umproject/images/RadarChartIcon.png")));
        Image histogramImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/umproject/images/histogram.png")));

        ImageView lineCHartView = new ImageView(lineChartIcon);
        ImageView polygonView = new ImageView(polygonIcon);
        ImageView histogramView = new ImageView(histogramImage);

        double iconWidth = Launcher.Distance(1920,400, root.getHeight());

        //set the layout for the buttons
        lineCHartView.setFitWidth(iconWidth);
        lineCHartView.setFitHeight(iconWidth);
        polygonView.setFitWidth(iconWidth);
        polygonView.setFitHeight(iconWidth);
        histogramView.setFitWidth(iconWidth);
        histogramView.setFitHeight(iconWidth);

        lineChart.setGraphic(lineCHartView);
        radioChart.setGraphic(polygonView);
        barChart.setGraphic(histogramView);

        double height = Launcher.Distance(1080, 589.95, root.getHeight());
        double width = Launcher.Distance(1920,273.33, root.getWidth());

        lineChart.setLayoutX(Launcher.Distance(1920,546.5, root.getWidth()));
        lineChart.setLayoutY(Launcher.Distance(1080,365.85, root.getHeight()));

        lineChart.setPrefWidth(width);
        lineChart.setPrefHeight(height);

        radioChart.setLayoutX(Launcher.Distance(1920,841.33, root.getWidth()));
        radioChart.setLayoutY(Launcher.Distance(1080,365.85, root.getHeight()));

        radioChart.setPrefWidth(width);
        radioChart.setPrefHeight(height);

        barChart.setLayoutX(Launcher.Distance(1920,1136.16, root.getWidth()));
        barChart.setLayoutY(Launcher.Distance(1080,365.85, root.getHeight()));

        barChart.setPrefWidth(width);
        barChart.setPrefHeight(height);

        //Create line chart when clicked
        lineChart.setOnAction(event -> {
            Chart chart = new Chart();
            chart.showGraphInPopup(stage,root, 0);
        });

        //Create radio chart when clicked
        radioChart.setOnAction(event -> {
            Chart chart = new Chart();
            chart.showGraphInPopup(stage,root, 1);
        });

        //Create bar chart when clicked
        barChart.setOnAction(event -> {
            Chart chart = new Chart();
            chart.showGraphInPopup(stage,root, 2);
        });

        //change the side panel accordingly
        Widget.updateLabelsAndIconsGraph();
    }

    public void draw() {
        //create the graph buttons and add the buttons if they don't already exist
        createGraphButton();
        if (!root.getChildren().contains(lineChart)) {
            root.getChildren().add(lineChart);
        }
        if (!root.getChildren().contains(radioChart)) {
            root.getChildren().add(radioChart);
        }
        if (!root.getChildren().contains(barChart)) {
            root.getChildren().add(barChart);
        }
    }

    public void clear() {
        //remove everything from the root
        root.getChildren().removeAll(lineChart, radioChart, barChart);
    }
}
