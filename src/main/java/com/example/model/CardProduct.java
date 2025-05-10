package com.example.model;

import com.example.apptiendita.MainApplication;
import com.example.config.StyleConfig;
import com.example.enumeraciones.Category;
import com.example.interfaces.Keyable;
import com.example.vista.PanelEditarProducto;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.io.IOException;
import java.util.List;

public class CardProduct extends CardDisplayable {

    @FXML
    private VBox container;

    @Override
    public <T extends Keyable> void action(T item, String modo) {
        try {
            if ("Venta".equals(modo)) {
                FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("cardInfoVenta.fxml"));
                HBox footerNode = loader.load();

                Label categoryFooter = (Label) footerNode.lookup("#category");
                Label priceFooter = (Label) footerNode.lookup("#price");

                if (item instanceof Product product) {
                    Category cat = product.getCategoria();
                    categoryFooter.setText(cat.toString());
                    priceFooter.setText("$" + product.getPrecio());

                    Color baseColor = StyleConfig.getColorByCategory(cat);
                    Color adjustedColor = baseColor.interpolate(Color.BLACK, 0.3);
                    categoryFooter.setTextFill(adjustedColor);

                    Color backgroundColor = baseColor.interpolate(Color.WHITE, 0.92);
                    categoryFooter.setBackground(new Background(
                            new BackgroundFill(backgroundColor, new CornerRadii(7), Insets.EMPTY)
                    ));
                }

                container.getChildren().add(footerNode);

            } else if ("Inventario".equals(modo)) {
                FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("cardInfoInventario.fxml"));
                HBox footerNode = loader.load();

                Label disponibleLabel = (Label) footerNode.lookup("#disponible");
                Button editButton = (Button) footerNode.lookup("#edit");

                editButton.getStyleClass().add("button-edit");
                editButton.getStyleClass().remove("button");

                Tooltip tooltip = new Tooltip("Editar producto");
                tooltip.setShowDelay(Duration.millis(100));
                tooltip.getStyleClass().add("tooltip");
                Tooltip.install(editButton, tooltip);

                if (item instanceof Product product) {
                    editButton.setOnAction(e -> {
                        new PanelEditarProducto(product, catalogo, controlador).mostrarVentana();
                    });
                    boolean activo = product.isActivo();
                    disponibleLabel.setText(activo ? "Stock: "  + product.getCantidadDisponible() : "Resurtir");

                    Color color = activo ? Color.web("#32CD32") : Color.web("#FF6347"); // verde lima / rojo tomate
                    Color adjustedTextColor = color.interpolate(Color.BLACK, 0.8);
                    Color backgroundColor = color.interpolate(Color.WHITE, 0.92);


                    disponibleLabel.setTextFill(adjustedTextColor);
                    disponibleLabel.setBackground(new Background(
                            new BackgroundFill(backgroundColor, new CornerRadii(7), Insets.EMPTY)
                    ));
                }

                container.getChildren().add(footerNode);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al cargar el footer", e);
        }
    }

    private List<Product> catalogo;
    private ViewOperable controlador;

    public void setContext(List<Product> catalogo, ViewOperable controlador) {
        this.catalogo = catalogo;
        this.controlador = controlador;
    }

}
