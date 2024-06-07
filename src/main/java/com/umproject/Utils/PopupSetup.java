package com.umproject.Utils;

import com.umproject.Launcher;
import javafx.scene.layout.Pane;
import javafx.stage.Popup;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

public class PopupSetup {

    Pane graphPane;
    double graphPaneWidth;
    double graphPaneHeight;

    public void popupSetup(Stage stage, Popup popup, Pane graphPane, double graphPaneWidth, double paneHeight) {
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

    public Popup createPopupWithPane(@NotNull Pane root) {
        Popup popup = new Popup();

        //create a pane
        Pane graphPane = new Pane();
        graphPane.getStyleClass().add("graph-pane");

        //set the size
        double graphPaneWidth = Launcher.Distance(1920, 1280, root.getWidth());
        double graphPaneHeight = Launcher.Distance(1080, 720, root.getHeight());

        double additionalVerticalSpace = Launcher.Distance(1080, 10, root.getHeight());
        double additionalHorizontalSpace = Launcher.Distance(1920, 10, root.getWidth());
        double paneWidth = graphPaneWidth + additionalHorizontalSpace;
        double paneHeight = graphPaneHeight + additionalVerticalSpace;
        graphPane.setPrefSize(paneWidth, paneHeight);

        this.graphPane = graphPane;
        this.graphPaneWidth = graphPaneWidth;
        this.graphPaneHeight = graphPaneHeight;

        popup.getContent().add(graphPane);

        return popup;
    }

    public Pane getGraphPane() {
        return graphPane;
    }

    public double getGraphPaneWidth() {
        return graphPaneWidth;
    }

    public double getGraphPaneHeight() {
        return graphPaneHeight;
    }
}
