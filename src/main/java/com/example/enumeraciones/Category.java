package com.example.enumeraciones;

public enum Category {
    BEBIDAS,
    COMESTIBLES,
    ELECTRONICOS,
    HIGIENE,
    SALUD,
    HOGAR,
    OTROS;

    public static Category fromString(String value) {
        return switch (value.trim()) {
            case "Bebidas" -> BEBIDAS;
            case "Comestibles" -> COMESTIBLES;
            case "Electrónicos", "Electronicos" -> ELECTRONICOS;
            case "Higiene" -> HIGIENE;
            case "Salud" -> SALUD;
            case "Hogar" -> HOGAR;
            default -> OTROS;
        };
    }


    @Override
    public String toString() {
        return switch (this) {
            case BEBIDAS -> "Bebidas";
            case COMESTIBLES -> "Comestibles";
            case ELECTRONICOS -> "Electrónicos";
            case HIGIENE -> "Higiene";
            case SALUD -> "Salud";
            case HOGAR -> "Hogar";
            default -> "Otros";
        };
    }
}
