package com.mauvaisetroupe.eadesignit.web.rest.errors;

public class ApplicationImportException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public ApplicationImportException(String messString) {
        super(ErrorConstants.DEFAULT_TYPE, messString, "IMPORT ENTITY", "BAD-EXCEL");
    }
}
