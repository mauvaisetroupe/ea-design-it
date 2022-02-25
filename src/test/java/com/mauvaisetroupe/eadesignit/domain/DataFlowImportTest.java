package com.mauvaisetroupe.eadesignit.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mauvaisetroupe.eadesignit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DataFlowImportTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DataFlowImport.class);
        DataFlowImport dataFlowImport1 = new DataFlowImport();
        dataFlowImport1.setId(1L);
        DataFlowImport dataFlowImport2 = new DataFlowImport();
        dataFlowImport2.setId(dataFlowImport1.getId());
        assertThat(dataFlowImport1).isEqualTo(dataFlowImport2);
        dataFlowImport2.setId(2L);
        assertThat(dataFlowImport1).isNotEqualTo(dataFlowImport2);
        dataFlowImport1.setId(null);
        assertThat(dataFlowImport1).isNotEqualTo(dataFlowImport2);
    }
}
