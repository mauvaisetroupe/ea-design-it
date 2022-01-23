package com.mauvaisetroupe.eadesignit.service.plantuml;

import com.mauvaisetroupe.eadesignit.domain.Capability;
import com.mauvaisetroupe.eadesignit.domain.FlowInterface;
import com.mauvaisetroupe.eadesignit.domain.FunctionalFlow;
import com.mauvaisetroupe.eadesignit.domain.FunctionalFlowStep;
import com.mauvaisetroupe.eadesignit.domain.LandscapeView;
import com.mauvaisetroupe.eadesignit.service.importfile.dto.CapabilityDTO;
import com.mauvaisetroupe.eadesignit.service.importfile.util.CapabilityUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
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
        LinkedHashMap<SourceTarget, SortedSet<FunctionalFlow>> relationships = new LinkedHashMap<>();
        for (FunctionalFlow functionalFlow : landscapeView.getFlows()) {
            for (FlowInterface flowInterface : functionalFlow.getInterfaces()) {
                SourceTarget key = new SourceTarget(flowInterface.getSource(), flowInterface.getTarget());
                if (!relationships.containsKey(key)) {
                    relationships.put(key, new TreeSet<>());
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
        StringBuilder plantUMLSource = new StringBuilder();
        plantUMLBuilder.getPlantumlHeader(plantUMLSource);
        for (FunctionalFlowStep step : functionalFlow.getSteps()) {
            List<String[]> labelAndURLs = new ArrayList<>();
            String label = step.getStepOrder() + ". " + step.getDescription();
            labelAndURLs.add(new String[] { label, null });
            plantUMLBuilder.getPlantumlRelationShip(
                plantUMLSource,
                step.getFlowInterface().getSource(),
                step.getFlowInterface().getTarget(),
                labelAndURLs
            );
        }
        plantUMLBuilder.getPlantumlFooter(plantUMLSource);
        return plantUMLBuilder.getSVGFromSource(plantUMLSource.toString());
    }

    public String getSVG(SortedSet<FlowInterface> interfaces) throws IOException {
        LinkedHashMap<SourceTarget, SortedSet<FlowInterface>> relationships = new LinkedHashMap<>();
        for (FlowInterface flowInterface : interfaces) {
            SourceTarget key = new SourceTarget(flowInterface.getSource(), flowInterface.getTarget());
            if (!relationships.containsKey(key)) {
                relationships.put(key, new TreeSet<>());
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

    public String getCapabilitiesFromLeavesSVG(Collection<Capability> capabilities) throws IOException {
        StringBuilder plantUMLSource = new StringBuilder();
        plantUMLBuilder.getPlantumlHeader(plantUMLSource);

        CapabilityUtil capabilityUtil = new CapabilityUtil();
        Collection<CapabilityDTO> rootDTO = capabilityUtil.getRoot(capabilities);
        plantUMLBuilder.getPlantumlCapabilitiesDTO(plantUMLSource, rootDTO);
        plantUMLBuilder.getPlantumlFooter(plantUMLSource);
        System.out.println(plantUMLSource);
        return plantUMLBuilder.getSVGFromSource(plantUMLSource.toString());
    }

    public String getCapabilitiesFromRootsSVG(Collection<Capability> capabilities) throws IOException {
        StringBuilder plantUMLSource = new StringBuilder();
        plantUMLBuilder.getPlantumlHeader(plantUMLSource);
        plantUMLBuilder.getPlantumlCapabilities(plantUMLSource, capabilities);
        plantUMLBuilder.getPlantumlFooter(plantUMLSource);
        System.out.println(plantUMLSource);
        return plantUMLBuilder.getSVGFromSource(plantUMLSource.toString());
    }
}
