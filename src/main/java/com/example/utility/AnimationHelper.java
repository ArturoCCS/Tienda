package com.example.utility;

import animatefx.animation.Pulse;
import javafx.scene.Node;

public class AnimationHelper {

    public static void applyHoverAnimation(Node node) {
        node.setOnMouseEntered(event -> {
            new Pulse(node).play();
        });

    }

}
