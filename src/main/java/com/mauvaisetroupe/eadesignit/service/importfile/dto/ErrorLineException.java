package com.mauvaisetroupe.eadesignit.service.importfile.dto;

import java.util.List;

public class ErrorLineException extends Exception {

    private List<ErrorLine> errorLines;

    public ErrorLineException(String message, List<ErrorLine> errorLines) {
        super(message);
        this.errorLines = errorLines;
    }

    public List<ErrorLine> getErrorLines() {
        return errorLines;
    }

    public void setErrorLines(List<ErrorLine> errorLines) {
        this.errorLines = errorLines;
    }
}
