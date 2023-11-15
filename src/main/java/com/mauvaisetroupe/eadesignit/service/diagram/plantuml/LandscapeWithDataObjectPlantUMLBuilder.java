package com.mauvaisetroupe.eadesignit.service.diagram.plantuml;

import com.mauvaisetroupe.eadesignit.domain.Application;
import com.mauvaisetroupe.eadesignit.domain.DataObject;
import com.mauvaisetroupe.eadesignit.domain.FlowInterface;
import com.mauvaisetroupe.eadesignit.domain.LandscapeView;
import com.mauvaisetroupe.eadesignit.domain.enumeration.DataObjectType;
import com.mauvaisetroupe.eadesignit.service.diagram.plantuml.PlantUMLBuilder.Layout;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LandscapeWithDataObjectPlantUMLBuilder {

    @Autowired
    private PlantUMLBuilder plantUMLBuilder;

    public String getLandscapeDiagramSVG(LandscapeView landscape) throws IOException {
        StringBuilder plantUMLSource = new StringBuilder();
        getPlantumlHeader(plantUMLSource);
        getPlantUMLBody(plantUMLSource, landscape);
        getPlantumlFooter(plantUMLSource);
        System.out.println(plantUMLSource);
        return plantUMLBuilder.getSVGFromSource(plantUMLSource.toString());
    }

    ///////////////////////////////////
    // HEADER
    ///////////////////////////////////

    public void getPlantumlHeader(StringBuilder plantUMLSource) {
        plantUMLSource.append("@startuml\n");
        plantUMLSource.append("!pragma layout " + Layout.elk + "\n");
        plantUMLSource.append("skinparam componentStyle rectangle\n");
        plantUMLSource.append("skinparam hyperlinkColor #000000\n");
        plantUMLSource.append("skinparam hyperlinkUnderline false\n");
        plantUMLSource.append("skinparam shadowing false\n");
        plantUMLSource.append("skinparam padding 5\n");

        plantUMLSource.append("skinparam component {\n");
        plantUMLSource.append("  backgroundColor white\n");
        plantUMLSource.append("  borderColor black\n");
        plantUMLSource.append("}\n");
        plantUMLSource.append("skinparam rectangleFontSize 10\n");

        plantUMLSource.append("hide footbox\n");
    }

    ///////////////////////////////////
    // BODY
    ///////////////////////////////////

    public void getPlantUMLBody(StringBuilder plantUMLSource, LandscapeView landscape) {
        Set<FlowInterface> allInterfaces = landscape
            .getFlows()
            .stream()
            .flatMap(f -> f.getInterfaces().stream())
            .collect(Collectors.toSet());

        // write sprites
        int dataObjectIndex = 1;
        Map<Long, Integer> dataObjectMap = new HashMap<>();
        List<DataObject> allDataObjects = new ArrayList<>(landscape.getDataObjects())
            .stream()
            .sorted(Comparator.comparing(DataObject::getId))
            .collect(Collectors.toList());
        for (DataObject dataObj : allDataObjects) {
            dataObjectMap.put(dataObj.getId(), Integer.valueOf(dataObjectIndex));
            plantUMLSource.append("sprite foo" + dataObjectIndex + " " + getSprite(dataObjectIndex, dataObj.getType()) + " \n");
            dataObjectIndex++;
        }

        // We implement a grouped / bidirectional landscape (one single arrow between application, double if needed)
        EdgeMap edgeMap = new EdgeMap(allInterfaces);

        for (EdgeMap.Edge edge : edgeMap.bidirectionalConsolidatedMap.values()) {
            plantUMLSource.append(getComponent(edge.source, dataObjectMap, edgeMap.getNumberConnections(edge.source.getId())));
            plantUMLSource.append((edge.bidirectional ? "<-->" : "-->"));
            plantUMLSource.append(getComponent(edge.target, dataObjectMap, edgeMap.getNumberConnections(edge.target.getId())) + " \n");
        }
    }

    private String getComponent(Application application, Map<Long, Integer> dataObjectMap, int nbConnection) {
        StringBuilder component = new StringBuilder();

        String space = getSpaces(application.getName(), nbConnection, Layout.elk);
        component.append("[");
        component.append(space + application.getName() + space);
        boolean firstDataObject = true;
        if (application.getDataObjects() != null && application.getDataObjects().size() > 0) {
            for (DataObject dataObject : application.getDataObjects()) {
                if (dataObjectMap.containsKey(dataObject.getId())) {
                    // some dataobjects are not presnet in this landscape, do not display them
                    if (firstDataObject) {
                        component.append(" \\n\\n ");
                    }
                    component.append(" <$foo" + dataObjectMap.get(dataObject.getId()) + "> ");
                    firstDataObject = false;
                }
            }
        }
        component.append("]");
        return component.toString();
    }

    ///////////////////////////////////
    // FOOTER
    ///////////////////////////////////

    public void getPlantumlFooter(StringBuilder plantUMLSource) {
        if (!plantUMLSource.toString().endsWith("\n")) {
            plantUMLSource.append("\n");
        }
        plantUMLSource.append("@enduml\n");
    }

    ///////////////////////////////////
    // Helpers
    ///////////////////////////////////

    private String getSprite(int i, DataObjectType goldenReplicaType) {
        String color1, color2;
        if (goldenReplicaType == DataObjectType.GOLDEN_SOURCE) {
            color1 = "#FFC000";
            color2 = "#ED8B00";
        } else {
            color1 = "#CCDAFF";
            color2 = "#A6B9F7";
        }
        String number = String.format("%02d", i);
        String template =
            "<svg width=\"30\" height=\"30\"><g fill=\"" +
            color1 +
            "\"><ellipse ry=\"5\" rx=\"15\" cy=\"25\" cx=\"15\"/><path d=\"M0 5h30v20H0z\"/></g><ellipse fill=\"" +
            color2 +
            "\" ry=\"5\" rx=\"15\" cy=\"5\" cx=\"15\"/><text fill=\"#123456\" font-size=\"15\" y=\"25\" x=\"4\">" +
            number +
            "</text></svg>";
        return template;
    }

    private String getSpaces(String name, int nbConnection, Layout layout) {
        int factor = layout == Layout.elk ? 3 : 1;
        return " ".repeat(Math.max(0, factor * nbConnection - name.length()));
    }
}

final class EdgeMap {

    protected class Edge {

        protected Application source;
        protected Application target;
        protected boolean bidirectional;
    }

    protected Map<String, Edge> bidirectionalConsolidatedMap = new HashMap<>();

    protected EdgeMap(Set<FlowInterface> allInterfaces) {
        for (FlowInterface flowInterface : allInterfaces) {
            String consolidatedKey = getConsolidatedKey(flowInterface);
            Edge _edge = bidirectionalConsolidatedMap.get(consolidatedKey);
            if (_edge == null) {
                _edge = new Edge();
                _edge.source = flowInterface.getSource();
                _edge.target = flowInterface.getTarget();
                _edge.bidirectional = false;
                this.bidirectionalConsolidatedMap.put(consolidatedKey, _edge);
            } else {
                if (!_edge.source.getId().equals(flowInterface.getSource().getId())) {
                    _edge.bidirectional = true;
                }
            }
        }
    }

    protected int getNumberConnections(Long applicationId) {
        int nbConnection = 0;
        for (Edge edge : bidirectionalConsolidatedMap.values()) {
            if (edge.source.getId() == applicationId || edge.target.getId() == applicationId) {
                nbConnection++;
            }
        }
        return nbConnection;
    }

    private static String getConsolidatedKey(FlowInterface flowInterface) {
        return (
            Math.min(flowInterface.getSource().getId(), flowInterface.getTarget().getId()) +
            "-" +
            Math.max(flowInterface.getSource().getId(), flowInterface.getTarget().getId())
        );
    }
}
