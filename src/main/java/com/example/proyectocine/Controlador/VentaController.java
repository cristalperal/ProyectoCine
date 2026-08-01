package com.example.proyectocine.Controlador;

import com.example.proyectocine.DAO.PeliculaDAO;
import com.example.proyectocine.DAO.VentaDAO;
import com.example.proyectocine.Modelo.Pelicula;
import com.example.proyectocine.Modelo.VentaResumen;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class VentaController implements Initializable {

    @FXML private ComboBox<String> cmbSucursal;
    @FXML private ComboBox<String> cmbEmpleado;
    @FXML private ComboBox<String> cmbCliente;
    @FXML private ComboBox<Pelicula> cmbPelicula;
    @FXML private ComboBox<String> cmbFuncion;
    @FXML private Spinner<Integer> spnCantidad;
    @FXML private TextField txtTotal;

    @FXML private Button btnVender;
    @FXML private Button btnCancelar;

    @FXML private TableView<VentaResumen> tblVentas;
    @FXML private TableColumn<VentaResumen, String> colIDVenta;
    @FXML private TableColumn<VentaResumen, String> colPelicula;
    @FXML private TableColumn<VentaResumen, Integer> colCantidad;
    @FXML private TableColumn<VentaResumen, Double> colTotal;
    @FXML private TableColumn<VentaResumen, String> colFecha;

    private PeliculaDAO peliculaDAO;
    private VentaDAO ventaDAO;

    private double precioActualBoleto = 0.0;
    private String idBoletoActual = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        peliculaDAO = new PeliculaDAO();
        ventaDAO = new VentaDAO();

        configurarTabla();
        configurarSpinner();
        cargarCombosBase();
        cargarPeliculas();
        cargarTablaVentas();

        txtTotal.setDisable(true);

        btnVender.setOnAction(this::procesarVenta);
        btnCancelar.setOnAction(e -> limpiarCampos());

        // Al seleccionar Película, busca sus funciones mediante el Stored Procedure
        cmbPelicula.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, peliculaSeleccionada) -> {
            if (peliculaSeleccionada != null) {
                List<String> funciones = ventaDAO.obtenerFuncionesPorPelicula(peliculaSeleccionada.getIdPelicula());
                cmbFuncion.setItems(FXCollections.observableArrayList(funciones));
                if (!funciones.isEmpty()) {
                    cmbFuncion.getSelectionModel().selectFirst();
                }
            } else {
                cmbFuncion.getItems().clear();
            }
        });

        //Al cambiar la Función, consulta  el precio y boleto real.
        cmbFuncion.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, funcionSeleccionada) -> {
            if (funcionSeleccionada != null) {
                String idFuncion = funcionSeleccionada.split(" \\| ")[0];
                String[] datosBoleto = ventaDAO.obtenerBoletoYPrecio(idFuncion);

                idBoletoActual = datosBoleto[0];
                precioActualBoleto = (datosBoleto[1] != null) ? Double.parseDouble(datosBoleto[1]) : 0.0;
                calcularTotal();
            }
        });

        // Al cambiar de sucursal, filtra y carga solo los Empleados de esa ella.
        cmbSucursal.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, sucursalSeleccionada) -> {
            if (sucursalSeleccionada != null) {
                String idSucursal = sucursalSeleccionada.split(" \\| ")[0];
                List<String> empleadosFiltrados = ventaDAO.obtenerEmpleadosPorSucursal(idSucursal);
                cmbEmpleado.setItems(FXCollections.observableArrayList(empleadosFiltrados));
            } else {
                cmbEmpleado.getItems().clear();
            }
        });

        //Recalcular total cuando cambia la cantidad
        spnCantidad.valueProperty().addListener((obs, oldVal, newVal) -> calcularTotal());
    }

    @FXML
    private void procesarVenta(ActionEvent event) {
        if (cmbCliente.getValue() == null || cmbEmpleado.getValue() == null ||
                cmbSucursal.getValue() == null || cmbPelicula.getValue() == null ||
                cmbFuncion.getValue() == null || idBoletoActual == null) {

            mostrarAlerta("Campos Incompletos", "Por favor seleccione todos los campos de la venta.", Alert.AlertType.WARNING);
            return;
        }

        // Extraer los id reales de la selección realizada
        String idCliente  = cmbCliente.getValue().split(" \\| ")[0];
        String idEmpleado = cmbEmpleado.getValue().split(" \\| ")[0];
        String idSucursal = cmbSucursal.getValue().split(" \\| ")[0];

        // usa el store procedure de la BD
        boolean exito = ventaDAO.registrarVentaViaProcedure(idCliente, idEmpleado, idSucursal, idBoletoActual, precioActualBoleto);

        if (exito) {
            mostrarAlerta("Venta Registrada",
                    " VENTA PROCESADA \n" +
                            "-------------------------------------\n" +
                            "• Cliente: " + cmbCliente.getValue() + "\n" +
                            "• Película: " + cmbPelicula.getValue().getTitulo() + "\n" +
                            "-------------------------------------\n" +
                            "¡Venta realizada exitosamente!",
                    Alert.AlertType.INFORMATION);

            cargarTablaVentas();
            cargarCombosBase();
            limpiarCampos();
        } else {
            mostrarAlerta("Error de Venta", "No se pudo procesar la venta.", Alert.AlertType.ERROR);
        }
    }

    private void calcularTotal() {
        double total = spnCantidad.getValue() * precioActualBoleto;
        txtTotal.setText(String.format("RD$ %.2f", total));
    }

    private void cargarCombosBase() {
        cmbSucursal.setItems(FXCollections.observableArrayList(ventaDAO.obtenerSucursales()));
        cmbEmpleado.setItems(FXCollections.observableArrayList(ventaDAO.obtenerEmpleados()));
        cmbCliente.setItems(FXCollections.observableArrayList(ventaDAO.obtenerClientes()));
    }

    private void cargarPeliculas() {
        List<Pelicula> peliculasBD = peliculaDAO.obtenerTodasLasPeliculas();
        cmbPelicula.setItems(FXCollections.observableArrayList(peliculasBD));

        cmbPelicula.setConverter(new StringConverter<>() {
            @Override
            public String toString(Pelicula p) {
                return (p != null) ? p.getTitulo() : "";
            }

            @Override
            public Pelicula fromString(String string) {
                return null;
            }
        });
    }

    private void cargarTablaVentas() {
        tblVentas.setItems(FXCollections.observableArrayList(ventaDAO.obtenerResumenVentas()));
    }

    private void configurarSpinner() {
        spnCantidad.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1));
    }

    private void configurarTabla() {
        colIDVenta.setCellValueFactory(data -> data.getValue().idVentaProperty());
        colPelicula.setCellValueFactory(data -> data.getValue().tituloPeliculaProperty());
        colCantidad.setCellValueFactory(data -> data.getValue().cantidadProperty().asObject());
        colTotal.setCellValueFactory(data -> data.getValue().totalProperty().asObject());
        colFecha.setCellValueFactory(data -> data.getValue().fechaProperty());
    }

    private void limpiarCampos() {
        cmbPelicula.getSelectionModel().clearSelection();
        cmbFuncion.getItems().clear();
        spnCantidad.getValueFactory().setValue(1);
        precioActualBoleto = 0.0;
        idBoletoActual = null;
        calcularTotal();
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}