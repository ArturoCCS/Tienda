package com.example.vista;

import com.example.model.Product;
import com.example.model.Resurtido;
import com.example.model.ViewOperable;
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


public class PanelResurtir extends Application {
        private List<Resurtido> catalogo;
        private final ViewOperable PRODUCT_CONTROLLER;
        Text titulo;
        TextField codigoBarrasField;
        TextField cantidadField;
        TextField mesField;
        Button guardarButton;
        Button limpiarButton;

    public PanelResurtir(List<Resurtido> catalogo, ViewOperable PRODUCT_CONTROLLER) {
        this.PRODUCT_CONTROLLER = PRODUCT_CONTROLLER;
        this.catalogo = catalogo;
    }

    @Override
    public void start(Stage primaryStage) {

        titulo = new Text("Captura de productos");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        codigoBarrasField = new TextField();
        cantidadField = new TextField();
        mesField = new TextField();


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



            cantidadField.requestFocus();
        });

        cantidadField.setOnAction(e -> {
            if (cantidadField.getText().isEmpty()) {
                message("Alerta","Debe ingresar una cantidad" );
                return;
            }
            cantidadField.setText(formatText(cantidadField.getText()));

            mesField.requestFocus();
        });

        mesField.setOnAction(e -> {
            if (mesField.getText().isEmpty()) {
                message("Alerta","Debe ingresar el mes" );
                return;
            }
            mesField.setText(formatText(mesField.getText()));

            mesField.requestFocus();
        });

        guardarButton = new Button("Guardar");
        limpiarButton = new Button("Limpiar");

        guardarButton.setOnAction(e -> {
            if (cajasLLenas()) {
                String codigo = codigoBarrasField.getText();
                int cantidad = Integer.parseInt(cantidadField.getText());
                String mes = mesField.getText();

                Resurtido nuevo = new Resurtido(codigo);

                int index = catalogo.indexOf(nuevo);

                if (index != -1) {
                    Resurtido existente = catalogo.get(index);
                    existente.setCantidad(existente.getCantidad() + cantidad);
                    existente.setMes(mes);
                } else {
                    nuevo.setCantidad(cantidad);
                    nuevo.setMes(mes);
                    catalogo.add(nuevo);
                }

                message("Alerta", "Resurtido agregado con éxito o actualizado.");
                PRODUCT_CONTROLLER.setCatalogFiltered(catalogo);
                PRODUCT_CONTROLLER.setupPagination(catalogo);
            } else {
                message("Alerta", "Debe llenar los campos.");
                codigoBarrasField.requestFocus();
            }
        });



        limpiarButton.setOnAction(e -> {
            codigoBarrasField.clear();
            cantidadField.clear();
            mesField.clear();
        });

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 20, 20));

        grid.add(new Label("Código de Barras:"), 0, 0);
        grid.add(codigoBarrasField, 1, 0);
        grid.add(new Label("Cantidad:"), 0, 1);
        grid.add(cantidadField, 1, 1);
        grid.add(new Label("Mes:"), 0, 2);
        grid.add(mesField, 1, 2);

        HBox botonesBox = new HBox(10, guardarButton, limpiarButton);
        botonesBox.setPadding(new Insets(20, 0, 0, 0));

        VBox root = new VBox(20, titulo, grid, botonesBox);
        root.setPadding(new Insets(20));

        Scene scene = new Scene(root, 600, 600);
        primaryStage.setTitle("Captura de resurtido");
        primaryStage.setScene(scene);
        primaryStage.show();
    }



    private boolean cajasLLenas(){
        return  !codigoBarrasField.getText().isEmpty() &&
                !cantidadField.getText().isEmpty() &&
                !mesField.getText().isEmpty();
    }



}
