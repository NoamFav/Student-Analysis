module com.example.umproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.jetbrains.annotations;
    requires transitive javafx.graphics;

    exports com.umproject;
    exports com.umproject.Graphs;
    exports com.umproject.Cards;
    exports com.umproject.MainPage;
    exports com.umproject.AI;
    exports com.umproject.Utils;
}
