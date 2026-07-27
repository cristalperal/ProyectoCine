package com.example.proyectocine.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

        private static final String URL = "jdbc:mysql://localhost:3306/proyecto_cine?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
        private static final String USER = "root";
        private static final String PASSWORD = "123456";

        private static Connection conexion = null;

        private ConexionBD() {}

        public static Connection getConexion() throws SQLException {
            try {
                if (conexion == null || conexion.isClosed()) {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    conexion = DriverManager.getConnection(URL, USER, PASSWORD);
                    System.out.println(" Conexión exitosa a MySQL .");
                }
            } catch (ClassNotFoundException e) {
                System.err.println(" Error: No se encontró el driver JDBC de MySQL.");
                e.printStackTrace();
            }
            return conexion;
        }

        public static void cerrarConexion() {
            if (conexion != null) {
                try {
                    if (!conexion.isClosed()) {
                        conexion.close();
                        System.out.println(" Conexión cerrada.");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
}
