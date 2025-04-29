package com.example.apptiendita;

import com.example.interfaces.Displayable;
import com.example.interfaces.Keyable;
import com.example.model.Product;
import com.example.model.ViewOperable;
import com.example.vista.PanelCapturaProductos;
import com.example.vista.PanelEditarProducto;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.CacheHint;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.utility.FormUtility.message;

public class ViewProductController extends ViewOperable {

    @FXML
    private Button agregarTab;
    @FXML
    private Label label;

    @FXML
    private Button inventarioTab;

    @FXML
    private Button ventaTab;

    @FXML
    private TextField txtSearch;

    private ShoppingCartController shoppingCartController;

    @FXML
    protected void initialize() {
        //Establecemos el modo por defecto "Inventario"
        setModo("Inventario");
        setupPagination(catalog);
        super.setupScrollListener();

        getContainer().setCache(true);
        getContainer().setCacheHint(CacheHint.SPEED);


        getScroll().viewportBoundsProperty().addListener((obs, oldVal, newVal) -> {
            getContainer().setMinWidth(newVal.getWidth());
            getContainer().setPrefWidth(newVal.getWidth());
            getContainer().setMaxWidth(newVal.getWidth());
        });


        Platform.runLater(() -> getContainer().requestFocus());
        txtSearch.setOnAction(this::search);

        List<Button> tabs = List.of(inventarioTab, ventaTab, agregarTab);

        for (Button tab : tabs) {
            tab.getStyleClass().remove("button");
            if (tab.equals(agregarTab)) {
                Tooltip tooltip = new Tooltip("Agregar nuevo producto al editar");
                tooltip.setShowDelay(Duration.millis(100));
                tooltip.getStyleClass().add("tooltip");
                Tooltip.install(agregarTab, tooltip);
                continue;
            }
            tab.setOnMouseClicked(e -> {
                for (Button t : tabs) {
                    t.getStyleClass().remove("selected");

                }
                tab.getStyleClass().add("selected");
            });
        }

        inventarioTab.getStyleClass().add("selected");
    }

    @FXML
    private void search(ActionEvent event) {
        String query = txtSearch.getText().toLowerCase().trim();

        if (super.catalogFiltered == null){
            if (super.catalog == null || catalog.isEmpty()) {
                return;
            }
            catalogFiltered = catalog;
        }

        List<? extends Keyable> filteredData = catalogFiltered.stream()
                .filter(product -> product instanceof Product)
                .map(product -> (Product) product)
                .filter(product -> {
                    return product.getNombre().toLowerCase().contains(query) ||
                            product.getDescripcion().toLowerCase().contains(query) ||
                            product.getCategoria().toLowerCase().contains(query);
                }).collect(Collectors.toList());

        if (filteredData.isEmpty()) {
            message("Sin resultados","No se encontraron productos que coincidan con '" + query + "'.");
        } else {
            resetGrid(null);
            setupPagination(filteredData);
        }

        txtSearch.clear();
        getGrid().requestFocus();
    }


    @SuppressWarnings("unchecked")
    @Override
    public void insertAction(AnchorPane anchorPane, Displayable data) {
        if (getModo().equals("Inventario")) {
            if (data instanceof Product producto && catalog != null) {
                anchorPane.setOnMouseClicked(event -> {
                    new PanelEditarProducto(producto, (List<Product>) catalog, this).mostrarVentana();
                });
            }
        } else if (getModo().equals("Venta")) {
            if (shoppingCartController != null && data instanceof Product) {
                anchorPane.setOnMouseClicked(event -> shoppingCartController.modoVenta(data));
            }
        }
    }





    @FXML
    private void handleInventario(ActionEvent event) {

        this.setModo("Inventario");

        agregarTab.setVisible(true);
        this.resetGrid(null);
        this.setupPagination(catalog);
        getAnchorPaneVenta().getChildren().clear();
    }

    @FXML
    private void handleModoVenta(ActionEvent event) throws IOException {

        this.setModo("Venta");

        agregarTab.setVisible(false);
        String query = "Bebidas".toLowerCase().trim();
        if (catalog == null || catalog.isEmpty()) {
            return;
        }

        List<Product> filteredData = catalog.stream()
                .filter(product -> product instanceof Product)
                .map(product -> (Product) product)
                .filter(product ->
                        product.getCategoria().toLowerCase().contains(query))
                .collect(Collectors.toList());

        if (filteredData.isEmpty()) {
            message("Sin resultados", "No se encontraron productos que coincidan con '" + query + "'.");
        } else {

            FXMLLoader loader = new FXMLLoader(ViewProductController.class.getResource("shoppingCart.fxml"));

            VBox shoppingCart = loader.load();

            shoppingCartController = loader.getController();

            this.resetGrid(filteredData);
            this.setupPagination(filteredData);
            getAnchorPaneVenta().getChildren().setAll(shoppingCart);
        }

    }
    @FXML
    private void handleAgregar(ActionEvent event){
        Stage stageCaptura = new Stage();
        stageCaptura.initModality(javafx.stage.Modality.APPLICATION_MODAL);
        @SuppressWarnings("unchecked")
        PanelCapturaProductos panelCaptura = new PanelCapturaProductos((List<Product>) catalog, this);
        panelCaptura.start(stageCaptura);
    }
}
