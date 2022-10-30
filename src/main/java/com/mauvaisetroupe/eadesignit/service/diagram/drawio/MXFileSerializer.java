package com.mauvaisetroupe.eadesignit.service.diagram.drawio;

import com.mauvaisetroupe.eadesignit.domain.LandscapeView;
import com.mauvaisetroupe.eadesignit.service.diagram.dto.Application;
import com.mauvaisetroupe.eadesignit.service.diagram.dto.Edge;
import com.mauvaisetroupe.eadesignit.service.diagram.dto.GraphBuilder;
import com.mauvaisetroupe.eadesignit.service.diagram.dto.GraphDTO;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class MXFileSerializer {

    private static final String COLOR_GREEN = "#00FF00";
    private static final String COLOR_RED = "#FF0000";
    public static final String EDGE_ID_PREFIX = "edge_";
    public static final String APP_ID_PREFIX = "app_";

    private static final String CELL_DEFAULT_WIDTH = "70";
    private static final String CELL_DEFAULT_HEIGHT = "50";

    private LandscapeView landscapeView;

    public MXFileSerializer(LandscapeView landscapeView) throws ParserConfigurationException {
        this.landscapeView = landscapeView;
    }

    public String createMXFileXML(String svgXML) throws ParserConfigurationException {
        GraphBuilder graphBuilder = new GraphBuilder();
        GraphDTO graphDTO = graphBuilder.createGraph(landscapeView);

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();

        Element mxfile = (Element) doc.appendChild(doc.createElement("mxfile"));
        Element diagram = createElement(doc, mxfile, "diagram");
        Element mxGraphModel = createElement(doc, diagram, "mxGraphModel");
        Element root = createElement(doc, mxGraphModel, "root");
        Element cell0 = createElement(doc, root, "mxCell");
        cell0.setAttribute("id", "0");
        Element cell1 = createElement(doc, root, "mxCell");
        cell1.setAttribute("id", "1");
        cell1.setAttribute("parent", "0");

        for (Edge edge : graphDTO.getBidirectionalConsolidatedEdges()) {
            createEdge(doc, root, edge);
        }

        for (Application application : graphDTO.getApplications()) {
            createRectangle(doc, root, application.getId().toString(), application.getName());
        }

        PLantumlToDrawioPositioner drawioPositioner = new PLantumlToDrawioPositioner();
        doc = drawioPositioner.addPositions(doc, svgXML);
        return getStringFromDocument(doc);
    }

    /**
     *
     * @return null when if no modification
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws XPathExpressionException
     */
    public String updateMXFileXML() throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
        boolean updated = false;
        Document doc = getDecodedExistingXML();

        GraphBuilder graphBuilder = new GraphBuilder();
        GraphDTO graphFromLandscape = graphBuilder.createGraph(landscapeView);
        GraphDTO graphFromMXFile = graphBuilder.createGraph(doc);

        XPathFactory xpathfactory = XPathFactory.newInstance();
        XPath xpath = xpathfactory.newXPath();
        Element root = (Element) xpath.evaluate("//root", doc, XPathConstants.NODE);
        for (Application application : graphFromLandscape.getApplications()) {
            if (!graphFromMXFile.containsApplication(application.getId())) {
                Element elem = createRectangle(doc, root, application.getId().toString(), application.getName());
                applyStrokeColor(elem, COLOR_GREEN);
                updated = true;
            }
        }

        for (Edge edge : graphFromLandscape.getEdges()) {
            if (!graphFromMXFile.containsEdge(edge.getSourceId(), edge.getTargetId(), false)) {
                Element elem = createEdge(doc, root, edge);
                applyStrokeColor(elem, COLOR_GREEN);
                updated = true;
            }
        }

        for (Application application : graphFromMXFile.getApplications()) {
            if (!graphFromLandscape.containsApplication(application.getId())) {
                Element elem = (Element) xpath.evaluate(
                    "//mxCell[@elementId='" + APP_ID_PREFIX + application.getId() + "']",
                    doc,
                    XPathConstants.NODE
                );
                applyStrokeColor(elem, COLOR_RED);
                updated = true;
            }
        }

        for (Edge edgeFromMXFile : graphFromMXFile.getEdges()) {
            Edge edgeFromLandscape = graphFromLandscape.getBidirectionalConsolidatedEdge(edgeFromMXFile);
            Element elem = (Element) xpath.evaluate(
                "//mxCell[@elementId='" + EDGE_ID_PREFIX + edgeFromMXFile.getId() + "']",
                doc,
                XPathConstants.NODE
            );
            if (!graphFromLandscape.containsEdge(edgeFromMXFile.getSourceId(), edgeFromMXFile.getTargetId(), false)) {
                applyStrokeColor(elem, COLOR_RED);
                updated = true;
            } else {
                //update labels with latest version
                String labels = edgeFromLandscape.getLabelsAsString();
                String oldLabels = elem.getAttribute("value");
                if (!labels.equals(oldLabels)) {
                    updated = true;
                    elem.setAttribute("value", labels);
                    applyStrokeColor(elem, COLOR_GREEN);
                }

                //updates arrow (bidirectional)

                if (
                    // mono vs bidirection
                    (edgeFromMXFile.isBidirectional() != edgeFromLandscape.isBidirectional()) ||
                    // mono but direction inverse
                    (!edgeFromLandscape.isBidirectional() && !edgeFromLandscape.getSourceId().equals(edgeFromMXFile.getSourceId()))
                ) {
                    updated = true;
                    applyArrowDirections(elem, edgeFromLandscape);
                    applyStrokeColor(elem, COLOR_GREEN);
                }
            }
        }

        return updated ? getStringFromDocument(doc) : null;
    }

    private void applyArrowDirections(Element elem, Edge edge) {
        String style = elem.getAttribute("style");
        style = style.replaceAll("startArrow=.*?;", "");
        style = style.replaceAll("endArrow=.*?;", "");
        if (edge.isBidirectional()) {
            style = style + "startArrow=classic;endArrow=classic;";
        }
        elem.setAttribute("style", style);
        elem.setAttribute("source", APP_ID_PREFIX + edge.getSourceId());
        elem.setAttribute("target", APP_ID_PREFIX + edge.getTargetId());
        elem.setAttribute("sourceId", APP_ID_PREFIX + edge.getSourceId());
        elem.setAttribute("targetId", APP_ID_PREFIX + edge.getTargetId());
    }

    public Document getDecodedExistingXML()
        throws DOMException, SAXException, IOException, XPathExpressionException, ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new InputSource(new StringReader(this.landscapeView.getCompressedDrawXML())));
        XPath xPath = XPathFactory.newInstance().newXPath();

        Element diagram = (Element) xPath.compile("//diagram").evaluate(doc, XPathConstants.NODE);
        String compressedContent = diagram.getTextContent();
        if (StringUtils.hasText(compressedContent)) {
            byte[] decodedBase64 = Base64.getDecoder().decode(compressedContent);
            String xmlURLEncoded = FormatUtils.inflate(decodedBase64);
            String xml = URLDecoder.decode(xmlURLEncoded, StandardCharsets.UTF_8);
            diagram.setTextContent(null);
            Node newNode = doc.importNode(builder.parse(new InputSource(new StringReader(xml))).getFirstChild(), true);
            diagram.appendChild(newNode);
        } else {
            Assert.isTrue(
                diagram.getFirstChild() != null && diagram.getFirstChild().getNodeName().equals("mxGraphModel"),
                "Non encoded mxFile should contain mxGraphModel " + getStringFromDocument(doc)
            );
        }
        String newXML = getStringFromDocument(doc);
        System.out.println(newXML);
        return doc;
    }

    /////////////////
    // MXFile Helpers
    /////////////////

    private Element createRectangle(Document doc, Element root, String id, String label) {
        Element mxCell = createElement(doc, root, "mxCell");
        // ID
        mxCell.setAttribute("id", APP_ID_PREFIX + id);
        mxCell.setAttribute("elementId", APP_ID_PREFIX + id);
        // Parent ID
        mxCell.setAttribute("parent", "1");
        // Label
        mxCell.setAttribute("value", label);
        mxCell.setAttribute("style", "rounded=0;whiteSpace=wrap;html=1;");
        mxCell.setAttribute("vertex", "1");

        Element geometry = createElement(doc, mxCell, "mxGeometry");
        geometry.setAttribute("width", CELL_DEFAULT_WIDTH);
        geometry.setAttribute("height", CELL_DEFAULT_HEIGHT);
        geometry.setAttribute("as", "geometry");

        return mxCell;
    }

    private Element createEdge(Document doc, Element root, Edge edge) {
        Element mxCell = createElement(doc, root, "mxCell");
        //mxCell.setAttribute("id", EDGE_ID_PREFIX + edgeId);
        mxCell.setAttribute("elementId", EDGE_ID_PREFIX + edge.getId());
        mxCell.setAttribute("value", edge.getLabelsAsString());
        mxCell.setAttribute("style", "endArrow=classic;html=1;rounded=0;");
        mxCell.setAttribute("parent", "1");
        mxCell.setAttribute("edge", "1");
        mxCell.setAttribute("source", APP_ID_PREFIX + edge.getSourceId());
        mxCell.setAttribute("target", APP_ID_PREFIX + edge.getTargetId());
        mxCell.setAttribute("sourceId", APP_ID_PREFIX + edge.getSourceId());
        mxCell.setAttribute("targetId", APP_ID_PREFIX + edge.getTargetId());

        Element geometry = createElement(doc, mxCell, "mxGeometry");
        geometry.setAttribute("relative", "1");
        geometry.setAttribute("as", "geometry");

        Element mxPoint1 = createElement(doc, geometry, "mxPoint");
        mxPoint1.setAttribute("as", "sourcePoint");

        Element mxPoint2 = createElement(doc, geometry, "mxPoint");
        mxPoint2.setAttribute("as", "targetPoint");

        return mxCell;
    }

    private void applyStrokeColor(Element elem, String color) {
        String styleValue = elem.getAttribute("style");
        if (!StringUtils.hasText(styleValue)) {
            elem.setAttribute("style", "strokeColor=" + color + ";");
        } else if (!styleValue.contains("strokeColor=")) {
            elem.setAttribute("style", styleValue + "strokeColor=" + color + ";");
        } else {
            elem.setAttribute("style", styleValue.replaceAll("strokeColor=.*?;", "strokeColor=" + color + ";"));
        }
    }

    /////////////////
    // DOM Helpers
    ////////////////

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

    private Element createElement(Document doc, Element parent, String name) {
        return (Element) parent.appendChild(doc.createElement(name));
    }
}
