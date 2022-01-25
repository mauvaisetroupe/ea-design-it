package com.mauvaisetroupe.eadesignit.repository;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import com.mauvaisetroupe.eadesignit.IntegrationTest;
import com.mauvaisetroupe.eadesignit.domain.Application;
import com.mauvaisetroupe.eadesignit.domain.FlowInterface;
import com.mauvaisetroupe.eadesignit.domain.FunctionalFlow;
import com.mauvaisetroupe.eadesignit.domain.FunctionalFlowStep;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
public class FunctionFlowStepRepositoryTest {

    @Autowired
    private FlowInterfaceRepository flowInterfaceRepository;

    @Autowired
    private FunctionalFlowRepository functionalFlowRepository;

    @Autowired
    private FunctionalFlowStepRepository functionalFlowStepRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Test
    void assertEqualityConsistency() {
        Application application1 = new Application();
        application1.setAlias("HPX.CMP.00000001");
        application1.setName("application1.name");

        Application application2 = new Application();
        application2.setAlias("HPX.CMP.00000002");
        application2.setName("application2.name");

        applicationRepository.save(application1);
        applicationRepository.save(application2);

        FunctionalFlow flow1 = new FunctionalFlow();
        flow1.setAlias("flow1");

        FunctionalFlow flow2 = new FunctionalFlow();
        flow2.setAlias("flow2");

        FlowInterface inter1 = new FlowInterface();
        inter1.setAlias("inter1");
        inter1.setSource(application1);
        inter1.setTarget(application2);

        FlowInterface inter2 = new FlowInterface();
        inter2.setAlias("inter2");

        FunctionalFlowStep step1 = new FunctionalFlowStep();
        step1.setDescription("step1");

        FunctionalFlowStep step2 = new FunctionalFlowStep();
        step2.setDescription("step2");

        assertFalse(flow1.getSteps().contains(step1));
        flow1.addSteps(step1);
        assertTrue(flow1.getSteps().contains(step1));
        assertFalse(flow1.getSteps().contains(step2));
        flow1.removeSteps(step1);
        assertFalse(flow1.getSteps().contains(step1));
        assertEquals(0, flow1.getSteps().size());
        functionalFlowRepository.save(flow1);
        flowInterfaceRepository.save(inter1);
        assertEquals(0, flow1.getSteps().size());

        flow1.addSteps(step1);
        inter1.addSteps(step1);
        assertEquals(1, flow1.getSteps().size());
        assertTrue(flow1.getSteps().contains(step1));
        assertFalse(flow1.getSteps().contains(step2));

        functionalFlowRepository.save(flow1);
        functionalFlowStepRepository.save(step1);
        assertEquals(1, flow1.getSteps().size());
        assertTrue(flow1.getSteps().contains(step1));
        assertFalse(flow1.getSteps().contains(step2));

        flow1.removeSteps(step1);
        inter1.removeSteps(step1);
        functionalFlowRepository.save(flow1);
        flowInterfaceRepository.save(inter1);
        functionalFlowStepRepository.delete(step1);
        assertFalse(flow1.getSteps().contains(step1));
        assertFalse(flow1.getSteps().contains(step2));
        assertEquals(0, flow1.getSteps().size());
    }
}
