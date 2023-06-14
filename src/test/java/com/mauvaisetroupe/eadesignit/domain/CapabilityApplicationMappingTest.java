package com.mauvaisetroupe.eadesignit.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mauvaisetroupe.eadesignit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CapabilityApplicationMappingTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CapabilityApplicationMapping.class);
        CapabilityApplicationMapping capabilityApplicationMapping1 = new CapabilityApplicationMapping();
        capabilityApplicationMapping1.setId(1L);
        CapabilityApplicationMapping capabilityApplicationMapping2 = new CapabilityApplicationMapping();
        capabilityApplicationMapping2.setId(capabilityApplicationMapping1.getId());
        assertThat(capabilityApplicationMapping1).isEqualTo(capabilityApplicationMapping2);
        capabilityApplicationMapping2.setId(2L);
        assertThat(capabilityApplicationMapping1).isNotEqualTo(capabilityApplicationMapping2);
        capabilityApplicationMapping1.setId(null);
        assertThat(capabilityApplicationMapping1).isNotEqualTo(capabilityApplicationMapping2);
    }
}
