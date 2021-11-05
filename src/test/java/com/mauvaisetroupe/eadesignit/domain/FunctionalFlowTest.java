package com.mauvaisetroupe.eadesignit.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mauvaisetroupe.eadesignit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FunctionalFlowTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FunctionalFlow.class);
        FunctionalFlow functionalFlow1 = new FunctionalFlow();
        functionalFlow1.setId(1L);
        FunctionalFlow functionalFlow2 = new FunctionalFlow();
        functionalFlow2.setId(functionalFlow1.getId());
        assertThat(functionalFlow1).isEqualTo(functionalFlow2);
        functionalFlow2.setId(2L);
        assertThat(functionalFlow1).isNotEqualTo(functionalFlow2);
        functionalFlow1.setId(null);
        assertThat(functionalFlow1).isNotEqualTo(functionalFlow2);
    }
}
