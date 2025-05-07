package com.example.model;

import com.example.interfaces.Keyable;

import java.util.Objects;

public class Resurtido implements Keyable {
    private String codigoBarras;
    private int cantidad;
    private String mes;

    public Resurtido(){

    }

    public Resurtido(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    @Override
    public String getKey() {
        return getCodigoBarras();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Resurtido other = (Resurtido) obj;
        return Objects.equals(codigoBarras, other.codigoBarras);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigoBarras);
    }

    @Override
    public String toString() {
        return "Resurtido{" +
                "codigoBarras='" + codigoBarras + '\'' +
                ", cantidad=" + cantidad +
                ", mes='" + mes + '\'' +
                '}';
    }
}
