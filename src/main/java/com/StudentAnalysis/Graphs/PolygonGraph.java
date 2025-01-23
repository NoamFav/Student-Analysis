package com.StudentAnalysis.Graphs;

import static javafx.scene.paint.Color.*;

import com.StudentAnalysis.Launcher;
import com.StudentAnalysis.MainPage.Student;
import com.StudentAnalysis.Utils.PopupSetup;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.transform.Rotate;
import javafx.stage.Popup;
import javafx.stage.Stage;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class PolygonGraph {
    public static List<String> courses =
            Arrays.asList(
                    "JTE-234", "ATE-003", "TGL-013", "PPL-239", "WDM-974", "GHL-823", "HLU-200",
                    "MON-014", "FEA-907", "LPG-307", "TSO-010", "LDE-009", "JJP-001", "MTE-004",
                    "LUU-003", "LOE-103", "PLO-132", "BKO-800", "SLE-332", "BKO-801", "DSE-003",
                    "DSE-005", "ATE-014", "JTW-004", "ATE-008", "DSE-007", "ATE-214", "JHF-101",
                    "KMO-007", "WOT-104");

    public static double[] averageGrades = {
        7.446601941747573,
        8.008880994671403,
        6.675041876046901,
        7.058528428093646,
        7.004854368932039,
        6.769230769230769,
        7.651282051282052,
        6.983164983164984,
        7.767169179229481,
        7.607023411371237,
        0,
        6.951690821256038,
        7.67741935483871,
        7.4378330373001775,
        7.547842401500938,
        7.231793960923623,
        0,
        6.680320569902048,
        7.451342281879195,
        0,
        7.145648312611012,
        7.975806451612903,
        6.703883495145631,
        7.4966329966329965,
        6.703703703703703,
        6.9144385026737964,
        7.8294849023090585,
        6.5610859728506785,
        7.371980676328502,
        6.432885906040268
    };
    public static double[] averageGrades2 = {
        7.35729490748322,
        7.98848316139949,
        6.89798591779925,
        7.10288739697615,
        7.07248512635773,
        6.82435456579881,
        7.50537634408602,
        7.1413132470935,
        7.72217673707767,
        7.6767097865837,
        6.33677201026145,
        7.12641231373833,
        7.74903116642105,
        7.43283663555483,
        7.42175645434201,
        7.34042901588341,
        7.46580426832596,
        6.82195295016648,
        7.52207848916544,
        7.03247639320998,
        7.27443916816768,
        7.9633753616069,
        6.87626221276131,
        7.5093062605753,
        6.78128923093718,
        7.05933082255335,
        7.80863489984171,
        6.83800010916435,
        7.31482997652967,
        6.85382893946837
    };
    public static double[] averageGrades3 = {
        7.32281211301727,
        8.0,
        6.71075660240512,
        7.10102320927759,
        6.89580087604077,
        6.66726055362304,
        7.46551652681431,
        7.01914759150483,
        7.80772053905871,
        7.63984508045933,
        0,
        6.82442935353474,
        7.77856326849531,
        7.4,
        7.43608860670856,
        7.2,
        0,
        6.7,
        7.47680877771967,
        0,
        7.1,
        8.07379344643138,
        6.58957145574883,
        7.53987223629698,
        6.66505091334492,
        6.9,
        7.8,
        6.51171884053733,
        7.26046602034481,
        6.45537817737636
    };

    private void drawElements(
            Pane graphpane,
            double centerX,
            double centerY,
            double radius,
            double[] data1,
            double[] data2,
            Pane root) {

        // calculate the coordinates of the vertices, of 5 triacontagon, and draw them
        for (double i = radius; i > 0; i -= radius / 5) {
            double[][] cord = generateTriacontagonVertices(i, new double[] {centerX, centerY}, 0);
            double[] flattenedCord = Arrays.stream(cord).flatMapToDouble(Arrays::stream).toArray();
            Polygon polygon = new Polygon(flattenedCord);
            polygon.setFill(new Color(1, 1, 1, 0.0));
            polygon.setStroke(BLACK);
            polygon.setStrokeWidth(Math.max(1, i / 100));
            graphpane.getChildren().add(polygon);
        }

        // add a line connecting every corner by the center
        for (int i = 0; i < 360; i += 12) {
            Line line = new Line();
            line.setStroke(new Color(0, 0, 0, 0.8));
            line.setStartX(centerX - radius);
            line.setEndX(centerX + radius);
            line.setStartY(centerY);
            line.setEndY(centerY);

            Rotate rotate = new Rotate();
            rotate.setAngle(i);
            rotate.setPivotX(centerX);
            rotate.setPivotY(centerY);

            line.getTransforms().add(rotate);
            graphpane.getChildren().add(line);
        }

        // add points to each corner of the final triacontagon
        // add the label for each corner of the final triacontagon
        double[][] cordFinal =
                generateTriacontagonVertices(
                        radius,
                        new double[] {
                            centerX - Launcher.Distance(1920, 30, root.getWidth()),
                            centerY - Launcher.Distance(1080, 5, root.getHeight())
                        },
                        35);
        double[][] cordOriginal =
                generateTriacontagonVertices(radius, new double[] {centerX, centerY}, 0);
        for (int i = 0; i < cordFinal.length; i++) {
            Label label = new Label(courses.get(i));
            Circle circle = new Circle();
            circle.setCenterX(cordOriginal[i][0]);
            circle.setCenterY(cordOriginal[i][1]);
            circle.setRadius(4);
            circle.setFill(BLACK);
            label.setLayoutX(cordFinal[i][0]);
            label.setLayoutY(cordFinal[i][1]);
            label.setTextFill(Color.BLACK);
            label.setFont(new Font(Launcher.Distance(1920, 15, root.getWidth())));

            graphpane.getChildren().add(circle);
            graphpane.getChildren().add(label);
        }

        // create the array for the grades
        double[][] point = new double[30][2];

        // plot the data1 array
        for (String i : courses) {
            double[] position =
                    createPoint(
                            graphpane,
                            i,
                            data1[courses.indexOf(i)],
                            radius,
                            new double[] {centerX, centerY},
                            Color.RED);
            point[courses.indexOf(i)] = position;
        }

        // create lines between each point on the point array if they are next to each other
        createLine(graphpane, point, "red", RED, point);

        // plot the data2 array
        for (String i : courses) {
            double[] position =
                    createPoint(
                            graphpane,
                            i,
                            data2[courses.indexOf(i)],
                            radius,
                            new double[] {centerX, centerY},
                            Color.BLUE);
            point[courses.indexOf(i)] = position;
        }

        // create lines between each point on the point array if they are next to each other
        createLine(graphpane, point, "blue", BLUE, point);
    }

    public void showGraphInPopup(Stage stage, Pane root, double[] data1, int index) {
        // create popup
        Popup popup = new Popup();

        // create the graph pane
        Pane graphPane = new Pane();
        graphPane.getStyleClass().add("graph-pane");

        // set the radius, and the graph pane width, and height
        double radius = Launcher.Distance(1080, 400, root.getHeight());
        double graphPaneWidth = 2 * radius + Launcher.Distance(1920, 20, root.getWidth());
        double graphPaneHeight = 2 * radius + Launcher.Distance(1080, 20, root.getHeight());

        double additionalVerticalSpace = Launcher.Distance(1080, 110, root.getHeight());
        double paneWidth = graphPaneWidth + additionalVerticalSpace;
        double paneHeight = graphPaneHeight + additionalVerticalSpace;

        // set the graph pane's width and height'
        graphPane.setPrefSize(paneWidth, paneHeight);

        // set the center X and Y of the triacontagon
        double centerX =
                radius
                        + Launcher.Distance(1920, 10, root.getWidth())
                        + (additionalVerticalSpace / 2);
        double centerY =
                radius
                        + Launcher.Distance(1080, 10, root.getHeight())
                        + (additionalVerticalSpace / 2);

        // add the legend to the graph pane
        addLegendToPane(graphPane, root, 0, 0, 0);

        // create the triacontagon with the data1 array based on the index, and the Students
        // predicate checkbox state
        // and add it to the graph pane
        if (index == 0 && Student.predicate.isSelected()) {
            drawElements(graphPane, centerX, centerY, radius, data1, averageGrades3, root);
        } else if (index == 0) {
            drawElements(graphPane, centerX, centerY, radius, data1, averageGrades, root);
        } else if (index == 1) {
            drawElements(graphPane, centerX, centerY, radius, data1, averageGrades2, root);
        }

        PopupSetup popupSetup = new PopupSetup();
        popupSetup.popupSetup(stage, popup, graphPane, graphPaneWidth, paneHeight);
    }

    public static double[][] generateTriacontagonVertices(
            double radius, double[] center, double offset) {
        // create the vertices positions of the triacontagon based on the equation of regular
        // polygon
        double[][] cord = new double[30][2];
        for (int i = 0; i < 30; i++) {
            double angle_deg = 12 * i;
            double angle_rad = Math.toRadians(angle_deg);
            double radius2 = radius + offset;
            double x = center[0] + radius2 * Math.cos(angle_rad);
            double y = center[1] + radius2 * Math.sin(angle_rad);

            x = Math.round(x * 100.0) / 100.0;
            y = Math.round(y * 100.0) / 100.0;

            cord[i][0] = x;
            cord[i][1] = y;
        }
        return cord;
    }

    @Contract("_, _, _, _, _, _ -> new")
    public static double @NotNull [] createPoint(
            @NotNull Pane root,
            String course,
            double value,
            double radius,
            double @NotNull [] center,
            Color color) {

        // create the point on each corresponding line with trigonometric equations, for a triangle
        // rectangle
        Circle circle = new Circle();
        int number = courses.indexOf(course);
        int angle = (12 * number);
        double lengthX = Math.cos(Math.toRadians(angle)) * (radius * (value / 10));
        double lengthY = Math.sin(Math.toRadians(angle)) * (radius * (value / 10));

        circle.setCenterX(center[0] + lengthX);
        circle.setCenterY(center[1] + lengthY);

        circle.setRadius(4);
        circle.setFill(color);
        root.getChildren().add(circle);
        return new double[] {center[0] + lengthX, center[1] + lengthY};
    }

    public static void createLine(
            Pane root, double[][] point, String color, Color colors, double[][] cord) {
        // connect each line from a starting point to the next point in the point array
        for (int i = 0; i < 30; i++) {
            Line line = getLine(point, color, i);
            root.getChildren().add(line);
        }
        double[] flattenedCord = Arrays.stream(cord).flatMapToDouble(Arrays::stream).toArray();

        Polygon shade = new Polygon(flattenedCord);
        shade.setFill(colors);
        shade.setOpacity(0.2);
        root.getChildren().add(shade);
    }

    @NotNull
    private static Line getLine(double[][] point, String color, int i) {
        Line line = new Line();
        line.setStrokeWidth(2);
        line.setStyle(String.format("-fx-stroke: %s;", color));
        if (i == 29) {
            line.setStartX(point[i][0]);
            line.setStartY(point[i][1]);
            line.setEndX(point[0][0]);
            line.setEndY(point[0][1]);
        } else {
            line.setStartX(point[i][0]);
            line.setStartY(point[i][1]);
            line.setEndX(point[i + 1][0]);
            line.setEndY(point[i + 1][1]);
        }
        return line;
    }

    public void addLegendToPane(@NotNull Pane graphPane, Pane root, int index, int id1, int id2) {
        // add a custom legend to the given pane
        HBox legendBox = new HBox(10);
        legendBox.setPadding(new Insets(10));
        legendBox.setStyle(
                "-fx-background-color: white; -fx-border-color: black; -fx-border-width: 2px;"
                        + " -fx-border-radius: 5; -fx-background-radius: 5;");

        VBox legendItem1 =
                createLegendItem(index == 0 ? "Student Grade" : "Student " + id1, Color.RED, root);
        VBox legendItem2 =
                createLegendItem(
                        index == 0 ? "Students averages" : "Student " + id2, Color.BLUE, root);

        legendBox.getChildren().addAll(legendItem1, legendItem2);

        legendBox.setLayoutX(10);
        legendBox.setLayoutY(graphPane.getHeight() - 50);

        legendBox.setLayoutX((graphPane.getWidth() - legendBox.getWidth()) / 2);
        legendBox.setLayoutY(graphPane.getHeight() - legendBox.getHeight() - 10);

        graphPane.getChildren().add(legendBox);
    }

    private @NotNull VBox createLegendItem(String labelText, Color color, @NotNull Pane root) {
        // create a legend item with the given label and color
        Rectangle colorIndicator =
                new Rectangle(
                        Launcher.Distance(1920, 10, root.getWidth()),
                        Launcher.Distance(1080, 10, root.getHeight()),
                        color);
        Label label = new Label(labelText, colorIndicator);
        label.setFont(new Font("Georgia", Launcher.Distance(1920, 20, root.getWidth())));
        label.setGraphicTextGap(5);

        VBox legendItem = new VBox(label);
        legendItem.setAlignment(Pos.CENTER_LEFT);

        return legendItem;
    }

    public Pane draw(
            double centerX,
            double centerY,
            double radius,
            double[] data1,
            double[] data2,
            Pane root,
            int id1,
            int id2) {
        // draw a custom version of the triacontagon based on the data1 and data2 arrays
        Pane item = new Pane();
        drawElements(item, centerX, centerY, radius, data1, data2, root);
        addLegendToPane(item, root, 1, id1, id2);
        return item;
    }
}
