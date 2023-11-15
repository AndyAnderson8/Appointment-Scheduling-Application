module andyanderson.appointments {
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    exports andyanderson.appointments;
    exports andyanderson.appointments.controllers;
    opens andyanderson.appointments.controllers to javafx.fxml;
    opens andyanderson.appointments.models to javafx.base;
    opens andyanderson.appointments to javafx.base, javafx.fxml;
}