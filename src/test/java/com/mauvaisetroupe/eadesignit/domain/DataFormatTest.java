package com.mauvaisetroupe.eadesignit.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mauvaisetroupe.eadesignit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DataFormatTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DataFormat.class);
        DataFormat dataFormat1 = new DataFormat();
        dataFormat1.setId(1L);
        DataFormat dataFormat2 = new DataFormat();
        dataFormat2.setId(dataFormat1.getId());
        assertThat(dataFormat1).isEqualTo(dataFormat2);
        dataFormat2.setId(2L);
        assertThat(dataFormat1).isNotEqualTo(dataFormat2);
        dataFormat1.setId(null);
        assertThat(dataFormat1).isNotEqualTo(dataFormat2);
    }
}
