package com.example.model;

import com.example.apptiendita.MainApplication;
import com.example.config.StyleConfig;
import com.example.enumeraciones.Category;
import com.example.interfaces.Keyable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.io.IOException;

public class CardProduct extends CardDisplayable {

    @FXML
    private VBox container;


    @Override
    public <T extends Keyable> void action(T item) {
        try {
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("cardInfoProduct.fxml"));
            HBox footerNode = loader.load();

            Label categoryFooter = (Label) footerNode.lookup("#category");
            Label priceFooter = (Label) footerNode.lookup("#price");

            priceFooter.setText("$0.0");

            if (item instanceof Product product) {
                Category cat = product.getCategoria();
                categoryFooter.setText(cat.toString());

                Color baseColor = StyleConfig.getColorByCategory(cat);
                Color adjustedColor = baseColor.interpolate(Color.BLACK, 0.3);
                categoryFooter.setTextFill(adjustedColor);

                Color backgroundColor = baseColor.interpolate(Color.WHITE, 0.92);
                categoryFooter.setBackground(new Background(
                        new BackgroundFill(backgroundColor, new CornerRadii(7), Insets.EMPTY)
                ));
            }

            container.getChildren().add(footerNode);

        } catch (IOException e) {
            throw new RuntimeException("Error al cargar el footer", e);
        }
    }

}
