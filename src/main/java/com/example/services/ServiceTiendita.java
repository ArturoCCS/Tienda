package com.example.services;

import com.example.interfaces.Operable;
import com.example.model.Container;
import com.example.model.Product;

import java.util.List;

import static com.example.dao.IOXUtility.loadData;

public class ServiceTiendita {

    public static void main(String[] args) {

        try {

            List<Product> products = loadData("productos.txt");
            Operable<Product> catalogo =  new Container<>();
            catalogo.addAll(products);

            for (Product product : catalogo.getAll()) {
                System.out.println(product);
                System.out.println("_".repeat(50));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

//    public static void main(String[] args) {
//        Container<Producto> catalogo = new Container<>();
//        Producto px1 = new Producto();
//        Producto pxx1 = new Producto();
//        Producto px2 = new Producto();
//        Producto px3 = new Producto();
//        Producto px4 = new Producto();
//
//        px1.setCodigpBarras("1111");
//        px1.setNombre("Atun");
//        px1.setMarca("Tuny");
//        px1.setContenido("200");
//        px1.setUnidadMedida("Gramos");
//        px1.setPresentacion("Lata");
//        px1.setCategoria("Alimento");
//        px1.setDescripcion("Atún etc etc");
//
//        pxx1.setCodigpBarras("1111");
//        pxx1.setNombre("Atun");
//        pxx1.setMarca("Tuny");
//        pxx1.setContenido("200");
//        pxx1.setUnidadMedida("Gramos");
//        pxx1.setPresentacion("Lata");
//        pxx1.setCategoria("Alimento");
//        pxx1.setDescripcion("Atún etc etc");
//
//        px2.setCodigpBarras("1112");
//        px2.setNombre("2");
//        px2.setMarca("Tuny");
//        px2.setContenido("200");
//        px2.setUnidadMedida("Gramos");
//        px2.setPresentacion("Lata");
//        px2.setCategoria("Alimento");
//        px2.setDescripcion("Atún etc etc");
//
//        px3.setCodigpBarras("1113");
//        px3.setNombre("3");
//        px3.setMarca("Tuny");
//        px3.setContenido("200");
//        px3.setUnidadMedida("Gramos");
//        px3.setPresentacion("Lata");
//        px3.setCategoria("Alimento");
//        px3.setDescripcion("Atún etc etc");
//
//        px4.setCodigpBarras("1114");
//        px4.setNombre("4");
//        px4.setMarca("Tuny");
//        px4.setContenido("200");
//        px4.setUnidadMedida("Gramos");
//        px4.setPresentacion("Lata");
//        px4.setCategoria("Alimento");
//        px4.setDescripcion("Atún etc etc");
//
//        catalogo.add(px1);
//        catalogo.add(px2);
//        catalogo.add(px3);
//        catalogo.add(px4);
//
//        if (!catalogo.contains(pxx1)){
//            catalogo.add(pxx1);
//        }else{
//            System.out.println("Ya existe");
//        }
//        if (!catalogo.contains(px2)){
//            catalogo.add(px2);
//        }
//        if (!catalogo.contains(px3)){
//            catalogo.add(px1);
//        }
//        if (!catalogo.contains(px4)){
//            catalogo.add(px1);
//        }
//
//
//        List<Producto> productos = catalogo.getAll();
//        productos.forEach(System.out::println);
//
//    }
}
