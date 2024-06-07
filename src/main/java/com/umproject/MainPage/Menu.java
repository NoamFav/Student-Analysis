package com.umproject.MainPage;

import com.umproject.AI.Predict;
import com.umproject.Graphs.Graph;
import com.umproject.Launcher;
import com.umproject.Utils.CheckBoxPopup;
import com.umproject.Utils.PdfSaver;
import com.umproject.Utils.SearchBar;
import com.umproject.Utils.Widget;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.Comparator;
import java.util.Objects;

public class Menu {
    public static Student student;
    public static DataSet dataSet;
    public static Courses course;
    public static Graph graph;

    public void createScene(Stage stage,Pane root) {

        //initialize the images for the menu
        Image Logo = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/umproject/images/cropped.png")));
        Image DataSetIcon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/umproject/images/dataset.png")));
        Image StudentsIcon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/umproject/images/student.png")));
        Image CoursesIcon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/umproject/images/courses.png")));
        Image GraphicsIcon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/umproject/images/graph.png")));
        Image LeaveIcon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/umproject/images/Leave.png")));
        Image HelpIcon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/umproject/images/help.png")));

        //initialize the pages for the menu
        dataSet = new DataSet(root, stage);
        course = new Courses(root, SearchBar.outputCourse, stage);
        student = new Student(root, SearchBar.outputStudent, stage);
        graph = new Graph(root, stage);
        CheckBoxPopup.setAll(true);
        CheckBoxPopup.all.setSelected(true);
        
        double fontSize = Launcher.Distance(1920,34,root.getWidth());
        double iconSize = Launcher.Distance(1920,54,root.getWidth());

        //create the panel of the menu
        Rectangle leftSide = new Rectangle();
        leftSide.setHeight(root.getHeight());
        leftSide.setWidth(root.getWidth()/4);
        leftSide.setFill(Color.web("#090F1B"));

        //create logo icon
        ImageView LogoViewer = new ImageView(Logo);
        double size = Launcher.Distance(1920,100,root.getWidth());
        LogoViewer.setFitWidth(size);
        LogoViewer.setFitHeight(size);
        LogoViewer.setX(Launcher.Distance(1920,40,root.getWidth()));
        LogoViewer.setY(Launcher.Distance(1080,40,root.getHeight()));

        stage.getIcons().add(Logo);

        //create the title of the app
        Label titleDataSet = new Label("Project Phase 2");
        titleDataSet.setFont(Font.font("Georgia", Launcher.Distance(1920,40,root.getWidth())));
        titleDataSet.setTextFill(Color.web("white"));
        titleDataSet.setLayoutX(Launcher.Distance(1920,170,root.getWidth()));
        titleDataSet.setLayoutY(Launcher.Distance(1080,60,root.getHeight()));

        Label groupName = new Label("The 7 Rats");
        groupName.setFont(Font.font("Georgia", Launcher.Distance(1920,24,root.getWidth())));
        groupName.setTextFill(Color.web("white"));
        groupName.setLayoutX(Launcher.Distance(1920,170,root.getWidth()));
        groupName.setLayoutY(Launcher.Distance(1080,105,root.getHeight()));

        //create the buttons for the data set
        Button DataSetButton = new Button("DataSet");
        DataSetButton.setFont(Font.font("Georgia", fontSize));
        DataSetButton.setTextFill(Color.web("black"));
        DataSetButton.getStyleClass().add("button-6");
        DataSetButton.setLayoutX(Launcher.Distance(1920,205,root.getWidth()));
        DataSetButton.setLayoutY(Launcher.Distance(1080,229,root.getHeight()));

        //create the icon for the data set button
        ImageView DataSetIconViewer = new ImageView(DataSetIcon);
        DataSetIconViewer.setFitHeight(iconSize);
        DataSetIconViewer.setFitWidth(iconSize);
        DataSetIconViewer.setX(Launcher.Distance(1920,145,root.getWidth()));
        DataSetIconViewer.setY(Launcher.Distance(1080,229,root.getHeight()));

        //create the buttons for the student
        Button StudentsButton = new Button("Students");
        StudentsButton.setFont(Font.font("Georgia", fontSize));
        StudentsButton.setTextFill(Color.web("black"));
        StudentsButton.getStyleClass().add("button-6");
        StudentsButton.setLayoutX(Launcher.Distance(1920,205,root.getWidth()));
        StudentsButton.setLayoutY(Launcher.Distance(1080,329,root.getHeight()));

        //create the icon for the student button
        ImageView StudentsIconViewer = new ImageView(StudentsIcon);
        StudentsIconViewer.setFitHeight(iconSize);
        StudentsIconViewer.setFitWidth(iconSize);
        StudentsIconViewer.setX(Launcher.Distance(1920,145,root.getWidth()));
        StudentsIconViewer.setY(Launcher.Distance(1080,329,root.getHeight()));

        //create the buttons for the courses
        Button CoursesButton = new Button("Courses");
        CoursesButton.setFont(Font.font("Georgia", fontSize));
        CoursesButton.setTextFill(Color.web("black"));
        CoursesButton.getStyleClass().add("button-6");
        CoursesButton.setLayoutX(Launcher.Distance(1920,205,root.getWidth()));
        CoursesButton.setLayoutY(Launcher.Distance(1080,429,root.getHeight()));

        //create the icon for the course button
        ImageView CoursesIconViewer = new ImageView(CoursesIcon);
        CoursesIconViewer.setFitHeight(iconSize);
        CoursesIconViewer.setFitWidth(iconSize);
        CoursesIconViewer.setX(Launcher.Distance(1920,145,root.getWidth()));
        CoursesIconViewer.setY(Launcher.Distance(1080,429,root.getHeight()));

        //create the buttons for the graph
        Button GraphicsButton = new Button("Graphics");
        GraphicsButton.setFont(Font.font("Georgia", fontSize));
        GraphicsButton.setTextFill(Color.web("black"));
        GraphicsButton.getStyleClass().add("button-6");
        GraphicsButton.setLayoutX(Launcher.Distance(1920,205,root.getWidth()));
        GraphicsButton.setLayoutY(Launcher.Distance(1080,529,root.getHeight()));

        //create the icon for the graph button
        ImageView GraphicsIconViewer = new ImageView(GraphicsIcon);
        GraphicsIconViewer.setFitHeight(iconSize);
        GraphicsIconViewer.setFitWidth(iconSize);
        GraphicsIconViewer.setX(Launcher.Distance(1920,145,root.getWidth()));
        GraphicsIconViewer.setY(Launcher.Distance(1080,529,root.getHeight()));

        //create the buttons for the help
        Button LeaveButton = new Button("Leave");
        LeaveButton.setFont(Font.font("Georgia", fontSize));
        LeaveButton.setTextFill(Color.web("black"));
        LeaveButton.getStyleClass().add("button-6");
        LeaveButton.setLayoutX(Launcher.Distance(1920,205,root.getWidth()));
        LeaveButton.setLayoutY(Launcher.Distance(1080,779,root.getHeight()));

        //close the app when clicked
        LeaveButton.setOnAction(event -> stage.close());

        //create the icon for the leave button
        ImageView LeaveIconViewer = new ImageView(LeaveIcon);
        LeaveIconViewer.setFitHeight(iconSize);
        LeaveIconViewer.setFitWidth(iconSize);
        LeaveIconViewer.setX(Launcher.Distance(1920,145,root.getWidth()));
        LeaveIconViewer.setY(Launcher.Distance(1080,779,root.getHeight()));

        //create the buttons for the help
        Button HelpButton = new Button("Help");
        HelpButton.setFont(Font.font("Georgia", fontSize));
        HelpButton.setTextFill(Color.web("black"));
        HelpButton.getStyleClass().add("button-6");
        HelpButton.setLayoutX(Launcher.Distance(1920,205,root.getWidth()));
        HelpButton.setLayoutY(Launcher.Distance(1080,889,root.getHeight()));

        //create the icon for the help button
        ImageView HelpIconViewer = new ImageView(HelpIcon);
        HelpIconViewer.setFitHeight(iconSize);
        HelpIconViewer.setFitWidth(iconSize);
        HelpIconViewer.setX(Launcher.Distance(1920,145,root.getWidth()));
        HelpIconViewer.setY(Launcher.Distance(1080,889,root.getHeight()));

        //add the search bar, the pdf saver button, and the widget to the root
        new SearchBar(root, Launcher.Distance(1920,1066,root.getWidth()), Launcher.Distance(1080,76,root.getHeight()), stage);
        new PdfSaver(root, Launcher.Distance(1920,1527,root.getWidth()), Launcher.Distance(1080,76,root.getHeight()));
        new Widget(root);
        new Predict(root, stage);

        //for each, check every instance of the pages created over the entire application, delete them if they exist, and draw the one needed
        DataSetButton.setOnAction(e -> {
            dataSet.draw(DataSet.index);
            if (student != null) {
                student.clear();
            }
            if (SearchBar.student != null) {
                SearchBar.student.clear();
            }
            if (course!= null) {
                course.clear();
            }
            if (SearchBar.course != null) {
                SearchBar.course.clear();
            }
            if (graph != null) {
                graph.clear();
            }
            CheckBoxPopup.setAll(true);
            DataSet.predicate.setSelected(false);
            Student.predicate.setSelected(false);
            Courses.predicate.setSelected(false);
            Widget.page.setText("Data Set");
        });
        StudentsButton.setOnAction(e -> {
            if (dataSet != null) {
                dataSet.clear();
            }
            if (student != null) {
                student.clear();
            }
            if (SearchBar.student != null) {
                SearchBar.student.clear();
            }
            if (course!= null) {
                course.clear();
            }
            if (SearchBar.course != null) {
                SearchBar.course.clear();
            }
            if (SearchBar.outputStudent != null && !SearchBar.outputStudent.isEmpty()) {
                student = new Student(root, SearchBar.outputStudent, stage);
                student.draw(Student.index);
            }
            if (graph != null) {
                graph.clear();
            }
            DataSet.predicate.setSelected(false);
            Student.predicate.setSelected(false);
            Courses.predicate.setSelected(false);
            Widget.page.setText("Student " + SearchBar.outputStudent);
        });
        CoursesButton.setOnAction(e -> {
            if (dataSet != null) {
                dataSet.clear();
            }
            if (student != null) {
                student.clear();
            }
            if (SearchBar.student != null) {
                SearchBar.student.clear();
            }
            if (course!= null) {
                course.clear();
            }
            if (SearchBar.course!= null) {
                SearchBar.course.clear();
            }
            if (SearchBar.outputCourse != null && !SearchBar.outputCourse.isEmpty()) {
                course = new Courses(root, SearchBar.outputCourse, stage);
                course.draw(Courses.CourseIndex);
            }
            if (graph != null) {
                graph.clear();
            }
            DataSet.predicate.setSelected(false);
            Student.predicate.setSelected(false);
            Courses.predicate.setSelected(false);
            Widget.page.setText("Course " + SearchBar.outputCourse);
        });
        GraphicsButton.setOnAction(e -> {
            if (dataSet != null) {
                dataSet.clear();
            }
            if (student != null) {
                student.clear();
            }
            if (SearchBar.student != null) {
                SearchBar.student.clear();
            }
            if (course!= null) {
                course.clear();
            }
            if (SearchBar.course != null) {
                SearchBar.course.clear();
            }
            graph.draw();
            DataSet.predicate.setSelected(false);
            Student.predicate.setSelected(false);
            Courses.predicate.setSelected(false);
            Widget.page.setText("Graphics");
        });
        HelpButton.setOnAction(e -> {
            if (dataSet != null) {
                dataSet.clear();
            }
            if (student != null) {
                student.clear();
            }
            if (SearchBar.student != null) {
                SearchBar.student.clear();
            }
            if (course!= null) {
                course.clear();
            }
            if (SearchBar.course != null) {
                SearchBar.course.clear();
            }
            if (graph != null) {
                graph.clear();
            }
            Widget.updateLabelsAndIconsDatasetGraph();
            DataSet.predicate.setSelected(false);
            Student.predicate.setSelected(false);
            Courses.predicate.setSelected(false);
            Widget.page.setText("Open READ_ME file");
            Widget.info.setText("To get some help open the file READ_ME");
        });

        //add everything to the root
        root.getChildren().addAll( leftSide, DataSetButton,StudentsButton,CoursesButton,GraphicsButton,HelpButton,LeaveButton, DataSetIconViewer,StudentsIconViewer,CoursesIconViewer,GraphicsIconViewer,LeaveIconViewer,HelpIconViewer,LogoViewer,titleDataSet,groupName);

        //launch the starting page
        dataSet.draw(DataSet.index);
    }
    public static Comparator<String> createNumericComparator() {
        //create a custom comparator for the table so that it sorts string as if they were numbers
        return (o1, o2) -> {
            double d1, d2;
            try {
                d1 = Double.parseDouble(o1);
                if (Double.isNaN(d1)) {
                    d1 = 0.0;
                }
            } catch (NumberFormatException e) {
                d1 = 0.0;
            }
            try {
                d2 = Double.parseDouble(o2);
                if (Double.isNaN(d2)) {
                    d2 = 0.0;
                }
            } catch (NumberFormatException e) {
                d2 = 0.0;
            }
            return Double.compare(d1, d2);
        };
    }
}
