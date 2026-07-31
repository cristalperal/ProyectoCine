package com.example.proyectocine.Controlador;

import com.example.proyectocine.DAO.PeliculaDAO;
import com.example.proyectocine.Modelo.Pelicula;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;

import javafx.scene.image.ImageView;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class PeliculaController implements Initializable {


    @FXML
    private TextField txtCod;
    @FXML
    private TextField txtTitulo;
    @FXML
    private DatePicker DatePickerFecha;
    @FXML
    private Spinner<Integer> spnduracion;
    @FXML
    private ComboBox<String> cmboxClasificacion;
    @FXML
    private TextArea sinopsis;

    @FXML
    private ComboBox<String> cmbBuscar;
    @FXML
    private Button btnBuscar;

    // Para mostrar la portada en la app
    @FXML
    private ImageView imgPortada;
    @FXML
    private Button btnCargarPortada;

    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnCancelarAccion;
    @FXML
    private Button btnActualizar;
    @FXML
    private Button btneliminar;


    @FXML
    private TableView<Pelicula> tblPelicula;
    @FXML
    private TableColumn<Pelicula, String> colID;
    @FXML
    private TableColumn<Pelicula, String> colTitulo;
    @FXML
    private TableColumn<Pelicula, LocalDate> colFecha;
    @FXML
    private TableColumn<Pelicula, Integer> colDuracion;
    @FXML
    private TableColumn<Pelicula, String> colClasificacion;
    @FXML
    private TableColumn<Pelicula, String> colSinopsis;

    // Contador para el id de película
    private int generarId = 1;

    private PeliculaDAO peliculaDAO;
    private ObservableList<Pelicula> listaPeliculasO;
    private Pelicula peliculaSeleccionada = null;

    private byte[] bytesPortada = null; // Para la imagen BLOB


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        peliculaDAO = new PeliculaDAO();
        configurarTabla();
        configurarSpinner();
        cargarCombos();
        cargarDatos();
        setInitialPeliculaId();
        txtCod.setDisable(true);
        DatePickerFecha.setEditable(false);

        btnGuardar.setOnAction(this::guardarPelicula);
        btnActualizar.setOnAction(this::modificarPelicula);
        btneliminar.setOnAction(this::eliminarPelicula);
        btnCancelarAccion.setOnAction(this::cancelarAccion);
        btnCargarPortada.setOnAction(this::seleccionarImagen);
        btnBuscar.setOnAction(this::buscarPelicula);

        // Listener de selección de tabla
        tblPelicula.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> mostrarDetallesPelicula(newVal));

    }

    // Método para filtrar la tabla según lo elegido en combo
    @FXML
    private void buscarPelicula(ActionEvent event) {
        String tituloSeleccionado = cmbBuscar.getValue();

        if (tituloSeleccionado == null || tituloSeleccionado.trim().isEmpty()) {
            mostrarAlerta("Atención", "Por favor seleccione un título del desplegable para buscar.", Alert.AlertType.WARNING);
            return;
        }

        ObservableList<Pelicula> resultadoBusqueda = FXCollections.observableArrayList();

        for (Pelicula p : listaPeliculasO) {
            if (p.getTitulo().equalsIgnoreCase(tituloSeleccionado)) {
                resultadoBusqueda.add(p);
                break;
            }
        }

        tblPelicula.setItems(resultadoBusqueda);

        if (!resultadoBusqueda.isEmpty()) {
            tblPelicula.getSelectionModel().select(0);
        } else {
            mostrarAlerta("Sin resultados", "No se encontró la película seleccionada.", Alert.AlertType.INFORMATION);
        }
    }

    // Método para seleccionar y mostrar la imagen de la portoda
    @FXML
    private void seleccionarImagen(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Portada de Película");

        //Aceptar solo imágenes
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Imágenes (*.png, *.jpg, *.jpeg)", "*.png", "*.jpg", "*.jpeg");
        fileChooser.getExtensionFilters().add(filter);

        // Elegir la imagen
        File archivoSeleccionado = fileChooser.showOpenDialog(btnCargarPortada.getScene().getWindow());

        if (archivoSeleccionado != null) {
            try {
                // Para mostrar la imagen en la pantalla
                Image imagen = new Image(archivoSeleccionado.toURI().toString());
                imgPortada.setImage(imagen);

                // Convertir el archivo a byte[] para guardarlo en la BD como BLOB
                FileInputStream fis = new FileInputStream(archivoSeleccionado);
                bytesPortada = fis.readAllBytes();
                fis.close();

            } catch (IOException e) {
                mostrarAlerta("Error", "No se pudo cargar la imagen seleccionada.", Alert.AlertType.ERROR);
                e.printStackTrace();
            }
        }
    }

    // Método auxiliar para cuando se seleccione una pelicula se muestre en el formulario
    private void mostrarImagenEnPantalla(byte[] bytes) {
        if (bytes != null && bytes.length > 0) {
            ByteArrayInputStream by = new ByteArrayInputStream(bytes);
            Image imagen = new Image(by);
            imgPortada.setImage(imagen);
        } else {
            imgPortada.setImage(null); // por si no hay portada
        }
    }

    //Guarda la pelicula en la BD
    private void guardarPelicula(ActionEvent event) {

        if (txtTitulo.getText().trim().isEmpty() || DatePickerFecha.getValue() == null || cmboxClasificacion.getValue() == null || spnduracion.getValue() == null || bytesPortada == null || sinopsis.getText().trim().isEmpty() ) {
            mostrarAlerta("Campos incompletos", "Por favor completar todos los campos ", Alert.AlertType.WARNING);
            return;
        }
        Pelicula nueva = new Pelicula(
                txtCod.getText(),
                txtTitulo.getText().trim(),
                bytesPortada,
                DatePickerFecha.getValue(),
                spnduracion.getValue(),
                cmboxClasificacion.getValue(),
                sinopsis.getText().trim()
        );

        peliculaDAO.guardarPelicula(nueva);
        mostrarAlerta("Éxito", "Película guardada correctamente.", Alert.AlertType.INFORMATION);
        cargarDatos();
        limpiarCampos();

    }

    //Modifica la pelicula
    private void modificarPelicula(ActionEvent event) {
        if (peliculaSeleccionada == null) {
            mostrarAlerta("Error", "Seleccione una película de la tabla para actualizar.", Alert.AlertType.WARNING);
            return;
        }

        if (txtTitulo.getText().trim().isEmpty() || cmboxClasificacion.getValue() == null || DatePickerFecha.getValue() == null || spnduracion.getValue() == null || bytesPortada == null || sinopsis.getText().trim().isEmpty() ) {
            mostrarAlerta("Campos incompletos", "Por favor completar todos los campos ", Alert.AlertType.WARNING);
            return;
        }

        // Confirmación
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar Modificación");
        confirmacion.setHeaderText("Modificar película: " + peliculaSeleccionada.getIdPelicula());
        confirmacion.setContentText("¿Está seguro de que desea modificar esta película?.");

        if (confirmacion.showAndWait().get() == ButtonType.OK) {
            Pelicula editada = new Pelicula(
                    txtCod.getText(),
                    txtTitulo.getText().trim(),
                    bytesPortada,
                    DatePickerFecha.getValue(),
                    spnduracion.getValue(),
                    cmboxClasificacion.getValue(),
                    sinopsis.getText().trim()
            );

            peliculaDAO.modificarPelicula(editada);
            mostrarAlerta("Éxito", "Película actualizada correctamente.", Alert.AlertType.INFORMATION);
            cargarDatos();
            limpiarCampos();
        } else {

            mostrarAlerta("Error", "La película no fue modificada.", Alert.AlertType.INFORMATION);
        }


    }

    //Eliminar una pelicula seleccionada
    private void eliminarPelicula(ActionEvent event) {
        if (peliculaSeleccionada == null) {
            mostrarAlerta("Error", "Seleccione una película de la tabla para eliminar.", Alert.AlertType.WARNING);
            return;
        }

        // Confirmación
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar Eliminación");
        confirmacion.setHeaderText("Eliminar película: " + peliculaSeleccionada.getIdPelicula());
        confirmacion.setContentText("¿Está seguro de que desea eliminar esta película?.");

        if (confirmacion.showAndWait().get() == ButtonType.OK) {
            peliculaDAO.eliminarPelicula(peliculaSeleccionada.getIdPelicula());
            mostrarAlerta("Éxito", "Película eliminada correctamente.", Alert.AlertType.INFORMATION);
            cargarDatos();
            limpiarCampos();
        } else {

            mostrarAlerta("Error", "La película no fue eliminada.", Alert.AlertType.INFORMATION);
        }

    }

//Cargar los datos en la table
    private void cargarDatos() {
        // Carga la lista desde tu DAO hacia la lista Observable de JavaFX
        listaPeliculasO = FXCollections.observableArrayList(peliculaDAO.obtenerTodasLasPeliculas());
        tblPelicula.setItems(listaPeliculasO);
        cargarCombos();
    }

    //Objetivo: cargar los datos de la tabla en el formulario para actualizar
    private void mostrarDetallesPelicula(Pelicula pelicula) {
        peliculaSeleccionada = pelicula;
        if (pelicula != null) {
            txtCod.setText(pelicula.getIdPelicula());
            txtTitulo.setText(pelicula.getTitulo());
            DatePickerFecha.setValue(pelicula.getFechaEstreno());
            spnduracion.getValueFactory().setValue(pelicula.getDuracionPelicula());
            cmboxClasificacion.setValue(pelicula.getClasificacion());
            sinopsis.setText(pelicula.getSinopsis());

            bytesPortada = pelicula.getPortada();
            mostrarImagenEnPantalla(bytesPortada);

            btnGuardar.setDisable(true);
            btnActualizar.setDisable(false);
            btneliminar.setDisable(false);
            btnCancelarAccion.setDisable(false);
        } else {
            limpiarCampos();
        }
    }

    //Opciones predeterminadas de los combos box
    private void cargarCombos() {
        cmboxClasificacion.setItems(FXCollections.observableArrayList("G", "PG", "PG-13", "R", "NC-17"));
        // cmbBuscar.setItems(FXCollections.observableArrayList("Código", "Título", "Clasificación"));

       // Cargar todas las películas de la BD en el ComboBox de búsqueda
        List<Pelicula> peliculasBD = peliculaDAO.obtenerTodasLasPeliculas();
        List<String> titulos = new ArrayList<>();

        for (Pelicula p : peliculasBD) {
            titulos.add(p.getTitulo());
        }

        cmbBuscar.setItems(FXCollections.observableArrayList(titulos));
    }

    //Objetivo: Configurar los spinner con valores iniciales
    private void configurarSpinner() {
        spnduracion.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 300, 120, 5));
        spnduracion.setEditable(true);
    }

    //Objetivo: Muestra los datos en la tabla
    private void configurarTabla() {
        colID.setCellValueFactory(cellData -> cellData.getValue().idPeliculaProperty());
        colTitulo.setCellValueFactory(cellData -> cellData.getValue().tituloProperty());
        colFecha.setCellValueFactory(cellData -> cellData.getValue().fechaEstrenoProperty());
        colDuracion.setCellValueFactory(cellData -> cellData.getValue().duracionPeliculaProperty().asObject());
        colClasificacion.setCellValueFactory(cellData -> cellData.getValue().clasificacionProperty());
        colSinopsis.setCellValueFactory(cellData -> cellData.getValue().sinopsisProperty());
    }

    // Objetivo: Cancelar las acciones de actualizar y eliminar
    private void cancelarAccion(ActionEvent event) {
        tblPelicula.getSelectionModel().clearSelection();
        peliculaSeleccionada = null;
        limpiarCampos();
        cargarDatos();
    }

    //Objetivo: Limpiar los campos después de una acción
    private void limpiarCampos() {
        txtCod.setText(generateNextPeliculaId());
        txtTitulo.clear();
        DatePickerFecha.setValue(null);
        spnduracion.getValueFactory().setValue(120);
        cmboxClasificacion.getSelectionModel().clearSelection();
        sinopsis.clear();
        cmbBuscar.getSelectionModel().clearSelection();

        btnGuardar.setDisable(false);
        btnActualizar.setDisable(true);
        btneliminar.setDisable(true);
        btnCancelarAccion.setDisable(true);
        imgPortada.setImage(null);
        bytesPortada = null;
    }

    //Objetivo:  Método genérico para las alertas
    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private String generateNextPeliculaId() {
        return String.format("PEL-%03d", generarId);
    }

    private void setInitialPeliculaId() {
        int maxIdInDB = peliculaDAO.obtenerMaxIdPelicula();
        generarId = maxIdInDB + 1;
        txtCod.setText(generateNextPeliculaId());
    }

}
