package com.mauvaisetroupe.eadesignit.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.mauvaisetroupe.eadesignit.web.rest.TestUtil;
import java.util.SortedSet;
import java.util.TreeSet;
import org.junit.jupiter.api.Test;

class FunctionalFlowTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FunctionalFlow.class);
        FunctionalFlow functionalFlow1 = new FunctionalFlow();
        functionalFlow1.setId(1L);
        FunctionalFlow functionalFlow2 = new FunctionalFlow();
        functionalFlow2.setId(functionalFlow1.getId());
        assertThat(functionalFlow1).isEqualTo(functionalFlow2);
        functionalFlow2.setId(2L);
        assertThat(functionalFlow1).isNotEqualTo(functionalFlow2);
        functionalFlow1.setId(null);
        assertThat(functionalFlow1).isNotEqualTo(functionalFlow2);
    }

    @Test
    void checkMap() {
        FunctionalFlow functionalFlow1 = new FunctionalFlow();
        FunctionalFlow functionalFlow2 = new FunctionalFlow();
        SortedSet<FunctionalFlow> set = new TreeSet<>();

        functionalFlow1 = new FunctionalFlow();
        functionalFlow2 = new FunctionalFlow();
        set = new TreeSet<>();
        set.add(functionalFlow1);
        set.add(functionalFlow2);
        // same ID, same alias... should be consider as same object
        assertEquals(1, set.size());

        functionalFlow1 = new FunctionalFlow();
        functionalFlow1.setId(1L);
        functionalFlow2 = new FunctionalFlow();
        set = new TreeSet<>();
        set.add(functionalFlow1);
        set.add(functionalFlow2);
        // same ID, same alias... should be consider as same object
        assertEquals(2, set.size());
        assertEquals(1L, set.first().getId());

        functionalFlow1 = new FunctionalFlow();
        functionalFlow1.setAlias("A");
        functionalFlow2 = new FunctionalFlow();
        functionalFlow2.setAlias("B");
        set = new TreeSet<>();
        set.add(functionalFlow1);
        set.add(functionalFlow2);
        // same ID, same alias... should be consider as same object
        assertEquals(2, set.size());
        assertEquals("A", set.first().getAlias());
        functionalFlow1 = new FunctionalFlow();

        functionalFlow1.setId(1L);
        functionalFlow1.setAlias("B");
        functionalFlow2 = new FunctionalFlow();
        functionalFlow2.setId(2L);
        functionalFlow2.setAlias("A");
        set = new TreeSet<>();
        set.add(functionalFlow1);
        set.add(functionalFlow2);
        // same ID, same alias... should be consider as same object
        assertEquals(2, set.size());
        assertEquals("A", set.first().getAlias());
        functionalFlow1 = new FunctionalFlow();
        functionalFlow1.setId(1L);
        functionalFlow1.setAlias("B");

        functionalFlow2 = new FunctionalFlow();
        functionalFlow2.setId(2L);
        set = new TreeSet<>();
        set.add(functionalFlow1);
        set.add(functionalFlow2);
        // same ID, same alias... should be consider as same object
        assertEquals(2, set.size());
        assertEquals("B", set.first().getAlias());
    }
}
