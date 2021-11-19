package com.mauvaisetroupe.eadesignit.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mauvaisetroupe.eadesignit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EventDataTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EventData.class);
        EventData eventData1 = new EventData();
        eventData1.setId(1L);
        EventData eventData2 = new EventData();
        eventData2.setId(eventData1.getId());
        assertThat(eventData1).isEqualTo(eventData2);
        eventData2.setId(2L);
        assertThat(eventData1).isNotEqualTo(eventData2);
        eventData1.setId(null);
        assertThat(eventData1).isNotEqualTo(eventData2);
    }
}
