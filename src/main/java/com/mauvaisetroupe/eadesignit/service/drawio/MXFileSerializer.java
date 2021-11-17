package com.mauvaisetroupe.eadesignit.service.drawio;

import com.mauvaisetroupe.eadesignit.domain.Application;
import com.mauvaisetroupe.eadesignit.domain.FlowInterface;
import com.mauvaisetroupe.eadesignit.domain.FunctionalFlow;
import com.mauvaisetroupe.eadesignit.domain.LandscapeView;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
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

    protected static final String EDGE_ID_PREFIX = "edge_";
    protected static final String APP_ID_PREFIX = "app_";

    private static final String CELL_DEFAULT_WIDTH = "70";
    private static final String CELL_DEFAULT_HEIGHT = "50";

    private LandscapeView landscapeView;

    public MXFileSerializer(LandscapeView landscapeView) throws ParserConfigurationException {
        this.landscapeView = landscapeView;
    }

    public String createMXFileXML() throws ParserConfigurationException {
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

        for (Application application : getDistinctApplications(landscapeView)) {
            createRectangle(doc, root, application.getId().toString(), application.getName());
        }

        for (FunctionalFlow flow : this.landscapeView.getFlows()) {
            for (FlowInterface inter : flow.getInterfaces()) {
                createEdge(doc, root, flow, inter);
            }
        }

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
        XPathFactory xpathfactory = XPathFactory.newInstance();
        XPath xpath = xpathfactory.newXPath();
        Element root = (Element) xpath.evaluate("//root", doc, XPathConstants.NODE);
        for (Application application : getDistinctApplications(landscapeView)) {
            Element applicationElement = (Element) xpath
                .compile("//mxCell[@elementId='" + APP_ID_PREFIX + application.getId() + "']")
                .evaluate(doc, XPathConstants.NODE);
            if (applicationElement == null) {
                createRectangle(doc, root, application.getId().toString(), application.getName());
                updated = true;
            }
        }

        for (FunctionalFlow flow : this.landscapeView.getFlows()) {
            for (FlowInterface inter : flow.getInterfaces()) {
                Element edgeElement = (Element) xpath
                    .compile("//mxCell[@elementId='" + EDGE_ID_PREFIX + flow.getId() + "_" + inter.getId() + "']")
                    .evaluate(doc, XPathConstants.NODE);
                if (edgeElement == null) {
                    createEdge(doc, root, flow, inter);
                    updated = true;
                }
            }
        }
        return updated ? getStringFromDocument(doc) : null;
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

    private Element createEdge(Document doc, Element root, FunctionalFlow flow, FlowInterface inter) {
        Element mxCell = createElement(doc, root, "mxCell");
        mxCell.setAttribute("id", EDGE_ID_PREFIX + flow.getId() + "_" + inter.getId());
        mxCell.setAttribute("elementId", EDGE_ID_PREFIX + flow.getId() + "_" + inter.getId());
        mxCell.setAttribute("value", flow.getAlias());
        mxCell.setAttribute("style", "endArrow=classic;html=1;rounded=0;");
        mxCell.setAttribute("parent", "1");
        mxCell.setAttribute("edge", "1");
        mxCell.setAttribute("source", APP_ID_PREFIX + inter.getSource().getId());
        mxCell.setAttribute("target", APP_ID_PREFIX + inter.getTarget().getId());
        mxCell.setAttribute("sourceId", APP_ID_PREFIX + inter.getSource().getId());
        mxCell.setAttribute("targetId", APP_ID_PREFIX + inter.getTarget().getId());

        Element geometry = createElement(doc, mxCell, "mxGeometry");
        geometry.setAttribute("relative", "1");
        geometry.setAttribute("as", "geometry");

        Element mxPoint1 = createElement(doc, geometry, "mxPoint");
        mxPoint1.setAttribute("as", "sourcePoint");

        Element mxPoint2 = createElement(doc, geometry, "mxPoint");
        mxPoint2.setAttribute("as", "targetPoint");

        return mxCell;
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
