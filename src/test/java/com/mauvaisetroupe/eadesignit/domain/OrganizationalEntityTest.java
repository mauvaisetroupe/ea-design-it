package com.mauvaisetroupe.eadesignit.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mauvaisetroupe.eadesignit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrganizationalEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrganizationalEntity.class);
        OrganizationalEntity organizationalEntity1 = new OrganizationalEntity();
        organizationalEntity1.setId(1L);
        OrganizationalEntity organizationalEntity2 = new OrganizationalEntity();
        organizationalEntity2.setId(organizationalEntity1.getId());
        assertThat(organizationalEntity1).isEqualTo(organizationalEntity2);
        organizationalEntity2.setId(2L);
        assertThat(organizationalEntity1).isNotEqualTo(organizationalEntity2);
        organizationalEntity1.setId(null);
        assertThat(organizationalEntity1).isNotEqualTo(organizationalEntity2);
    }
}
