package com.StudentAnalysis.Graphs;

import com.StudentAnalysis.Launcher;

import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class NGClusters {
    private final Integer[] ngCounter = new Integer[30];
    private final String[] courseNames = {
        "JTE-234", "ATE-003", "TGL-013", "PPL-239", "WDM-974", "GHL-823", "HLU-200", "MON-014",
        "FEA-907", "LPG-307", "TSO-010", "LDE-009", "JJP-001", "MTE-004", "LUU-003", "LOE-103",
        "PLO-132", "BKO-800", "SLE-332", "BKO-801", "DSE-003", "DSE-005", "ATE-014", "JTW-004",
        "ATE-008", "DSE-007", "ATE-214", "JHF-101", "KMO-007", "WOT-104"
    };

    private void CreateClusters(
            String[][] gradeArray, Pane root, double dimensionX, double dimensionY) {
        Arrays.fill(ngCounter, 0);
        Map<String, Integer> courseNGMap = new HashMap<>();

        for (String courseName : courseNames) {
            courseNGMap.put(courseName, 0);
        }

        // Count the number of NGs in each course
        for (int x = 0; x < gradeArray[0].length; x++) {
            for (String[] strings : gradeArray) {
                if (strings[x].equals("NG")) {
                    courseNGMap.put(courseNames[x], courseNGMap.get(courseNames[x]) + 1);
                }
            }
        }

        Map<String, Integer> sortedMap =
                courseNGMap.entrySet().stream()
                        .sorted(Map.Entry.comparingByValue())
                        .collect(
                                Collectors.toMap(
                                        Map.Entry::getKey,
                                        Map.Entry::getValue,
                                        (e1, e2) -> e1,
                                        LinkedHashMap::new));

        int index = 0;
        for (Map.Entry<String, Integer> entry : sortedMap.entrySet()) {
            courseNames[index] = entry.getKey();
            ngCounter[index] = entry.getValue();
            index++;
        }

        // Create the scatterChart with XY axis
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Course");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Count");
        ScatterChart<String, Number> scatterChart = new ScatterChart<>(xAxis, yAxis);

        // sort the counter ascending and create a series with the data
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (int i = 0; i < ngCounter.length; i++) {
            XYChart.Data<String, Number> data = new XYChart.Data<>(courseNames[i], ngCounter[i]);
            series.getData().add(data);

            // Create and set the tooltip for the data point
            Tooltip tooltip = new Tooltip(courseNames[i] + " number of NGs : " + ngCounter[i]);
            Tooltip.install(data.getNode(), tooltip);

            // Set hover effect
            data.nodeProperty()
                    .addListener(
                            (obs, oldNode, newNode) -> {
                                if (newNode != null) {
                                    Tooltip.install(newNode, tooltip);

                                    newNode.setOnMouseEntered(
                                            event -> {
                                                newNode.setScaleX(2);
                                                newNode.setScaleY(2);
                                                newNode.setStyle(
                                                        "-fx-background-color: rgba(173, 216, 230,"
                                                                + " 0.8)");
                                            });
                                    newNode.setOnMouseExited(
                                            event -> {
                                                newNode.setScaleX(1.0);
                                                newNode.setScaleY(1.0);
                                                newNode.setStyle("");
                                            });
                                }
                            });
        }

        series.setName("Number of NG");

        // set the size, the title, and add the series to the scatterChart
        scatterChart.setMinSize(dimensionX, dimensionY);
        scatterChart.setTitle("Sorted number of NG per course");
        scatterChart.getData().add(series);
        System.out.println("Number of NGs per course: " + Arrays.toString(ngCounter));
        System.out.println("Course names: " + Arrays.toString(courseNames));
        scatterChart.getStyleClass().add("chart-title");

        // add the scatterChart to the root
        root.getChildren().add(scatterChart);
    }

    public void ShowGraphInPopup(Stage stage, String[][] data, Pane root) {
        // create a popup
        Popup popup = new Popup();

        // create a pane
        Pane graphPane = new Pane();
        graphPane.getStyleClass().add("graph-pane");

        // set the size
        double graphPaneWidth = Launcher.Distance(1920, 1280, root.getWidth());
        double graphPaneHeight = Launcher.Distance(1080, 720, root.getHeight());

        double additionalVerticalSpace = Launcher.Distance(1080, 10, root.getHeight());
        double additionalHorizontalSpace = Launcher.Distance(1920, 10, root.getWidth());
        double paneWidth = graphPaneWidth + additionalHorizontalSpace;
        double paneHeight = graphPaneHeight + additionalVerticalSpace;

        graphPane.setPrefSize(paneWidth, paneHeight);

        // add the cluster chart to the graph pane
        CreateClusters(data, graphPane, graphPaneWidth, graphPaneHeight);

        // add the graph pane to the popup
        popup.getContent().add(graphPane);
        popup.setAutoHide(true);

        // show the popup
        popup.show(stage);
    }
}
