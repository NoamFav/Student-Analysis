package com.StudentAnalysis.Graphs;

import com.StudentAnalysis.Launcher;
import com.StudentAnalysis.Utils.DataListenerTooltip;
import com.StudentAnalysis.Utils.PopupSetup;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class HeatMap {

    private Map<String, Map<Integer, Integer>> courseGrades;
    private final String[] courseNames = {
        "JTE-234", "ATE-003", "TGL-013", "PPL-239", "WDM-974", "GHL-823", "HLU-200", "MON-014",
        "FEA-907", "LPG-307", "TSO-010", "LDE-009", "JJP-001", "MTE-004", "LUU-003", "LOE-103",
        "PLO-132", "BKO-800", "SLE-332", "BKO-801", "DSE-003", "DSE-005", "ATE-014", "JTW-004",
        "ATE-008", "DSE-007", "ATE-214", "JHF-101", "KMO-007", "WOT-104"
    };

    public void CreateHeatMap(
            Pane root, String[][] gradeArray, double dimensionWidth, double dimensionHeight) {

        // Create a 2D hashmap for each course to get the grade distribution of each course
        courseGrades = new HashMap<>();
        double a;
        for (int x = 0; x < gradeArray[0].length; x++) {
            Map<Integer, Integer> gradeCounts = new HashMap<>();
            for (String[] strings : gradeArray) {
                try {
                    a = Double.parseDouble(strings[x]);
                    int grade = (int) a;
                    gradeCounts.put(grade, gradeCounts.getOrDefault(grade, 0) + 1);
                } catch (NumberFormatException e) {
                    e.printStackTrace(new java.io.PrintWriter(System.err));
                }
            }
            courseGrades.put(courseNames[x], gradeCounts);
        }

        // create XY axis for the stacked bar chart
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Course");
        NumberAxis yAxis = new NumberAxis();

        double minGrades = Math.max(2, calculateMinGrade(gradeArray));
        double maxGrades = calculateMaxGrade(gradeArray);

        yAxis.setAutoRanging(false);

        // Set the Y-axis bounds based on the calculated values
        yAxis.setLowerBound(minGrades - 0.2);
        yAxis.setUpperBound(maxGrades + 1.2);
        yAxis.setLabel("Grades");

        // create stacked bar chart and set its size
        StackedBarChart<String, Number> stackedBarChart = new StackedBarChart<>(xAxis, yAxis);
        stackedBarChart.getStyleClass().add("stacked-bar-chart");
        stackedBarChart.setMinSize(dimensionWidth, dimensionHeight);

        int maxCount =
                courseGrades.values().stream()
                        .flatMap(m -> m.values().stream())
                        .max(Integer::compare)
                        .orElse(1);

        // Iterate over each course and its grade distribution
        for (String course : courseNames) {
            Map<Integer, Integer> gradeCounts =
                    courseGrades.getOrDefault(course, Collections.emptyMap());
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName(course);

            int minGrade = findMinGrade(gradeCounts);
            int maxGrade = findMaxGrade(gradeCounts);

            // Create a transparent segment up to the minimum grade
            if (minGrade > 0) {
                XYChart.Data<String, Number> transparentData = new XYChart.Data<>(course, minGrade);
                series.getData().add(transparentData);
                transparentData
                        .nodeProperty()
                        .addListener(
                                (obs, oldNode, newNode) -> {
                                    if (newNode != null) {
                                        newNode.setStyle("-fx-bar-fill: transparent;");
                                    }
                                });
            }

            // create a segment between the minimum grade and the maximum grade for each increment
            // of 1
            for (int grade = minGrade; grade <= maxGrade; grade++) {
                int count = gradeCounts.getOrDefault(grade, 0);
                // get the color based on the count and the maximum count with function, if the
                // count is greater than 0
                String color = count > 0 ? getColorIntensity(count, maxCount) : "transparent";

                XYChart.Data<String, Number> data = new XYChart.Data<>(course, 1);
                series.getData().add(data);

                final String style = "-fx-bar-fill: " + color + ";";
                DataListenerTooltip dataListenerTooltip = new DataListenerTooltip();
                dataListenerTooltip.dataListenerTooltip(course, grade, count, data, style);
            }
            stackedBarChart.getData().add(series);
        }

        // set the title of the stacked bar chart and add it to the root
        stackedBarChart.setTitle("HeatMap of grades distribution per course");
        stackedBarChart.getStyleClass().add("HeatMap-title");
        stackedBarChart.setLegendVisible(false);
        root.getChildren().add(stackedBarChart);
    }

    public void ShowGraphInPopup(Stage stage, Pane root, String[][] rawGrade) {
        // create the popup
        Popup popup = new Popup();

        HBox container = new HBox(10); // container for heatmap and legend
        container.getStyleClass().add("graph-pane");

        double graphPaneWidth = Launcher.Distance(1920, 1280, root.getWidth());
        double graphPaneHeight = Launcher.Distance(1080, 720, root.getHeight());

        Pane graphPane = new Pane();
        graphPane.setPrefSize(graphPaneWidth, graphPaneHeight);

        CreateHeatMap(graphPane, rawGrade, graphPaneWidth, graphPaneHeight);

        int maxCount =
                courseGrades.values().stream()
                        .flatMap(m -> m.values().stream())
                        .max(Integer::compare)
                        .orElse(1);

        VBox legend = createLegend(maxCount); // create the legend

        container
                .getChildren()
                .addAll(graphPane, legend); // add both heatmap and legend to the container

        // set the size of the popup to include space for the legend
        double additionalVerticalSpace = 10;
        double additionalHorizontalSpace = 20;
        double paneWidth = graphPaneWidth + additionalHorizontalSpace + legend.getPrefWidth();
        double paneHeight = graphPaneHeight + additionalVerticalSpace;

        PopupSetup popupSetup = new PopupSetup();
        popupSetup.popupSetup(stage, popup, container, paneWidth, paneHeight);
    }

    private String getColorIntensity(int count, int maxCount) {
        // create a function to get a gradient from green to yellow to orange to red based on
        // calculation of the normalized value
        double normalizedValue = (double) count / maxCount;

        double yellowStart = 0.1;
        double orangeStart = 0.9;
        int red = 255, green = 255;

        if (normalizedValue <= yellowStart) {
            red = (int) (normalizedValue / yellowStart * 255);
        } else if (normalizedValue <= orangeStart) {
            green =
                    (int)
                            (255
                                    - (normalizedValue - yellowStart)
                                            / (orangeStart - yellowStart)
                                            * 255);
        } else {
            green = (int) ((1 - normalizedValue) / (1 - orangeStart) * 255);
        }
        return String.format("#%02x%02x%02x", red, green, 0);
    }

    private VBox createLegend(int maxCount) {
        // create the legend on the side for the color gradient
        VBox legendPane = new VBox(5);
        legendPane.setAlignment(Pos.CENTER);
        legendPane.setPadding(new Insets(5, 5, 5, 5));

        VBox swatchContainer = new VBox();
        for (int i = 0; i <= 10; i++) {
            Pane colorPane = new Pane();
            colorPane.setPrefSize(20, 30);

            int count = calculateNonLinearCount(i, maxCount);
            String color = getColorIntensity(count, maxCount);
            colorPane.setStyle("-fx-background-color: " + color + ";");
            swatchContainer.getChildren().add(colorPane);
        }
        HBox labelBox = new HBox();
        labelBox.setSpacing(10);
        Label lowLabel = new Label("From the top: lowest to highest count");
        lowLabel.setFont(new Font("Georgia", 12));
        labelBox.getChildren().addAll(lowLabel);
        swatchContainer.getChildren().add(labelBox);
        legendPane.getChildren().add(swatchContainer);
        return legendPane;
    }

    private int calculateNonLinearCount(int i, int maxCount) {
        double normalized = Math.pow((double) i / 10, 2);
        return (int) (normalized * maxCount);
    }

    private int findMinGrade(Map<Integer, Integer> gradeCounts) {
        return gradeCounts.keySet().stream().min(Integer::compare).orElse(0);
    }

    private int findMaxGrade(Map<Integer, Integer> gradeCounts) {
        return gradeCounts.keySet().stream().max(Integer::compare).orElse(10);
    }

    private double calculateMinGrade(String[][] gradeArray) {
        double minGrade = Double.MAX_VALUE;

        for (String[] strings : gradeArray) {
            for (String grade : strings) {
                try {
                    double value = Double.parseDouble(grade);
                    if (value < minGrade) {
                        minGrade = value;
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace(new java.io.PrintWriter(System.err));
                }
            }
        }

        return minGrade;
    }

    private double calculateMaxGrade(String[][] gradeArray) {
        double maxGrade = Double.MIN_VALUE;

        for (String[] strings : gradeArray) {
            for (String grade : strings) {
                try {
                    double value = Double.parseDouble(grade);
                    if (value > maxGrade) {
                        maxGrade = value;
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace(new java.io.PrintWriter(System.err));
                }
            }
        }

        return maxGrade;
    }
}
