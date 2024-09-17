module com.example.umproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.jetbrains.annotations;
    requires transitive javafx.graphics;

    opens com.umproject to javafx.fxml;
    exports com.umproject;
    exports com.umproject.Graphs;
    opens com.umproject.Graphs to javafx.fxml;
    exports com.umproject.Cards;
    opens com.umproject.Cards to javafx.fxml;
    exports com.umproject.MainPage;
    opens com.umproject.MainPage to javafx.fxml;
    exports com.umproject.AI;
    opens com.umproject.AI to javafx.fxml;
    exports com.umproject.Utils;
    opens com.umproject.Utils to javafx.fxml;
}
