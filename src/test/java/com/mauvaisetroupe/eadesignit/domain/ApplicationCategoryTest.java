package com.mauvaisetroupe.eadesignit.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mauvaisetroupe.eadesignit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ApplicationCategoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ApplicationCategory.class);
        ApplicationCategory applicationCategory1 = new ApplicationCategory();
        applicationCategory1.setId(1L);
        ApplicationCategory applicationCategory2 = new ApplicationCategory();
        applicationCategory2.setId(applicationCategory1.getId());
        assertThat(applicationCategory1).isEqualTo(applicationCategory2);
        applicationCategory2.setId(2L);
        assertThat(applicationCategory1).isNotEqualTo(applicationCategory2);
        applicationCategory1.setId(null);
        assertThat(applicationCategory1).isNotEqualTo(applicationCategory2);
    }
}
