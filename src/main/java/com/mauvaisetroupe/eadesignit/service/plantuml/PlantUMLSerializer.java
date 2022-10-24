package com.mauvaisetroupe.eadesignit.service.plantuml;

import com.mauvaisetroupe.eadesignit.domain.Capability;
import com.mauvaisetroupe.eadesignit.domain.FunctionalFlow;
import com.mauvaisetroupe.eadesignit.domain.LandscapeView;
import com.mauvaisetroupe.eadesignit.repository.view.FlowInterfaceLight;
import com.mauvaisetroupe.eadesignit.service.drawio.GraphBuilder;
import com.mauvaisetroupe.eadesignit.service.drawio.dto.Application;
import com.mauvaisetroupe.eadesignit.service.drawio.dto.Edge;
import com.mauvaisetroupe.eadesignit.service.drawio.dto.GraphDTO;
import com.mauvaisetroupe.eadesignit.service.importfile.dto.CapabilityDTO;
import com.mauvaisetroupe.eadesignit.service.importfile.util.CapabilityUtil;
import com.mauvaisetroupe.eadesignit.service.plantuml.PlantUMLBuilder.Layout;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.SortedSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlantUMLSerializer {

    private final Logger log = LoggerFactory.getLogger(PlantUMLSerializer.class);

    @Autowired
    private PlantUMLBuilder plantUMLBuilder;

    public String getSource(LandscapeView landscapeView, boolean sequenceDiagram) throws IOException {
        GraphBuilder graphBuilder = new GraphBuilder();
        GraphDTO graph = graphBuilder.createGraph(landscapeView);

        List<String[]> legend = new ArrayList<>();
        legend.add(new String[] { "Flows", "Description" });
        for (FunctionalFlow functionalFlow : landscapeView.getFlows()) {
            legend.add(new String[] { functionalFlow.getAlias(), functionalFlow.getDescription() });
        }

        StringBuilder plantUMLSource = new StringBuilder();
        plantUMLBuilder.getPlantumlHeader(plantUMLSource);

        for (Edge edge : graph.getConsolidatedEdges()) {
            Application source = graph.getApplication(edge.getSourceId());
            Application target = graph.getApplication(edge.getTargetId());
            plantUMLBuilder.getPlantumlRelationShip(plantUMLSource, source, target, edge.getLabels(), sequenceDiagram, false);
        }

        plantUMLBuilder.getLegend(plantUMLSource, legend);
        plantUMLBuilder.getPlantumlFooter(plantUMLSource);

        return plantUMLSource.toString();
    }

    public String getSVG(LandscapeView landscapeView, boolean sequenceDiagram, Layout layout) throws IOException {
        StringBuilder plantUMLSource = new StringBuilder();
        plantUMLBuilder.getPlantumlHeader(plantUMLSource, layout);
        GraphBuilder graphBuilder = new GraphBuilder();
        GraphDTO graph = graphBuilder.createGraph(landscapeView);

        for (Application application : graph.getApplications()) {
            plantUMLBuilder.createComponent(plantUMLSource, application, sequenceDiagram);
        }

        for (Edge edge : graph.getConsolidatedEdges()) {
            Application source = graph.getApplication(edge.getSourceId());
            Application target = graph.getApplication(edge.getTargetId());
            plantUMLBuilder.getPlantumlRelationShip(plantUMLSource, source, target, edge.getLabels(), sequenceDiagram, true);
        }
        plantUMLBuilder.getPlantumlFooter(plantUMLSource);

        return plantUMLBuilder.getSVGFromSource(plantUMLSource.toString());
    }

    public String getSVG(FunctionalFlow functionalFlow, boolean sequenceDiagram) throws IOException {
        StringBuilder plantUMLSource = new StringBuilder();
        plantUMLBuilder.getPlantumlHeader(plantUMLSource);

        GraphBuilder graphBuilder = new GraphBuilder();
        GraphDTO graph = graphBuilder.createGraph(functionalFlow);

        for (Application application : graph.getApplications()) {
            plantUMLBuilder.createComponent(plantUMLSource, application, sequenceDiagram);
        }

        for (Edge edge : graph.getEdges()) {
            Application source = graph.getApplication(edge.getSourceId());
            Application target = graph.getApplication(edge.getTargetId());
            plantUMLBuilder.getPlantumlRelationShip(plantUMLSource, source, target, edge.getLabels(), sequenceDiagram, true);
        }
        plantUMLBuilder.getPlantumlFooter(plantUMLSource);
        return plantUMLBuilder.getSVGFromSource(plantUMLSource.toString());
    }

    public String getSource(FunctionalFlow functionalFlow, boolean sequenceDiagram) throws IOException {
        StringBuilder plantUMLSource = new StringBuilder();
        plantUMLBuilder.getPlantumlHeader(plantUMLSource);

        GraphBuilder graphBuilder = new GraphBuilder();
        GraphDTO graph = graphBuilder.createGraph(functionalFlow);

        for (Edge edge : graph.getEdges()) {
            Application source = graph.getApplication(edge.getSourceId());
            Application target = graph.getApplication(edge.getTargetId());
            plantUMLBuilder.getPlantumlRelationShip(plantUMLSource, source, target, edge.getLabels(), sequenceDiagram, false);
        }
        plantUMLBuilder.getPlantumlFooter(plantUMLSource);
        return plantUMLSource.toString();
    }

    public String getSVG(SortedSet<FlowInterfaceLight> interfaces, boolean sequenceDiagram) throws IOException {
        GraphBuilder graphBuilder = new GraphBuilder();
        GraphDTO graph = graphBuilder.createGraph(interfaces);

        StringBuilder plantUMLSource = new StringBuilder();
        plantUMLBuilder.getPlantumlHeader(plantUMLSource);

        for (Application application : graph.getApplications()) {
            plantUMLBuilder.createComponent(plantUMLSource, application, sequenceDiagram);
        }

        for (Edge edge : graph.getConsolidatedEdges()) {
            Application source = graph.getApplication(edge.getSourceId());
            Application target = graph.getApplication(edge.getTargetId());
            plantUMLBuilder.getPlantumlRelationShip(plantUMLSource, source, target, edge.getLabels(), sequenceDiagram, true);
        }
        plantUMLBuilder.getPlantumlFooter(plantUMLSource);
        return plantUMLBuilder.getSVGFromSource(plantUMLSource.toString());
    }

    public String getSource(SortedSet<FlowInterfaceLight> interfaces, boolean sequenceDiagram) {
        List<String[]> legend = new ArrayList<>();
        legend.add(new String[] { "Interface", "Source", "Target", "Protocol" });
        for (FlowInterfaceLight flowInterface : interfaces) {
            legend.add(
                new String[] {
                    flowInterface.getAlias(),
                    flowInterface.getSource().getName(),
                    flowInterface.getTarget().getName(),
                    flowInterface.getProtocol() != null ? flowInterface.getProtocol().getName() : "",
                }
            );
        }

        StringBuilder plantUMLSource = new StringBuilder();
        plantUMLBuilder.getPlantumlHeader(plantUMLSource);
        GraphBuilder graphBuilder = new GraphBuilder();
        GraphDTO graph = graphBuilder.createGraph(interfaces);
        for (Edge edge : graph.getConsolidatedEdges()) {
            Application source = graph.getApplication(edge.getSourceId());
            Application target = graph.getApplication(edge.getTargetId());
            plantUMLBuilder.getPlantumlRelationShip(plantUMLSource, source, target, edge.getLabels(), sequenceDiagram, false);
        }

        plantUMLBuilder.getLegend(plantUMLSource, legend);
        plantUMLBuilder.getPlantumlFooter(plantUMLSource);
        return plantUMLSource.toString();
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

    public String getSVG(String plantUMLSource) throws IOException {
        return plantUMLBuilder.getSVGFromSource(plantUMLSource);
    }
}
