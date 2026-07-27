package com.example.proyectocine.Modelo;

import javafx.beans.property.*;

import java.time.LocalDate;

public class Pelicula {
    // StringProperty nos permite actualizar más rápido las tablas
    private StringProperty idPelicula;
    private StringProperty titulo;
    private ObjectProperty<byte[]> portada; // Mapea el campo BLOB de MySQL
    private ObjectProperty<LocalDate> fechaEstreno;
    private IntegerProperty duracionPelicula; // En minutos
    private StringProperty clasificacion;
    private StringProperty sinopsis;

    //Para la base de datos
    public Pelicula() {
        this.idPelicula = new SimpleStringProperty();
        this.titulo = new SimpleStringProperty();
        this.portada = new SimpleObjectProperty<>();
        this.fechaEstreno = new SimpleObjectProperty<>();
        this.duracionPelicula = new SimpleIntegerProperty();
        this.clasificacion = new SimpleStringProperty();
        this.sinopsis = new SimpleStringProperty();
    }
    // Constructor con parámetros que recibe las Properties
    public Pelicula(StringProperty idPelicula, StringProperty titulo, ObjectProperty<byte[]> portada,
                    ObjectProperty<LocalDate> fechaEstreno, IntegerProperty duracionPelicula,
                    StringProperty clasificacion, StringProperty sinopsis) {
        this.idPelicula = idPelicula;
        this.titulo = titulo;
        this.portada = portada;
        this.fechaEstreno = fechaEstreno;
        this.duracionPelicula = duracionPelicula;
        this.clasificacion = clasificacion;
        this.sinopsis = sinopsis;
    }

    public Pelicula(String idPelicula, String titulo, byte[] portada, LocalDate fechaEstreno,
                    int duracionPelicula, String clasificacion, String sinopsis) {
        this.idPelicula = new SimpleStringProperty(idPelicula);
        this.titulo = new SimpleStringProperty(titulo);
        this.portada = new SimpleObjectProperty<>(portada);
        this.fechaEstreno = new SimpleObjectProperty<>(fechaEstreno);
        this.duracionPelicula = new SimpleIntegerProperty(duracionPelicula);
        this.clasificacion = new SimpleStringProperty(clasificacion);
        this.sinopsis = new SimpleStringProperty(sinopsis);
    }


    public String getIdPelicula() {
        return idPelicula.get();
    }

    public StringProperty idPeliculaProperty() {
        return idPelicula;
    }

    public void setIdPelicula(String idPelicula) {
        this.idPelicula.set(idPelicula);
    }

    public String getTitulo() {
        return titulo.get();
    }

    public StringProperty tituloProperty() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo.set(titulo);
    }

    public byte[] getPortada() {
        return portada.get();
    }

    public ObjectProperty<byte[]> portadaProperty() {
        return portada;
    }

    public void setPortada(byte[] portada) {
        this.portada.set(portada);
    }

    public LocalDate getFechaEstreno() {
        return fechaEstreno.get();
    }

    public ObjectProperty<LocalDate> fechaEstrenoProperty() {
        return fechaEstreno;
    }

    public void setFechaEstreno(LocalDate fechaEstreno) {
        this.fechaEstreno.set(fechaEstreno);
    }

    public int getDuracionPelicula() {
        return duracionPelicula.get();
    }

    public IntegerProperty duracionPeliculaProperty() {
        return duracionPelicula;
    }

    public void setDuracionPelicula(int duracionPelicula) {
        this.duracionPelicula.set(duracionPelicula);
    }

    public String getClasificacion() {
        return clasificacion.get();
    }

    public StringProperty clasificacionProperty() {
        return clasificacion;
    }

    public void setClasificacion(String clasificacion) {
        this.clasificacion.set(clasificacion);
    }

    public String getSinopsis() {
        return sinopsis.get();
    }

    public StringProperty sinopsisProperty() {
        return sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis.set(sinopsis);
    }
}

