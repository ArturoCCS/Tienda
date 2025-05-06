package com.example.vista;

import com.example.model.Product;
import com.example.model.ViewOperable;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.List;

import static com.example.utility.FormUtility.formatText;
import static com.example.utility.FormUtility.message;

public class PanelEditarProducto {
    private final ViewOperable PRODUCT_CONTROLLER;
    private final Product producto;
    private final List<Product> catalogo;

    private TextField nombreField;
    private TextField marcaField;
    private TextArea descripcionArea;
    private ComboBox<String> categoriaCombo;
    private ComboBox<String> unidadMedidaCombo;
    private TextField contenidoField;
    private ComboBox<String> presentacionCombo;
    private CheckBox activoCheckBox;
    private TextField imagenUrlField;

    public PanelEditarProducto(Product producto, List<Product> catalogo, ViewOperable PRODUCT_CONTROLLER) {
        this.producto = producto;
        this.catalogo = catalogo;
        this.PRODUCT_CONTROLLER = PRODUCT_CONTROLLER;
    }

    public void mostrarVentana() {
        Stage stage = new Stage();
        Text titulo = new Text("Editar producto");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        // Campos
        TextField codigoBarrasField = new TextField(producto.getKey());
        codigoBarrasField.setDisable(true);
        nombreField = new TextField(producto.getNombre());
        marcaField = new TextField(producto.getMarca());
        descripcionArea = new TextArea(producto.getDescripcion());

        categoriaCombo = new ComboBox<>();
        categoriaCombo.getItems().addAll("Bebidas", "Comestibles", "Electrónicos", "Otros");
        categoriaCombo.setValue(producto.getCategoria());

        unidadMedidaCombo = new ComboBox<>();
        unidadMedidaCombo.getItems().addAll("Unidades", "Kilogramos", "Litros", "Gramos");
        unidadMedidaCombo.setValue(producto.getUnidadMedida());

        contenidoField = new TextField(producto.getContenido());
        presentacionCombo = new ComboBox<>();
        presentacionCombo.getItems().addAll("Botella", "Caja", "Bolsa", "Lata");
        presentacionCombo.setValue(producto.getPresentacion());

        activoCheckBox = new CheckBox();
        activoCheckBox.setSelected(producto.isActivo());

        imagenUrlField = new TextField(producto.getImageUrl());

        Button guardarButton = new Button("Guardar Cambios");
        Button eliminarButton = new Button("Eliminar Producto");
        Button cerrarButton = new Button("Cerrar");

        guardarButton.setOnAction(e -> {
            if (!camposLlenos()) {
                message("Alerta", "Debe llenar todos los campos");
                return;
            }

            producto.setNombre(formatText(nombreField.getText()));
            producto.setMarca(formatText(marcaField.getText()));
            producto.setDescripcion(formatText(descripcionArea.getText()));
            producto.setCategoria(categoriaCombo.getValue());
            producto.setUnidadMedida(unidadMedidaCombo.getValue());
            producto.setContenido(formatText(contenidoField.getText()));
            producto.setPresentacion(presentacionCombo.getValue());
            producto.setActivo(activoCheckBox.isSelected());
            producto.setImagenUrl(imagenUrlField.getText());

            message("Confirmación", "Producto actualizado correctamente");

            PRODUCT_CONTROLLER.setLastPageIndex(-1);
            PRODUCT_CONTROLLER.setCatalogFiltered(catalogo);
            PRODUCT_CONTROLLER.setupPagination(catalogo);
            stage.close();
        });


        eliminarButton.setOnAction(e -> {
            if (catalogo.remove(producto)) {
                message("Confirmación", "Producto eliminado correctamente");
                PRODUCT_CONTROLLER.setLastPageIndex(-1);
                PRODUCT_CONTROLLER.setCatalogFiltered(catalogo);
                PRODUCT_CONTROLLER.setupPagination(catalogo);
                stage.close();
            } else {
                message("Error", "No se pudo eliminar el producto");
            }
        });

        cerrarButton.setOnAction(e -> stage.close());

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

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

        HBox botonesBox = new HBox(10, guardarButton, eliminarButton, cerrarButton);
        VBox root = new VBox(20, titulo, grid, botonesBox);
        root.setPadding(new Insets(20));

        Scene scene = new Scene(root, 600, 600);
        stage.setTitle("Editar producto");
        stage.setScene(scene);
        stage.show();
    }

    private boolean camposLlenos() {
        return !nombreField.getText().isEmpty() &&
                !marcaField.getText().isEmpty() &&
                !descripcionArea.getText().isEmpty() &&
                categoriaCombo.getValue() != null &&
                unidadMedidaCombo.getValue() != null &&
                !contenidoField.getText().isEmpty() &&
                presentacionCombo.getValue() != null &&
                !imagenUrlField.getText().isEmpty();
    }
}
