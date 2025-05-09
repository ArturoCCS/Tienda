package com.example.apptiendita;

import com.example.interfaces.Keyable;
import com.example.interfaces.Operable;
import com.example.model.Container;
import com.example.model.Product;
import com.example.model.Resurtido;
import com.example.utility.JFXDecorator;
import javafx.animation.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.example.dao.IOXUtility.*;
import static com.example.utility.FormUtility.message;

public class MainApplication extends Application {

    private static final String FILE_PATH = "productos";
    private static final String MAIN_VIEW = "main.fxml";
    private static final String SPLASH_VIEW = "splash.fxml";
    private Map<String, Operable<? extends Keyable>> catalogs;

    @Override
    public void start(Stage splashStage) throws IOException {
        FXMLLoader splashLoader = new FXMLLoader(getClass().getResource(SPLASH_VIEW));
        Parent splashRoot = splashLoader.load();
        Scene splashScene = new Scene(splashRoot, 800, 450);
        splashScene.setFill(Color.TRANSPARENT);

        splashStage.initStyle(StageStyle.UNDECORATED);
        splashStage.setScene(splashScene);
        splashStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResource("/img/Logo.png")).toExternalForm()));
        splashStage.show();

        new Thread(() -> {
            try {
                initCatalogs();
                Thread.sleep(200);
                MainController controller = new MainController();
                controller.setCatalogs(catalogs);
                controller.preloadCatalogView();

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(MAIN_VIEW));
                fxmlLoader.setController(controller);
                Parent mainRoot = fxmlLoader.load();

                Platform.runLater(() -> {

                    ParallelTransition splashTransition = getParallelTransition(splashRoot);

                    splashTransition.setOnFinished(ev -> {
                        controller.oyenteCatalogo(new ActionEvent());

                        Stage mainStage = new Stage();

                        mainStage.initStyle(StageStyle.UNDECORATED);
                        mainStage.initStyle(StageStyle.TRANSPARENT);

                        FXMLLoader fxmlLoader2 = new FXMLLoader(getClass().getResource("titlebar.fxml"));

                        Node customGraphic = loadFXML(fxmlLoader2);


                        JFXDecorator decorator = new JFXDecorator(mainStage, mainRoot, true, true, true, catalogs);


                        decorator.setScaleX(0.9);
                        decorator.setScaleY(0.9);
                        decorator.setOpacity(0);

                        Timeline timeline = new Timeline(
                                new KeyFrame(Duration.ZERO,
                                        new KeyValue(decorator.scaleXProperty(), 0.9),
                                        new KeyValue(decorator.scaleYProperty(), 0.9),
                                        new KeyValue(decorator.opacityProperty(), 0)
                                ),
                                new KeyFrame(Duration.seconds(1.6),
                                        new KeyValue(decorator.scaleXProperty(), 1),
                                        new KeyValue(decorator.scaleYProperty(), 1),
                                        new KeyValue(decorator.opacityProperty(), 1)
                                )
                        );
                        timeline.play();

                        customGraphic.setTranslateY(15);
                        decorator.setGraphic(customGraphic);

                        Scene mainScene = new Scene(decorator);
                        addStyles(mainScene);

                        mainScene.setFill(Color.TRANSPARENT);


                        mainStage.setScene(mainScene);
                        Screen screen = Screen.getPrimary();
                        double screenWidth = screen.getVisualBounds().getWidth();
                        double screenHeight = screen.getVisualBounds().getHeight();

                        mainStage.setWidth(screenWidth);
                        mainStage.setHeight(screenHeight);
                        mainStage.setMinWidth(900);
                        mainStage.setMinHeight(400);
                        mainStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResource("/img/Logo.png")).toExternalForm()));

                        mainStage.setTitle("Tiendita");
                        decorator.setTitle("");
                        mainStage.show();

                        splashStage.close();
                    });
                    splashTransition.play();



                });

            } catch (InterruptedException | IOException e) {
                Platform.runLater(() -> message("Error", "Ocurrió un error durante la carga." + e.getMessage()));
            }
        }).start();
    }

    private static ParallelTransition getParallelTransition(Parent splashRoot) {
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(1.5), splashRoot);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);

        ScaleTransition scaleOut = new ScaleTransition(Duration.seconds(1), splashRoot);
        scaleOut.setFromX(1);
        scaleOut.setFromY(1);
        scaleOut.setToX(1.1);
        scaleOut.setToY(1.1);

        return new ParallelTransition(fadeOut, scaleOut);
    }

    private Node loadFXML(FXMLLoader loader) {
        try {
            return loader.load();
        } catch (IOException e) {
            throw new RuntimeException("Error al cargar FXML", e);
        }
    }

    private void addStyles(Scene scene) {
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/ViewProducts.css")).toExternalForm());
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/ShoppingCart.css")).toExternalForm());
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/Global.css")).toExternalForm());
    }

    private void initCatalogs() {
        catalogs = new HashMap<>();

        Operable<Product> catalogProduct = new Container<>();
        Operable<Resurtido> catalogResurtido = new Container<>();

        catalogProduct.addAll(loadDataProduct(FILE_PATH));
        catalogResurtido.addAll(loadDataResurtido("resurtidos"));
        catalogs.put("productos", catalogProduct);
        catalogs.put("resurtidos", catalogResurtido);
    }

    public static void main(String[] args) {
        launch();
    }
}