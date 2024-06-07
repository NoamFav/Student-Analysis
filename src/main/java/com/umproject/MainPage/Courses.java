package com.umproject.MainPage;

import com.umproject.AI.RegressionTreePoissonDeviance;
import com.umproject.Cards.CourseCard;
import com.umproject.Cards.graduateCourseCard;
import com.umproject.Graphs.BoxPlot;
import com.umproject.Graphs.HistogramGraph;
import com.umproject.Graphs.PieChartGraph;
import com.umproject.Graphs.PolygonGraph;
import com.umproject.Launcher;
import com.umproject.Utils.SearchBar;
import com.umproject.Utils.Widget;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;

public class Courses {
    private final TableView<String[]> table;
    private final Pane root;
    private final Stage stage;
    private final String input;
    private final Button histogramButton;
    private final Button pieChartButton;
    private final Button switchButton;
    private final Button treeButton;
    private final Button boxPLotsButton;
    public static CheckBox predicate;
    public static int CourseIndex = 0;
    public static String[] coursesCurrentGrades = new String[1128];
    public static String[] coursesGraduateGrades = new String[18321];

    public Courses(Pane root, String input, Stage stage) {
        //add the root, input, and stage to the class
        this.root = root;
        this.input = input;
        this.stage = stage;

        //instantiate the buttons and check box
        predicate = new CheckBox("Allow predictions");
        histogramButton = new Button();
        pieChartButton = new Button();
        boxPLotsButton = new Button();
        switchButton = new Button("Switch DataSet");
        treeButton =  new Button();

        //add css to the buttons and check box
        histogramButton.getStyleClass().add("Button");
        pieChartButton.getStyleClass().add("Button");
        switchButton.getStyleClass().add("Button");
        boxPLotsButton.getStyleClass().add("Button");
        predicate.getStyleClass().add("CheckBox");
        treeButton.getStyleClass().add("Button");

        //set the position of the switch button and check box
        predicate.setLayoutX(Launcher.Distance(1920,828, root.getWidth()));
        predicate.setLayoutY(Launcher.Distance(1080,308, root.getHeight()));
        predicate.setPrefWidth(Launcher.Distance(1920,150, root.getWidth()));
        predicate.setPrefHeight(Launcher.Distance(1080,20, root.getHeight()));

        switchButton.setLayoutX(Launcher.Distance(1920,1300, root.getWidth()));
        switchButton.setLayoutY(Launcher.Distance(1080,297, root.getHeight()));
        switchButton.setPrefWidth(Launcher.Distance(1920,107, root.getWidth()));
        switchButton.setPrefHeight(Launcher.Distance(1080,50, root.getHeight()));
        switchButton.setStyle("-fx-font-family: georgia;");



        //create the table
        table = new TableView<>();
        table.setLayoutX(Launcher.Distance(1920,549, root.getWidth()));
        table.setLayoutY(Launcher.Distance(1080,365.85, root.getHeight()));
        table.setPrefWidth(Launcher.Distance(1920,858, root.getWidth()));
        table.setPrefHeight(Launcher.Distance(1080,589.95, root.getHeight()));

        //switch between the two data sets on action: Current(index 0) and Graduate(index 1) and change the index accordingly
        switchButton.setOnAction(event -> {
            if (CourseIndex == 0) {
                clear();
                draw(1);
                predicate.setSelected(false);
            } else if (CourseIndex == 1) {
                clear();
                draw(0);
                predicate.setSelected(false);
            }
            CourseIndex = CourseIndex == 0 ? 1 : 0;
        });
    }
    private void setData(int index) {
        //set the method to load the data

        //change the dataset from current grade to current predicted grade on click
        predicate.setOnAction(event -> {
            clear();
            draw(0);
        });

        CourseCard card = new CourseCard();

        //load current grades with or without predictions based on the checkbox state
        boolean checked = predicate.isSelected();
        String[][] infoArray = card.coursesArray(input, checked);

        //create the array for both data sets
        graduateCourseCard graduateCard = new graduateCourseCard();
        String[][] graduateInfoArray = graduateCard.getCourseInfo(input);

        //fill the grade array based on the index
        if (index == 0) {
            //load the current grades
            for (int i = 1; i < coursesCurrentGrades.length +1; i++) {
                coursesCurrentGrades[i-1] = infoArray[i][1];
            }
        } else if (index == 1) {
            //load the graduate grades
            for (int i = 1; i < coursesGraduateGrades.length +1; i++) {
                coursesGraduateGrades[i-1] = graduateInfoArray[i][1];
            }
        }

        //remove the old data from the table
        table.getItems().clear();
        table.getColumns().clear();

        //Load the data into the table based on the index
        ObservableList<String[]> dataList = FXCollections.observableArrayList();
        if (index == 0) {
            LoadFile(infoArray, dataList);
        } else if (index == 1) {
            LoadFile(graduateInfoArray, dataList);
        }
        table.setItems(dataList);

        //calculate the average grade of the course based on the index
        double averages = 0;
        int validNumbersCount = 0;
        if (index == 0) {
            for (String[] strings : infoArray) {
                String stringValue = strings[1];
                if (isNumeric(stringValue) &&!stringValue.equals("NG")) {
                    averages += Double.parseDouble(stringValue);
                    validNumbersCount++;
                }
            }} else if (index == 1) {
            for (String[] strings : graduateInfoArray) {
                String stringValue = strings[1];
                if (isNumeric(stringValue) &&!stringValue.equals("NG")) {
                    averages += Double.parseDouble(stringValue);
                    validNumbersCount++;
                }
            }
        }
        averages = averages/validNumbersCount;

        //calculate the difference between the average of the course and the average of all courses, based on the index and the checkbox state
        double sum = 0;
        if (index == 0) {
            double[] averageGrades = predicate.isSelected() ? PolygonGraph.averageGrades3 : PolygonGraph.averageGrades;
            for (double i : averageGrades) {
                sum += i;
            }
            sum /= 27; //divide by 27 to avoid the 3 empty courses to mess with the calculation
        } else if (index == 1) {
            double[] averageGrades = PolygonGraph.averageGrades2;
            for (double i : averageGrades) {
                sum += i;
            }
            sum /= 30; //all 30 courses have grades
        }

        double difference1 = ((averages - sum)/sum)*100; //use the method above
        double difference2 = (((1128 - validNumbersCount) - 598.1)/598.1)*100; //calculate the difference between the number of NG on the course and the average number of NG on all courses
        double difference3 = (((validNumbersCount) - 529.9)/529.9)*100; //calculate the difference between the number of grades on the course and the average number of grades on all courses

        //update the side Panel with the values and differences
        Widget.updateLabelsAndIconsCourse(averages, 1128 - validNumbersCount, validNumbersCount, difference1, difference2, difference3,index);

        //create the Histogram button
        Image histogramImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/umproject/images/histogram.png")));
        ImageView histogramView = new ImageView(histogramImage);
        histogramView.setFitWidth(Launcher.Distance(1920,40, root.getWidth()));
        histogramView.setFitHeight(Launcher.Distance(1080,40, root.getHeight()));
        histogramButton.setGraphic(histogramView);

        histogramButton.setLayoutX(Launcher.Distance(1920,1230, root.getWidth()));
        histogramButton.setLayoutY(Launcher.Distance(1080,297, root.getHeight()));

        //create the histogram graph on action based on the index
        histogramButton.setOnAction(event -> {
            HistogramGraph histogram = new HistogramGraph();
            if (index == 0) {
                histogram.showGraphInPopup(stage, coursesCurrentGrades, root);
            } else if (index == 1) {
                histogram.showGraphInPopup(stage, coursesGraduateGrades, root);
            }
        });

        Image treeImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/umproject/images/TreeIcon.png")));
        ImageView treeView = new ImageView(treeImage);
        treeView.setFitWidth(Launcher.Distance(1920,40, root.getWidth()));
        treeView.setFitHeight(Launcher.Distance(1080,40, root.getHeight()));
        treeButton.setGraphic(treeView);

        treeButton.setLayoutX(Launcher.Distance(1920,1020, root.getWidth()));
        treeButton.setLayoutY(Launcher.Distance(1080,297, root.getHeight()));

        //create the histogram graph on action based on the index
        treeButton.setOnAction(event -> {
            try {
                RegressionTreePoissonDeviance tree = new RegressionTreePoissonDeviance();

                if (index == 0 || index == 1) {
                    tree.showTreeInPopup(stage, SearchBar.outputCourse, tree, root);
                }
            } catch (Exception ignored) {
            }
        });


        //create the pie chart button
        Image pieChartImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/umproject/images/PieChartIcon.png")));
        ImageView pieChartView = new ImageView(pieChartImage);
        pieChartView.setFitWidth(Launcher.Distance(1920,40, root.getWidth()));
        pieChartView.setFitHeight(Launcher.Distance(1080,40, root.getHeight()));
        pieChartButton.setGraphic(pieChartView);

        pieChartButton.setLayoutX(Launcher.Distance(1920,1160, root.getWidth()));
        pieChartButton.setLayoutY(Launcher.Distance(1080,297, root.getHeight()));

        //create the pie chart graph on action based on the index
        pieChartButton.setOnAction(event -> {
            PieChartGraph pieChart = new PieChartGraph();
            if (index == 0) {
                pieChart.showGraphInPopup(stage, coursesCurrentGrades, root);
            } else if (index == 1) {
                pieChart.showGraphInPopup(stage, coursesGraduateGrades, root);
            }
        });

        //create the box plot button
        Image boxPLotsIcon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/umproject/images/BoxPlotIcon.png")));
        ImageView boxPLotsView = new ImageView(boxPLotsIcon);
        boxPLotsView.setFitWidth(Launcher.Distance(1920,40, root.getWidth()));
        boxPLotsView.setFitHeight(Launcher.Distance(1080,40, root.getHeight()));
        boxPLotsButton.setGraphic(boxPLotsView);

        boxPLotsButton.setLayoutX(Launcher.Distance(1920,1090, root.getWidth()));
        boxPLotsButton.setLayoutY(Launcher.Distance(1080,297, root.getHeight()));

        //create the box plot graph on action based on the index
        boxPLotsButton.setOnAction(event -> {
            BoxPlot boxPlot = new BoxPlot();
            if (index == 0) {
                boxPlot.showGraphInPopup(stage, coursesCurrentGrades, root, 0, input);
            } else if (index == 1) {
                boxPlot.showGraphInPopup(stage, coursesGraduateGrades, root, 0, input);
            }
        });
    }
    private void LoadFile(String[][] graduateInfoArray, ObservableList<String[]> dataList) {
        //load the array into an ObservableList for the table
        dataList.addAll(Arrays.asList(Objects.requireNonNull(graduateInfoArray)));
        dataList.remove(0);

        Comparator<String> numericComparator = Menu.createNumericComparator();
        for (int i = 0; i < graduateInfoArray[0].length; i++) {
            TableColumn<String[], String> tc = new TableColumn<>(graduateInfoArray[0][i]);
            final int colNo = i;
            tc.setCellValueFactory(p -> new SimpleStringProperty((p.getValue()[colNo])));
            tc.prefWidthProperty().bind(table.widthProperty().divide(graduateInfoArray[0].length));
            table.getColumns().add(tc);
            tc.setComparator(numericComparator); //add custom comparator to handle the String input with numeric values
        }
    }
    private boolean isNumeric(String str) {
        //check if the string is a number
        if (str == null) {
            return false;
        }
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e) {
            return false;
        }
    }
    public void draw(int index) {

        //add the predicate button to the root if it doesn't already exist and the index is 0
        setData(index);
        if (index == 0) {
            if (!root.getChildren().contains(predicate)) {
                root.getChildren().add(predicate);
            }
            if (!predicate.isSelected()){
                if (!root.getChildren().contains(treeButton)) {
                    root.getChildren().add(treeButton);
                }
            }
        }
        //Change the text of the info label to the title
        Widget.info.setText("Course grades");

        //add the buttons and the table to the root if they don't already exist
        if (!root.getChildren().contains(table)) {
            root.getChildren().add(table);
        }
        if (!root.getChildren().contains(histogramButton)) {
            root.getChildren().add(histogramButton);
        }
        if (!root.getChildren().contains(switchButton)) {
            root.getChildren().add(switchButton);
        }
        if (!root.getChildren().contains(pieChartButton)) {
            root.getChildren().add(pieChartButton);
        }
        if (!root.getChildren().contains(boxPLotsButton)) {
            root.getChildren().add(boxPLotsButton);
        }

    }
    public void clear() {
        //remove all the buttons from the root and if the table is not null, remove its items.
        root.getChildren().removeAll(table, histogramButton,switchButton,pieChartButton, predicate, boxPLotsButton, treeButton);
        if (table != null) {
            table.getItems().clear();
        }
    }
}
