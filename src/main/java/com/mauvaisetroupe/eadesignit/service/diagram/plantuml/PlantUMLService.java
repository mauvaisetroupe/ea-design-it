package com.mauvaisetroupe.eadesignit.service.diagram.plantuml;

import com.mauvaisetroupe.eadesignit.domain.BusinessObject;
import com.mauvaisetroupe.eadesignit.domain.DataObject;
import com.mauvaisetroupe.eadesignit.domain.FunctionalFlow;
import com.mauvaisetroupe.eadesignit.domain.IFlowInterface;
import com.mauvaisetroupe.eadesignit.domain.LandscapeView;
import com.mauvaisetroupe.eadesignit.service.diagram.dto.Application;
import com.mauvaisetroupe.eadesignit.service.diagram.dto.Edge;
import com.mauvaisetroupe.eadesignit.service.diagram.dto.EdgeGroup;
import com.mauvaisetroupe.eadesignit.service.diagram.dto.GraphBuilder;
import com.mauvaisetroupe.eadesignit.service.diagram.dto.GraphDTO;
import com.mauvaisetroupe.eadesignit.service.diagram.plantuml.PlantUMLBuilder.Layout;
import java.io.IOException;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlantUMLService {

    private final Logger log = LoggerFactory.getLogger(PlantUMLService.class);

    public enum DiagramType {
        COMPONENT_DIAGRAM,
        SEQUENCE_DIAGRAM,
    }

    public enum ShowSubComponent {
        KEEP_DEFAULT,
        FORCE_SHOW,
        FORCE_HIDE,
    }

    public enum GroupSubComponent {
        GROUP,
        DO_NOT_GROUP,
    }

    @Autowired
    private PlantUMLBuilder plantUMLBuilder;

    @Autowired
    private BusinessObjectPlantUMLBuilder businessObjectPlantUMLBuilder;

    private String createPlantUMLSource(
        GraphDTO graph,
        DiagramType diagramType,
        boolean consolidatedEdge,
        boolean addURL,
        Layout layout,
        boolean groupComponents,
        boolean adaptWidth,
        boolean componentStyle
    ) {
        if (adaptWidth) {
            for (Application application : groupComponents ? graph.getApplicationsWithoutGroups() : graph.getApplications()) {
                String space = getSpaces(application.getName(), graph.getNbConnection(application), layout);
                application.setName(space + application.getName() + space);
            }
            for (List<Application> groupedAplications : graph.getApplicationGroups().values()) {
                for (Application application : groupedAplications) {
                    String space = getSpaces(application.getName(), graph.getNbConnection(application), layout);
                    application.setName(space + application.getName() + space);
                }
            }
        }

        StringBuilder plantUMLSource = new StringBuilder();
        plantUMLBuilder.getPlantumlHeader(plantUMLSource, layout, componentStyle);
        boolean useID = false;
        if (addURL) {
            // Declare application without the ones included in packages
            // Will be declared in package, including url declaretaion
            for (Application application : groupComponents ? graph.getApplicationsWithoutGroups() : graph.getApplications()) {
                // itś not possible to add an URL for an Application or an ApplicationComponent inside a reliation [A] -> [B]
                // so we need to create a component for each Application / ApplicationComponent
                // and associate the URL to that componnet
                plantUMLBuilder.createComponentWithId(plantUMLSource, application, diagramType);
                useID = true;
            }
        } else {
            // declare actor
            for (Application application : groupComponents ? graph.getApplicationsWithoutGroups() : graph.getApplications()) {
                plantUMLBuilder.createActor(plantUMLSource, application, diagramType, false);
            }
        }

        if (groupComponents) {
            // crerate groups (packages)
            for (Entry<String, List<Application>> groupEntry : graph.getApplicationGroups().entrySet()) {
                plantUMLBuilder.getPlantumlPackage(plantUMLSource, groupEntry.getKey(), groupEntry.getValue(), useID);
            }
        }

        for (Edge edge : consolidatedEdge ? graph.getConsolidatedEdges() : graph.getEdges()) {
            Application source = graph.getApplication(edge.getSourceId());
            Application target = graph.getApplication(edge.getTargetId());
            EdgeGroup startGroup = graph.isStartingGroup(edge);
            EdgeGroup endGroup = graph.isEndingGroup(edge);
            plantUMLBuilder.getPlantumlRelationShip(
                plantUMLSource,
                source,
                target,
                edge.getLabels(),
                diagramType,
                useID,
                addURL,
                startGroup,
                endGroup
            );
        }
        plantUMLBuilder.getPlantumlFooter(plantUMLSource);
        return plantUMLSource.toString();
    }

    private String getSpaces(String name, int nbConnection, Layout layout) {
        int factor = layout == Layout.elk ? 3 : 1;
        return " ".repeat(Math.max(0, factor * nbConnection - name.length()));
    }

    public String getLandscapeDiagramSVG(
        LandscapeView landscapeView,
        Layout layout,
        boolean groupComponents,
        boolean adaptWidth,
        boolean showLabels
    ) throws IOException {
        GraphBuilder graphBuilder = new GraphBuilder();
        GraphDTO graph = graphBuilder.createGraph(landscapeView, showLabels);
        String plantUMLSource = createPlantUMLSource(
            graph,
            DiagramType.COMPONENT_DIAGRAM,
            true,
            true,
            layout,
            groupComponents,
            adaptWidth,
            false
        );
        return plantUMLBuilder.getSVGFromSource(plantUMLSource.toString());
    }

    public String getLandscapeDiagramSource(LandscapeView landscapeView, Layout layout, boolean showLabels) throws IOException {
        GraphBuilder graphBuilder = new GraphBuilder();
        GraphDTO graph = graphBuilder.createGraph(landscapeView, showLabels);
        return createPlantUMLSource(graph, DiagramType.COMPONENT_DIAGRAM, true, false, Layout.none, false, false, false);
    }

    public String getFunctionalFlowDiagramSVG(FunctionalFlow functionalFlow, DiagramType diagramType) throws IOException {
        GraphBuilder graphBuilder = new GraphBuilder();
        GraphDTO graph = graphBuilder.createGraph(functionalFlow, true);
        String plantUMLSource = createPlantUMLSource(graph, diagramType, false, true, Layout.smetana, false, true, false);
        return plantUMLBuilder.getSVGFromSource(plantUMLSource.toString());
    }

    public String getFunctionalFlowDiagramSource(FunctionalFlow functionalFlow, DiagramType diagramType) throws IOException {
        GraphBuilder graphBuilder = new GraphBuilder();
        GraphDTO graph = graphBuilder.createGraph(functionalFlow, false);
        return createPlantUMLSource(graph, diagramType, false, false, Layout.smetana, false, false, false);
    }

    public String getInterfacesCollectionDiagramSVG(
        SortedSet<IFlowInterface> interfaces,
        Layout layout,
        boolean groupComponents,
        boolean showLabels
    ) throws IOException {
        GraphBuilder graphBuilder = new GraphBuilder();
        GraphDTO graph = graphBuilder.createGraph(interfaces, showLabels);
        String plantUMLSource = createPlantUMLSource(
            graph,
            DiagramType.COMPONENT_DIAGRAM,
            true,
            true,
            layout,
            groupComponents,
            true,
            false
        );
        return plantUMLBuilder.getSVGFromSource(plantUMLSource.toString());
    }

    public String getInterfacesCollectionDiagramSource(SortedSet<IFlowInterface> interfaces) {
        GraphBuilder graphBuilder = new GraphBuilder();
        GraphDTO graph = graphBuilder.createGraph(interfaces, true);
        String plantUMLSource = createPlantUMLSource(graph, DiagramType.COMPONENT_DIAGRAM, true, false, Layout.none, false, false, false);
        return plantUMLSource.toString();
    }

    public String getSVGFromSource(String plantUMLSource) throws IOException {
        return plantUMLBuilder.getSVGFromSource(plantUMLSource);
    }

    public String getApplicationStructureSVG(com.mauvaisetroupe.eadesignit.domain.Application application) throws IOException {
        StringBuilder plantUMLSource = new StringBuilder();
        plantUMLBuilder.getPlantumlHeader(plantUMLSource, Layout.smetana, true);
        plantUMLBuilder.getApplicationStructure(plantUMLSource, application);
        plantUMLBuilder.getPlantumlFooter(plantUMLSource);
        return plantUMLBuilder.getSVGFromSource(plantUMLSource.toString());
    }

    public String getDatObjectSVG(BusinessObject bo) throws IOException {
        StringBuilder plantUMLSource = new StringBuilder();
        businessObjectPlantUMLBuilder.getPlantumlHeader(plantUMLSource);
        businessObjectPlantUMLBuilder.getBusinessObjectsBusiness(plantUMLSource, bo);
        businessObjectPlantUMLBuilder.getPlantumlFooter(plantUMLSource);
        return businessObjectPlantUMLBuilder.getSVGFromSource(plantUMLSource.toString());
    }

    public String getDatObjectSVG(DataObject dataObject) throws IOException {
        StringBuilder plantUMLSource = new StringBuilder();
        businessObjectPlantUMLBuilder.getPlantumlHeader(plantUMLSource);
        businessObjectPlantUMLBuilder.getDataObjectsBusiness(plantUMLSource, dataObject);
        businessObjectPlantUMLBuilder.getPlantumlFooter(plantUMLSource);
        return businessObjectPlantUMLBuilder.getSVGFromSource(plantUMLSource.toString());
    }
}
