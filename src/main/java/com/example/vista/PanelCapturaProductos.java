package com.example.vista;

import com.example.model.ViewOperable;
import com.example.model.Product;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.List;

import static com.example.utility.FormUtility.formatText;
import static com.example.utility.FormUtility.message;


public class PanelCapturaProductos extends Application {
        private List<Product> catalogo;
        private final ViewOperable PRODUCT_CONTROLLER;
        Text titulo;
        TextField nombre;
        TextField codigoBarrasField;
        TextField nombreField;
        TextField marcaField;
        TextArea descripcionArea;
        ComboBox<String> categoriaCombo;
        ComboBox<String> unidadMedidaCombo;
        TextField contenidoField;
        ComboBox<String> presentacionCombo;
        CheckBox activoCheckBox;
        TextField imagenUrlField;
        Button guardarButton;
        Button limpiarButton;

    public PanelCapturaProductos(List<Product> catalogo, ViewOperable PRODUCT_CONTROLLER) {
        this.PRODUCT_CONTROLLER = PRODUCT_CONTROLLER;
        this.catalogo = catalogo;
    }

    @Override
    public void start(Stage primaryStage) {

        titulo = new Text("Captura de productos");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        // Crear campos del formulario
        codigoBarrasField = new TextField();
        nombreField = new TextField();
        marcaField = new TextField();
        descripcionArea = new TextArea();
        categoriaCombo = new ComboBox<>();
        categoriaCombo.getItems().addAll("Bebidas", "Comestibles", "Electrónicos", "Otros");
        unidadMedidaCombo = new ComboBox<>();
        unidadMedidaCombo.getItems().addAll("Unidades", "Kilogramos", "Litros", "Gramos");
        contenidoField = new TextField();
        presentacionCombo = new ComboBox<>();
        presentacionCombo.getItems().addAll("Botella", "Caja", "Bolsa", "Lata");
        activoCheckBox = new CheckBox();
        imagenUrlField = new TextField();

        codigoBarrasField.setOnAction(e -> {
            String codigo = codigoBarrasField.getText().trim();

            if (codigo.isEmpty()) {
                message("Alerta","Debe escribir algo" );
                codigoBarrasField.requestFocus();
                return;
            }

            if (!codigo.matches("\\d{15}")) {

                message("Alerta","El código de barras debe tener exactamente 15 dígitos numéricos" );
                codigoBarrasField.clear();
                codigoBarrasField.requestFocus();
                return;
            }

            if (catalogo.contains(new Product(codigo))) {
                message("Alerta","El código de barras ya existe" );
                codigoBarrasField.clear();
                codigoBarrasField.requestFocus();
                return;
            }

            nombreField.requestFocus();
        });

        nombreField.setOnAction(e -> {
            if (nombreField.getText().isEmpty()) {
                message("Alerta","Debe ingresar el nombre" );
                return;
            }
            nombreField.setText(formatText(nombreField.getText()));

            marcaField.requestFocus();
        });

        marcaField.setOnAction(e -> {
            if (marcaField.getText().isEmpty()) {
                message("Alerta","Debe ingresar la marca" );
                return;
            }
            marcaField.setText(formatText(marcaField.getText()));

            descripcionArea.requestFocus();
        });

        descripcionArea.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case ENTER -> verificarDescripcion();
            }
        });

        categoriaCombo.setOnAction(e -> {

            unidadMedidaCombo.requestFocus();
            unidadMedidaCombo.show();
            Platform.runLater(() -> unidadMedidaCombo.getSkin().getNode().requestFocus());
        });
        unidadMedidaCombo.setOnAction(e -> {

            contenidoField.requestFocus();
        });

        contenidoField.setOnAction(e -> {
            if (contenidoField.getText().isEmpty()) {
                message("Alerta","Debe ingresar el contenido" );
                return;
            }
            contenidoField.setText(formatText(contenidoField.getText()));


            presentacionCombo.requestFocus();
            presentacionCombo.show();
            Platform.runLater(() -> presentacionCombo.getSkin().getNode().requestFocus());
        });

        presentacionCombo.setOnAction(e ->{

            activoCheckBox.requestFocus();

        });
        imagenUrlField.setOnAction(e -> guardarButton.requestFocus());

        // Botones
        guardarButton = new Button("Guardar");
        limpiarButton = new Button("Limpiar");

        // Acciones de los botones
        guardarButton.setOnAction(e -> {
            if(cajasLLenas()){
                if (catalogo.contains(new Product(codigoBarrasField.getText()))){
                    message("Alerta","El codigo de barras ya existe " );
                }else{

                    Product product = new Product(codigoBarrasField.getText());
                    product.setNombre(nombreField.getText());
                    System.out.println(nombreField.getText());
                    product.setMarca(marcaField.getText());

                    System.out.println(marcaField.getText());
                    product.setDescripcion(descripcionArea.getText());


                    System.out.println(descripcionArea.getText());
                    product.setCategoria(categoriaCombo.getValue());

                    System.out.println(categoriaCombo.getValue());
                    product.setUnidadMedida(unidadMedidaCombo.getValue());

                    System.out.println(unidadMedidaCombo.getValue());
                    product.setContenido(contenidoField.getText());
                    product.setPresentacion(presentacionCombo.getValue());
                    product.setActivo(activoCheckBox.isSelected());
                    product.setImagenUrl(imagenUrlField.getText());

                    catalogo.add(product);
                    message("Alerta", "Producto agregado con exito, el editar se a actualizada" );
                    PRODUCT_CONTROLLER.setCatalogFiltered(catalogo);
                    PRODUCT_CONTROLLER.setupPagination(catalogo);
                }
            }else {
                message("Alerta", "Debe llenar los campos ");
                codigoBarrasField.requestFocus();
            }
        });



        limpiarButton.setOnAction(e -> {
            // Limpiar todos los campos
            codigoBarrasField.clear();
            nombreField.clear();
            marcaField.clear();
            descripcionArea.clear();
            categoriaCombo.getSelectionModel().clearSelection();
            unidadMedidaCombo.getSelectionModel().clearSelection();
            contenidoField.clear();
            presentacionCombo.getSelectionModel().clearSelection();
            activoCheckBox.setSelected(false);
            imagenUrlField.clear();
        });

        // Crear el layout del formulario
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 20, 20));

        // Agregar campos al grid
        grid.add(new Label("Código de Barras:"), 0, 0);
        grid.add(codigoBarrasField, 1, 0);
        grid.add(new Label("Nombre:"), 0, 1);
        grid.add(nombreField, 1, 1);
        grid.add(new Label("Marca:"), 0, 2);
        grid.add(marcaField, 1, 2);
        grid.add(new Label("Descripción:"), 0, 3);
        grid.add(descripcionArea, 1, 3);
        grid.add(new Label("Categoría:"), 0, 4);
        grid.add(categoriaCombo, 1, 4);
        grid.add(new Label("Unidad de Medida:"), 0, 5);
        grid.add(unidadMedidaCombo, 1, 5);
        grid.add(new Label("Contenido:"), 0, 6);
        grid.add(contenidoField, 1, 6);
        grid.add(new Label("Presentación:"), 0, 7);
        grid.add(presentacionCombo, 1, 7);
        grid.add(new Label("Disponible para la venta:"), 0, 8);
        grid.add(activoCheckBox, 1, 8);
        grid.add(new Label("URL de la Imagen:"), 0, 9);
        grid.add(imagenUrlField, 1, 9);

        // Botones en un HBox
        HBox botonesBox = new HBox(10, guardarButton, limpiarButton);
        botonesBox.setPadding(new Insets(20, 0, 0, 0));

        // Container principal
        VBox root = new VBox(20, titulo, grid, botonesBox);
        root.setPadding(new Insets(20));

        // Crear la escena
        Scene scene = new Scene(root, 600, 600);
        primaryStage.setTitle("Captura de productos");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void verificarDescripcion() {
        if (descripcionArea.getText().isEmpty()) {
            message("Alerta","Debe ingresar el contenido" );
            return;
        }
        System.out.println(descripcionArea.getText());
        descripcionArea.setText(formatText(descripcionArea.getText().trim()));

        categoriaCombo.requestFocus();
        categoriaCombo.show();

        Platform.runLater(() -> categoriaCombo.getSkin().getNode().requestFocus());
    }

    private boolean cajasLLenas(){
        return  !codigoBarrasField.getText().isEmpty() &&
                !nombreField.getText().isEmpty() &&
                !marcaField.getText().isEmpty() &&
                !descripcionArea.getText().isEmpty() &&
                !unidadMedidaCombo.getValue().isEmpty() &&
                !contenidoField.getText().isEmpty() &&
                !presentacionCombo.getValue().isEmpty() &&
                !imagenUrlField.getText().isEmpty();
    }



}
