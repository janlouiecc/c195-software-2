module app_sched_sys {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;


    opens app_sched_sys to javafx.fxml;
    exports app_sched_sys;
}