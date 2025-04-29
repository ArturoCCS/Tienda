package com.example.apptiendita;

import com.example.interfaces.Keyable;
import com.example.interfaces.Operable;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.*;

import static com.example.utility.FormUtility.message;

public class MainController {

    @FXML
    private Button btnHome;

    @FXML
    private Button closeBtn;

    @FXML
    private VBox customBar;

    @FXML
    private Button maximizeBtn;

    @FXML
    private Button minimizeBtn;

    @FXML
    private AnchorPane anchorPaneCenter;

    @FXML
    private AnchorPane anchorPaneVenta;

    private Map<String, Operable<? extends Keyable>> catalogs;

    private Parent vistaProductos;

    private Task<Parent> preloadTask;

    private Button selectedButton;

    private ViewProductController productController;

    @FXML
    public void initialize() {

        btnHome.setOnAction(e -> {
            selectButton(btnHome);
            oyenteCatalogo(e);
        });

        selectButton(btnHome);

    }


    private void selectButton(Button button) {
        if (selectedButton != null) {
            selectedButton.getStyleClass().remove("selected-button");
        }
        button.getStyleClass().add("selected-button");
        selectedButton = button;
    }

    void preloadCatalogView() {
        preloadTask = new Task<>() {
            @Override
            protected Parent call() throws Exception {
                productController = new ViewProductController();
                productController.setDataList(catalogs.get("productos").getAll());

                FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("viewProducts.fxml"));
                loader.setController(productController);

                return loader.load();
            }
        };

        preloadTask.setOnSucceeded(e -> vistaProductos = preloadTask.getValue());
        preloadTask.setOnFailed(e -> {
            message("Error", preloadTask.getException().getMessage());

            System.out.println(preloadTask.getException().getMessage());
        });

        new Thread(preloadTask).start();
    }

    @FXML
    void oyenteCatalogo(ActionEvent event) {
        if (vistaProductos != null) {
            mostrarEnAnchorPane(vistaProductos);
            return;
        }

        ProgressIndicator progress = new ProgressIndicator();
        mostrarEnAnchorPane(progress);

        if (preloadTask == null) {
            preloadCatalogView();
        }

        preloadTask.setOnSucceeded(e -> {
            vistaProductos = preloadTask.getValue();
            mostrarEnAnchorPane(vistaProductos);
        });

        preloadTask.setOnFailed(e -> {
            message("Error", preloadTask.getException().getMessage());
            System.out.println(preloadTask.getException().getMessage());
        });
    }

    private void mostrarEnAnchorPane(Parent nodo) {
        anchorPaneCenter.getChildren().clear();

        if (nodo instanceof ProgressIndicator progress) {
            progress.setPrefSize(60, 60);

            StackPane wrapper = new StackPane(progress);
            AnchorPane.setTopAnchor(wrapper, 0.0);
            AnchorPane.setBottomAnchor(wrapper, 0.0);
            AnchorPane.setLeftAnchor(wrapper, 0.0);
            AnchorPane.setRightAnchor(wrapper, 0.0);

            anchorPaneCenter.getChildren().add(wrapper);
        } else {
            AnchorPane.setTopAnchor(nodo, 0.0);
            AnchorPane.setBottomAnchor(nodo, 0.0);
            AnchorPane.setLeftAnchor(nodo, 0.0);
            AnchorPane.setRightAnchor(nodo, 0.0);
            productController.setAnchorPaneVenta(anchorPaneVenta);
            anchorPaneCenter.getChildren().add(nodo);
        }
    }



    public void setCatalogs(Map<String, Operable<? extends Keyable>> catalogs) {
        this.catalogs = catalogs;
    }
}
