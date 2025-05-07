package com.example.dao;

import com.example.interfaces.Keyable;
import com.example.interfaces.Operable;
import com.example.model.Product;
import com.example.model.Resurtido;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static com.example.utility.FormatDataUtility.*;
import static com.example.utility.FormUtility.*;

public class IOXUtility {

    public static List<Product> loadDataProduct(String path) {
        InputStream inputStream = IOXUtility.class.getClassLoader().getResourceAsStream(path);
        if (inputStream!=null) {
             Scanner entrada = new Scanner(inputStream);
             List<Product> data = new ArrayList<>();
             while (entrada.hasNext()) {
                 String line = entrada.nextLine();
                 data.add(getProducto(line));
             }
             entrada.close();
             return data;
        }
        System.out.println("No se encontro el archivo");

        return null;
    }

    public static List<Resurtido> loadDataResurtido(String path) {
        InputStream inputStream = IOXUtility.class.getClassLoader().getResourceAsStream(path);
        if (inputStream!=null) {
            Scanner entrada = new Scanner(inputStream);
            List<Resurtido> data = new ArrayList<>();
            while (entrada.hasNext()) {
                String line = entrada.nextLine();
                data.add(getResurtido(line));
            }
            entrada.close();
            return data;
        }
        System.out.println("No se encontro el archivo");

        return null;
    }

    public static void writeData(String path, List<Product> products) throws Exception {
        File file = new File(path);
        PrintWriter writer = new PrintWriter(file);
        for (Product product : products) {
            String salida = product.getKey()+", " +
                    product.getNombre() + ", " +
                    product.getMarca() +  ", " +
                    product.getDescripcion() + ", " +
                    product.getCategoria() + ", " +
                    product.getUnidadMedida() +  ", " +
                    product.getContenido() +  ", " +
                    product.getPresentacion() + ", " +
                    product.isActivo() + ", " +
                    product.getImageUrl() +  ", ";
            writer.println(salida);
            writer.flush();
        }
        writer.close();
    }



    private static Resurtido getResurtido(String line) {
        Resurtido resurtido = new Resurtido();
        String[] items = line.split(",");
        for(int i = 0; i < items.length; i++){
            resurtido.setCodigoBarras(items[0]);
            resurtido.setCantidad(Integer.parseInt(items[1].trim()));
            resurtido.setMes(items[2]);
        }
        return resurtido;
    }


    private static Product getProducto(String line) {
        Product product = new Product();
        String[] items = line.split(",");
        for(int i = 0; i < items.length; i++){
            product.setCodigpBarras(items[0]);
            product.setNombre(items[1]);
            product.setMarca(items[2]);
            product.setDescripcion(items[3]);
            product.setCategoria(items[4]);
            product.setUnidadMedida(items[5]);
            product.setContenido(items[6]);
            product.setPresentacion(items[7]);
            product.setActivo(Boolean.parseBoolean(items[8]));
            product.setImagenUrl(items[9]);
        }
        return product;
    }

    public static void saveData(Map<String, Operable<? extends Keyable>> catalogs){
        catalogs.forEach((name, catalog) -> {
            try {
                URL resourceUrl = IOXUtility.class.getClassLoader().getResource(name);

                if (resourceUrl == null) {
                    throw new FileNotFoundException("Archivo no encontrado"+name);
                }
                File file = new File(resourceUrl.toURI());

                try (BufferedWriter writer = Files.newBufferedWriter(file.toPath(),
                        StandardCharsets.UTF_8, StandardOpenOption.TRUNCATE_EXISTING)){


                    for (Keyable item : catalog.getAll()){
                        if (item instanceof Product product) {
                            writer.write(formatDataProducto(product));
                        }
                    }
                    message("Alerta", "Datos guardados exitosamente en: " + file.getAbsolutePath());
                }
            }catch (Exception e){
                message("Error", "Error al guardar:" + e.getMessage());
            }
        });
    }

}
