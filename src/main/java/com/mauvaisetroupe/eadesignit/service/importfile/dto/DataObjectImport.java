package com.mauvaisetroupe.eadesignit.service.importfile.dto;

import com.mauvaisetroupe.eadesignit.domain.enumeration.DataObjectType;
import com.mauvaisetroupe.eadesignit.domain.enumeration.ImportStatus;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A FlowImport.
 */
public class DataObjectImport implements Serializable {

    private String businessobject;
    private String generalization;
    private boolean abstractValue;
    private String dataobject;
    private String application;
    private DataObjectType type;
    private ImportStatus importStatus;
    private String errorMessage;

    public DataObjectType getType() {
        return type;
    }

    public void setType(String type) {
        if (type != null) this.type = DataObjectType.valueOf(type.toUpperCase());
    }

    private List<String> landscapes = new ArrayList<>();

    @Override
    public String toString() {
        return (
            "DataObjectImport [businessobject=" +
            businessobject +
            ", generalization=" +
            generalization +
            ", abstractValue=" +
            abstractValue +
            ", dataobject=" +
            dataobject +
            ", application=" +
            application +
            ", landscapes=" +
            landscapes +
            ", importStatus=" +
            importStatus +
            ", errorMessage=" +
            errorMessage +
            "]"
        );
    }

    public List<String> getLandscapes() {
        return landscapes;
    }

    public void setLandscapes(List<String> landscapes) {
        this.landscapes = landscapes;
    }

    public ImportStatus getImportStatus() {
        return importStatus;
    }

    public void setImportStatus(ImportStatus importStatus) {
        this.importStatus = importStatus;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getBusinessobject() {
        return businessobject;
    }

    public void setBusinessobject(String businessobject) {
        this.businessobject = businessobject;
    }

    public String getGeneralization() {
        return generalization;
    }

    public void setGeneralization(String generalization) {
        this.generalization = generalization;
    }

    public boolean isAbstractValue() {
        return abstractValue;
    }

    public void setAbstractValue(boolean abstractValue) {
        this.abstractValue = abstractValue;
    }

    public String getDataobject() {
        return dataobject;
    }

    public void setDataobject(String dataobject) {
        this.dataobject = dataobject;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }
}
