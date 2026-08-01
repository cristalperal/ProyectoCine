package com.example.proyectocine.Modelo;

import javafx.beans.property.*;

public class VentaResumen {
    private final StringProperty idVenta;
    private final StringProperty tituloPelicula;
    private final IntegerProperty cantidad;
    private final DoubleProperty total;
    private final StringProperty fecha;

    public VentaResumen(String idVenta, String tituloPelicula, int cantidad, double total, String fecha) {
        this.idVenta = new SimpleStringProperty(idVenta);
        this.tituloPelicula = new SimpleStringProperty(tituloPelicula);
        this.cantidad = new SimpleIntegerProperty(cantidad);
        this.total = new SimpleDoubleProperty(total);
        this.fecha = new SimpleStringProperty(fecha);
    }

    public String getIdVenta() { return idVenta.get(); }
    public StringProperty idVentaProperty() { return idVenta; }

    public String getTituloPelicula() { return tituloPelicula.get(); }
    public StringProperty tituloPeliculaProperty() { return tituloPelicula; }

    public int getCantidad() { return cantidad.get(); }
    public IntegerProperty cantidadProperty() { return cantidad; }

    public double getTotal() { return total.get(); }
    public DoubleProperty totalProperty() { return total; }

    public String getFecha() { return fecha.get(); }
    public StringProperty fechaProperty() { return fecha; }
}