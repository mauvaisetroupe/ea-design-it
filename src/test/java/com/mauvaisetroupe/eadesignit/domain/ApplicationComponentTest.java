package com.mauvaisetroupe.eadesignit.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mauvaisetroupe.eadesignit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ApplicationComponentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ApplicationComponent.class);
        ApplicationComponent applicationComponent1 = new ApplicationComponent();
        applicationComponent1.setId(1L);
        ApplicationComponent applicationComponent2 = new ApplicationComponent();
        applicationComponent2.setId(applicationComponent1.getId());
        assertThat(applicationComponent1).isEqualTo(applicationComponent2);
        applicationComponent2.setId(2L);
        assertThat(applicationComponent1).isNotEqualTo(applicationComponent2);
        applicationComponent1.setId(null);
        assertThat(applicationComponent1).isNotEqualTo(applicationComponent2);
    }
}
