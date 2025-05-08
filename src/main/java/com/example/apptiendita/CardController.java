package com.example.apptiendita;

import com.example.interfaces.Displayable;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class CardController {

    @FXML
    private Label descrip;

    @FXML
    private ImageView image;

    @FXML
    private Label name;


    public void setProducto(Displayable item) {

        name.setText(item.getTitle().trim());


        name.setWrapText(true);
        name.setMaxWidth(150);
        name.setMinHeight(50);
        if (descrip != null) {
            descrip.setText(item.getShortDescription().trim());
            descrip.setWrapText(true);
            descrip.setMaxWidth(150);
            descrip.setMinHeight(50);
        }

        // Este todavia no se puede usar por las url que no son validas
//            if (item.getImageUrl() != null) {
//                image.setImage(new Image(item.getImageUrl()));
//            }

    }


}
