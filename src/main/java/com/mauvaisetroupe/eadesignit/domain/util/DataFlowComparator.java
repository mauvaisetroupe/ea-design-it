package com.mauvaisetroupe.eadesignit.domain.util;

import com.mauvaisetroupe.eadesignit.domain.DataFlow;
import com.mauvaisetroupe.eadesignit.domain.DataFormat;
import com.mauvaisetroupe.eadesignit.domain.FlowImport;
import com.mauvaisetroupe.eadesignit.domain.Protocol;
import com.mauvaisetroupe.eadesignit.domain.enumeration.Frequency;
import com.mauvaisetroupe.eadesignit.domain.enumeration.ProtocolType;

public class DataFlowComparator {

    public boolean areEquivalent(DataFlow obj, DataFlow other) {
        if (obj == null && other == null) return true;
        if (obj == null && other != null) return false;
        if (obj != null && other == null) return false;

        if (obj.getContractURL() == null) {
            if (other.getContractURL() != null) return false;
        } else if (!obj.getContractURL().equals(other.getContractURL())) return false;
        if (obj.getDescription() == null) {
            if (other.getDescription() != null) return false;
        } else if (!obj.getDescription().equals(other.getDescription())) return false;
        if (obj.getDocumentationURL() == null) {
            if (other.getDocumentationURL() != null) return false;
        } else if (!obj.getDocumentationURL().equals(other.getDocumentationURL())) return false;
        if (obj.getEndDate() == null) {
            if (other.getEndDate() != null) return false;
        } else if (!obj.getEndDate().equals(other.getEndDate())) return false;
        if (obj.getFormat() == null) {
            if (other.getFormat() != null) return false;
        } else if (!obj.getFormat().equals(other.getFormat())) return false;

        if (obj.getResourceName() == null) {
            if (other.getResourceName() != null) return false;
        } else if (!obj.getResourceName().equals(other.getResourceName())) return false;
        if (obj.getResourceType() == null) {
            if (other.getResourceType() != null) return false;
        } else if (!obj.getResourceType().equals(other.getResourceType())) return false;
        if (obj.getStartDate() == null) {
            if (other.getStartDate() != null) return false;
        } else if (!obj.getStartDate().equals(other.getStartDate())) return false;

        if (obj.getFrequency() != other.getFrequency()) return false;
        return true;
    }

    public boolean areEquivalent(FlowImport flowImport, DataFlow potentiaDataFlow) {
        // in import excel process, just insert frequency, format and contract if API
        if (!checkEqual(clean(flowImport.getFrequency()), potentiaDataFlow.getFrequency())) return false;
        if (!checkEqual(flowImport.getFormat(), potentiaDataFlow.getFormat())) return false;
        Protocol protocol = potentiaDataFlow.getFlowInterface().getProtocol();
        if (protocol != null && protocol.getType().equals(ProtocolType.API)) {
            if (!checkEqual(flowImport.getSwagger(), potentiaDataFlow.getContractURL())) return false;
        }
        return true;
    }

    private boolean checkEqual(String string1, String string2) {
        if (string1 == null && string2 == null) return true;
        if (string1 == null || string2 == null) return false;
        return (string2.equals(string1));
    }

    private boolean checkEqual(String string, DataFormat dataformat) {
        if (dataformat == null && string == null) return true;
        if (dataformat == null || string == null) return false;
        return (string.equals(dataformat.getName()));
    }

    private boolean checkEqual(String string, Frequency frequency) {
        if (frequency == null && string == null) return true;
        if (frequency == null || string == null) return false;
        return (string.toLowerCase().equals(frequency.name().toLowerCase()));
    }

    private String clean(String value) {
        if (value == null) return null;
        return value
            .replace('/', '_')
            .replace(' ', '_')
            .replace('(', '_')
            .replace(')', '_')
            .replace("__", "_")
            .replace("__", "_")
            .replaceAll("(.*)_$", "$1");
    }

    public Frequency getFrequency(String frequency) {
        return Frequency.valueOf(clean(frequency));
    }
}
