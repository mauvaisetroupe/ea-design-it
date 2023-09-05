package com.mauvaisetroupe.eadesignit.domain.util;

import com.mauvaisetroupe.eadesignit.domain.DataFlow;
import com.mauvaisetroupe.eadesignit.domain.enumeration.Frequency;

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

    public Frequency getFrequency(String frequency) {
        return Frequency.valueOf(EnumUtil.clean(frequency));
    }
}
