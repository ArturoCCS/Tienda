package com.example.apptiendita;

import com.example.interfaces.Displayable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.io.IOException;

import static com.example.utility.FormUtility.message;

public class ShoppingCartController {

    @FXML
    private GridPane contentItems;

    @FXML
    private Label emptyMessage;


    private int row = 0;


    @FXML
    private void initialize() {}


    public void modoVenta(Displayable data) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("cardProductShopping.fxml"));
            AnchorPane anchorPane = loader.load();
            CardController cardController = loader.getController();

            cardController.setProducto(data);

            anchorPane.getStyleClass().add("responsive-card");

            contentItems.add(anchorPane, 0, row);

            GridPane.setMargin(anchorPane, new Insets(10, 10, 10, 10));
            row++;
            emptyMessage.setVisible(false);
        } catch (IOException e) {
            message("Error", e.getMessage());
        }
    }


}
