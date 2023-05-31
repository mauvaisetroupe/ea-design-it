package com.mauvaisetroupe.eadesignit.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mauvaisetroupe.eadesignit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ExternalSystemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExternalSystem.class);
        ExternalSystem externalSystem1 = new ExternalSystem();
        externalSystem1.setId(1L);
        ExternalSystem externalSystem2 = new ExternalSystem();
        externalSystem2.setId(externalSystem1.getId());
        assertThat(externalSystem1).isEqualTo(externalSystem2);
        externalSystem2.setId(2L);
        assertThat(externalSystem1).isNotEqualTo(externalSystem2);
        externalSystem1.setId(null);
        assertThat(externalSystem1).isNotEqualTo(externalSystem2);
    }
}
