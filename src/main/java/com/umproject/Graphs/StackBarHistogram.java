package com.umproject.Graphs;

import com.umproject.Launcher;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class StackBarHistogram {
    private final String[] courseNames = {"JTE-234", "ATE-003", "TGL-013", "PPL-239", "WDM-974", "GHL-823", "HLU-200", "MON-014", "FEA-907", "LPG-307", "TSO-010", "LDE-009", "JJP-001", "MTE-004", "LUU-003", "LOE-103", "PLO-132", "BKO-800", "SLE-332", "BKO-801", "DSE-003", "DSE-005", "ATE-014", "JTW-004", "ATE-008", "DSE-007", "ATE-214", "JHF-101", "KMO-007", "WOT-104"};
    private final String[] colors = {
            "#14A989", "#3BB143", "#4FFFB0", "#50BFE6", "#66FF66", "#83F52C",
            "#AAF0D1", "#ACACE6", "#CC00FF", "#CCFF00", "#EE34D2", "#FAD6A5",
            "#FD5B78", "#FD5E53", "#FF007C", "#FF00CC", "#FF355E", "#FF4D00",
            "#FF5E4D", "#FF6037", "#FF6EFF", "#FF90C9", "#FF9933", "#FF9966",
            "#FFA6C9", "#FFCC33", "#FFD12A", "#FFD300", "#FFFF66", "#FFFF66"
    };
    /*"#798996", "#7D7387", "#867369", "#8F625C", "#945A5A", "#9B6670", "#A26B80", "#996A88", "#8F6A91", "#86769E",
            "#7C8399", "#738F95", "#6A9B8E", "#63A587", "#5BAF7F", "#56B972", "#5CC162", "#68C957", "#76D14A", "#86D73C",
            "#98DB2D", "#ABDC22", "#BDDD1E", "#CFDA25", "#DDDA38", "#E7D94F", "#EDD569", "#F0D182", "#F1CD9C", "#F0CAB6"*/
    /*"#7F7F7F", "#708090", "#778899", "#B0C4DE", "#6A5ACD", "#483D8B", "#4169E1", "#6495ED", "#4682B4", "#008B8B",
            "#5F9EA0", "#20B2AA", "#008080", "#006400", "#228B22", "#808000", "#BDB76B", "#556B2F", "#8FBC8B", "#2E8B57",
            "#3CB371", "#9ACD32", "#6B8E23", "#B8860B", "#DAA520", "#CD853F", "#D2691E", "#8B4513", "#A0522D", "#BC8F8F"*/
    public void createStackBarHistogram(Pane root, String[][] gradeArray, double dimensionWidth, double dimensionHeight) {
        //create a 2D map of the distribution, of grades for each course
        Map<String, Map<Integer, Integer>> courseGrades = new HashMap<>();
        for (int x = 0; x < gradeArray[0].length; x++) {
            Map<Integer, Integer> gradeCounts = new HashMap<>();
            double a;
            for (String[] strings : gradeArray) {
                try {
                    a = Double.parseDouble(strings[x]);
                    int grade = (int) a;
                    gradeCounts.put(grade, gradeCounts.getOrDefault(grade, 0) + 1);
                } catch (NumberFormatException ignored) {}
            }
            courseGrades.put(courseNames[x], gradeCounts);
        }

        //create a XY axis for the stacked bar chart
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Grades");
        xAxis.setCategories(FXCollections.observableArrayList(
                IntStream.rangeClosed(0, 10).mapToObj(String::valueOf).collect(Collectors.toList())
        ));

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Count");

        //create the stacked bar chart
        StackedBarChart<String, Number> stackedBarChart = new StackedBarChart<>(xAxis, yAxis);
        stackedBarChart.setMinSize(dimensionWidth, dimensionHeight);

        //populate the data for the stacked bar chart with the map
        int i = 0;
        for (String courseName : courseNames) {
            XYChart.Series<String, Number> data = new XYChart.Series<>();
            data.setName(courseName);

            Map<Integer, Integer> gradeCounts = courseGrades.get(courseName);

            for (int grade = 0; grade <= 10; grade++) {
                int count = gradeCounts.getOrDefault(grade, 0);
                XYChart.Data<String, Number> dataPoint = new XYChart.Data<>(String.valueOf(grade), count);

                //add listener to the data point for creating the tooltip and implementing the hover effects
                //also style each course bar with the appropriate color
                final String style = "-fx-bar-fill: " + colors[i] + ";";
                int finalGrade = grade;
                dataPoint.nodeProperty().addListener((observable, oldNode, newNode) -> {
                    if (newNode != null) {
                        newNode.setStyle(style);
                        Tooltip tooltip = new Tooltip(courseName + "\nGrade: " + finalGrade + "\nCount: " + count);
                        Tooltip.install(newNode, tooltip);
                        newNode.setOnMouseEntered(e -> newNode.setStyle("-fx-bar-fill: rgba(173, 216, 230, 0.8);"));
                        newNode.setOnMouseExited(e -> newNode.setStyle(style));
                    }
                });
                //add the data point to the data series
                data.getData().add(dataPoint);

            }
            //add everything to the stacked bar chart
            stackedBarChart.getData().add(data);

            i++;
        }

        //set the title of the stacked bar chart
        stackedBarChart.setTitle("Histogram of Grades by Course");
        stackedBarChart.getStyleClass().add("stacked-bar-chart-title");
        stackedBarChart.setLegendVisible(false);

        //create the legend for the stacked bar chart, with the appropriate color
        VBox legendContainer = new VBox(10);
        HBox currentRow = new HBox(10);
        int itemsPerRow = 15;
        currentRow.setAlignment(Pos.CENTER);

        for (int z = 0; z < courseNames.length; z++) {
            if (z % itemsPerRow == 0 && z != 0) {
                legendContainer.getChildren().add(currentRow);
                currentRow = new HBox(10);
                currentRow.setAlignment(Pos.CENTER);
            }

            VBox legendItem = new VBox(5);
            legendItem.setAlignment(Pos.CENTER);
            Rectangle colorRect = new Rectangle(20, 20);
            colorRect.setFill(Color.web(colors[z]));

            Label label = new Label(courseNames[z]);
            legendItem.getChildren().addAll(colorRect, label);

            currentRow.getChildren().add(legendItem);
        }
        legendContainer.getChildren().add(currentRow);

        VBox mainLayout = new VBox(20);
        mainLayout.getChildren().addAll(stackedBarChart, legendContainer);
        root.getChildren().add(mainLayout);
    }

    public void ShowGraphInPopup(Stage stage, String[][] data, Pane root) {
        //create the popup
        Popup popup = new Popup();

        //create the graph pane
        Pane graphPane = new Pane();
        graphPane.getStyleClass().add("graph-pane");

        //set the size of the graph pane
        double graphPaneWidth = Launcher.Distance(1920, 1280, root.getWidth()) ;
        double graphPaneHeight = Launcher.Distance(1080, 830, root.getHeight());

        double additionalVerticalSpace = 10;
        double paneWidth = graphPaneWidth + additionalVerticalSpace;
        double paneHeight = graphPaneHeight + additionalVerticalSpace;

        graphPane.setPrefSize(paneWidth, paneHeight);

        //create the graph and add it to the graph pane
        createStackBarHistogram(graphPane, data, root.getWidth()/1.5, root.getHeight()/1.5);

        //add the graph pane to the popup
        popup.getContent().add(graphPane);
        popup.setAutoHide(true);

        //set the size of the popup
        popup.setWidth(paneWidth);
        popup.setHeight(paneHeight);

        double stageX = stage.getX();
        double stageY = stage.getY();
        double stageWidth = stage.getWidth();
        double stageHeight = stage.getHeight();

        double popupX = stageX + (stageWidth - paneWidth) / 2;
        double popupY = stageY + (stageHeight - paneHeight) / 2;

        popup.setX(popupX);
        popup.setY(popupY);

        //show the popup
        popup.show(stage);
    }
}
