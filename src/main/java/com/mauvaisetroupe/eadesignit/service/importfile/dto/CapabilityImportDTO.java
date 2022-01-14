package com.mauvaisetroupe.eadesignit.service.importfile.dto;

public class CapabilityImportDTO {

    private CapabilityDTO L0;
    private CapabilityDTO L1;
    private CapabilityDTO L2;
    private CapabilityDTO L3;

    public CapabilityDTO getL0() {
        return L0;
    }

    public void setL0(CapabilityDTO l0) {
        L0 = l0;
    }

    public CapabilityDTO getL1() {
        return L1;
    }

    public void setL1(CapabilityDTO l1) {
        L1 = l1;
    }

    public CapabilityDTO getL2() {
        return L2;
    }

    public void setL2(CapabilityDTO l2) {
        L2 = l2;
    }

    public CapabilityDTO getL3() {
        return L3;
    }

    public void setL3(CapabilityDTO l3) {
        L3 = l3;
    }

    @Override
    public String toString() {
        return "CapabilityImportDTO [L0=" + L0 + ", L1=" + L1 + ", L2=" + L2 + ", L3=" + L3 + "]";
    }
}
