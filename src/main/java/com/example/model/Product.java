package com.example.model;

import com.example.enumeraciones.Category;
import com.example.interfaces.Keyable;
import com.example.interfaces.Displayable;

public class Product implements Displayable, Keyable {
    private String codigoBarras;
    private String nombre;
    private String marca;
    private Category categoria;
    private String contenido;
    private String presentacion;
    private String unidadMedida;
    private boolean activo;
    private String descripcion;
    private String imagenUrl;

    public Product() {}

    public Product(String codigoBarras){
        super();
        this.codigoBarras = codigoBarras;
    }

    @Override
    public String getKey() {
        return codigoBarras;
    }

    public String getCodigpBarras() {
        return codigoBarras;
    }


    public void setCodigpBarras(String codigpBarras) {
        this.codigoBarras = codigpBarras;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public Category getCategoria() {
        return categoria;
    }

    public void setCategoria(Category categoria) {
        this.categoria = categoria;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getPresentacion() {
        return presentacion;
    }

    public void setPresentacion(String presentacion) {
        this.presentacion = presentacion;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean estaActivo) {
        this.activo = estaActivo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    @Override
    public String toString() {
        return "Producto{" +
                "codigoBarras='" + codigoBarras + '\'' +
                ", nombre='" + nombre + '\'' +
                ", marca='" + marca + '\'' +
                ", categoria='" + categoria + '\'' +
                ", contenido='" + contenido + '\'' +
                ", presentacion='" + presentacion + '\'' +
                ", unidadMedida='" + unidadMedida + '\'' +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Product other = (Product) obj;
        return this.getKey().equals(other.getKey());
    }

    @Override
    public int hashCode() {
        return getKey().hashCode();
    }
    @Override
    public String getImageUrl() {
        return imagenUrl;
    }

    @Override
    public String getTitle() {
        return nombre;
    }

    @Override
    public String getShortDescription() {
        return descripcion;
    }

}
