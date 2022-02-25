package com.mauvaisetroupe.eadesignit.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mauvaisetroupe.eadesignit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DataFlowItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DataFlowItem.class);
        DataFlowItem dataFlowItem1 = new DataFlowItem();
        dataFlowItem1.setId(1L);
        DataFlowItem dataFlowItem2 = new DataFlowItem();
        dataFlowItem2.setId(dataFlowItem1.getId());
        assertThat(dataFlowItem1).isEqualTo(dataFlowItem2);
        dataFlowItem2.setId(2L);
        assertThat(dataFlowItem1).isNotEqualTo(dataFlowItem2);
        dataFlowItem1.setId(null);
        assertThat(dataFlowItem1).isNotEqualTo(dataFlowItem2);
    }
}
