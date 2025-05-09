package com.example.config;

import com.example.enumeraciones.Category;
import javafx.scene.paint.Color;

import java.util.EnumMap;
import java.util.Map;

public class StyleConfig {

    private static final Map<Category, Color> categoryColors = new EnumMap<>(Category.class);

    static {
        categoryColors.put(Category.BEBIDAS, Color.web("#00BCD4"));
        categoryColors.put(Category.COMESTIBLES, Color.web("#8BC34A"));
        categoryColors.put(Category.ELECTRONICOS, Color.web("#FF9800"));
        categoryColors.put(Category.OTROS, Color.web("#9E9E9E"));
    }

    public static Color getColorByCategory(Category category) {
        return categoryColors.getOrDefault(category, Color.GRAY);
    }

    public static void setColorForCategory(Category category, Color color) {
        categoryColors.put(category, color);
    }

    public static Map<Category, Color> getAllCategoryColors() {
        return new EnumMap<>(categoryColors);
    }
}
