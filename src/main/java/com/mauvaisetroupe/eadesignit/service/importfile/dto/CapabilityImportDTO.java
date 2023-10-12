package com.mauvaisetroupe.eadesignit.service.importfile.dto;

import com.mauvaisetroupe.eadesignit.domain.Capability;
import com.mauvaisetroupe.eadesignit.domain.enumeration.ImportStatus;

public class CapabilityImportDTO {

    private Capability domain;
    private Capability L0;
    private Capability L1;
    private Capability L2;
    private Capability L3;
    private ImportStatus status;
    private String error;

    public CapabilityImportDTO(Capability domain, Capability l0, Capability l1, Capability l2, Capability l3) {
        this.L0 = l0;
        this.L1 = l1;
        this.L2 = l2;
        this.L3 = l3;
        this.domain = domain;
    }

    public Capability getL0() {
        return L0;
    }

    public void setL0(Capability l0) {
        L0 = l0;
    }

    public Capability getL1() {
        return L1;
    }

    public void setL1(Capability l1) {
        L1 = l1;
    }

    public Capability getL2() {
        return L2;
    }

    public void setL2(Capability l2) {
        L2 = l2;
    }

    public Capability getL3() {
        return L3;
    }

    public void setL3(Capability l3) {
        L3 = l3;
    }

    public ImportStatus getStatus() {
        return status;
    }

    public void setStatus(ImportStatus status) {
        this.status = status;
    }

    public Capability getDomain() {
        return domain;
    }

    public void setDomain(Capability domain) {
        this.domain = domain;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "CapabilityImportDTO [Sur-Domain=" + domain + ", L0=" + L0 + ", L1=" + L1 + ", L2=" + L2 + ", L3=" + L3 + "]";
    }
}
