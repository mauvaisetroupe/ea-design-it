package com.mauvaisetroupe.eadesignit.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mauvaisetroupe.eadesignit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BusinessObjectTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BusinessObject.class);
        BusinessObject businessObject1 = new BusinessObject();
        businessObject1.setId(1L);
        BusinessObject businessObject2 = new BusinessObject();
        businessObject2.setId(businessObject1.getId());
        assertThat(businessObject1).isEqualTo(businessObject2);
        businessObject2.setId(2L);
        assertThat(businessObject1).isNotEqualTo(businessObject2);
        businessObject1.setId(null);
        assertThat(businessObject1).isNotEqualTo(businessObject2);
    }
}
