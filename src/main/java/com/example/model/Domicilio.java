package com.example.model;

import com.example.enumeraciones.Orientacion;

public record Domicilio(String numeroCasa, String calle, Orientacion orientacion, String colonia,
                        String codigoPostal, String ciudad, String estado) {
}
