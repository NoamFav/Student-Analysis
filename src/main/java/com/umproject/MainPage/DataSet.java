package com.umproject.MainPage;

import com.umproject.Graphs.HeatMap;
import com.umproject.Graphs.NGClusters;
import com.umproject.Graphs.PolygonGraph;
import com.umproject.Graphs.StackBarHistogram;
import com.umproject.Launcher;
import com.umproject.Utils.CheckBoxPopup;
import com.umproject.Utils.ReaderCsv;
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

import java.util.*;

public class DataSet {
    //create a list of attributes names
    private static final String[] SURUNA_LABELS = {"doot", "lobi", "nulp"};
    private static final String[] HURNI_LABELS = {"full", "high", "medium", "low", "nothing"};
    private static final String[] VOLTA_LABELS = {"1 star", "2 star", "3 star", "4 star", "5 star"};
    private static final String[] LAL_COUNT_LABELS = {"59", "60", "61", "62", "63", "64", "65", "66", "67", "68", "69", "70", "71", "72", "73", "74", "75", "76", "77", "78", "79", "80", "81", "82", "83", "84", "85", "86", "87", "88", "89", "90", "91", "92", "93", "94", "95", "96", "97", "98", "99", "100"}; // Example labels for Lal Count
    private static final TableView<String[]> table = new TableView<>();
    private static ObservableList<String[]> originalDataList;
    private static String[][] rawGrades;
    private final Pane root;
    private final Button switchButton;
    private final Button heatMap;
    private final Button stackBarHistogram;
    private final Button scatterChart;
    private final Button checkBoxButton;
    private final String[][][] data = ReaderCsv.getData();
    public static int index = 3;
    private final Stage stage;
    public static CheckBox predicate;
    public static ObservableList<String[]> dataList;
    public DataSet(Pane root, Stage stage) {
        //initialize the root, the stage, buttons, the predicate checkBox, and the table
        this.root = root;
        this.stage = stage;
        switchButton = new Button("Switch DataSet");
        heatMap = new Button();
        stackBarHistogram = new Button();
        scatterChart = new Button();
        checkBoxButton = new Button();
        predicate = new CheckBox("Allow predictions");

        heatMap.getStyleClass().add("Button");
        switchButton.getStyleClass().add("Button");
        stackBarHistogram.getStyleClass().add("Button");
        scatterChart.getStyleClass().add("Button");
        predicate.getStyleClass().add("CheckBox");
        checkBoxButton.getStyleClass().add("Button");

        switchButton.setLayoutX(Launcher.Distance(1920,1300, root.getWidth()));
        switchButton.setLayoutY(Launcher.Distance(1080,297, root.getHeight()));
        switchButton.setPrefWidth(Launcher.Distance(1920,107, root.getWidth()));
        switchButton.setPrefHeight(Launcher.Distance(1080,50, root.getHeight()));
        switchButton.setStyle("-fx-font-family: georgia;");

        predicate.setLayoutX(Launcher.Distance(1920,850, root.getWidth()));
        predicate.setLayoutY(Launcher.Distance(1080,308, root.getHeight()));
        predicate.setPrefWidth(Launcher.Distance(1920,150, root.getWidth()));
        predicate.setPrefHeight(Launcher.Distance(1080,20, root.getHeight()));

        //switch data set based on the index and the predicate checkBox state
        //index3 means current Grades and index1 means graduate grades
        switchButton.setOnAction(event -> {
            if (index == 3) {
                clear();
                draw(1);
                predicate.setSelected(false);
            } else if (index == 1) {
                clear();
                draw(3);
                predicate.setSelected(false);
            }
            CheckBoxPopup.setAll(true);
            index = index == 1 ? 3 : 1;
        });

        //change to index 4 when the predicate checkbox is selected and keep the current filtered data set
        //index 4 is current grades with predictions
        predicate.setOnAction(event -> {
            if (predicate.isSelected()) {
               clear();
               draw(4);
           } else {
               clear();
               draw(3);
           }

            int[] surunaStates = CheckBoxPopup.getCheckboxStates(CheckBoxPopup.surunaCheckboxes);
            int[] hurniStates = CheckBoxPopup.getCheckboxStates(CheckBoxPopup.hurniCheckboxes);
            int[] lalCountStates = CheckBoxPopup.getCheckboxStates(CheckBoxPopup.lalCountCheckboxes);
            int[] voltaStates = CheckBoxPopup.getCheckboxStates(CheckBoxPopup.voltaCheckboxes);

            filterTable(dataList, surunaStates, hurniStates, lalCountStates, voltaStates);
        });
    }

    public void setData(int index) {
        //initialize the table dataSets depending on the index value and the predicate checkBox state
        originalDataList = FXCollections.observableArrayList();
        dataList = FXCollections.observableArrayList();
        String[][] grades = data[index];
        dataList.addAll(Arrays.asList(grades));
        originalDataList.addAll(dataList);
        dataList.removeFirst();


        //calculate the raw grades 2D array, without headers, info, or ID
        int push = index == 3 || index == 4 ? 6 : 1;
        rawGrades = new String[grades.length-1][grades[0].length - push];
        for (int i = 1; i < grades.length; i++) {
            if (grades[0].length - push >= 0)
                System.arraycopy(grades[i], push, rawGrades[i - 1], 0, grades[0].length - push);
        }

        Comparator<String> numericComparator = Menu.createNumericComparator();

        //load the table with the selected dataSet
        table.getColumns().clear();
        for (int i = 0; i < grades[0].length; i++) {
            TableColumn<String[], String> tc = new TableColumn<>(grades[0][i]);
            final int colNo = i;
            tc.setCellValueFactory(p -> new SimpleStringProperty((p.getValue()[colNo])));
            tc.setPrefWidth(90);
            table.getColumns().add(tc);

            if (index == 3 || index == 4) {
                if (i == 3 || (i >= 5 && i <= 35 || i == 0)) {
                    tc.setComparator(numericComparator);
                }
            } else if (index == 1) {
                tc.setComparator(numericComparator);
            }
        }

        table.setItems(dataList);
        table.setLayoutX(Launcher.Distance(1920,549, root.getWidth()));
        table.setLayoutY(Launcher.Distance(1080,365.85, root.getHeight()));
        table.setPrefWidth(Launcher.Distance(1920,858, root.getWidth()));
        table.setPrefHeight(Launcher.Distance(1080,589.95, root.getHeight()));

        //calculate the average grades of the data set depending on the index, and the predicate checkBox state
        //also calculates the difference from the global average of the global average, of the other data set.
        double sum = 0;
        double differences1;
        if (index == 3) {
            for (double i : PolygonGraph.averageGrades) {
                sum += i;
            }
            sum = sum / 27;

            differences1 = (((sum) - 7.26) / 7.26) * 100;

            Widget.updateLabelsAndIconsDataset(sum, 30, grades.length - 1, differences1, index); //update the side panel
        } else if (index == 4) {
            for (double i : PolygonGraph.averageGrades3) {
                sum += i;
            }
            sum = sum / 27;

            differences1 = (((sum) - 7.26) / 7.26) * 100;

            Widget.updateLabelsAndIconsDataset(sum, 30, grades.length - 1, differences1, index); //update the side panel
        } else if (index == 1) {
            for (double i : PolygonGraph.averageGrades2) {
                sum += i;
            }
            sum = sum / 30;

            differences1 = (((sum) - 7.23) / 7.23) * 100;

            Widget.updateLabelsAndIconsDataset(sum, 30, grades.length - 1, differences1, index); //update the side panel
        }

        //create heatMap button
        Image heatMapIcon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/umproject/images/HeatMap.png")));
        ImageView heatMapView = new ImageView(heatMapIcon);
        heatMapView.setFitWidth(Launcher.Distance(1920,40, root.getWidth()));
        heatMapView.setFitHeight(Launcher.Distance(1080,40, root.getHeight()));
        heatMap.setGraphic(heatMapView);

        heatMap.setLayoutX(Launcher.Distance(1920,1230, root.getWidth()));
        heatMap.setLayoutY(Launcher.Distance(1080,297, root.getHeight()));

        //create heatMap graph on click
        heatMap.setOnAction(event -> {
            HeatMap heatMap = new HeatMap();
            heatMap.ShowGraphInPopup(stage, root, rawGrades);
        });

        //create stackBarHistogram button
        Image stackBarHistogramIcon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/umproject/images/stackBarHistogramIcon.png")));
        ImageView stackBarHistogramView = new ImageView(stackBarHistogramIcon);
        stackBarHistogramView.setFitWidth(Launcher.Distance(1920,40, root.getWidth()));
        stackBarHistogramView.setFitHeight(Launcher.Distance(1080,40, root.getHeight()));
        stackBarHistogram.setGraphic(stackBarHistogramView);

        stackBarHistogram.setLayoutX(Launcher.Distance(1920,1160, root.getWidth()));
        stackBarHistogram.setLayoutY(Launcher.Distance(1080,297, root.getHeight()));

        //create stackBarHistogram graph on click
        stackBarHistogram.setOnAction(event -> {
            StackBarHistogram stackBarHistogram = new StackBarHistogram();
            stackBarHistogram.ShowGraphInPopup(stage, rawGrades, root);
        });

        //create scatterChart button
        Image scatterImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/umproject/images/ScatterIcon.png")));
        ImageView scatterImageView = new ImageView(scatterImage);
        scatterImageView.setFitWidth(Launcher.Distance(1920,40, root.getWidth()));
        scatterImageView.setFitHeight(Launcher.Distance(1080,40, root.getHeight()));
        scatterChart.setGraphic(scatterImageView);

        scatterChart.setLayoutX(Launcher.Distance(1920,1090, root.getWidth()));
        scatterChart.setLayoutY(Launcher.Distance(1080,297, root.getHeight()));

        //create scatterChart graph on click
        scatterChart.setOnAction(event -> {
            NGClusters scatterChart = new NGClusters();
            scatterChart.ShowGraphInPopup(stage, rawGrades, root);
        });

        //create attribute panel button
        Image checkBoxImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/umproject/images/CheckBoxIcon.png")));
        ImageView checkBoxImageView = new ImageView(checkBoxImage);
        checkBoxImageView.setFitWidth(Launcher.Distance(1920,40, root.getWidth()));
        checkBoxImageView.setFitHeight(Launcher.Distance(1080,40, root.getHeight()));
        checkBoxButton.setGraphic(checkBoxImageView);

        checkBoxButton.setLayoutX(Launcher.Distance(1920,1020, root.getWidth()));
        checkBoxButton.setLayoutY(Launcher.Distance(1080,297, root.getHeight()));

        //open popup panel of the attributes on click
        checkBoxButton.setOnAction(event -> {
            CheckBoxPopup checkBoxButton = new CheckBoxPopup();
            checkBoxButton.createPopUP(stage, root);
        });
    }
    public static void filterTable(ObservableList<String[]> data, int[] one, int[] two, int[] three, int[] four){

        //filter the table loaded based on the states of the checkboxes.
        //if the checkBox state array is 0, the row is removed from the table.
        //reload the datalist from the original data list at the start to re-add the row when checking again a checkbox

        dataList.clear();
        for (int i = 1; i < originalDataList.size(); i++) {
            dataList.add(originalDataList.get(i));
        }

        Map<int[], LabelColumnPair> checkBoxMappings = new HashMap<>();
        checkBoxMappings.put(one, new LabelColumnPair(SURUNA_LABELS, 1));
        checkBoxMappings.put(two, new LabelColumnPair(HURNI_LABELS, 2));
        checkBoxMappings.put(three, new LabelColumnPair(LAL_COUNT_LABELS, 3));
        checkBoxMappings.put(four, new LabelColumnPair(VOLTA_LABELS, 4));

        checkBoxMappings.forEach((checkboxes, labelColumnPair) -> {
            for (int i = 0; i < checkboxes.length; i++) {
                if (checkboxes[i] == 0) {
                    String label = labelColumnPair.labels[i];
                    int columnIndex = labelColumnPair.columnIndex;
                    data.removeIf(row -> row.length > columnIndex && row[columnIndex].equals(label));
                }
            }
        });
        dataList = data;

        int push = index == 3 || index == 4 ? 6 : 1;

        rawGrades = new String[dataList.size()][];
        for (int i = 0; i < dataList.size(); i++) {
            String[] filteredRow = dataList.get(i);
            String[] newRow = new String[filteredRow.length - push];
            if (filteredRow.length - push >= 0) {
                System.arraycopy(filteredRow, push, newRow, 0, filteredRow.length - push);
            }
            rawGrades[i] = newRow;
        }

        table.refresh();
    }
    private static class LabelColumnPair {
        //link a column to an index
        String[] labels;
        int columnIndex;
        LabelColumnPair(String[] labels, int columnIndex) {
            this.labels = labels;
            this.columnIndex = columnIndex;
        }
    }
    public void draw(int index) {
        //set the data based on the index
        setData(index);

        //add the predicate checkBoxButton to the root if the index is 3 or 4
        //and add the scatterChart to the root if the index is 3,
        //change the name of the info label based on the index
        if(index == 3 || index == 4) {
            Widget.info.setText("Current Grades");
            if (!root.getChildren().contains(predicate)) {
                root.getChildren().add(predicate);
            }
            if (!root.getChildren().contains(checkBoxButton)) {
                root.getChildren().add(checkBoxButton);
            }
            if (index == 3) {
                if (!root.getChildren().contains(scatterChart)) {
                    root.getChildren().add(scatterChart);
                }
            }
        } else if (index == 1) {
            Widget.info.setText("Graduate Grades");
        }

        //add the table, the switch button, the heatMap and the stackBarHistogram to the root if it doesn't already exist
        if (!root.getChildren().contains(table)) {
            root.getChildren().add(table);
        }
        if (!root.getChildren().contains(switchButton)) {
            root.getChildren().add(switchButton);
        }
        if (!root.getChildren().contains(heatMap)) {
            root.getChildren().add(heatMap);
        }
        if (!root.getChildren().contains(stackBarHistogram)) {
            root.getChildren().add(stackBarHistogram);
        }

    }
    public void clear() {
        //remove everything from the root
        root.getChildren().removeAll(table, switchButton, heatMap, stackBarHistogram, predicate, scatterChart, checkBoxButton);
    }
}
