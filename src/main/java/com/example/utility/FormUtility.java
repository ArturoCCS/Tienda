package com.example.utility;

import javafx.scene.control.Alert;
public class FormUtility {

    public static void message(String title, String text){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(text);

        alert.showAndWait();
    }

    public static String formatText(String content){
        return content.substring(0, 1).toUpperCase() + content.substring(1).toLowerCase();
    }
}
