package com.mauvaisetroupe.eadesignit.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mauvaisetroupe.eadesignit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FlowInterfaceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FlowInterface.class);
        FlowInterface flowInterface1 = new FlowInterface();
        flowInterface1.setId("id1");
        FlowInterface flowInterface2 = new FlowInterface();
        flowInterface2.setId(flowInterface1.getId());
        assertThat(flowInterface1).isEqualTo(flowInterface2);
        flowInterface2.setId("id2");
        assertThat(flowInterface1).isNotEqualTo(flowInterface2);
        flowInterface1.setId(null);
        assertThat(flowInterface1).isNotEqualTo(flowInterface2);
    }
}
