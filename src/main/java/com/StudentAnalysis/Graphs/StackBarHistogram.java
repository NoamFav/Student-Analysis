package com.StudentAnalysis.Graphs;

import com.StudentAnalysis.Launcher;
import com.StudentAnalysis.Utils.DataListenerTooltip;
import com.StudentAnalysis.Utils.PopupSetup;

import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Popup;
import javafx.stage.Stage;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class StackBarHistogram {
    private final String[] courseNames = {
        "JTE-234", "ATE-003", "TGL-013", "PPL-239", "WDM-974", "GHL-823", "HLU-200", "MON-014",
        "FEA-907", "LPG-307", "TSO-010", "LDE-009", "JJP-001", "MTE-004", "LUU-003", "LOE-103",
        "PLO-132", "BKO-800", "SLE-332", "BKO-801", "DSE-003", "DSE-005", "ATE-014", "JTW-004",
        "ATE-008", "DSE-007", "ATE-214", "JHF-101", "KMO-007", "WOT-104"
    };
    private final String[] colors;

    public StackBarHistogram() {
        colors =
                new String[] {
                    "#14A989", "#3BB143", "#4FFFB0", "#50BFE6", "#66FF66", "#83F52C",
                    "#AAF0D1", "#ACACE6", "#CC00FF", "#CCFF00", "#EE34D2", "#FAD6A5",
                    "#FD5B78", "#FD5E53", "#FF007C", "#FF00CC", "#FF355E", "#FF4D00",
                    "#FF5E4D", "#FF6037", "#FF6EFF", "#FF90C9", "#FF9933", "#FF9966",
                    "#FFA6C9", "#FFCC33", "#FFD12A", "#FFD300", "#FFFF66", "#FFFF66"
                };
    }

    public void createStackBarHistogram(
            Pane root,
            String[] @NotNull [] gradeArray,
            double dimensionWidth,
            double dimensionHeight) {
        // create a 2D map of the distribution, of grades for each course
        Map<String, Map<Integer, Integer>> courseGrades = new HashMap<>();
        for (int x = 0; x < gradeArray[0].length; x++) {
            Map<Integer, Integer> gradeCounts = new HashMap<>();
            double a;
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

        // create a XY axis for the stacked bar chart
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Grades");
        xAxis.setCategories(
                FXCollections.observableArrayList(
                        IntStream.rangeClosed(0, 10)
                                .mapToObj(String::valueOf)
                                .collect(Collectors.toList())));

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Count");

        // create the stacked bar chart
        StackedBarChart<String, Number> stackedBarChart = new StackedBarChart<>(xAxis, yAxis);
        stackedBarChart.setMinSize(dimensionWidth, dimensionHeight);

        // populate the data for the stacked bar chart with the map
        int i = 0;
        for (String courseName : courseNames) {
            XYChart.Series<String, Number> data = new XYChart.Series<>();
            data.setName(courseName);

            Map<Integer, Integer> gradeCounts = courseGrades.get(courseName);

            for (int grade = 0; grade <= 10; grade++) {
                int count = gradeCounts.getOrDefault(grade, 0);
                XYChart.Data<String, Number> dataPoint =
                        new XYChart.Data<>(String.valueOf(grade), count);

                // add listener to the data point for creating the tooltip and implementing the
                // hover effects
                // also style each course bar with the appropriate color
                final String style = "-fx-bar-fill: " + colors[i] + ";";
                DataListenerTooltip dataListenerTooltip = new DataListenerTooltip();
                dataListenerTooltip.dataListenerTooltip(courseName, grade, count, dataPoint, style);
                // add the data point to the data series
                data.getData().add(dataPoint);
            }
            // add everything to the stacked bar chart
            stackedBarChart.getData().add(data);

            i++;
        }

        // set the title of the stacked bar chart
        stackedBarChart.setTitle("Histogram of Grades by Course");
        stackedBarChart.getStyleClass().add("stacked-bar-chart-title");
        stackedBarChart.setLegendVisible(false);

        // create the legend for the stacked bar chart, with the appropriate color
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

    public void ShowGraphInPopup(@NotNull Stage stage, String[][] data, @NotNull Pane root) {
        // create the popup
        Popup popup = new Popup();

        // create the graph pane
        Pane graphPane = new Pane();
        graphPane.getStyleClass().add("graph-pane");

        // set the size of the graph pane
        double graphPaneWidth = Launcher.Distance(1920, 1280, root.getWidth());
        double graphPaneHeight = Launcher.Distance(1080, 830, root.getHeight());

        double additionalVerticalSpace = 10;
        double paneWidth = graphPaneWidth + additionalVerticalSpace;
        double paneHeight = graphPaneHeight + additionalVerticalSpace;

        graphPane.setPrefSize(paneWidth, paneHeight);

        // create the graph and add it to the graph pane
        createStackBarHistogram(graphPane, data, root.getWidth() / 1.5, root.getHeight() / 1.5);

        // add the graph pane to the popup
        PopupSetup popupSetup = new PopupSetup();
        popupSetup.popupSetup(stage, popup, graphPane, graphPaneWidth, paneHeight);
    }
}
