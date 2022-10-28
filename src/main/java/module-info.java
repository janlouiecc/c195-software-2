module wgu.softwareiiproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;


    opens wgu.softwareiiproject to javafx.fxml;
    exports wgu.softwareiiproject;
}