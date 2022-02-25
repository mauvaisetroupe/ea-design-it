package com.mauvaisetroupe.eadesignit.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mauvaisetroupe.eadesignit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DataFlowTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DataFlow.class);
        DataFlow dataFlow1 = new DataFlow();
        dataFlow1.setId(1L);
        DataFlow dataFlow2 = new DataFlow();
        dataFlow2.setId(dataFlow1.getId());
        assertThat(dataFlow1).isEqualTo(dataFlow2);
        dataFlow2.setId(2L);
        assertThat(dataFlow1).isNotEqualTo(dataFlow2);
        dataFlow1.setId(null);
        assertThat(dataFlow1).isNotEqualTo(dataFlow2);
    }
}
