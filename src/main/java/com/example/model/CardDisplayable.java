package com.example.model;

import com.example.interfaces.Displayable;
import com.example.interfaces.Keyable;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public abstract class CardDisplayable {

    @FXML
    private Label descrip;

    @FXML
    private ImageView image;

    @FXML
    private Label name;


    public void setDisplayable(Displayable item) {

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

    abstract <T extends Keyable> void action(T item);


}
