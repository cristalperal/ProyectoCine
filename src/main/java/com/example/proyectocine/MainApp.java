package com.example.proyectocine;

import com.example.proyectocine.DAO.ConexionBD;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("/com/example/proyectocine/vistas/menuPrincipalV.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        stage.setTitle("Cine Gestión - Sistema de Películas");
        stage.setScene(scene);
        stage.show();

        try {
            ConexionBD.getConexion();
        } catch (Exception e) {
            System.err.println("Error de conexión: " + e.getMessage());
        }
    }
}
