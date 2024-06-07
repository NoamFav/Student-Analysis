package com.umproject.Graphs;

import com.umproject.Launcher;
import com.umproject.Utils.PopupSetup;
import com.umproject.Utils.SearchBar;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Chart {
    private static final String CURRENT_FILE_PATH = "src/main/resources/com/example/umproject/CurrentGrades.csv";
    private static final String GRADUATE_FILE_PATH = "src/main/resources/com/example/umproject/bugFreeGraduateGrades.csv";
    private static final String RATIO_FILE_PATH = "src/main/resources/com/example/umproject/RatioPredictionRaw.csv";

    private String selectedFilePath;
    private String[] courses;
    private String selectedStudent;
    private String sortingOption;
    private String secondSelectedStudent;
    private static TextField studentIdField;
    private static TextField secondStudentIdField;
    private ContextMenu contextMenu;

    public void CreateGraph(Pane root, int index) {

        //create the text field and context menu for the input and recommendations menu
        studentIdField = new TextField();
        secondStudentIdField = new TextField();
        contextMenu = new ContextMenu();

        //set up the textfield listener for the context menu
        setupTextFieldListener();

        //add css components to the text fields
        studentIdField.getStyleClass().add("Graph-text");
        secondStudentIdField.getStyleClass().add("Graph-text");

        //create the Apply button and choice box for outputting the resulting graph
        Button applyButton = new Button("Apply");
        applyButton.getStyleClass().add("Button");
        ChoiceBox<String> fileChoiceBox = new ChoiceBox<>(FXCollections.observableArrayList("CurrentGrades.csv", "GraduateGrades.csv", "RatioPredictedGrades.csv"));
        fileChoiceBox.setValue("GraduateGrades.csv");
        fileChoiceBox.getStyleClass().add("choiceBox");
        ChoiceBox<String> sortingChoiceBox = new ChoiceBox<>(FXCollections.observableArrayList("Not Sorted", "Highest to Lowest", "Lowest to Highest"));
        sortingChoiceBox.setValue("Not Sorted");
        sortingChoiceBox.getStyleClass().add("choiceBox");

        //set the default path
        selectedFilePath = GRADUATE_FILE_PATH;

        //create Labels to guide the user of what he needs to input
        Label student1Label = new Label("Enter Student ID: ");
        Label student2Label = new Label("Compare with Student ID: ");
        Label fileLabel = new Label("Select File: ");
        Label sortingLabel = new Label("Sort Order: ");

        //add css components to the labels
        student1Label.getStyleClass().add("Graph-Label");
        student2Label.getStyleClass().add("Graph-Label");
        fileLabel.getStyleClass().add("Graph-Label");
        sortingLabel.getStyleClass().add("Graph-Label");

        //create the main Vbox to show the textfield, labels, button, and choice box with HBoxes
        HBox studentSelection = new HBox(10, student1Label, studentIdField, applyButton);
        HBox secondStudentSelection = new HBox(10,student2Label, secondStudentIdField);
        HBox fileSortingSelection = new HBox(10, fileLabel, fileChoiceBox);
        if (!(index == 1)) {
            fileSortingSelection.getChildren().addAll(sortingLabel, sortingChoiceBox);
        }
        VBox pane = new VBox(20, studentSelection, secondStudentSelection, fileSortingSelection);

        pane.setPadding(new Insets(20));

        //add components to the main root
        root.getChildren().add(pane);

        //set up the listener for the "apply" button
        applyButton.setOnAction(e -> {
            selectedStudent = studentIdField.getText();
            secondSelectedStudent = secondStudentIdField.getText();
            sortingOption = sortingChoiceBox.getValue();
            String[] courseNames = loadCourses();
            if (courseNames != null) {
                if (!SearchBar.combinedList.contains(selectedStudent) || !SearchBar.combinedList.contains(secondSelectedStudent)) {return;}
                if (index == 0) {
                    updateLineChart(courseNames, pane, root);
                } else if (index == 1) {
                    updateRadarChart(pane, root);
                } else if (index == 2) {
                    updateBarChart(courseNames, pane, root);
                }
            }
        });

        //set up the listener for the choice box
        fileChoiceBox.setOnAction(e -> {
            if (fileChoiceBox.getValue().equals("GraduateGrades.csv")) selectedFilePath = GRADUATE_FILE_PATH;
            else if (fileChoiceBox.getValue().equals("CurrentGrades.csv")) selectedFilePath = CURRENT_FILE_PATH;
            else if (fileChoiceBox.getValue().equals("RatioPredictedGrades.csv")) selectedFilePath = RATIO_FILE_PATH;
            String[] courseNames = loadCourses();
            if (courseNames != null) {
                if (index == 0) {
                    updateLineChart(courseNames, pane, root);
                } else if (index == 1) {
                    updateRadarChart(pane, root);
                } else if (index == 2) {
                    updateBarChart(courseNames,pane, root);
                }
            }
        });
        loadCourses();
    }
    private String[] loadCourses() {
        //load the courses using the filepath used
        try (BufferedReader br = new BufferedReader(new FileReader(selectedFilePath))) {
            courses = Arrays.copyOfRange(br.readLine().split(","), 1, br.readLine().split(",").length);
        } catch (IOException e) {
            e.printStackTrace(new java.io.PrintWriter(System.err));
        }
        return courses;
    }
    private void updateLineChart(String[] courseNames, VBox root, Pane mainPane) {

        if (selectedStudent == null || selectedStudent.isEmpty()) {
            return;
        }

        //load grades for both selected students
        double[] grades = loadStudentGrades(selectedStudent);
        double[] secondStudentGrades = loadStudentGrades(secondSelectedStudent);

        if (grades == null) {
            return;
        }

        //create a line chart for the selected student with XY axis
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis(0, 10, 1);
        LineChart<String, Number> chart = new LineChart<>(xAxis, yAxis);

        //create a series for the first student
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Grades student " + selectedStudent);

        //add the first student grades to the series
        for (int i = 0; i < courseNames.length; i++) {
            series.getData().add(new XYChart.Data<>(courseNames[i], grades[i]));
        }

        //sort the data if choice box is selected
        if ("Highest to Lowest".equals(sortingOption)) {
            series.getData().sort(Comparator.<XYChart.Data<String, Number>>comparingDouble(data -> data.getYValue().doubleValue()).reversed());
        } else if ("Lowest to Highest".equals(sortingOption)) {
            series.getData().sort(Comparator.comparingDouble(data -> data.getYValue().doubleValue()));
        }

        //check if the second student has grades
        if (secondStudentGrades != null) {
            //create a series for the second student and add the grades to the series
            XYChart.Series<String, Number> secondSeries = new XYChart.Series<>();
            secondSeries.setName("Student " + secondSelectedStudent + " Grades");
            for (int i = 0; i < courseNames.length; i++) {
                secondSeries.getData().add(new XYChart.Data<>(courseNames[i], secondStudentGrades[i]));
            }

            //add both series to the chart
            chart.getData().addAll(series, secondSeries);
        } else {
            //if the second student does not have grades, only add the first series to the chart
            chart.getData().setAll(series);
        }

        //set minSize for the chart, remove the previous chart if it exists, and add the new chart
        chart.setMinSize(mainPane.getWidth()*0.98, mainPane.getHeight()/1.3);
        ObservableList<Node> rootChildren = root.getChildren();
        rootChildren.removeIf(node -> node instanceof LineChart);
        root.getChildren().add(chart);
    }
    private void updateBarChart(String[] courseNames, Pane root, Pane mainPane) {

        if (selectedStudent == null || selectedStudent.isEmpty()) {
            return;
        }

        //load grades for both selected students
        double[] grades = loadStudentGrades(selectedStudent);
        double[] grades2 = loadStudentGrades(secondSelectedStudent);

        if (grades == null) {
            return;
        }

        //create a bar chart for the selected student with XY axis
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis(0, 10, 1);
        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);

        //create a series for the first student
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Grades student " + selectedStudent);

        //add the first student grades to the series
        for (int i = 0; i < courses.length; i++) {
            series.getData().add(new XYChart.Data<>(courses[i], grades[i]));
        }

        //sort the data if choice box is selected
        if ("Highest to Lowest".equals(sortingOption)) {
            series.getData().sort(Comparator.<XYChart.Data<String, Number>>comparingDouble(data -> (double) data.getYValue()).reversed());
        } else if ("Lowest to Highest".equals(sortingOption)) {
            series.getData().sort(Comparator.comparingDouble(data -> (double) data.getYValue()));
        }

        //check if the second student has grades
        if (grades2!= null) {
            //create a series for the second student and add the grades to the series
            XYChart.Series<String, Number> averageSeries = new XYChart.Series<>();
            averageSeries.setName("Average Grades");
            for (int i = 0; i < courses.length; i++) {
                averageSeries.getData().add(new XYChart.Data<>(courseNames[i], grades2[i]));
            }

            //add both series to the chart
            chart.getData().addAll(series, averageSeries);
        } else {
            //if the second student does not have grades, only add the first series to the chart
            chart.getData().setAll(series);
        }

        //set minSize for the chart, remove the previous chart if it exists, and add the new chart
        chart.setMinSize(mainPane.getWidth()*0.98, mainPane.getHeight()/1.3);
        ObservableList<Node> rootChildren = root.getChildren();
        rootChildren.removeIf(node -> node instanceof BarChart);
        root.getChildren().add(chart);
    }
    private void updateRadarChart(VBox root, Pane mainPane) {

        if (selectedStudent == null || selectedStudent.isEmpty()) {
            return;
        }

        //load the grades for both selected students
        double[] grades = loadStudentGrades(selectedStudent);
        double[] secondStudentGrades = loadStudentGrades(secondSelectedStudent);

        if (grades == null) {
            return;
        }

        //create an instance of the radar chart and draw it with the two selected students grades
        PolygonGraph polygonGraph = new PolygonGraph();
        Pane pane = polygonGraph.draw(mainPane.getWidth()/2, mainPane.getHeight()/2.7, Launcher.Distance(1080,400, mainPane.getHeight()), grades, secondStudentGrades, mainPane, Integer.parseInt(selectedStudent), Integer.parseInt(secondSelectedStudent));
        pane.setId("radarChartPane"); //set an id to the graph pane so that it can be used in lookup

        //lookup if the radarChart is already in the root and if so, remove it and add the new graph
        Node oldPane = root.lookup("#radarChartPane");
        if (oldPane != null) {
            root.getChildren().remove(oldPane);
        }
        root.getChildren().add(pane);
    }
    private double[] loadStudentGrades(String studentId) {
        //load the grades of a student using the filepath currently selected
        //and output the grades in an array
        try (BufferedReader br = new BufferedReader(new FileReader(selectedFilePath))) {
            String[] allCourses = br.readLine().split(",");
            double[] grades = new double[allCourses.length - 1];

            String studentData;
            boolean studentFound = false;
            while ((studentData = br.readLine()) != null) {
                String[] studentInfo = studentData.split(",");
                if (studentInfo[0].trim().equals(studentId.trim())) {
                    studentFound = true;

                    if (studentInfo.length < allCourses.length) {
                        return null;
                    }

                    for (int i = 0; i < allCourses.length - 1; i++) {
                        grades[i] = parseGrade(studentInfo[i + 1]);
                    }
                    break;
                }
            }
            return studentFound ? grades : null;

        } catch (IOException e) {
            e.printStackTrace(new java.io.PrintWriter(System.err));
            return null;
        }
    }
    private double parseGrade(String grade) {
        //replace NG or empty string by 0.0 to avoid errors
        if (grade == null || grade.trim().isEmpty() || grade.equalsIgnoreCase("NG")) {
            return 0.0;
        }
        return Double.parseDouble(grade);
    }
    public void showGraphInPopup(Stage stage, Pane root, int index) {
        //create a popup to display the graph
        Popup popup = new Popup();

        //create a pane to display the graph and add CSS to it
        Pane graphPane = new Pane();
        graphPane.getStyleClass().add("graph-pane");

        //calculate the width and height of the graph pane and add it to the graph pane
        double graphPaneWidth = Launcher.Distance(1920, 1280, root.getWidth()) ;
        double graphPaneHeight = Launcher.Distance(1080, 720, root.getHeight());

        double additionalVerticalSpace = Launcher.Distance(1080, 80, root.getHeight());
        double additionalHorizontalSpace = Launcher.Distance(1920, 10, root.getWidth());
        double paneWidth = graphPaneWidth + additionalHorizontalSpace;
        double paneHeight = graphPaneHeight + additionalVerticalSpace;

        graphPane.setPrefSize(paneWidth, paneHeight);

        //add the graph to the graph pane
        CreateGraph(graphPane, index);

        //add the graph pane to the popup
        PopupSetup popupSetup = new PopupSetup();
        popupSetup.popupSetup(stage, popup, graphPane, graphPaneWidth, paneHeight);
    }
    private void setupTextFieldListener() {studentIdField.textProperty().addListener((observable, oldValue, newValue) -> updateSuggestions(newValue, studentIdField)); secondStudentIdField.textProperty().addListener((observable, oldValue, newValue) -> updateSuggestions(newValue, secondStudentIdField));} //add a listener to the text fields
    private void updateSuggestions(String input, TextField textField) {
        //update the list of suggestions based on the input
        contextMenu.getItems().clear();

        if (!input.isEmpty()) {
            List<String> suggestions = getSuggestions(input);
            suggestions.forEach(suggestion -> {
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
    private List<String> getSuggestions(String input) {
        //return a list of suggestions based on the input in the context menu
        return SearchBar.combinedList.stream()
                .filter(item -> item.toLowerCase().startsWith(input.toLowerCase()))
                .limit(4)
                .collect(Collectors.toList());
    }
}
