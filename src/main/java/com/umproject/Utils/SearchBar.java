package com.umproject.Utils;

import com.umproject.Launcher;
import com.umproject.MainPage.Courses;
import com.umproject.MainPage.Student;
import com.umproject.MainPage.Menu;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SearchBar{

    public static final List<String> courses = Arrays.asList("JTE-234","ATE-003","TGL-013","PPL-239","WDM-974","GHL-823","HLU-200","MON-014","FEA-907","LPG-307","TSO-010","LDE-009","JJP-001","MTE-004","LUU-003","LOE-103","PLO-132","BKO-800","SLE-332","BKO-801","DSE-003","DSE-005","ATE-014","JTW-004","ATE-008","DSE-007","ATE-214","JHF-101","KMO-007","WOT-104");
    public static final List<String> studentIds = new ArrayList<>();
    public static final List<String> studentsGraduateID = new ArrayList<>();
    public static List<String> combinedList;
    private final TextField textField;
    private final ContextMenu contextMenu;
    private final Pane root;
    public static String outputStudent = "1001196";
    public static String outputCourse = "JTE-234";
    public static Student student;
    public static Courses course;

    public SearchBar(Pane root, double x, double y, Stage stage) {
        //initialize the root
        this.root = root;

        //populate the student grades list
        for(String[] i: ReaderCsv.getData()[1]) {
            studentsGraduateID.add(i[0]);
        }
        studentsGraduateID.remove("StudentID");

        //populate the graduates grades list
        for(String[] i: ReaderCsv.getData()[0]) {
            studentIds.add(i[0]);
        }
        studentIds.remove("StudentID");
        Image loop = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/umproject/images/magnifier.png")));

        //initialize the textfield, the context menu, and the search button
        textField = new TextField();
        contextMenu = new ContextMenu();
        contextMenu.getStyleClass().add("context-menu");
        Button search = new Button();

        //create the search button
        search.setLayoutX(x - Launcher.Distance(1920,30, root.getWidth()));
        search.setLayoutY(y);
        search.setPrefWidth(Launcher.Distance(1920,200, root.getWidth()));
        search.setPrefHeight(Launcher.Distance(1080,70, root.getHeight()));
        search.getStyleClass().add("loop");
        search.toBack();

        //create the textfield
        textField.getStyleClass().add("Search-bar");
        textField.setPromptText("Search here...");
        textField.setPrefWidth(Launcher.Distance(1920,381, root.getWidth()));
        textField.setPrefHeight(Launcher.Distance(1080,71, root.getHeight()));
        textField.setLayoutX(x + Launcher.Distance(1920,40, root.getWidth()));
        textField.setLayoutY(y);
        textField.toFront();

        //create the icon cor the search button
        ImageView magnifier =  new ImageView(loop);
        magnifier.setFitWidth(Launcher.Distance(1920,36, root.getWidth()));
        magnifier.setFitHeight(Launcher.Distance(1080,36, root.getHeight()));
        magnifier.setX(x - Launcher.Distance(1920,10, root.getWidth()));
        magnifier.setY(y + Launcher.Distance(1080, 17.5, root.getHeight()));

        //add all three elements to the root
        root.getChildren().addAll(search, textField, magnifier);

        //combine the course, student, and graduates grades into a single list
        combinedList = Stream.concat(
                Stream.concat(
                        courses.stream(),
                        studentIds.stream().map(String::valueOf)
                ),
                studentsGraduateID.stream()
        ).collect(Collectors.toList());

        //set up the listener for the textfield and the listener for key press
        setupTextFieldListener();
        setupEnterKeyListener(stage);

        //when clicked, search every instance created in the application, if there are, remove them, and draw the appropriate page
        search.setOnAction(event -> {
            if (courses.contains(textField.getText())) {
                outputCourse = textField.getText();
                Menu.dataSet.clear();
                if (course!= null) {
                    course.clear();
                }
                if (Menu.course!= null) {
                    Menu.course.clear();
                }
                if (student!= null) {
                    student.clear();
                }
                if (Menu.student != null) {
                    Menu.student.clear();
                }
                if (Menu.graph != null) {
                    Menu.graph.clear();
                }
                course = new Courses(root, outputCourse, stage);
                course.draw(Courses.CourseIndex);

                WidgetSetup.page.setText("Course " + outputCourse);
            } else if (studentIds.contains(textField.getText())) {
                outputStudent = textField.getText();
                Menu.dataSet.clear();
                if (student != null) {
                    student.clear();
                }
                if (Menu.student != null) {
                    Menu.student.clear();
                }
                if (course!= null) {
                    course.clear();
                }
                if (Menu.course!= null) {
                    Menu.course.clear();
                }
                if (Menu.graph != null) {
                    Menu.graph.clear();
                }
                student = new Student(root, outputStudent, stage);
                Student.index = 0;
                student.draw(Student.index);


                WidgetSetup.page.setText("Student " + outputStudent);
            } else if (studentsGraduateID.contains(textField.getText())) {
                outputStudent = textField.getText();
                Menu.dataSet.clear();
                if (student!= null) {
                    student.clear();
                }
                if (Menu.student != null) {
                    Menu.student.clear();
                }
                if (course!= null) {
                    course.clear();
                }
                if (Menu.course!= null) {
                    Menu.course.clear();
                }
                if (Menu.graph != null) {
                    Menu.graph.clear();
                }
                student = new Student(root, outputStudent, stage);
                Student.index = 1;
                student.draw(Student.index);
                WidgetSetup.page.setText("Student " + outputStudent);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.initOwner(stage);
                alert.setTitle("Invalid input");
                alert.setHeaderText("Valid input required");
                alert.setContentText("Please fill with valid input");
                alert.showAndWait();
            }
            textField.clear();
            contextMenu.hide();
        });
    }
    private void setupTextFieldListener() {textField.textProperty().addListener((observable, oldValue, newValue) -> updateSuggestions(newValue));} //listener for the textfield
    private void setupEnterKeyListener(Stage stage) {
        //when pressing enter, search every instance created in the application, if there are, remove them, and draw the appropriate page
        textField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                if (courses.contains(textField.getText())) {
                    outputCourse = textField.getText();
                    Menu.dataSet.clear();
                    if (course!= null) {
                        course.clear();
                    }
                    if (Menu.course!= null) {
                        Menu.course.clear();
                    }
                    if (student!= null) {
                        student.clear();
                    }
                    if (Menu.student != null) {
                        Menu.student.clear();
                    }
                    if (Menu.graph != null) {
                        Menu.graph.clear();
                    }
                    course = new Courses(root, outputCourse, stage);
                    course.draw(Courses.CourseIndex);

                    WidgetSetup.page.setText("Course " + outputCourse);
                } else if (studentIds.contains(textField.getText())) {
                    outputStudent = textField.getText();
                    Menu.dataSet.clear();
                    if (student != null) {
                        student.clear();
                    }
                    if (Menu.student != null) {
                        Menu.student.clear();
                    }
                    if (course!= null) {
                        course.clear();
                    }
                    if (Menu.course!= null) {
                        Menu.course.clear();
                    }
                    if (Menu.graph != null) {
                        Menu.graph.clear();
                    }
                    student = new Student(root, outputStudent, stage);
                    student.draw(0);
                    Student.index = 0;

                    WidgetSetup.page.setText("Student " + outputStudent);
                } else if (studentsGraduateID.contains(textField.getText())) {
                    outputStudent = textField.getText();
                    Menu.dataSet.clear();
                    if (student!= null) {
                        student.clear();
                    }
                    if (Menu.student != null) {
                        Menu.student.clear();
                    }
                    if (course!= null) {
                        course.clear();
                    }
                    if (Menu.course!= null) {
                        Menu.course.clear();
                    }
                    if (Menu.graph != null) {
                        Menu.graph.clear();
                    }
                    student = new Student(root, outputStudent, stage);
                    student.draw(1);
                    Student.index = 1;

                    WidgetSetup.page.setText("Student " + outputStudent);
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.initOwner(stage);
                    alert.setTitle("Invalid input");
                    alert.setHeaderText("Valid input required");
                    alert.setContentText("Please fill with valid input");
                    alert.showAndWait();
                }
                textField.clear();
                contextMenu.hide();
            }
        });
    }
    private void updateSuggestions(String input) {

        //update the suggestion based on the current input in the textfield
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
        //get the suggestions based on the current input in the textfield
        //and output the 4 first suggestions
        return combinedList.stream()
                .filter(item -> item.toLowerCase().startsWith(input.toLowerCase()))
                .limit(4)
                .collect(Collectors.toList());
    }
}