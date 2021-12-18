package com.mauvaisetroupe.eadesignit.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.mauvaisetroupe.eadesignit.IntegrationTest;
import com.mauvaisetroupe.eadesignit.domain.Application;
import com.mauvaisetroupe.eadesignit.domain.FlowInterface;
import com.mauvaisetroupe.eadesignit.domain.Protocol;
import com.mauvaisetroupe.eadesignit.domain.enumeration.ProtocolType;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
public class FlowInterfaceRepositoryTest {

    @Autowired
    private FlowInterfaceRepository flowInterfaceRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private ProtocolRepository protocolRepository;

    @Test
    void testGetDuplicatedInterface() {
        Application application1 = new Application();
        application1.setAlias("HPX.CMP.00000001");
        application1.setName("application1.name");

        Application application2 = new Application();
        application2.setAlias("HPX.CMP.00000002");
        application2.setName("application2.name");

        FlowInterface interface1 = new FlowInterface();
        interface1.setAlias("interface1");
        interface1.setSource(application1);
        interface1.setTarget(application2);

        FlowInterface interface2 = new FlowInterface();
        interface2.setAlias("interface2");
        interface2.setSource(application1);
        interface2.setTarget(application2);

        applicationRepository.save(application1);
        applicationRepository.save(application2);
        flowInterfaceRepository.save(interface1);
        flowInterfaceRepository.save(interface2);

        List<FlowInterface> duplicatedInterfaces = flowInterfaceRepository.getDuplicatedInterface();

        // Missing protocol, nothing found
        assertEquals(0, duplicatedInterfaces.size());

        Protocol protocol = new Protocol();
        protocol.setType(ProtocolType.API);
        protocol.setName("custom protocol based on API");
        protocolRepository.save(protocol);
        interface1.setProtocol(protocol);
        interface2.setProtocol(protocol);
        flowInterfaceRepository.save(interface1);
        flowInterfaceRepository.save(interface2);

        duplicatedInterfaces = flowInterfaceRepository.getDuplicatedInterface();
        assertEquals(2, duplicatedInterfaces.size());
    }
}
