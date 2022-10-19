package com.mauvaisetroupe.eadesignit.service.drawio;

import com.mauvaisetroupe.eadesignit.domain.ApplicationComponent;
import com.mauvaisetroupe.eadesignit.domain.FlowInterface;
import com.mauvaisetroupe.eadesignit.domain.FunctionalFlow;
import com.mauvaisetroupe.eadesignit.domain.FunctionalFlowStep;
import com.mauvaisetroupe.eadesignit.domain.LandscapeView;
import com.mauvaisetroupe.eadesignit.repository.view.FlowInterfaceLight;
import com.mauvaisetroupe.eadesignit.service.drawio.dto.Application;
import com.mauvaisetroupe.eadesignit.service.drawio.dto.Edge;
import com.mauvaisetroupe.eadesignit.service.drawio.dto.GraphDTO;
import com.mauvaisetroupe.eadesignit.service.drawio.dto.Label;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.apache.commons.text.WordUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class GraphBuilder {

    public static final String KEY_PROTOCOL = "protocol";

    public GraphDTO createGraph(LandscapeView landscape) {
        GraphDTO graph = new GraphDTO();
        for (FunctionalFlow flow : landscape.getFlows()) {
            for (FunctionalFlowStep step : flow.getSteps()) {
                FlowInterface interface1 = step.getFlowInterface();
                Application source = getApplication(interface1.getSource(), interface1.getSourceComponent());
                graph.addApplication(source);
                Application target = getApplication(interface1.getTarget(), interface1.getTargetComponent());
                graph.addApplication(target);
                String id = flow.getId() + "-" + interface1.getId();
                String url = "/functional-flow/" + flow.getId() + "/view";
                Edge edge = new Edge(id, source.getId(), target.getId(), new Label(flow.getAlias(), url), false);
                graph.addEdge(edge);
            }
        }
        graph.consolidateEdges();
        return graph;
    }

    public GraphDTO createGraph(FunctionalFlow flow) {
        GraphDTO graph = new GraphDTO();
        for (FunctionalFlowStep step : flow.getSteps()) {
            FlowInterface interface1 = step.getFlowInterface();
            Application source = getApplication(interface1.getSource(), interface1.getSourceComponent());
            graph.addApplication(source);
            Application target = getApplication(interface1.getTarget(), interface1.getTargetComponent());
            graph.addApplication(target);
            String id = flow.getId() + "-" + interface1.getId();
            String _label = step.getStepOrder() + ". " + WordUtils.wrap(step.getDescription(), 50, "\\n", false);
            Label label = new Label(_label, null);
            if (interface1.getProtocol() != null) {
                label.addMetadata(KEY_PROTOCOL, interface1.getProtocol().getName());
            }
            Edge edge = new Edge(id, source.getId(), target.getId(), label, false);
            graph.addEdge(edge);
        }
        graph.consolidateEdges();
        return graph;
    }

    public GraphDTO createGraph(SortedSet<FlowInterfaceLight> interfaces) {
        GraphDTO graph = new GraphDTO();
        for (FlowInterfaceLight interface1 : interfaces) {
            Application source = getApplication(interface1.getSource(), interface1.getSourceComponent());
            graph.addApplication(source);
            Application target = getApplication(interface1.getTarget(), interface1.getTargetComponent());
            graph.addApplication(target);
            Long id = interface1.getId();
            String label = interface1.getAlias();
            String url = "/flow-interface/" + interface1.getId() + "/view";
            Edge edge = new Edge("" + id, source.getId(), target.getId(), new Label(label, url), false);
            graph.addEdge(edge);
        }
        graph.consolidateEdges();
        return graph;
    }

    private Application getApplication(com.mauvaisetroupe.eadesignit.domain.Application application, ApplicationComponent component) {
        if (component != null && component.getDisplayInLandscape() != null && component.getDisplayInLandscape()) {
            return new Application(
                component.getId(),
                application.getName() + " / " + component.getName(),
                "/application-component/" + component.getId() + "/view"
            );
        } else {
            return new Application(application.getId(), application.getName(), "/application/" + application.getId() + "/view");
        }
    }

    public GraphDTO createGraph(Document doc) throws XPathExpressionException {
        GraphDTO graph = new GraphDTO();
        Map<Long, Application> aMap = new HashMap<>();

        // applications in exisiting XML
        XPathFactory xpathfactory = XPathFactory.newInstance();
        XPath xpath = xpathfactory.newXPath();
        NodeList nodeList = (NodeList) xpath.evaluate(
            "//mxCell[starts-with(@elementId,'" + MXFileSerializer.APP_ID_PREFIX + "')]",
            doc,
            XPathConstants.NODESET
        );
        for (int i = 0; i < nodeList.getLength(); i++) {
            String elementIdValue = ((Element) nodeList.item(i)).getAttribute("elementId");
            Long id = Long.parseLong(elementIdValue.replace(MXFileSerializer.APP_ID_PREFIX, ""));
            String applicationName = ((Element) nodeList.item(i)).getAttribute("value");
            Application application = new Application(id, applicationName, null);
            graph.addApplication(application);
            aMap.put(application.getId(), application);
        }

        xpath = xpathfactory.newXPath();
        nodeList =
            (NodeList) xpath.evaluate(
                "//mxCell[starts-with(@elementId,'" + MXFileSerializer.EDGE_ID_PREFIX + "')]",
                doc,
                XPathConstants.NODESET
            );
        for (int i = 0; i < nodeList.getLength(); i++) {
            String elementIdValue = ((Element) nodeList.item(i)).getAttribute("elementId").replace(MXFileSerializer.EDGE_ID_PREFIX, "");

            String _sourceId = ((Element) nodeList.item(i)).getAttribute("sourceId");
            Long sourceId = Long.parseLong(_sourceId.replace(MXFileSerializer.APP_ID_PREFIX, ""));

            String _targetId = ((Element) nodeList.item(i)).getAttribute("targetId");
            Long targetId = Long.parseLong(_targetId.replace(MXFileSerializer.APP_ID_PREFIX, ""));

            String labels = ((Element) nodeList.item(i)).getAttribute("value");

            String style = ((Element) nodeList.item(i)).getAttribute("style");
            boolean bidirectional = false;
            if (style.toLowerCase().contains("startarrow=classic;") && style.toLowerCase().contains("endarrow=classic;")) {
                bidirectional = true;
            }

            Edge edge = new Edge(elementIdValue, sourceId, targetId, new Label(labels, null), bidirectional);
            graph.addEdge(edge);
        }
        graph.consolidateEdges();
        return graph;
    }
}
