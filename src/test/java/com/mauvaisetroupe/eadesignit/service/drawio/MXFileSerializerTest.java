package com.mauvaisetroupe.eadesignit.service.drawio;

import com.mauvaisetroupe.eadesignit.domain.Application;
import com.mauvaisetroupe.eadesignit.domain.FlowInterface;
import com.mauvaisetroupe.eadesignit.domain.FunctionalFlow;
import com.mauvaisetroupe.eadesignit.domain.LandscapeView;
import javax.xml.parsers.ParserConfigurationException;
import org.junit.jupiter.api.Test;

public class MXFileSerializerTest {

    @Test
    public void test() throws ParserConfigurationException {
        LandscapeView landscapeView = new LandscapeView();
        landscapeView.setId(1L);
        landscapeView.setDiagramName("diagramName");
        FunctionalFlow functionalFlow = new FunctionalFlow();
        functionalFlow.setAlias("I.01");
        functionalFlow.setId(01L);
        Application app1 = getApplication("APP1");
        Application app2 = getApplication("APP2");
        Application app3 = getApplication("APP3");
        Application app4 = getApplication("APP4");

        functionalFlow.addInterfaces(creteInterface(1L, functionalFlow, app1, app2));
        functionalFlow.addInterfaces(creteInterface(2L, functionalFlow, app3, app2));
        functionalFlow.addInterfaces(creteInterface(3L, functionalFlow, app1, app4));
        landscapeView.addFlows(functionalFlow);

        MXFileSerializer mxFileSerializer = new MXFileSerializer(landscapeView);
        System.out.println(mxFileSerializer.createMXFileXML());
    }

    private FlowInterface creteInterface(Long id, FunctionalFlow functionalFlow, Application source, Application target) {
        FlowInterface flowInterface = new FlowInterface();
        flowInterface.setId(id);
        flowInterface.setSource(source);
        flowInterface.setTarget(target);
        return flowInterface;
    }

    private Application getApplication(String applicationName) {
        Application a = new Application();
        a.setName(applicationName);
        a.setId(0L + applicationName.hashCode());
        return a;
    }
}
