package com.StudentAnalysis.Graphs;

import com.StudentAnalysis.Launcher;

import javafx.application.Platform;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BoxPlot {

    public void createIndividualBox(
            Pane root,
            double[] grades,
            double dimensionX,
            double dimensionY,
            String courseName,
            Pane mainPane) {

        // create the XY axis
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel(courseName);
        yAxis.setLabel("Value");

        yAxis.setUpperBound(10.2);
        yAxis.setLowerBound(0);

        // make an empty scatter chart for showing the XYaxis
        final ScatterChart<String, Number> scatterChart = new ScatterChart<>(xAxis, yAxis);
        scatterChart.setMinSize(dimensionX, dimensionY);
        scatterChart.setTitle("Box Plot of course " + courseName);
        scatterChart.getStyleClass().add("title-chart");

        // add the scatter chart to the root pane
        root.getChildren().add(scatterChart);

        // run later to allow the box plot to be displayed at the right place
        Platform.runLater(
                () -> {
                    scatterChart.applyCss();
                    scatterChart.layout();

                    // calculate the required values for the box plot
                    double q1 = quartile1(grades);
                    double median = median(grades);
                    double q3 = quartile3(grades);
                    double minVal = min(grades);
                    double maxVal = max(grades);

                    // get the Y positions for the box plot parts
                    double adjustment = 0.75;
                    double yForQ1 = yAxis.getDisplayPosition(q1 - adjustment);
                    double yForMedian = yAxis.getDisplayPosition(median - adjustment);
                    double yForQ3 = yAxis.getDisplayPosition(q3 - adjustment);
                    double yForMinVal = yAxis.getDisplayPosition(minVal - adjustment);
                    double yForMaxVal = yAxis.getDisplayPosition(maxVal - adjustment);
                    double boxHeight = Math.abs(yForQ1 - yForQ3);

                    // create the main rectangle with the first and third quartiles
                    Rectangle box =
                            new Rectangle(
                                    root.getWidth() / 2
                                            - Launcher.Distance(1920, 250, mainPane.getWidth()),
                                    yForQ3,
                                    Launcher.Distance(1920, 500, mainPane.getWidth()),
                                    boxHeight);
                    box.setStyle("-fx-fill: grey");
                    box.setStroke(javafx.scene.paint.Color.BLACK);
                    box.setStrokeWidth(2);

                    double xPosition = root.getWidth() / 2;

                    // create the line for the median
                    Line medianLine =
                            new Line(
                                    root.getWidth() / 2
                                            - Launcher.Distance(1920, 250, mainPane.getWidth()),
                                    yForMedian,
                                    root.getWidth() / 2
                                            + Launcher.Distance(1920, 250, mainPane.getWidth()),
                                    yForMedian);
                    medianLine.setStroke(javafx.scene.paint.Color.RED);
                    medianLine.setStrokeWidth(2);
                    medianLine.toFront();

                    // create the line for the lower whisker and the upper whisker for the box plot
                    Line lowerWhisker = new Line(xPosition, yForMinVal, xPosition, yForQ1);
                    Line upperWhisker = new Line(xPosition, yForQ3, xPosition, yForMaxVal);
                    lowerWhisker.setStrokeWidth(2);
                    upperWhisker.setStrokeWidth(2);

                    // add the box to the root pane
                    root.getChildren().addAll(box, medianLine, lowerWhisker, upperWhisker);

                    // calculate the outliers for the box plot and output them as circles
                    List<Double> outlierValues = outliers(grades);
                    for (Double outlierValue : outlierValues) {
                        double yForOutlier = yAxis.getDisplayPosition(outlierValue - adjustment);
                        Circle outlierCircle =
                                new Circle(
                                        xPosition, yForOutlier, 3, javafx.scene.paint.Color.ORANGE);
                        root.getChildren().add(outlierCircle);
                    }
                });
    }

    public static double median(double[] data) {
        // find the median in the data array
        Arrays.sort(data);
        int middle = data.length / 2;
        if (data.length % 2 == 0) {
            return (data[middle - 1] + data[middle]) / 2.0;
        } else {
            return data[middle];
        }
    }

    public static double quartile1(double[] data) {
        // find the first quartile in the data array
        Arrays.sort(data);
        return median(Arrays.copyOfRange(data, 0, data.length / 2));
    }

    public static double quartile3(double[] data) {
        // find the third quartile in the data array
        Arrays.sort(data);
        return median(Arrays.copyOfRange(data, data.length / 2, data.length));
    }

    public static double interquartileRange(double[] data) {
        // find the interquartile range in the data array
        return quartile3(data) - quartile1(data);
    }

    public static double min(double[] data) {
        Arrays.sort(data);
        return data[0];
    }

    public static double max(double[] data) {
        Arrays.sort(data);
        return data[data.length - 1];
    }

    public static List<Double> outliers(double[] data) {
        // find the outliers in the data array
        double q1 = quartile1(data);
        double q3 = quartile3(data);
        double iqr = interquartileRange(data);
        double lowerBound = q1 - 1.5 * iqr;
        double upperBound = q3 + 1.5 * iqr;

        List<Double> outliers = new ArrayList<>();
        for (double value : data) {
            if (value < lowerBound || value > upperBound) {
                outliers.add(value);
            }
        }
        return outliers;
    }

    public void showGraphInPopup(
            Stage stage, String[] data, Pane root, int index, String courseName) {
        // Create Popup
        Popup popup = new Popup();

        // create a pane to display the graph and add css components to it
        Pane graphPane = new Pane();
        graphPane.getStyleClass().add("graph-pane");

        // calculate the width and height of the graph pane and add it to the graph pane
        double graphPaneWidth = Launcher.Distance(1920, 1280, root.getWidth());
        double graphPaneHeight = Launcher.Distance(1080, 720, root.getHeight());

        double additionalVerticalSpace = Launcher.Distance(1080, 10, root.getHeight());
        double additionalHorizontalSpace = Launcher.Distance(1920, 10, root.getWidth());
        double paneWidth = graphPaneWidth + additionalHorizontalSpace;
        double paneHeight = graphPaneHeight + additionalVerticalSpace;

        graphPane.setPrefSize(paneWidth, paneHeight);

        if (index == 0) {
            // add the individual box to the graph pane
            createIndividualBox(
                    graphPane,
                    StringToDouble(data),
                    graphPaneWidth,
                    graphPaneHeight,
                    courseName,
                    root);
        }

        // Add the graph pane to the popup
        popup.getContent().add(graphPane);
        popup.setAutoHide(true);

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

        // set dimension and show the popup
        popup.show(stage);
    }

    public static double[] StringToDouble(String[] stringArray) {
        // convert the string array to a double array
        List<Double> doubleList = new ArrayList<>();

        for (String str : stringArray) {
            try {
                double convertedValue = Double.parseDouble(str);
                doubleList.add(convertedValue);
            } catch (NumberFormatException e) {
                e.printStackTrace(new java.io.PrintWriter(System.err));
            }
        }
        double[] result = new double[doubleList.size()];
        for (int i = 0; i < doubleList.size(); i++) {
            result[i] = doubleList.get(i);
        }
        return result;
    }
}
