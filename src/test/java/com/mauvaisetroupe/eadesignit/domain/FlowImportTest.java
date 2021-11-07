package com.mauvaisetroupe.eadesignit.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mauvaisetroupe.eadesignit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FlowImportTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FlowImport.class);
        FlowImport flowImport1 = new FlowImport();
        flowImport1.setId(1L);
        FlowImport flowImport2 = new FlowImport();
        flowImport2.setId(flowImport1.getId());
        assertThat(flowImport1).isEqualTo(flowImport2);
        flowImport2.setId(2L);
        assertThat(flowImport1).isNotEqualTo(flowImport2);
        flowImport1.setId(null);
        assertThat(flowImport1).isNotEqualTo(flowImport2);
    }
}
