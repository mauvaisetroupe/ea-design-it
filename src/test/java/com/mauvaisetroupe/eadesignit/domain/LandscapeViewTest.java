package com.mauvaisetroupe.eadesignit.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mauvaisetroupe.eadesignit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LandscapeViewTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LandscapeView.class);
        LandscapeView landscapeView1 = new LandscapeView();
        landscapeView1.setId(1L);
        LandscapeView landscapeView2 = new LandscapeView();
        landscapeView2.setId(landscapeView1.getId());
        assertThat(landscapeView1).isEqualTo(landscapeView2);
        landscapeView2.setId(2L);
        assertThat(landscapeView1).isNotEqualTo(landscapeView2);
        landscapeView1.setId(null);
        assertThat(landscapeView1).isNotEqualTo(landscapeView2);
    }
}
