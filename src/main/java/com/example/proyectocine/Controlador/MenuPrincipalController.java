package com.example.proyectocine.Controlador;

import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class MenuPrincipalController {

    @FXML private Button btnAdmPeliculas;

    @FXML
    public void initialize() {
        // Sombra de neón violeta para el botón
        DropShadow glow = new DropShadow();
        glow.setColor(Color.web("#a076f9"));
        glow.setRadius(15);

        // Animación al pasar el mouse por encima
        btnAdmPeliculas.setOnMouseEntered(e -> {
            btnAdmPeliculas.setEffect(glow);
            ScaleTransition st = new ScaleTransition(Duration.millis(150), btnAdmPeliculas);
            st.setToX(1.04);
            st.setToY(1.04);
            st.play();
        });

        // Animación al retirar el mouse
        btnAdmPeliculas.setOnMouseExited(e -> {
            btnAdmPeliculas.setEffect(null);
            ScaleTransition st = new ScaleTransition(Duration.millis(150), btnAdmPeliculas);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();
        });
    }



    //Método genérico para cargar y mostrar una nueva ventana
    private void abrirNuevaVentana(String fxml, String titulo) {
        try {
            //  Cargar el FXML de la nueva ventana
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/proyectocine/vistas/" + fxml));
            Parent parent = fxmlLoader.load();
            // Crear una nueva ventana
            Stage stage = new Stage();
            stage.setTitle(titulo);
            stage.setScene(new Scene(parent));
            // bloquea la ventana principal hasta que se cierra
            stage.initModality(Modality.APPLICATION_MODAL);
            // Centrar la nueva ventana en la pantalla
            stage.centerOnScreen();
            // Mostrar la ventana
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error al cargar la ventana FXML: " + fxml);
        }
    }

    // Métodos para Abrir las ventanas de parada y ruta desde el menu principal
    @FXML
    public void AbrirGestionPelicula(ActionEvent actionEvent) {
        abrirNuevaVentana("gestionPeliculasV.fxml", "Gestión de películas");
    }

}
