package com.umproject.Utils;

import javafx.scene.chart.XYChart;
import javafx.scene.control.Tooltip;

public class DataListenerTooltip {
    public void dataListenerTooltip(String courseName, int grade, int count, XYChart.Data<String, Number> dataPoint, String style) {
        dataPoint.nodeProperty().addListener((observable, oldNode, newNode) -> {
            if (newNode != null) {
                newNode.setStyle(style);
                Tooltip tooltip = new Tooltip(courseName + "\nGrade: " + grade + "\nCount: " + count);
                Tooltip.install(newNode, tooltip);
                newNode.setOnMouseEntered(e -> newNode.setStyle("-fx-bar-fill: rgba(173, 216, 230, 0.8);"));
                newNode.setOnMouseExited(e -> newNode.setStyle(style));
            }
        });
    }
}
