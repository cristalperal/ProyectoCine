package com.example.proyectocine.DAO;

import com.example.proyectocine.Modelo.VentaResumen;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VentaDAO {

    //Registrar venta con el procedure  sp_registrar_venta_boleto
    public boolean registrarVentaViaProcedure(String idCliente, String idEmpleado, String idSucursal, String idBoleto, double precioBoleto) {
        String sql = "{CALL sp_registrar_venta_boleto(?, ?, ?, ?, ?)}";

        try (Connection conn = ConexionBD.getConexion();
             CallableStatement cstmt = conn.prepareCall(sql)) {

            cstmt.setString(1, idCliente);
            cstmt.setString(2, idEmpleado);
            cstmt.setString(3, idSucursal);
            cstmt.setString(4, idBoleto);
            cstmt.setDouble(5, precioBoleto);

            cstmt.execute();
            System.out.println("Venta registrada correctamente.");
            return true;

        } catch (SQLException e) {
            System.err.println("Error de venta: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Obtener las funciones de una pelicula
    public List<String> obtenerFuncionesPorPelicula(String idPelicula) {
        List<String> funciones = new ArrayList<>();
        String sql = "{CALL sp_funciones_pelicula(?)}";

        try (Connection conn = ConexionBD.getConexion();
             CallableStatement cstmt = conn.prepareCall(sql)) {

            cstmt.setString(1, idPelicula);
            ResultSet rs = cstmt.executeQuery();

            while (rs.next()) {
                String f = rs.getString("id_funcion") + " | " + rs.getString("idioma") + " - " + rs.getString("fecha_hora_inicio");
                funciones.add(f);
            }
        } catch (SQLException e) {
            System.err.println("Error al consultar funciones por película: " + e.getMessage());
            e.printStackTrace();
        }
        return funciones;
    }

    // Obtener un boleto y el precio
    public String[] obtenerBoletoYPrecio(String idFuncion) {
        String[] datos = new String[2];
        String sql = "SELECT id_boleto, precio FROM BOLETO WHERE FUNCION_id_funcion = ? LIMIT 1";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, idFuncion);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                datos[0] = rs.getString("id_boleto");
                datos[1] = String.valueOf(rs.getDouble("precio"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return datos;
    }

    // Obtener lista de los clientes, empleados y sucursales.
    public List<String> obtenerClientes() {
        List<String> lista = new ArrayList<>();
        String sql = "SELECT c.PERSONA_id_persona, p.nombre, p.apellido, c.puntos_acumulados " +
                "FROM CLIENTE c JOIN PERSONA p ON c.PERSONA_id_persona = p.id_persona";
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(rs.getString("PERSONA_id_persona") + " | " + rs.getString("nombre") + " " + rs.getString("apellido") + " (" + rs.getInt("puntos_acumulados") + " pts)");
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public List<String> obtenerEmpleados() {
        List<String> lista = new ArrayList<>();
        String sql = "SELECT e.PERSONA_id_persona, p.nombre, p.apellido " +
                "FROM EMPLEADO e JOIN PERSONA p ON e.PERSONA_id_persona = p.id_persona";
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(rs.getString("PERSONA_id_persona") + " | " + rs.getString("nombre") + " " + rs.getString("apellido"));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }


    // Obtener Empleados filtrados por la sucursal
    public List<String> obtenerEmpleadosPorSucursal(String idSucursal) {
        List<String> lista = new ArrayList<>();
        String sql = "SELECT e.PERSONA_id_persona, p.nombre, p.apellido " +
                "FROM EMPLEADO e " +
                "JOIN PERSONA p ON e.PERSONA_id_persona = p.id_persona " +
                "WHERE e.SUCURSAL_id_sucursal = ?";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, idSucursal);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(rs.getString("PERSONA_id_persona") + " | " + rs.getString("nombre") + " " + rs.getString("apellido"));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener empleados : " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }

    public List<String> obtenerSucursales() {
        List<String> lista = new ArrayList<>();
        String sql = "SELECT id_sucursal, nombre FROM SUCURSAL";
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(rs.getString("id_sucursal") + " | " + rs.getString("nombre"));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    // Resumen de las ventas
    public List<VentaResumen> obtenerResumenVentas() {
        List<VentaResumen> lista = new ArrayList<>();
        String sql = "SELECT v.id_venta, p.titulo, COUNT(dv.id_detalle_venta) AS cantidad, v.total, DATE_FORMAT(v.fecha_hora, '%Y-%m-%d %H:%i') AS fecha " +
                "FROM VENTA v " +
                "JOIN DETALLE_VENTA dv ON v.id_venta = dv.VENTA_id_venta " +
                "JOIN BOLETO b ON dv.BOLETO_id_boleto = b.id_boleto " +
                "JOIN FUNCION f ON b.FUNCION_id_funcion = f.id_funcion " +
                "JOIN PELICULA p ON f.PELICULA_id_pelicula = p.id_pelicula " +
                "GROUP BY v.id_venta, p.titulo, v.total, v.fecha_hora " +
                "ORDER BY v.fecha_hora DESC";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(new VentaResumen(
                        rs.getString("id_venta"),
                        rs.getString("titulo"),
                        rs.getInt("cantidad"),
                        rs.getDouble("total"),
                        rs.getString("fecha")
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }
}