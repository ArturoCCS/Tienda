package com.example.utility;

import com.example.model.Product;

public class FormatDataUtility {

    public static String formatDataProducto(Product product) {
        return String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%.2f,%d%n",
                product.getCodigpBarras(),
                product.getNombre(),
                product.getMarca(),
                product.getDescripcion(),
                product.getCategoria(),
                product.getUnidadMedida(),
                product.getContenido(),
                product.getPresentacion(),
                product.isActivo(),
                product.getImageUrl(),
                product.getPrecio(),
                product.getCantidadDisponible());
    }
}
