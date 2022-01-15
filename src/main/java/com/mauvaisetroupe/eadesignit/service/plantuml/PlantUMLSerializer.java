package com.mauvaisetroupe.eadesignit.service.plantuml;

import com.mauvaisetroupe.eadesignit.domain.Application;
import com.mauvaisetroupe.eadesignit.domain.FlowInterface;
import com.mauvaisetroupe.eadesignit.domain.FunctionalFlow;
import com.mauvaisetroupe.eadesignit.domain.LandscapeView;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
        Map<SourceTarget, Set<FunctionalFlow>> relationships = new HashMap<>();
        for (FunctionalFlow functionalFlow : sortFlow(landscapeView.getFlows())) {
            for (FlowInterface flowInterface : sortInterface(functionalFlow.getInterfaces())) {
                SourceTarget key = new SourceTarget(flowInterface.getSource(), flowInterface.getTarget());
                if (!relationships.containsKey(key)) {
                    relationships.put(key, new HashSet<>());
                }
                relationships.get(key).add(functionalFlow);
            }
        }

        StringBuilder plantUMLSource = new StringBuilder();
        plantUMLBuilder.getPlantumlHeader(plantUMLSource);
        for (SourceTarget sourceTarget : relationships.keySet()) {
            List<String[]> labelAndURLs = new ArrayList<>();
            for (FunctionalFlow functionalFlow : relationships.get(sourceTarget)) {
                String url = "/functional-flow/" + functionalFlow.getId() + "/view";
                String label = functionalFlow.getAlias();
                String[] labelAndURL = { label, url };
                labelAndURLs.add(labelAndURL);
            }
            plantUMLBuilder.getPlantumlRelationShip(plantUMLSource, sourceTarget.getSource(), sourceTarget.getTarget(), labelAndURLs);
        }
        plantUMLBuilder.getPlantumlFooter(plantUMLSource);

        return plantUMLBuilder.getSVGFromSource(plantUMLSource.toString());
    }

    public String getSVG(FunctionalFlow functionalFlow) throws IOException {
        return getSVG(functionalFlow.getInterfaces());
    }

    public String getSVG(Set<FlowInterface> interfaces) throws IOException {
        Map<SourceTarget, Set<FlowInterface>> relationships = new HashMap<>();
        for (FlowInterface flowInterface : sortInterface(interfaces)) {
            SourceTarget key = new SourceTarget(flowInterface.getSource(), flowInterface.getTarget());
            if (!relationships.containsKey(key)) {
                relationships.put(key, new HashSet<>());
            }
            relationships.get(key).add(flowInterface);
        }

        StringBuilder plantUMLSource = new StringBuilder();
        plantUMLBuilder.getPlantumlHeader(plantUMLSource);
        for (SourceTarget sourceTarget : relationships.keySet()) {
            List<String[]> labelAndURLs = new ArrayList<>();
            for (FlowInterface flowInterface : relationships.get(sourceTarget)) {
                String label = flowInterface.getAlias();
                String url = "/flow-interface/" + flowInterface.getId() + "/view";
                labelAndURLs.add(new String[] { label, url });
            }
            plantUMLBuilder.getPlantumlRelationShip(plantUMLSource, sourceTarget.getSource(), sourceTarget.getTarget(), labelAndURLs);
        }
        plantUMLBuilder.getPlantumlFooter(plantUMLSource);
        return plantUMLBuilder.getSVGFromSource(plantUMLSource.toString());
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

    public String getCapabilitiesSVG(Application application) throws IOException {
        StringBuilder plantUMLSource = new StringBuilder();
        plantUMLBuilder.getPlantumlHeader(plantUMLSource);
        plantUMLBuilder.getPlantumlCapabilities(plantUMLSource, application.getCapabilities());
        plantUMLBuilder.getPlantumlFooter(plantUMLSource);
        return plantUMLBuilder.getSVGFromSource(plantUMLSource.toString());
    }
}
