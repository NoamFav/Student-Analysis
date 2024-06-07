package com.umproject.Utils;

import com.umproject.Launcher;
import com.umproject.MainPage.DataSet;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.jetbrains.annotations.NotNull;

public class CheckBoxPopup {

    //create all CheckBox
    public static CheckBox all = new CheckBox("All");
    public static CheckBox doot = new CheckBox("Doot");
    public static CheckBox lobi = new CheckBox("lobi");
    public static CheckBox nulp = new CheckBox("nulp");
    public static CheckBox nothing = new CheckBox("nothing");
    public static CheckBox low = new CheckBox("low");
    public static CheckBox medium = new CheckBox("medium");
    public static CheckBox high = new CheckBox("high");
    public static CheckBox full = new CheckBox("full");
    public static CheckBox one = new CheckBox("one");
    public static CheckBox two = new CheckBox("two");
    public static CheckBox three = new CheckBox("three");
    public static CheckBox four = new CheckBox("four");
    public static CheckBox five = new CheckBox("five");
    public static CheckBox fiftynine = new CheckBox("fifty-nine");
    public static CheckBox sixty = new CheckBox("sixty");
    public static CheckBox sixtyone = new CheckBox("sixty-one");
    public static CheckBox sixtytwo = new CheckBox("sixty-two");
    public static CheckBox sixtythree = new CheckBox("sixty-three");
    public static CheckBox sixtyfour = new CheckBox("sixty-four");
    public static CheckBox sixtyfive = new CheckBox("sixty-five");
    public static CheckBox sixtysix = new CheckBox("sixty-six");
    public static CheckBox sixtyseven = new CheckBox("sixty-seven");
    public static CheckBox sixtyeight = new CheckBox("sixty-eight");
    public static CheckBox sixtynine = new CheckBox("sixty-nine");
    public static CheckBox seventy = new CheckBox("seventy");
    public static CheckBox seventyone = new CheckBox("seventy-one");
    public static CheckBox seventytwo = new CheckBox("seventy-two");
    public static CheckBox seventythree = new CheckBox("seventy-three");
    public static CheckBox seventyfour = new CheckBox("seventy-four");
    public static CheckBox seventyfive = new CheckBox("seventy-five");
    public static CheckBox seventysix = new CheckBox("seventy-six");
    public static CheckBox seventyseven = new CheckBox("seventy-seven");
    public static CheckBox seventyeight = new CheckBox("seventy-eight");
    public static CheckBox seventynine = new CheckBox("seventy-nine");
    public static CheckBox eighty = new CheckBox("eighty");
    public static CheckBox eightyone = new CheckBox("eighty-one");
    public static CheckBox eightytwo = new CheckBox("eighty-two");
    public static CheckBox eightythree = new CheckBox("eighty-three");
    public static CheckBox eightyfour = new CheckBox("eighty-four");
    public static CheckBox eightyfive = new CheckBox("eighty-five");
    public static CheckBox eightysix = new CheckBox("eighty-six");
    public static CheckBox eightyseven = new CheckBox("eighty-seven");
    public static CheckBox eightyeight = new CheckBox("eighty-eight");
    public static CheckBox eightynine = new CheckBox("eighty-nine");
    public static CheckBox ninety = new CheckBox("ninety");
    public static CheckBox ninetyone = new CheckBox("ninety-one");
    public static CheckBox ninetytwo = new CheckBox("ninety-two");
    public static CheckBox ninetythree = new CheckBox("ninety-three");
    public static CheckBox ninetyfour = new CheckBox("ninety-four");
    public static CheckBox ninetyfive = new CheckBox("ninety-five");
    public static CheckBox ninetysix = new CheckBox("ninety-six");
    public static CheckBox ninetyseven = new CheckBox("ninety-seven");
    public static CheckBox ninetyeight = new CheckBox("ninety-eight");
    public static CheckBox ninetynine = new CheckBox("ninety-nine");
    public static CheckBox hundred = new CheckBox("hundred");

    //create a list of all the checkboxes
    public static final CheckBox[] surunaCheckboxes = {doot, lobi, nulp};
    public static final CheckBox[] hurniCheckboxes = {full, high, medium, low, nothing};
    public static final CheckBox[] voltaCheckboxes = {one, two, three, four, five};
    public static final CheckBox[] lalCountCheckboxes = {fiftynine, sixty, sixtyone, sixtytwo, sixtythree, sixtyfour, sixtyfive, sixtysix, sixtyseven, sixtyeight, sixtynine, seventy, seventyone, seventytwo, seventythree, seventyfour, seventyfive, seventysix, seventyseven, seventyeight, seventynine, eighty, eightyone, eightytwo, eightythree, eightyfour, eightyfive, eightysix, eightyseven, eightyeight, eightynine, ninety, ninetyone, ninetytwo, ninetythree, ninetyfour, ninetyfive, ninetysix, ninetyseven, ninetyeight, ninetynine, hundred};
    public static final CheckBox[] allCheckboxes = {doot, lobi, nulp, full, high, medium, low, nothing, one, two, three, four, five, fiftynine, sixty, sixtyone, sixtytwo, sixtythree, sixtyfour, sixtyfive, sixtysix, sixtyseven, sixtyeight, sixtynine, seventy, seventyone, seventytwo, seventythree, seventyfour, seventyfive, seventysix, seventyseven, seventyeight, seventynine, eighty, eightyone, eightytwo, eightythree, eightyfour, eightyfive, eightysix, eightyseven, eightyeight, eightynine, ninety, ninetyone, ninetytwo, ninetythree, ninetyfour, ninetyfive, ninetysix, ninetyseven, ninetyeight, ninetynine, hundred};
    public static void setAll(boolean bool) {
        //change all checkboxes to a boolean
        for (CheckBox cb : allCheckboxes) {
            cb.setSelected(bool);
        }
    }
    private final static javafx.beans.value.ChangeListener<Boolean> individualCheckboxListener = (observable, oldValue, newValue) -> {
        //check if all checkboxes are selected and set the all CheckBox to false if they aren't
        if (!newValue) {
            all.setSelected(false);
        } else {
            checkIfAllSelected();
        }
    };
    public HBox CreateCheckBox(){
        //make so that when all is selected, all the other Checkboxes are check or unchecked
        all.setOnAction(event -> setAll(all.isSelected()));

        //make so that when all the checkboxes are selected, make the all checkbox selected and if not, make the all checkbox unselected
        for (CheckBox cb : allCheckboxes) {
            cb.selectedProperty().addListener(individualCheckboxListener);
            cb.getStyleClass().add("CheckBox");
        }

        //add label and check boxes to the VBox for each checkbox of the category
        Label surunaValue = new Label("Suruna Value: ");
        surunaValue.setFont(Font.font("Georgia", 25));

        VBox surunaBox = new VBox(5, surunaValue, doot, lobi, nulp);

        Label hurniValue = new Label("Hurni Level: ");
        hurniValue.setFont(Font.font("Georgia", 25));

        VBox hurniBox = new VBox(5, hurniValue, full, high, medium, low, nothing);

        Label voltaValue = new Label("Volta: ");
        voltaValue.setFont(Font.font("Georgia", 25));

        VBox voltaBox = new VBox(5, voltaValue, one, two, three, four, five);

        Label lalValue = new Label("Lal Count: ");
        lalValue.setFont(Font.font("Georgia", 25));

        surunaValue.getStyleClass().add("Graph-Label");
        hurniValue.getStyleClass().add("Graph-Label");
        voltaValue.getStyleClass().add("Graph-Label");
        lalValue.getStyleClass().add("Graph-Label");
        all.getStyleClass().add("CheckBox");

        VBox lalCountBoxFinal = getvBox(lalValue);

        //return the HBox containing all the CheckBoxes grouped as attributes categories
        return new HBox(20, all, surunaBox, hurniBox, voltaBox, lalCountBoxFinal);
    }

    private static @NotNull VBox getvBox(Label lalValue) {
        VBox lalCountBox1 = new VBox(5, fiftynine, sixty, sixtyone, sixtytwo, sixtythree, sixtyfour, sixtyfive, sixtysix, sixtyseven,sixtyeight, sixtynine, seventy, seventyone, seventytwo, seventythree, seventyfour, seventyfive, seventysix, seventyseven, seventyeight, seventynine);
        VBox lalCountBox2 = new VBox(5,  eighty, eightyone, eightytwo, eightythree, eightyfour, eightyfive, eightysix, eightyseven, eightyeight, eightynine, ninety, ninetyone, ninetytwo, ninetythree, ninetyfour, ninetyfive, ninetysix, ninetyseven, ninetyeight, ninetynine, hundred );
        HBox lalCountBox = new HBox( 40, lalCountBox1, lalCountBox2);
        return new VBox(5, lalValue,lalCountBox);
    }

    public void createPopUP(Stage stage, Pane root){
        //create a popup
        Popup popUp = new Popup();

        //create the pane for the popup and add CSS to it
        Pane popUpPane = new Pane();
        popUpPane.getStyleClass().add("graph-pane");

        //calculate the width and height of the popup and add it to the popup pane
        double graphPaneWidth = Launcher.Distance(1920, 700, root.getWidth()) ;
        double graphPaneHeight = Launcher.Distance(1080, 700, root.getHeight());

        double additionalVerticalSpace = Launcher.Distance(1080, 0, root.getHeight());
        double additionalHorizontalSpace = Launcher.Distance(1920, 0, root.getWidth());
        double paneWidth = graphPaneWidth + additionalHorizontalSpace;
        double paneHeight = graphPaneHeight + additionalVerticalSpace;

        popUpPane.setPrefSize(paneWidth, paneHeight);

        //Create the HBox containing all the CheckBoxes grouped and position it based on the popup width and height
        HBox popUpBox = CreateCheckBox();
        popUpBox.setLayoutX(Launcher.Distance(1920,20, root.getWidth()));
        popUpBox.setLayoutY(Launcher.Distance(1080,20, root.getHeight()));

        //add the HBox to the popup pane
        popUpPane.getChildren().add(popUpBox);

        //make listeners to when the popup is closed
        popUp.setOnHidden(this::onPopupClose);

        //add the popup pane to the popup and show it
        popUp.getContent().add(popUpPane);
        popUp.setAutoHide(true);
        popUp.show(stage);
    }
    private static void checkIfAllSelected() {
        //check if all checkboxes are selected and if yes make the all checkbox selected
        for (CheckBox cb : allCheckboxes) {
            if (!cb.isSelected()) {
                return;
            }
        }
        all.setSelected(true);
    }
    public void onPopupClose(WindowEvent event) {
        DataSet.filterTable(DataSet.dataList, getCheckboxStates(surunaCheckboxes), getCheckboxStates(hurniCheckboxes), getCheckboxStates(lalCountCheckboxes), getCheckboxStates(voltaCheckboxes));}
    public static int[] getCheckboxStates(CheckBox[] checkBoxes) {
        //get the states of the checkboxes
        //and output them in an int array
        int[] states = new int[checkBoxes.length];
        for (int i = 0; i < checkBoxes.length; i++) {
            states[i] = checkBoxes[i].isSelected() ? 1 : 0;
        }
        return states;
    }
}

