package com.mauvaisetroupe.eadesignit.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mauvaisetroupe.eadesignit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FlowGroupTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FlowGroup.class);
        FlowGroup flowGroup1 = new FlowGroup();
        flowGroup1.setId(1L);
        FlowGroup flowGroup2 = new FlowGroup();
        flowGroup2.setId(flowGroup1.getId());
        assertThat(flowGroup1).isEqualTo(flowGroup2);
        flowGroup2.setId(2L);
        assertThat(flowGroup1).isNotEqualTo(flowGroup2);
        flowGroup1.setId(null);
        assertThat(flowGroup1).isNotEqualTo(flowGroup2);
    }
}
