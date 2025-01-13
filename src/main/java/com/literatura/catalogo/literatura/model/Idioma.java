package com.literatura.catalogo.literatura.model;

public enum Idioma {
    ESPANIOL("es", "Español"),
    INGLES("en", "Inglés"),
    PORTUGUES("pt","Portugués"),
    FRANCES("fr","Francés"),
    ITALIANO("it", "Italiano"),
    ALEMAN("de", "Alemán");

    private String idiomaNativo;
    private String idiomaEspaniol;

    Idioma (String idiomaNativo, String idiomaEspaniol){
        this.idiomaNativo = idiomaNativo;
        this.idiomaEspaniol = idiomaEspaniol;
    }

    public static Idioma fromString(String text) {
        for (Idioma idioma : Idioma.values()) {
            if (idioma.idiomaNativo.equalsIgnoreCase(text)) {
                return idioma;
            }
        }
        throw new IllegalArgumentException("Ningún idioma encontrado: " + text);
    }
}
