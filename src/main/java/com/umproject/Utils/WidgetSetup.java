package com.umproject.Utils;

import com.umproject.Launcher;
import com.umproject.MainPage.Courses;
import com.umproject.MainPage.Student;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import java.util.Objects;

import static javafx.scene.paint.Color.BLACK;
import static javafx.scene.paint.Color.WHITE;

public class WidgetSetup {

    public static final Rectangle sidePanel1 = new Rectangle();
    public static final Rectangle sidePanel2 = new Rectangle();
    public static final Rectangle sidePanel3 = new Rectangle();
    public static Label page = new Label("Data Set");
    public static final Rectangle mainPanel = new Rectangle();
    public static Label info = new Label("Page");

    public static Label green;
    public static Label greenLabel;
    public static Label orange;
    public static Label orangeLabel;
    public static Label black;
    public static Label blackLabel;
    public static Label greenInfoLabel;
    public static Label orangeInfoLabel;
    public static Label blackInfoLabel;

    public static Image down;
    public static Image up;
    public static Image constant;

    public static ImageView upIconViewForGreen;
    public static ImageView upIconViewForOrange;
    public static ImageView upIconViewForBlack;
    public static ImageView downIconViewForGreen;
    public static ImageView downIconViewForOrange;
    public static ImageView downIconViewForBlack;
    public static ImageView constantViewForOrange;
    public static ImageView constantViewForBlack;

    public WidgetSetup(Pane root) {

        //load the three icons for comparison
        down = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/umproject/images/DOWN.png")));
        up = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/umproject/images/UP.png")));
        constant = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/umproject/images/ConstantIcon.png")));

        //create the constant icon for orange and black
        constantViewForOrange = new ImageView(constant);
        constantViewForBlack = new ImageView(constant);

        constantViewForOrange.setFitWidth(Launcher.Distance(1920, 40, root.getWidth()));
        constantViewForOrange.setFitHeight(Launcher.Distance(1080, 40, root.getHeight()));
        constantViewForBlack.setFitWidth(Launcher.Distance(1920, 40, root.getWidth()));
        constantViewForBlack.setFitHeight(Launcher.Distance(1080, 40, root.getHeight()));

        //create the up icon for green, orange and black
        upIconViewForGreen = new ImageView(up);
        upIconViewForOrange = new ImageView(up);
        upIconViewForBlack = new ImageView(up);

        upIconViewForGreen.setFitWidth(Launcher.Distance(1920, 40, root.getWidth()));
        upIconViewForGreen.setFitHeight(Launcher.Distance(1080, 40, root.getHeight()));
        upIconViewForOrange.setFitWidth(Launcher.Distance(1920, 40, root.getWidth()));
        upIconViewForOrange.setFitHeight(Launcher.Distance(1080, 40, root.getHeight()));
        upIconViewForBlack.setFitWidth(Launcher.Distance(1920, 40, root.getWidth()));
        upIconViewForBlack.setFitHeight(Launcher.Distance(1080, 40, root.getHeight()));

        //create the down icon for green, orange and black
        downIconViewForGreen = new ImageView(down);
        downIconViewForOrange = new ImageView(down);
        downIconViewForBlack = new ImageView(down);

        downIconViewForGreen.setFitWidth(Launcher.Distance(1920, 40, root.getWidth()));
        downIconViewForGreen.setFitHeight(Launcher.Distance(1080, 40, root.getHeight()));
        downIconViewForOrange.setFitWidth(Launcher.Distance(1920, 40, root.getWidth()));
        downIconViewForOrange.setFitHeight(Launcher.Distance(1080, 40, root.getHeight()));
        downIconViewForBlack.setFitWidth(Launcher.Distance(1920, 40, root.getWidth()));
        downIconViewForBlack.setFitHeight(Launcher.Distance(1080, 40, root.getHeight()));

        //position the info label
        info.setLayoutX(Launcher.Distance(1920, 564, root.getWidth()));
        info.setLayoutY(Launcher.Distance(1080, 297, root.getHeight()));
        info.setFont(Font.font("Georgia", Launcher.Distance(1920,34,root.getWidth())));
        info.getStyleClass().add("info");

        //position the page label
        page.getStyleClass().add("Page");
        page.setFont(Font.font("Georgia", Launcher.Distance(1920,50,root.getWidth())));
        page.setLayoutX(Launcher.Distance(1920,523.5,root.getWidth()));
        page.setLayoutY(Launcher.Distance(1080,183.6,root.getHeight()));

        //set the size for the side widgets
        double rectangleWidth = Launcher.Distance(1920,356,root.getWidth());
        double rectangleHeight = Launcher.Distance(1080, 230, root.getHeight());
        double xPosition =  Launcher.Distance(1920, 1443, root.getWidth());
        double cornerRadius = 45;

        //set the size for the main widget
        mainPanel.setArcWidth(cornerRadius);
        mainPanel.setArcHeight(cornerRadius);
        mainPanel.setStroke(BLACK);
        mainPanel.setStrokeWidth(1);

        mainPanel.setHeight(Launcher.Distance(1080, 710.1, root.getHeight()));
        mainPanel.setWidth(Launcher.Distance(1920,904.5,root.getWidth()));

        mainPanel.setX(Launcher.Distance(1920,526.5,root.getWidth()));
        mainPanel.setY(Launcher.Distance(1080,261.9,root.getHeight()));

        //set the size and position for the side widgets
        sidePanel1.setHeight(rectangleHeight); sidePanel2.setHeight(rectangleHeight); sidePanel3.setHeight(rectangleHeight);
        sidePanel1.setWidth(rectangleWidth); sidePanel2.setWidth(rectangleWidth); sidePanel3.setWidth(rectangleWidth);

        sidePanel1.setArcWidth(cornerRadius);
        sidePanel1.setArcHeight(cornerRadius);
        sidePanel1.setStroke(BLACK);
        sidePanel1.setStrokeWidth(1);

        sidePanel2.setArcWidth(cornerRadius);
        sidePanel2.setArcHeight(cornerRadius);
        sidePanel2.setStroke(BLACK);
        sidePanel2.setStrokeWidth(1);

        sidePanel3.setArcWidth(cornerRadius);
        sidePanel3.setArcHeight(cornerRadius);
        sidePanel3.setStroke(BLACK);
        sidePanel3.setStrokeWidth(1);

        sidePanel1.setX(xPosition);
        sidePanel1.setY(Launcher.Distance(1080,261.9,root.getHeight()));

        sidePanel2.setX(xPosition);
        sidePanel2.setY(Launcher.Distance(1080,502.2,root.getHeight()));

        sidePanel3.setX(xPosition);
        sidePanel3.setY(Launcher.Distance(1080,742.5,root.getHeight()));

        //add CSS to the widgets
        mainPanel.getStyleClass().add("MainPanel");
        sidePanel1.getStyleClass().add("SidePanel1");
        sidePanel2.getStyleClass().add("SidePanel2");
        sidePanel3.getStyleClass().add("SidePanel3");

        //create the labels for each side widget
        green = new Label("00.00");
        orange = new Label("00.00");
        black = new Label("00.00");

        greenLabel = new Label("XX");
        orangeLabel = new Label("XX");
        blackLabel = new Label("XX");

        greenInfoLabel = new Label("XX");
        orangeInfoLabel = new Label("XX");
        blackInfoLabel = new Label("XX");

        //set the colors for the labels
        green.setTextFill(WHITE);
        orange.setTextFill(WHITE);
        black.setTextFill(WHITE);

        greenLabel.setTextFill(WHITE);
        orangeLabel.setTextFill(WHITE);
        blackLabel.setTextFill(WHITE);

        greenInfoLabel.setTextFill(WHITE);
        orangeInfoLabel.setTextFill(WHITE);
        blackInfoLabel.setTextFill(WHITE);

        //set the fonts for the labels
        green.setFont(Font.font("Georgia", Launcher.Distance(1920, 80, root.getWidth())));
        orange.setFont(Font.font("Georgia", Launcher.Distance(1920, 80, root.getWidth())));
        black.setFont(Font.font("Georgia", Launcher.Distance(1920, 80, root.getWidth())));

        greenLabel.setFont(Font.font("Georgia", Launcher.Distance(1920, 35, root.getWidth())));
        orangeLabel.setFont(Font.font("Georgia", Launcher.Distance(1920, 35, root.getWidth())));
        blackLabel.setFont(Font.font("Georgia", Launcher.Distance(1920, 35, root.getWidth())));

        greenInfoLabel.setFont(Font.font("Georgia", Launcher.Distance(1920, 20, root.getWidth())));
        orangeInfoLabel.setFont(Font.font("Georgia", Launcher.Distance(1920, 20, root.getWidth())));
        blackInfoLabel.setFont(Font.font("Georgia", Launcher.Distance(1920, 20, root.getWidth())));

        //create the stack panes for each side widget
        StackPane stackPane1 = new StackPane();
        VBox vbox1 = new VBox(greenLabel, green, greenInfoLabel);
        vbox1.setAlignment(Pos.CENTER);
        stackPane1.getChildren().add(vbox1);
        stackPane1.setPrefSize(sidePanel1.getWidth(), sidePanel1.getHeight());
        stackPane1.setLayoutX(sidePanel1.getX());
        stackPane1.setLayoutY(sidePanel1.getY());

        StackPane stackPane2 = new StackPane();
        VBox vbox2 = new VBox(orangeLabel, orange, orangeInfoLabel);
        vbox2.setAlignment(Pos.CENTER);
        stackPane2.getChildren().add(vbox2);
        stackPane2.setPrefSize(sidePanel2.getWidth(), sidePanel2.getHeight());
        stackPane2.setLayoutX(sidePanel2.getX());
        stackPane2.setLayoutY(sidePanel2.getY());

        StackPane stackPane3 = new StackPane();
        VBox vbox3 = new VBox(blackLabel, black, blackInfoLabel);
        vbox3.setAlignment(Pos.CENTER);
        stackPane3.getChildren().add(vbox3);
        stackPane3.setPrefSize(sidePanel3.getWidth(), sidePanel3.getHeight());
        stackPane3.setLayoutX(sidePanel3.getX());
        stackPane3.setLayoutY(sidePanel3.getY());

        //add everything to the root
        root.getChildren().addAll(page, mainPanel, sidePanel1, sidePanel2, sidePanel3, stackPane1, stackPane2, stackPane3, info);
        root.getChildren().addAll(upIconViewForGreen, downIconViewForGreen, upIconViewForOrange, downIconViewForOrange, upIconViewForBlack, downIconViewForBlack);
    }
    public static void updateLabelsAndIconsStudent(double greenValue, double orangeValue, double blackValue, double difference1, double difference2, double difference3, int index) {

        //change the label values for each side widget for a student, based on the index and the predicate checkbox state
        //the infoLabel icons change up, down or constant based on the value of the corresponding value
        green.setText(String.format("%.2f", greenValue));
        difference1(difference1, greenInfoLabel, upIconViewForGreen, downIconViewForGreen);
        if (index == 0) {
            if (Student.predicate.isSelected()) {
                orangeInfoLabel.setGraphic(constantViewForOrange);
                blackInfoLabel.setGraphic(constantViewForBlack);

                orangeInfoLabel.setText("Constant from others");
                blackInfoLabel.setText("Constant from others");
            } else {
                difference1(difference2, orangeInfoLabel, upIconViewForOrange, downIconViewForOrange);
                difference1(difference3, blackInfoLabel, upIconViewForBlack, downIconViewForBlack);
            }
            greenLabel.setText("Student Average");
            orangeLabel.setText("Number of NG");
            blackLabel.setText("Number of Grades");
            orange.setText(String.valueOf(orangeValue));
            black.setText(String.valueOf(blackValue));
        } else if (index == 1) {
            greenLabel.setText("Student Average");
            orangeLabel.setText("Is Cum Laude?");
            blackLabel.setText("Number of grades");
            orange.setText(((greenValue) >= 8.0) ? "Yes" : "No");
            if (greenValue >= 8.0) {
                orangeInfoLabel.setText("");
                orangeInfoLabel.setGraphic(upIconViewForOrange);
            } else {
                orangeInfoLabel.setText("");
                orangeInfoLabel.setGraphic(downIconViewForOrange);
            }
            black.setText("30");
            blackLabel.setText("Number of courses");
            blackInfoLabel.setText("Constant from others");
            blackInfoLabel.setGraphic(constantViewForBlack);
        }


    }
    public static void updateLabelsAndIconsDataset(double greenValue, double orangeValue, double blackValue, double difference1,int index) {

        //change the label values for each side widget for a dataset, based on the index and the predicate checkbox state
        //the infoLabel icons change up, down, or constant based on the value of the corresponding value
        green.setText(String.format("%.2f", greenValue));
        orange.setText(String.valueOf(orangeValue));
        black.setText(String.valueOf(blackValue));

        greenLabel.setText("Global Average");
        orangeLabel.setText("Number of courses");
        blackLabel.setText("Number of students");

        blackInfoLabel.setGraphic(constantViewForBlack);
        orangeInfoLabel.setGraphic(constantViewForOrange);


        if (index == 3 || index == 4) {
            difference2(difference1, greenInfoLabel, upIconViewForGreen, downIconViewForGreen);
            blackInfoLabel.setText("Constant from Graduates");
            orangeInfoLabel.setText("Constant from Graduates");
        } else if (index == 1) {
            difference3(difference1, greenInfoLabel, upIconViewForGreen, downIconViewForGreen);
            blackInfoLabel.setText("Constant from Current");
            orangeInfoLabel.setText("Constant from Current");
        }
    }
    public static void updateLabelsAndIconsCourse(double greenValue, double orangeValue, double blackValue, double difference1, double difference2, double difference3, int index) {

        //change the label values for each side widget for a course, based on the index and the predicate checkbox state
        //the infoLabel icons change up down, or constant based on the value of the corresponding value
        green.setText(String.format("%.2f", greenValue));
        difference1(difference1, greenInfoLabel, upIconViewForGreen, downIconViewForGreen);
        greenLabel.setText("Course Average");

        if (index == 0) {
            orange.setText(String.valueOf(orangeValue));
            black.setText(String.valueOf(blackValue));
            orangeLabel.setText("Number of NG");
            blackLabel.setText("Number of grades");
            if (Courses.predicate.isSelected()) {
                if (blackValue == 0) {
                    blackInfoLabel.setGraphic(null);
                    blackInfoLabel.setText("");
                    blackInfoLabel.setGraphic(null);
                    blackInfoLabel.setText("");
                } else {
                    orangeInfoLabel.setGraphic(constantViewForOrange);
                    blackInfoLabel.setGraphic(constantViewForBlack);

                    orangeInfoLabel.setText("Constant from others");
                    blackInfoLabel.setText("Constant from others");
                }
            } else if (blackValue == 0) {
                orangeLabel.setText("Number of NG");
                orangeInfoLabel.setGraphic(null);
                blackInfoLabel.setGraphic(null);
                orangeInfoLabel.setText("");
                blackInfoLabel.setText("");
            } else {
                difference1(difference2, orangeInfoLabel, upIconViewForOrange, downIconViewForOrange);
                difference1(difference3, blackInfoLabel, upIconViewForBlack, downIconViewForBlack);
            }
        } else if (index == 1) {
            orangeLabel.setText("Number of students");
            orange.setText("18321");
            orangeInfoLabel.setText("Constant from others");
            orangeInfoLabel.setGraphic(constantViewForOrange);

            black.setText("");
            blackLabel.setText("");
            blackInfoLabel.setText("");
            blackInfoLabel.setGraphic(null);
        }
    }
    public static void updateLabelsAndIconsGraph() {
        //set text to the side Widget and delete the icon on the info label
        green.setText("1");
        orange.setText("2");
        black.setText("3");

        greenLabel.setText("Graph");
        orangeLabel.setText("Graph");
        blackLabel.setText("Graph");

        greenInfoLabel.setText("Line Chart of two students");
        orangeInfoLabel.setText("Radar Chart of two students");
        blackInfoLabel.setText("Bar Chart of two students");
        greenInfoLabel.setGraphic(null);
        blackInfoLabel.setGraphic(null);
        orangeInfoLabel.setGraphic(null);
    }
    public static void updateLabelsAndIconsDatasetGraph() {
        //set text to the side Widget and delete the icon on the help page
        green.setText("");
        orange.setText("");
        black.setText("");

        greenLabel.setText("");
        orangeLabel.setText("");
        blackLabel.setText("");

        greenInfoLabel.setText("");
        orangeInfoLabel.setText("");
        blackInfoLabel.setText("");
        greenInfoLabel.setGraphic(null);
        blackInfoLabel.setGraphic(null);
        orangeInfoLabel.setGraphic(null);
    }
    private static void difference1(double difference, Label InfoLabel, ImageView upIconView, ImageView downIconView) {
        //set the icon up or down based on the value of the difference (positive or negative)
        if (difference >= 0) {
            InfoLabel.setText("up " + String.format("%.2f", Math.abs(difference)) + "% from others");
            InfoLabel.setGraphic(upIconView);
        } else {
            InfoLabel.setText("down " + String.format("%.2f", Math.abs(difference)) + "% from others");
            InfoLabel.setGraphic(downIconView);
        }
    }
    private static void difference2(double difference, Label InfoLabel, ImageView upIconView, ImageView downIconView) {
        //set the icon up or down based on the value of the difference (positive or negative)
        if (difference >= 0) {
            InfoLabel.setText("up " + String.format("%.2f", Math.abs(difference)) + "% from graduates");
            InfoLabel.setGraphic(upIconView);
        } else {
            InfoLabel.setText("down " + String.format("%.2f", Math.abs(difference)) + "% from graduates");
            InfoLabel.setGraphic(downIconView);
        }
    }
    private static void difference3(double difference, Label InfoLabel, ImageView upIconView, ImageView downIconView) {
        //set the icon up or down based on the value of the difference (positive or negative)
        if (difference >= 0) {
            InfoLabel.setText("up " + String.format("%.2f", Math.abs(difference)) + "% from current");
            InfoLabel.setGraphic(upIconView);
        } else {
            InfoLabel.setText("down " + String.format("%.2f", Math.abs(difference)) + "% from current");
            InfoLabel.setGraphic(downIconView);
        }
    }

}
