package com.mauvaisetroupe.eadesignit.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mauvaisetroupe.eadesignit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ApplicationImportTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ApplicationImport.class);
        ApplicationImport applicationImport1 = new ApplicationImport();
        applicationImport1.setId(1L);
        ApplicationImport applicationImport2 = new ApplicationImport();
        applicationImport2.setId(applicationImport1.getId());
        assertThat(applicationImport1).isEqualTo(applicationImport2);
        applicationImport2.setId(2L);
        assertThat(applicationImport1).isNotEqualTo(applicationImport2);
        applicationImport1.setId(null);
        assertThat(applicationImport1).isNotEqualTo(applicationImport2);
    }
}
