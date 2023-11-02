package com.mauvaisetroupe.eadesignit.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mauvaisetroupe.eadesignit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DataObjectTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DataObject.class);
        DataObject dataObject1 = new DataObject();
        dataObject1.setId(1L);
        DataObject dataObject2 = new DataObject();
        dataObject2.setId(dataObject1.getId());
        assertThat(dataObject1).isEqualTo(dataObject2);
        dataObject2.setId(2L);
        assertThat(dataObject1).isNotEqualTo(dataObject2);
        dataObject1.setId(null);
        assertThat(dataObject1).isNotEqualTo(dataObject2);
    }
}
