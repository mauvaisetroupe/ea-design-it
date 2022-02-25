package com.mauvaisetroupe.eadesignit.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mauvaisetroupe.eadesignit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CapabilityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Capability.class);
        Capability capability1 = new Capability();
        capability1.setId(1L);
        Capability capability2 = new Capability();
        capability2.setId(capability1.getId());
        assertThat(capability1).isEqualTo(capability2);
        capability2.setId(2L);
        assertThat(capability1).isNotEqualTo(capability2);
        capability1.setId(null);
        assertThat(capability1).isNotEqualTo(capability2);
    }
}
