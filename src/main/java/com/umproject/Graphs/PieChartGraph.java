package com.umproject.Graphs;

import com.umproject.Launcher;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.Pane;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class PieChartGraph {
    private static void CreatePieChart(String[] grades, Pane root, double dimensionX, double dimensionY) {
        //create a map of the distribution, of the grades
        Map<String, Integer> map = new HashMap<>();
        for (String grade : grades) {
            String gradeString;
            if (!grade.equals("NG")) {
                int gradeInt = (int) Double.parseDouble(grade);
                gradeString = String.valueOf(gradeInt);
            } else {
                gradeString = "NG";
            }
            map.put(gradeString, map.getOrDefault(gradeString, 0) + 1);
        }

        //create pie chart data, with the map as values
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        map.forEach((key, value) -> pieChartData.add(new PieChart.Data("Grade: " + key, value)));

        //create a pie chart, with the data
        PieChart chart = new PieChart(pieChartData);
        chart.setTitle("Grade Distribution");
        chart.setMinSize(dimensionX, dimensionY);

        //add the chart to the root
        root.getChildren().add(chart);
    }
    public void showGraphInPopup(Stage stage, String[] data1, Pane root) {
        //create a popup
        Popup popup = new Popup();

        //create a pane
        Pane graphPane = new Pane();
        graphPane.getStyleClass().add("graph-pane");

        //set the size
        double graphPaneWidth = Launcher.Distance(1920, 1280, root.getWidth()) ;
        double graphPaneHeight = Launcher.Distance(1080, 720, root.getHeight());

        double additionalVerticalSpace = Launcher.Distance(1080, 10, root.getHeight());
        double additionalHorizontalSpace = Launcher.Distance(1920, 10, root.getWidth());
        double paneWidth = graphPaneWidth + additionalHorizontalSpace;
        double paneHeight = graphPaneHeight + additionalVerticalSpace;
        graphPane.setPrefSize(paneWidth, paneHeight);

        //add the pie chart to the graph pane
        CreatePieChart(data1, graphPane, graphPaneWidth, graphPaneHeight);

        //add the graph pane to the popup
        popup.getContent().add(graphPane);
        popup.setAutoHide(true);

        //show the popup
        popup.show(stage);
    }
}
