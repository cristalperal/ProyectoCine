module com.example.proyectocine {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires javafx.graphics;
    requires java.sql;
    requires java.desktop;


    opens com.example.proyectocine.Modelo to javafx.base;
    opens com.example.proyectocine.Controlador to javafx.fxml;
    opens com.example.proyectocine to javafx.fxml;

    exports com.example.proyectocine;
    exports com.example.proyectocine.Controlador;
}