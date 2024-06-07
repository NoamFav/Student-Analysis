package com.umproject.Graphs;


import com.umproject.Launcher;
import com.umproject.MainPage.Student;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Popup;
import javafx.stage.Stage;

public class LineGraph  {
    private final String[] courseNames = {
            "JTE-234", "ATE-003", "TGL-013", "PPL-239", "WDM-974",
            "GHL-823", "HLU-200", "MON-014", "FEA-907", "LPG-307",
            "TSO-010", "LDE-009", "JJP-001", "MTE-004", "LUU-003",
            "LOE-103", "PLO-132", "BKO-800", "SLE-332", "BKO-801",
            "DSE-003", "DSE-005", "ATE-014", "JTW-004", "ATE-008",
            "DSE-007", "ATE-214", "JHF-101", "KMO-007", "WOT-104"
    };
    private final double[] baselineData = { 5, 5, 3, 5, 5, 4, 5, 4, 5, 4, 0, 4, 5, 4, 5, 4, 0, 4, 4, 0, 4, 5, 4, 5, 5, 5, 5, 5, 4, 5, 4 };
    private final double[] floatingData = { 5, 5, 7, 5, 4, 5, 5, 5, 5, 6, 0, 6, 5, 6, 5, 6, 0, 5, 6, 0, 6, 5, 5, 5, 5, 5, 5, 5, 5, 5 };
    private final double[] graduateBaselineData = { 6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6};
    private final double[] graduateFloatingData = { 4,4,4,4,4,4,4,4,4,4,3,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4};

    double[] graduateStudentPerformanceData = {7.35729490748322,7.98848316139949,6.89798591779925,7.10288739697615,
            7.07248512635773,6.82435456579881,7.50537634408602,7.1413132470935,7.72217673707767,7.6767097865837,6.33677201026145,
            7.12641231373833,7.74903116642105,7.43283663555483,7.42175645434201,7.34042901588341,7.46580426832596,6.82195295016648,
            7.52207848916544,7.03247639320998,7.27443916816768,7.9633753616069,6.87626221276131,7.5093062605753,6.78128923093718,
            7.05933082255335,7.80863489984171,6.83800010916435,7.31482997652967,6.85382893946837};

    private void createAndShowCharts(double[] studentGrades, Pane root, double dimensionWidth, double dimensionHeight , int index) {

        //create XY axis
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Course");

        NumberAxis yAxis = new NumberAxis(0, 10, 1);
        yAxis.setLabel("Grades");

        yAxis.setUpperBound(10.2);
        yAxis.setLowerBound(2.8);

        //create a stacked barchart for the range of the grades
        StackedBarChart<String, Number> stackedBarChart = new StackedBarChart<>(xAxis, yAxis);
        stackedBarChart.getData().clear();

        //the baseline is the min and the floating series is the max - the min
        XYChart.Series<String, Number> baselineSeries = new XYChart.Series<>();
        baselineSeries.setName("Baseline");
        XYChart.Series<String, Number> floatingSeries = new XYChart.Series<>();
        floatingSeries.setName("Floating");

        //change the series based on the index
        if (index == 0) {
            for (int i = 0; i < courseNames.length; i++) {
                baselineSeries.getData().add(new XYChart.Data<>(courseNames[i], baselineData[i]));
            }
            for (int i = 0; i < courseNames.length; i++) {
                floatingSeries.getData().add(new XYChart.Data<>(courseNames[i], floatingData[i]));
            }
        } else if (index == 1) {
            for (int i = 0; i < courseNames.length; i++) {
                baselineSeries.getData().add(new XYChart.Data<>(courseNames[i], graduateBaselineData[i]));
            }
            for (int i = 0; i < courseNames.length; i++) {
                floatingSeries.getData().add(new XYChart.Data<>(courseNames[i], graduateFloatingData[i]));
            }
        }

        //add the series to the stacked bar chart and set the min size
        stackedBarChart.getData().addAll(baselineSeries, floatingSeries);
        stackedBarChart.setMinSize(dimensionWidth, dimensionHeight);

        //set the baseline color to transparent
        for (XYChart.Data<String, Number> data : baselineSeries.getData()) {
            if (data.getNode() != null) {
                data.getNode().setStyle("-fx-bar-fill: transparent;");
            }
        }

        //set the floating line color to #009966
        for (XYChart.Data<String, Number> data : floatingSeries.getData()) {
            if (data.getNode() != null) {
                data.getNode().setStyle("-fx-bar-fill: #009966;-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);");
                data.getNode().setStyle(data.getNode().getStyle() + "-fx-stroke: #000000; -fx-stroke-width: 1;");
            }
        }

        //create a line chart for the grade of a student, and the averages of the students
        LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.lookup(".chart-plot-background").setStyle("-fx-background-color: transparent;");
        lineChart.setStyle("-fx-background-color: transparent;");

        //create a series for the averages of the students
        XYChart.Series<String, Number> studentSeries = new XYChart.Series<>();
        studentSeries.setName("Students averages");

        //set the averages based on the index and the predicate checkbox state and add the series to the line chart
        if (index == 0) {
            for (int i = 0; i < courseNames.length; i++) {
                XYChart.Data<String, Number> data = new XYChart.Data<>(courseNames[i], Student.predicate.isSelected() ? PolygonGraph.averageGrades3[i] : PolygonGraph.averageGrades[i]);
                makeNodeInvisibleForZeroValue(data);
                studentSeries.getData().add(data);
            }
        } else if (index == 1) {
            for (int i = 0; i < courseNames.length; i++) {
                XYChart.Data<String, Number> data = new XYChart.Data<>(courseNames[i], graduateStudentPerformanceData[i]);
                makeNodeInvisibleForZeroValue(data);
                studentSeries.getData().add(data);
            }
        }

        //create a series for the grades of a student
        XYChart.Series<String, Number> inputSeries = new XYChart.Series<>();
        inputSeries.setName("Student grades");
        for (int i = 0; i < courseNames.length; i++) {
            XYChart.Data<String, Number> data = new XYChart.Data<>(courseNames[i], studentGrades[i]);
            makeNodeInvisibleForZeroValue(data);
            inputSeries.getData().add(data);
        }
        //add the series to the line chart
        lineChart.getData().addAll(studentSeries, inputSeries);

        //style the line chart averages
        studentSeries.getNode().getStyleClass().add("student-series");
        for (XYChart.Data<String, Number> data : studentSeries.getData()) {
            Node symbol = data.getNode();
            if (symbol != null) {
                symbol.getStyleClass().add("blue-line-symbol");
            }
        }

        //style the line chart grades
        inputSeries.getNode().getStyleClass().add("input-series");
        for (XYChart.Data<String, Number> data : inputSeries.getData()) {
            Node symbol = data.getNode();
            if (symbol != null) {
                symbol.getStyleClass().add("red-line-symbol");
            }
        }

        //add the tooltip to the line chart and delete the old legend
        addTooltipsToLineChartData(studentSeries, "Average Grade: ");
        addTooltipsToLineChartData(inputSeries, "Student Grade: ");
        stackedBarChart.setLegendVisible(false);
        lineChart.setLegendVisible(false);

        //set the size of the line chart
        lineChart.setMinSize(dimensionWidth, dimensionHeight);

        //create a group for the stacked bar chart and the line chart
        Group group = new Group(stackedBarChart, lineChart);

        //create the legend into and HBox and a VBox pane for the result
        HBox customLegend = createCustomLegend();
        VBox pane = new VBox(15);

        //create a title and add everything to the VBox and then to the root
        Label title = new Label("Student grades compared to averages with courses range");
        title.setFont(Font.font("Georgia", FontWeight.BOLD, Launcher.Distance(1920, 20, dimensionWidth*1.5)));
        pane.getChildren().addAll(title, group, customLegend);
        root.getChildren().add(pane);
        pane.setAlignment(Pos.CENTER);

    }

    private void addTooltipsToLineChartData(XYChart.Series<String, Number> series, String prefix) {
        //add the tooltip to every point in the line chart
        for (XYChart.Data<String, Number> data : series.getData()) {
            Tooltip tooltip = new Tooltip(prefix + data.getYValue());
            Node node = data.getNode();
            if (node != null) {
                Tooltip.install(node, tooltip);
            } else {
                data.nodeProperty().addListener((obs, oldNode, newNode) -> {
                    if (newNode != null) {
                        Tooltip.install(newNode, tooltip);
                    }
                });
            }
        }
    }
    private void makeNodeInvisibleForZeroValue(XYChart.Data<String, Number> data) {
        //if the node is at zero, make the node invisible
        data.nodeProperty().addListener((obs, oldNode, newNode) -> {
            if (newNode != null && data.getYValue().doubleValue() == 0) {
                newNode.setVisible(false);
            }
        });
    }
    private HBox createCustomLegend() {
        //create custom legend for the three items into the legend box HBox
        HBox legendBox = new HBox(20);
        legendBox.setAlignment(Pos.CENTER);
        legendBox.setPadding(new Insets(15, 0, 15, 0));

        Rectangle floatingSymbol = new Rectangle(20,20, Color.web("#009966"));
        Circle studentAverageSymbol = new Circle(10, Color.BLUE);
        Circle studentGradeSymbol = new Circle(10, Color.RED);

        Label floatingLabel = new Label("Range", floatingSymbol);
        floatingLabel.setContentDisplay(ContentDisplay.RIGHT);
        Label studentAverageLabel = new Label("Students averages", studentAverageSymbol);
        studentAverageLabel.setContentDisplay(ContentDisplay.RIGHT);
        Label studentGradeLabel = new Label("Student grades", studentGradeSymbol);
        studentGradeLabel.setContentDisplay(ContentDisplay.RIGHT);

        legendBox.getChildren().addAll(floatingLabel, studentAverageLabel, studentGradeLabel);

        return legendBox;
    }
    public void showGraphInPopup(Stage stage, double[] data1, Pane root, int index) {
        //create the popup
        Popup popup = new Popup();

        //create the graph pane
        Pane graphPane = new Pane();
        graphPane.getStyleClass().add("graph-pane");

        //set the size of the graph pane
        double graphPaneWidth = Launcher.Distance(1920, 1280, root.getWidth()) ;
        double graphPaneHeight = Launcher.Distance(1080, 720, root.getHeight());

        double additionalVerticalSpace = 100;
        double paneHeight = graphPaneHeight + additionalVerticalSpace;

        //set the size of the graph pane
        graphPane.setPrefSize(graphPaneWidth, paneHeight);

        //create the line chart and add it to the graph pane
        createAndShowCharts(data1, graphPane, root.getWidth()/1.5, root.getHeight()/1.5, index);

        //add the graph pane to the popup
        popup.getContent().add(graphPane);
        popup.setAutoHide(true);

        //set the size of the popup
        popup.setWidth(graphPaneWidth);
        popup.setHeight(paneHeight);

        double stageX = stage.getX();
        double stageY = stage.getY();
        double stageWidth = stage.getWidth();
        double stageHeight = stage.getHeight();

        double popupX = stageX + (stageWidth - graphPaneWidth) / 2;
        double popupY = stageY + (stageHeight - paneHeight) / 2;

        popup.setX(popupX);
        popup.setY(popupY);

        //show the popup
        popup.show(stage);
    }
}
