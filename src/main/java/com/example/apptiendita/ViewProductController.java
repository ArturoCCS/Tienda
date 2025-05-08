package com.example.apptiendita;

import animatefx.animation.Pulse;
import animatefx.animation.SlideInLeft;
import animatefx.animation.SlideOutRight;
import com.example.interfaces.Displayable;
import com.example.interfaces.Keyable;
import com.example.model.Product;
import com.example.model.Resurtido;
import com.example.model.ViewOperable;
import com.example.vista.PanelCapturaProductos;
import com.example.vista.PanelEditarProducto;
import com.example.vista.PanelResurtir;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.utility.AnimationHelper.applyHoverAnimation;
import static com.example.utility.FormUtility.message;

public class ViewProductController extends ViewOperable {

    @FXML
    private Button agregarTab;

    @FXML
    private VBox banner;

    @FXML
    private Rectangle bannerClip;

    @FXML
    private Circle circulo;

    @FXML
    private VBox container;

    @FXML
    private ImageView figura1;

    @FXML
    private ImageView figura2;

    @FXML
    private ImageView figura3;

    @FXML
    private ImageView figura4;

    @FXML
    private VBox homeItems;

    @FXML
    private ImageView icon;

    @FXML
    private ImageView imageView;

    @FXML
    private Button inventarioTab;

    @FXML
    private TextField txtSearch;

    @FXML
    private Button ventaTab;

    @FXML
    private Button resurtirTab;

    private List<? extends Keyable> resurtidos;

    private ShoppingCartController shoppingCartController;

    private AnchorPane anchorPaneVenta;

    private VBox shoppingCart;

    public void setAnchorPaneVenta(AnchorPane anchorPaneVenta) {
        this.anchorPaneVenta = anchorPaneVenta;
    }

    public void setCatalogoResurtidos(List<? extends Keyable> resurtidos) {
        this.resurtidos = resurtidos;
    }

    @FXML
    private void initialize() {
        //Establecemos el modo por defecto "Inventario"
        setModo("Inventario");
        setDataList(getCatalog());
        super.setupScrollListener();

        configBanner();
        
        setupPagination(getCatalog());

        getContainer().setCache(true);
        getContainer().setCacheHint(CacheHint.SPEED);


        getScroll().viewportBoundsProperty().addListener((obs, oldVal, newVal) -> {
            getContainer().setMinWidth(newVal.getWidth());
            getContainer().setPrefWidth(newVal.getWidth());
            getContainer().setMaxWidth(newVal.getWidth());
        });


        Platform.runLater(() -> getContainer().requestFocus());
        txtSearch.setOnAction(this::search);

        List<Button> tabs = List.of(inventarioTab, ventaTab, resurtirTab, agregarTab);

        for (Button tab : tabs) {
            applyHoverAnimation(tab);
            tab.getStyleClass().remove("button");
            if (tab.equals(agregarTab)) {
                Tooltip tooltip = new Tooltip("Agregar nuevo producto");
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

    private void configBanner() {
        bannerClip.widthProperty().bind(banner.widthProperty());
        bannerClip.heightProperty().bind(banner.heightProperty());

        ColorInput colorInputFiguras = new ColorInput(
                0, 0,
                icon.getImage().getWidth(),
                icon.getImage().getHeight(),
                Color.web("#b8d499")
        );

        Blend blend2 = new Blend(
                BlendMode.SRC_ATOP,
                null,
                colorInputFiguras
        );

        ColorInput colorInputImageView = new ColorInput(
                0, 0,
                imageView.getImage().getWidth(),
                imageView.getImage().getHeight(),
                Color.web("#ffffff")
        );

        Blend blend = new Blend(
                BlendMode.SRC_ATOP,
                null,
                colorInputImageView
        );

        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.web("#b8d499"));
        dropShadow.setRadius(20);
        dropShadow.setSpread(0.01);
        dropShadow.setOffsetX(0);
        dropShadow.setOffsetY(0.1);

        dropShadow.setInput(blend);
        imageView.setEffect(dropShadow);

        icon.setEffect(blend2);
        figura1.setEffect(blend2);
        figura2.setEffect(blend2);
        figura3.setEffect(blend2);
        figura4.setEffect(blend2);
        circulo.setFill(Color.web("#b8d499"));
    }



    @FXML
    private void search(ActionEvent event) {
        String query = txtSearch.getText().toLowerCase().trim();

        if (super.getCatalogFiltered() == null){
            if (super.getCatalog() == null || super.getCatalog().isEmpty()) {
                return;
            }
            setCatalogFiltered(getCatalog());
        }

        List<? extends Keyable> filteredData = getCatalogFiltered().stream()
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
            setLastPageIndex(-1);
            setCatalogFiltered(filteredData);
            setupPagination(filteredData);
        }

        txtSearch.clear();
        getGrid().requestFocus();
    }


    @SuppressWarnings("unchecked")
    @Override
    public void insertAction(AnchorPane anchorPane, Displayable data) {
        applyHoverAnimation(anchorPane);
        anchorPane.setOnMousePressed(e -> {
            anchorPane.setUserData(new double[]{e.getSceneX(), e.getSceneY()});
        });

        anchorPane.setOnMouseReleased(e -> {
            double[] start = (double[]) anchorPane.getUserData();
            double dx = Math.abs(e.getSceneX() - start[0]);
            double dy = Math.abs(e.getSceneY() - start[1]);

            double movementThreshold = 10;

            if (dx < movementThreshold && dy < movementThreshold) {

                if (getModo().equals("Inventario") && data instanceof Product producto) {
                    new PanelEditarProducto(producto, (List<Product>) getCatalog(), this).mostrarVentana();
                } else if (getModo().equals("Venta") && data instanceof Product) {
                    // Verificar si ya fue seleccionado
                    Object flag = anchorPane.getProperties().get("seleccionado");
                    if (flag != null && (boolean) flag) {
                        return; // Ya fue seleccionado antes, no hacer nada
                    }

                    shoppingCartController.modoVenta(data);
                    anchorPane.getProperties().put("seleccionado", true); // Marcar como seleccionado
                }

            }
        });

    }



    @FXML
    private void handleModoVenta(ActionEvent event) throws IOException {

        if(getModo().equals("Venta"))
            return;

        getGrid().setDisable(true);
        agregarTab.setVisible(false);
        AnchorPane anchorPaneVenta = this.anchorPaneVenta;
        anchorPaneVenta.setOpacity(0);
        anchorPaneVenta.setPrefWidth(0);

        double targetWidth = 300;

        FXMLLoader loader = new FXMLLoader(ViewProductController.class.getResource("shoppingCart.fxml"));
        shoppingCart = loader.load();
        shoppingCartController = loader.getController();

        AnchorPane.setTopAnchor(shoppingCart, 0.0);
        AnchorPane.setBottomAnchor(shoppingCart, 0.0);
        AnchorPane.setRightAnchor(shoppingCart, -200.0);

        anchorPaneVenta.getChildren().setAll(shoppingCart);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(anchorPaneVenta.prefWidthProperty(), 0),
                        new KeyValue(anchorPaneVenta.opacityProperty(), 0)

                ),
                new KeyFrame(Duration.seconds(0.5),
                        new KeyValue(anchorPaneVenta.prefWidthProperty(), targetWidth, Interpolator.EASE_BOTH),
                        new KeyValue(anchorPaneVenta.opacityProperty(), 1, Interpolator.EASE_BOTH)
                )
        );
        timeline.setOnFinished(e -> {
            AnchorPane.setRightAnchor(shoppingCart, 0.0);
            setModo("Venta");

        });

        Set<String> codigosResurtidos = resurtidos.stream()
                .map(Keyable::getKey)
                .collect(Collectors.toSet());

        List<? extends Keyable> filteredData = getCatalog().stream()
                .filter(p -> codigosResurtidos.contains(p.getKey()))
                .collect(Collectors.toList());

        if (filteredData.isEmpty()) {
            message("Sin resultados","No hay productos resurtidos'");
            return;
        } else {
            setCatalogFiltered(filteredData);
            setupPagination(filteredData);
        }

        timeline.play();

    }


    @FXML
    private void handleInventario(ActionEvent event) {
        if(getModo().equals("Inventario"))
            return;

        AnchorPane anchorPaneVenta = this.anchorPaneVenta;
        double currentWidth = anchorPaneVenta.getWidth();

        AnchorPane.setRightAnchor(shoppingCart, -200.0);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(anchorPaneVenta.prefWidthProperty(), currentWidth),
                        new KeyValue(anchorPaneVenta.opacityProperty(), 1)
                ),
                new KeyFrame(Duration.seconds(.4),
                        new KeyValue(anchorPaneVenta.prefWidthProperty(), 0, Interpolator.EASE_BOTH),
                        new KeyValue(anchorPaneVenta.opacityProperty(), 0, Interpolator.EASE_BOTH)
                )
        );

        timeline.setOnFinished(e -> {
            agregarTab.setVisible(true);
            anchorPaneVenta.getChildren().clear();
        });

        timeline.play();

        setCatalogFiltered(getCatalog());
        setupPagination(getCatalog());
        setModo("Inventario");
    }


    @FXML
    private void handleAgregar(ActionEvent event){
        Stage stageCaptura = new Stage();
        stageCaptura.initModality(javafx.stage.Modality.APPLICATION_MODAL);
        @SuppressWarnings("unchecked")
        PanelCapturaProductos panelCaptura = new PanelCapturaProductos((List<Product>) getCatalog(), this);
        panelCaptura.start(stageCaptura);
    }

    @FXML
    private void handleResurtir(ActionEvent event){
        Stage stageCaptura = new Stage();
        stageCaptura.initModality(javafx.stage.Modality.APPLICATION_MODAL);
        @SuppressWarnings("unchecked")
        PanelResurtir panelCaptura = new PanelResurtir((List<Resurtido>) resurtidos, this);
        panelCaptura.start(stageCaptura);
    }
}
