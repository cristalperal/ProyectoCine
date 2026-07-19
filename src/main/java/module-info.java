module com.example.proyectocine {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.proyectocine to javafx.fxml;
    exports com.example.proyectocine;
}