module com.example.c195 {
    requires javafx.controls;
    requires javafx.fxml;


    opens wgu.software2project to javafx.fxml;
    exports wgu.software2project;
}