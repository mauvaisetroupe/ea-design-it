package com.mauvaisetroupe.eadesignit.service.plantuml;

import com.mauvaisetroupe.eadesignit.domain.FlowInterface;
import com.mauvaisetroupe.eadesignit.domain.FunctionalFlow;
import com.mauvaisetroupe.eadesignit.domain.LandscapeView;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlantUMLSerializer {

    private final Logger log = LoggerFactory.getLogger(PlantUMLSerializer.class);

    @Autowired
    private PlantUMLBuilder plantUMLBuilder;

    public String getSVG(LandscapeView landscapeView) throws IOException {
        String plantUMLSource = plantUMLBuilder.getPlantumlHeader();
        for (FunctionalFlow functionalFlow : sortFlow(landscapeView.getFlows())) {
            for (FlowInterface flowInterface : sortInterface(functionalFlow.getInterfaces())) {
                String url = "/functional-flow/" + functionalFlow.getId() + "/view";
                String label = functionalFlow.getAlias();
                plantUMLSource =
                    plantUMLBuilder.getPlantumlRelationShip(
                        plantUMLSource,
                        flowInterface.getSource(),
                        flowInterface.getTarget(),
                        label,
                        url
                    );
            }
        }
        plantUMLSource = plantUMLBuilder.getPlantumlFooter(plantUMLSource);
        return plantUMLBuilder.getSVGFromSource(plantUMLSource);
    }

    public String getSVG(FunctionalFlow functionalFlow) throws IOException {
        return getSVG(functionalFlow.getInterfaces());
    }

    public String getSVG(Set<FlowInterface> interfaces) throws IOException {
        String plantUMLSource = plantUMLBuilder.getPlantumlHeader();
        for (FlowInterface flowInterface : sortInterface(interfaces)) {
            String url = "/flow-interface/" + flowInterface.getId() + "/view";
            plantUMLSource =
                plantUMLBuilder.getPlantumlRelationShip(
                    plantUMLSource,
                    flowInterface.getSource(),
                    flowInterface.getTarget(),
                    flowInterface.getAlias(),
                    url
                );
        }
        plantUMLSource = plantUMLBuilder.getPlantumlFooter(plantUMLSource);
        return plantUMLBuilder.getSVGFromSource(plantUMLSource);
    }

    private List<FlowInterface> sortInterface(Set<FlowInterface> interfaces) {
        List<FlowInterface> sortedList = new ArrayList<>(interfaces);
        Collections.sort(sortedList);
        return sortedList;
    }

    private List<FunctionalFlow> sortFlow(Set<FunctionalFlow> flows) {
        List<FunctionalFlow> sortedList = new ArrayList<>(flows);
        Collections.sort(sortedList);
        return sortedList;
    }
}
