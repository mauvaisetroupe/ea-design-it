package com.mauvaisetroupe.eadesignit.web.rest.errors;

public class MalformedExcelException extends Exception {

    private static final long serialVersionUID = 1L;

    public MalformedExcelException(String message) {
        super("Excel problem : " + message);
    }
}
