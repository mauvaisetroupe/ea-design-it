package com.mauvaisetroupe.eadesignit.repository;

import com.mauvaisetroupe.eadesignit.IntegrationTest;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
public class FlowInterfaceRepositoryTest {

    @Autowired
    private FlowInterfaceRepository flowInterfaceRepository;

    @Test
    void testGetDuplicatedInterface() {
        // check query is ok, no exception thrown
        List<Object[]> toto = flowInterfaceRepository.getDuplicatedInterfaceAsObject();
    }
}
