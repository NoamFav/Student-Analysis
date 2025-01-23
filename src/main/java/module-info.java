module com.StudentAnalysis {
    requires transitive javafx.controls;
    requires javafx.fxml;
    requires transitive org.jetbrains.annotations;
    requires transitive javafx.graphics;

    exports com.StudentAnalysis;
    exports com.StudentAnalysis.Graphs;
    exports com.StudentAnalysis.Cards;
    exports com.StudentAnalysis.MainPage;
    exports com.StudentAnalysis.AI;
    exports com.StudentAnalysis.Utils;
}
