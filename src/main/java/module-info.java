module com.example.apptiendita {
    requires javafx.fxml;
    requires de.jensd.fx.glyphs.fontawesome;
    requires javafx.controls;
    requires com.jfoenix;
    requires AnimateFX;
    requires eu.iamgio.animated;
    requires java.desktop;


    opens com.example.apptiendita to javafx.fxml, javafx.base;

    exports com.example.model;
    exports com.example.apptiendita;
    exports com.example.utility;
    exports com.example.interfaces;
    opens com.example.model to javafx.base, javafx.fxml;

}