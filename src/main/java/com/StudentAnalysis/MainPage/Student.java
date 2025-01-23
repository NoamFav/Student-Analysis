package com.StudentAnalysis.MainPage;

import com.StudentAnalysis.Cards.graduateStudentCard;
import com.StudentAnalysis.Cards.studentCard;
import com.StudentAnalysis.Graphs.LineGraph;
import com.StudentAnalysis.Graphs.PolygonGraph;
import com.StudentAnalysis.Launcher;
import com.StudentAnalysis.Utils.GradeCalculator;
import com.StudentAnalysis.Utils.ReaderCsv;
import com.StudentAnalysis.Utils.WidgetSetup;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;
import java.util.Objects;

public class Student {

    private final TableView<String[]> table;
    private final Pane root;
    private final Stage stage;
    private Button polygon;
    private Button lineChart;
    private final Label attributesLabel;
    private final String input;
    public static int index = 0;
    public static CheckBox predicate;
    public static double[] studentsGrades = new double[31];
    public static double[] graduatesGrades = new double[31];

    public Student(Pane root, String input, Stage stage) {
        // initialize the root, the input and the stage
        this.root = root;
        this.input = input;
        this.stage = stage;

        // initialize the attributesLabel, the predicate, and the table
        attributesLabel = new Label();
        predicate = new CheckBox("Allow predictions");
        predicate.getStyleClass().add("CheckBox");
        predicate.setLayoutX(Launcher.Distance(1920, 838, root.getWidth()));
        predicate.setLayoutY(Launcher.Distance(1080, 308, root.getHeight()));
        predicate.setPrefWidth(Launcher.Distance(1920, 150, root.getWidth()));
        predicate.setPrefHeight(Launcher.Distance(1080, 20, root.getHeight()));

        table = new TableView<>();
        table.setLayoutX(Launcher.Distance(1920, 549, root.getWidth()));
        table.setLayoutY(Launcher.Distance(1080, 365.85, root.getHeight()));
        table.setPrefWidth(Launcher.Distance(1920, 858, root.getWidth()));
        table.setPrefHeight(Launcher.Distance(1080, 589.95, root.getHeight()));
    }

    private void setData(int index) {
        // initialize both info and graduate array
        // info array change if the predicate checkbox is selected
        studentCard card = new studentCard();
        graduateStudentCard grades = new graduateStudentCard();
        boolean checked = predicate.isSelected();
        String[][] infoArray = card.infoStudent(input, checked);
        String[][] graduatesArray = grades.getStudentInfo(input);

        // redraw the table when selected
        predicate.setOnAction(
                event -> {
                    clear();
                    draw(index);
                });

        // load info or graduates array based on the index
        if (index == 0) {
            for (int i = 1; i < studentsGrades.length; i++) {
                if (infoArray[i][1].equals("NG")) {
                    studentsGrades[i - 1] = 0.0;
                } else {
                    try {
                        studentsGrades[i - 1] = Double.parseDouble(infoArray[i][1]);
                    } catch (NumberFormatException e) {
                        studentsGrades[i - 1] = 0;
                    }
                }
            }
        } else if (index == 1) {
            for (int i = 1; i < graduatesGrades.length; i++) {
                if (graduatesArray[i][1].equals("NG")) {
                    graduatesGrades[i - 1] = 0.0;
                } else {
                    try {
                        graduatesGrades[i - 1] = Double.parseDouble(graduatesArray[i][1]);
                    } catch (NumberFormatException e) {
                        graduatesGrades[i - 1] = 0;
                    }
                }
            }
        }

        // clear the old table
        table.getItems().clear();
        table.getColumns().clear();

        // populate the datalist depending on the index
        ObservableList<String[]> dataList = FXCollections.observableArrayList();
        if (index == 0) {
            dataList.addAll(Arrays.asList(Objects.requireNonNull(infoArray)));
        } else if (index == 1) {
            dataList.addAll(Arrays.asList(Objects.requireNonNull(graduatesArray)));
        }
        dataList.removeFirst();

        Comparator<String> numericComparator = Menu.createNumericComparator();

        // populate the table depending on the index
        if (index == 0) {
            loadTable(infoArray, numericComparator);
        } else if (index == 1) {
            loadTable(graduatesArray, numericComparator);
        }

        // set the data into the table
        GradeCalculator gradeCalculator = new GradeCalculator(infoArray, graduatesArray);
        double averages = gradeCalculator.calculateAverageGrade(index);
        int validNumbersCount = gradeCalculator.getValidNumbersCount();

        // calculate the average of students' grades depending on the index
        double differences1 = getDifferences1(index, averages);

        double differences2 =
                (((30 - validNumbersCount) - 15.906914893617) / 15.906914893617)
                        * 100; // compare its number of NG to the average NGs of students

        double differences3 =
                ((validNumbersCount - 14.093085106383) / 14.093085106383)
                        * 100; // compare its number of grades to the average grades of students

        // update the labels and icons
        WidgetSetup.updateLabelsAndIconsStudent(
                averages,
                30 - validNumbersCount,
                validNumbersCount,
                differences1,
                differences2,
                differences3,
                index);

        // create the radar chart button
        polygon = new Button();
        polygon.getStyleClass().add("Button");
        Image polygonIcon =
                new Image(
                        Objects.requireNonNull(
                                getClass()
                                        .getResourceAsStream(
                                                "/com/StudentAnalysis/images/RadarChartIcon.png")));
        ImageView imageView = new ImageView(polygonIcon);
        imageView.setFitWidth(Launcher.Distance(1920, 40, root.getWidth()));
        imageView.setFitHeight(Launcher.Distance(1080, 40, root.getHeight()));
        polygon.setGraphic(imageView);

        polygon.setLayoutX(Launcher.Distance(1920, 1347, root.getWidth()));
        polygon.setLayoutY(Launcher.Distance(1080, 297, root.getHeight()));

        // create the line chart graph depending on the index
        polygon.setOnAction(
                event -> {
                    PolygonGraph polygonGraph = new PolygonGraph();
                    if (index == 0) {
                        polygonGraph.showGraphInPopup(stage, root, studentsGrades, index);
                    } else if (index == 1) {
                        polygonGraph.showGraphInPopup(stage, root, graduatesGrades, index);
                    }
                });

        // create the line chart button
        lineChart = new Button();
        lineChart.getStyleClass().add("Button");
        Image lineChartIcon =
                new Image(
                        Objects.requireNonNull(
                                getClass()
                                        .getResourceAsStream(
                                                "/com/StudentAnalysis/images/lineChart.png")));
        ImageView lineCHartView = new ImageView(lineChartIcon);
        lineCHartView.setFitWidth(Launcher.Distance(1920, 40, root.getWidth()));
        lineCHartView.setFitHeight(Launcher.Distance(1080, 40, root.getHeight()));
        lineChart.setGraphic(lineCHartView);

        lineChart.setLayoutX(Launcher.Distance(1920, 1277, root.getWidth()));
        lineChart.setLayoutY(Launcher.Distance(1080, 297, root.getHeight()));

        // create the line chart graph depending on the index
        lineChart.setOnAction(
                event -> {
                    LineGraph lineGraph = new LineGraph();
                    if (index == 0) {
                        lineGraph.showGraphInPopup(stage, studentsGrades, root, index);
                    } else if (index == 1) {
                        lineGraph.showGraphInPopup(stage, graduatesGrades, root, index);
                    }
                });

        // create the attribute label
        attributesLabel.setLayoutX(Launcher.Distance(1920, 980, root.getWidth()));
        attributesLabel.setLayoutY(Launcher.Distance(1080, 193, root.getHeight()));
        attributesLabel.setFont(Font.font("Georgia", FontWeight.BOLD, 20));

        // output the attributes of the student if the index is 0
        if (index == 0) {
            String[][] data = ReaderCsv.getData()[3];
            for (String[] datum : data) {
                if (datum[0].equals(input)) {
                    DecimalFormat decimalFormat =
                            new DecimalFormat("#.###", new DecimalFormatSymbols(Locale.US));
                    double roundedValue = Double.parseDouble(datum[5]);
                    String roundedString = decimalFormat.format(roundedValue);
                    attributesLabel.setText(
                            "Suruna Value: "
                                    + datum[1]
                                    + ", Hurni Level: "
                                    + datum[2]
                                    + "\nLal Count: "
                                    + datum[3]
                                    + ", Volta: "
                                    + datum[4]
                                    + ", Ratio: "
                                    + roundedString);
                }
            }
        } else {
            attributesLabel.setText("");
        }
    }

    private static double getDifferences1(int index, double averages) {
        double sum = 0;
        if (index == 0) {
            for (double i :
                    predicate.isSelected()
                            ? PolygonGraph.averageGrades3
                            : PolygonGraph.averageGrades) {
                sum += i;
            }
            sum = sum / 27;
        } else if (index == 1) {
            for (double i : PolygonGraph.averageGrades2) {
                sum += i;
            }
            sum = sum / 30;
        }
        return ((averages - sum) / sum) * 100;
    }

    private void loadTable(
            String[] @NotNull [] graduatesArray, Comparator<String> numericComparator) {
        // load the graduatesArray into the table
        for (int i = 0; i < graduatesArray[0].length; i++) {
            TableColumn<String[], String> tc = new TableColumn<>(graduatesArray[0][i]);
            final int colNo = i;
            tc.setCellValueFactory(p -> new SimpleStringProperty((p.getValue()[colNo])));
            tc.prefWidthProperty().bind(table.widthProperty().divide(graduatesArray[0].length));
            table.getColumns().add(tc);

            if (i >= 1 && i <= 5) {
                tc.setComparator(numericComparator);
            }
        }

        // Create an ObservableList for the table data
        ObservableList<String[]> data = FXCollections.observableArrayList();

        // Add each row from graduatesArray to the ObservableList
        data.addAll(Arrays.asList(graduatesArray).subList(1, graduatesArray.length));

        // Set the items of the table
        table.setItems(data);
    }

    public void draw(int index) {
        // set the data depending on the index
        setData(index);

        // add the predicate button if the index is 0
        if (index == 0) {
            if (!root.getChildren().contains(predicate)) {
                root.getChildren().add(predicate);
            }
        }

        // change the info label
        WidgetSetup.info.setText("Student grades");

        // add the table, the polygon button, the line chart button and the attribute label, if they
        // don't already exist
        if (!root.getChildren().contains(table)) {
            root.getChildren().add(table);
        }
        if (!root.getChildren().contains(polygon)) {
            root.getChildren().add(polygon);
        }
        if (!root.getChildren().contains(lineChart)) {
            root.getChildren().add(lineChart);
        }
        if (!root.getChildren().contains(attributesLabel)) {
            root.getChildren().add(attributesLabel);
        }
    }

    public void clear() {
        // remove everything from the root
        root.getChildren().removeAll(table, polygon, lineChart, predicate, attributesLabel);

        // if the table is not null, remove its items
        if (table != null) {
            table.getItems().clear();
        }
    }
}
