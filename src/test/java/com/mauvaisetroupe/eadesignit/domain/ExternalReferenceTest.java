package com.mauvaisetroupe.eadesignit.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mauvaisetroupe.eadesignit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ExternalReferenceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExternalReference.class);
        ExternalReference externalReference1 = new ExternalReference();
        externalReference1.setId(1L);
        ExternalReference externalReference2 = new ExternalReference();
        externalReference2.setId(externalReference1.getId());
        assertThat(externalReference1).isEqualTo(externalReference2);
        externalReference2.setId(2L);
        assertThat(externalReference1).isNotEqualTo(externalReference2);
        externalReference1.setId(null);
        assertThat(externalReference1).isNotEqualTo(externalReference2);
    }
}
