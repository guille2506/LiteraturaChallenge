package com.aluracursos.literalura.model;

public enum Idioma {
    es("es", "español"),
    en("en", "inglés"),
    pt("pt", "portugués"),
    ru("ru", "ruso"),
    cn("zh", "chino");

    private String idiomaAPI;
    private String idiomaEspanol;

    Idioma(String idiomaAPI, String idiomaEspanol) {
        this.idiomaAPI = idiomaAPI;
        this.idiomaEspanol = idiomaEspanol;
    }

    public static Idioma fromString(String text) {
        for (Idioma idioma : Idioma.values()) {
            if (idioma.idiomaAPI.equalsIgnoreCase(text)) {
                return idioma;
            }
        }
        return null;
    }

    public static Idioma fromEspanol(String text) {
        for (Idioma idioma : Idioma.values()) {
            if (idioma.idiomaEspanol.equalsIgnoreCase(text)) {
                return idioma;
            }
        }
        return null;
    }
}