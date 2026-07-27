package com.example.proyectocine.DAO;

import com.example.proyectocine.Modelo.Pelicula;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class PeliculaDAO {

    //Obtener todas las peliculas desde la BD
    public List<Pelicula> obtenerTodasLasPeliculas() {
        List<Pelicula> lista = new ArrayList<>();
        String consulta = "SELECT id_pelicula, titulo, portada, fecha_estreno, duracion_pelicula, clasificacion, sinopsis FROM PELICULA";

        try(Connection connection = ConexionBD.getConexion()) {

            PreparedStatement preparedStatement = connection.prepareStatement(consulta);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String id = resultSet.getString("id_pelicula");
                String titulo = resultSet.getString("titulo");
                byte[] portada = resultSet.getBytes("portada");
                LocalDate fecha = resultSet.getObject("fecha_estreno", LocalDate.class);
                int duracion = resultSet.getInt("duracion_pelicula");
                String clasificacion = resultSet.getString("clasificacion");
                String sinopsis = resultSet.getString("sinopsis");

                Pelicula pelicula = new Pelicula(id, titulo, portada, fecha, duracion, clasificacion, sinopsis);
                lista.add(pelicula);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener los peliculas");
            e.printStackTrace();
        }
        return lista;
    }

    // Guardar las películas - insert
    public void guardarPelicula(Pelicula pelicula) {
        String consulta = "INSERT INTO PELICULA (id_pelicula, titulo, portada, fecha_estreno, duracion_pelicula, clasificacion, sinopsis) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try(Connection connection = ConexionBD.getConexion()){
            PreparedStatement preparedStatement = connection.prepareStatement(consulta);

            preparedStatement.setString(1, pelicula.getIdPelicula());
            preparedStatement.setString(2, pelicula.getTitulo());
            if(pelicula.getPortada() != null && pelicula.getPortada().length > 0){
                preparedStatement.setBytes(3, pelicula.getPortada());
            }else {
                preparedStatement.setNull(3,Types.BLOB);
            }
            preparedStatement.setObject(4, pelicula.getFechaEstreno());
            preparedStatement.setInt(5, pelicula.getDuracionPelicula());
            preparedStatement.setString(6, pelicula.getClasificacion());
            preparedStatement.setString(7, pelicula.getSinopsis());
            preparedStatement.executeUpdate();
            System.out.println("Película guardada correctamente");
        } catch (SQLException e) {
            System.out.println("Error al guardar película");
            e.printStackTrace();
        }
    }

    // Modificar las películas
    public void modificarPelicula(Pelicula pelicula) {
        String consulta = "UPDATE PELICULA SET titulo = ?, portada = ?, fecha_estreno = ?, duracion_pelicula = ?, clasificacion = ?, sinopsis = ? WHERE id_pelicula = ?";

        try(Connection connection = ConexionBD.getConexion()){
            PreparedStatement preparedStatement = connection.prepareStatement(consulta);

            preparedStatement.setString(1, pelicula.getTitulo());
            if(pelicula.getPortada() != null && pelicula.getPortada().length > 0){
                preparedStatement.setBytes(2, pelicula.getPortada());
            }else {
                preparedStatement.setNull(2,Types.BLOB);
            }
            preparedStatement.setObject(3, pelicula.getFechaEstreno());
            preparedStatement.setInt(4, pelicula.getDuracionPelicula());
            preparedStatement.setString(5, pelicula.getClasificacion());
            preparedStatement.setString(6, pelicula.getSinopsis());
            preparedStatement.setString(7, pelicula.getIdPelicula());
            preparedStatement.executeUpdate();
            System.out.println("Película modificada correctamente");
        } catch (SQLException e) {
            System.out.println("Error al modificar película");
            e.printStackTrace();
        }

    }

    //Eliminar las películas
    public void eliminarPelicula(String idPelicula) {
        String consulta = "DELETE FROM PELICULA WHERE id_pelicula = ?";

        try(Connection connection = ConexionBD.getConexion()){
            PreparedStatement preparedStatement = connection.prepareStatement(consulta);

            preparedStatement.setString(1, idPelicula);
            preparedStatement.executeUpdate();

            System.out.println("Película eliminada correctamente");
        } catch (SQLException e) {
            System.out.println("Error al eliminar película");
            e.printStackTrace();
        }
    }

    // Obtener el ultimo ID ingresado para autogenerar el siguiente
    public int obtenerMaxIdPelicula(){
        String consulta = "SELECT MAX(CAST(SUBSTRING(id_pelicula, 5) AS UNSIGNED)) AS max_id FROM PELICULA";

        try(Connection connection = ConexionBD.getConexion()){

            PreparedStatement preparedStatement = connection.prepareStatement(consulta);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("max_id");
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener el  ID");
            e.printStackTrace();
        }
        return 0;
    }

}
