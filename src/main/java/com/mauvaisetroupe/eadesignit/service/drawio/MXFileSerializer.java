package com.mauvaisetroupe.eadesignit.service.drawio;

import com.mauvaisetroupe.eadesignit.domain.Application;
import com.mauvaisetroupe.eadesignit.domain.FlowInterface;
import com.mauvaisetroupe.eadesignit.domain.FunctionalFlow;
import com.mauvaisetroupe.eadesignit.domain.LandscapeView;
import java.io.StringWriter;
import java.util.HashSet;
import java.util.Set;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class MXFileSerializer {

    private static final String EDGE_ID_PREFIX = "edge_";
    private static final String APP_ID_PREFIX = "app_";

    private static final String CELL_DEFAULT_WIDTH = "70";
    private static final String CELL_DEFAULT_HEIGHT = "50";

    private Document doc;
    private LandscapeView landscapeView;

    public MXFileSerializer(LandscapeView landscapeView) throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        doc = builder.newDocument();
        this.landscapeView = landscapeView;
    }

    public String createMXFileXML() throws ParserConfigurationException {
        Element mxfile = (Element) doc.appendChild(doc.createElement("mxfile"));
        Element diagram = createElement(mxfile, "diagram");
        Element mxGraphModel = createElement(diagram, "mxGraphModel");
        Element root = createElement(mxGraphModel, "root");
        Element cell0 = createElement(root, "mxCell");
        cell0.setAttribute("id", "0");
        Element cell1 = createElement(root, "mxCell");
        cell1.setAttribute("id", "1");
        cell1.setAttribute("parent", "0");

        for (Application application : getDistinctApplications(landscapeView)) {
            createRectangle(root, application.getId().toString(), application.getName());
        }

        for (FunctionalFlow flow : this.landscapeView.getFlows()) {
            for (FlowInterface inter : flow.getInterfaces()) {
                createEdge(root, flow, inter);
            }
        }

        return getStringFromDocument(doc);
    }

    private void createEdge(Element root, FunctionalFlow flow, FlowInterface inter) {
        Element mxCell = createElement(root, "mxCell");
        mxCell.setAttribute("id", EDGE_ID_PREFIX + flow.getId() + "_" + inter.getId());
        mxCell.setAttribute("value", flow.getAlias() + " / " + inter.getAlias());
        mxCell.setAttribute("style", "endArrow=classic;html=1;rounded=0;");
        mxCell.setAttribute("parent", "1");
        mxCell.setAttribute("edge", "1");
        mxCell.setAttribute("source", APP_ID_PREFIX + inter.getSource().getId());
        mxCell.setAttribute("target", APP_ID_PREFIX + inter.getTarget().getId());
        mxCell.setAttribute("sourceID", APP_ID_PREFIX + inter.getSource().getId());
        mxCell.setAttribute("targetID", APP_ID_PREFIX + inter.getTarget().getId());

        Element geometry = createElement(mxCell, "mxGeometry");
        geometry.setAttribute("relative", "1");
        geometry.setAttribute("as", "geometry");

        Element mxPoint1 = createElement(geometry, "mxPoint");
        mxPoint1.setAttribute("as", "sourcePoint");

        Element mxPoint2 = createElement(geometry, "mxPoint");
        mxPoint2.setAttribute("as", "targetPoint");
    }

    private Set<Application> getDistinctApplications(LandscapeView landscapeView2) {
        Set<Application> result = new HashSet<Application>();
        for (FunctionalFlow flow : this.landscapeView.getFlows()) {
            for (FlowInterface inter : flow.getInterfaces()) {
                result.add(inter.getSource());
                result.add(inter.getTarget());
            }
        }
        return result;
    }

    private String getStringFromDocument(Document doc) {
        try {
            DOMSource domSource = new DOMSource(doc);
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.transform(domSource, result);
            return writer.toString();
        } catch (TransformerException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private Element createElement(Element parent, String name) {
        return (Element) parent.appendChild(doc.createElement(name));
    }

    private void createRectangle(Element root, String id, String label) {
        Element mxCell = createElement(root, "mxCell");
        // ID
        mxCell.setAttribute("id", APP_ID_PREFIX + id);
        mxCell.setAttribute("elementId", id);
        // Parent ID
        mxCell.setAttribute("parent", "1");
        // Label
        mxCell.setAttribute("value", label);
        mxCell.setAttribute("style", "rounded=0;whiteSpace=wrap;html=1;");
        mxCell.setAttribute("vertex", "1");

        Element geometry = createElement(mxCell, "mxGeometry");
        geometry.setAttribute("width", CELL_DEFAULT_WIDTH);
        geometry.setAttribute("height", CELL_DEFAULT_HEIGHT);
        geometry.setAttribute("as", "geometry");
    }
}
