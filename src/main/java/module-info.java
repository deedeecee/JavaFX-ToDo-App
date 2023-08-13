module com.example.todoapplication {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;


    opens com.example.todoapplication to javafx.fxml;
    exports com.example.todoapplication;
}