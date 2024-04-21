package com.mauvaisetroupe.eadesignit.service.importfile.dto;

public class ErrorLine {

    private String errorMessage;

    public ErrorLine(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
