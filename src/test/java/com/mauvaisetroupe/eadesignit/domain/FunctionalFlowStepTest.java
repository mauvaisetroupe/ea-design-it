package com.mauvaisetroupe.eadesignit.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mauvaisetroupe.eadesignit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FunctionalFlowStepTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FunctionalFlowStep.class);
        FunctionalFlowStep functionalFlowStep1 = new FunctionalFlowStep();
        functionalFlowStep1.setId(1L);
        FunctionalFlowStep functionalFlowStep2 = new FunctionalFlowStep();
        functionalFlowStep2.setId(functionalFlowStep1.getId());
        assertThat(functionalFlowStep1).isEqualTo(functionalFlowStep2);
        functionalFlowStep2.setId(2L);
        assertThat(functionalFlowStep1).isNotEqualTo(functionalFlowStep2);
        functionalFlowStep1.setId(null);
        assertThat(functionalFlowStep1).isNotEqualTo(functionalFlowStep2);
    }
}
