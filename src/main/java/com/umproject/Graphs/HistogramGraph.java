package com.umproject.Graphs;

import com.umproject.Launcher;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import javafx.stage.Popup;
import javafx.stage.Stage;

public class HistogramGraph {

    public void createHistogram(String[] grades, Pane root){
        double count1;
        double count2;
        double count3;
        double count4;
        double count5;
        double count6;
        double count7;
        double count8;
        double count9;
        double count10;
        double count0;
        count0 = count1 = count2 = count3 = count4 = count5 = count6 = count7 = count8 = count9 = count10 = 0;

        //add the distribution of grades to the counters
        for (String grade : grades) {
            if (!(grade.equals("NG"))) {
                int parsedGrade = (int)Double.parseDouble(grade);
                if (parsedGrade == 0) count0++;
                else if (parsedGrade == 1) count1++;
                else if (parsedGrade == 2) count2++;
                else if (parsedGrade == 3) count3++;
                else if (parsedGrade == 4) count4++;
                else if (parsedGrade == 5) count5++;
                else if (parsedGrade == 6) count6++;
                else if (parsedGrade == 7) count7++;
                else if (parsedGrade == 8) count8++;
                else if (parsedGrade == 9) count9++;
                else if (parsedGrade == 10) count10++;
            }
        }

        //create the histogram with XY axis
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();

        xAxis.setLabel("Grades");
        yAxis.setLabel("Frequency");

        //create the histogram with the barchart and XY axis
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Histogram for this course");

        //create the series for the barchart
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Frequency");

        barChart.prefWidthProperty().bind(root.widthProperty());
        barChart.prefHeightProperty().bind(root.heightProperty());
        barChart.setMinSize(BarChart.USE_PREF_SIZE, BarChart.USE_PREF_SIZE);
        barChart.setMaxSize(BarChart.USE_PREF_SIZE, BarChart.USE_PREF_SIZE);

        //add an x category for every possible grade
        series.getData().add(new XYChart.Data<>("0", count0));
        series.getData().add(new XYChart.Data<>("1", count1));
        series.getData().add(new XYChart.Data<>("2", count2));
        series.getData().add(new XYChart.Data<>("3", count3));
        series.getData().add(new XYChart.Data<>("4", count4));
        series.getData().add(new XYChart.Data<>("5", count5));
        series.getData().add(new XYChart.Data<>("6", count6));
        series.getData().add(new XYChart.Data<>("7", count7));
        series.getData().add(new XYChart.Data<>("8", count8));
        series.getData().add(new XYChart.Data<>("9", count9));
        series.getData().add(new XYChart.Data<>("10", count10));

        //set tooltip for the bars and hover methods with listeners, as well as changing the style of the bars.
        for (XYChart.Data<String, Number> data : series.getData()) {
            Tooltip tooltip = new Tooltip("Grade: " + data.getXValue() + "\nCount: " + data.getYValue());
            data.nodeProperty().addListener((observable, oldNode, newNode) -> {
                if (newNode != null) {
                    // Set the fill and stroke for the bars
                    newNode.setStyle("-fx-bar-fill: #f3612d; -fx-border-color: black; -fx-border-width: 0.5;");
                    Tooltip.install(newNode, tooltip);
                    newNode.setOnMouseEntered(e -> newNode.setStyle("-fx-bar-fill: rgba(173, 216, 230, 0.8); -fx-border-color: black; -fx-border-width: 0.5;"));
                    newNode.setOnMouseExited(e -> newNode.setStyle("-fx-bar-fill: #f3612d; -fx-border-color: black; -fx-border-width: 0.5;"));
                }
            });
        }
        barChart.setCategoryGap(0);
        barChart.setBarGap(0);


        barChart.setTitle("Histogram for this course");
        barChart.getStyleClass().add("chart-title");
        barChart.getData().add(series);

        //add the bar chart to the root
        root.getChildren().add(barChart);
    }
    public void showGraphInPopup(Stage stage, String[] data1, Pane root) {
        //create the popup
        Popup popup = new Popup();

        //create the graph pane
        Pane graphPane = new Pane();
        graphPane.getStyleClass().add("graph-pane");

        double graphPaneWidth = Launcher.Distance(1920, 1280, root.getWidth()) ;
        double graphPaneHeight = Launcher.Distance(1080, 720, root.getHeight());

        double additionalVerticalSpace = Launcher.Distance(1080, 10, root.getHeight());
        double additionalHorizontalSpace = Launcher.Distance(1920, 10, root.getWidth());
        double paneWidth = graphPaneWidth + additionalHorizontalSpace;
        double paneHeight = graphPaneHeight + additionalVerticalSpace;

        //set the size of the graph pane
        graphPane.setPrefSize(paneWidth, paneHeight);

        //create the histogram and add it to the graph pane
        createHistogram(data1, graphPane);

        //add the graph pane to the popup
        popup.getContent().add(graphPane);
        popup.setAutoHide(true);

        //set the size of the popup to match the pane
        popup.setWidth(paneWidth);
        popup.setHeight(paneHeight);

        //calculate position to center the popup on the stage
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
