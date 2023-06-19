package com.mauvaisetroupe.eadesignit.service.diagram.drawio;

import com.mauvaisetroupe.eadesignit.service.diagram.dto.Application;
import com.mauvaisetroupe.eadesignit.service.diagram.dto.Edge;
import com.mauvaisetroupe.eadesignit.service.diagram.dto.PositionAndSize;
import java.io.IOException;
import java.io.StringReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class PLantumlToDrawioPositioner {

    // appliName -> PositionAndSize
    private Map<String, PositionAndSize> mapApplicationPosition = new HashMap<>();
    // applicationId -> Application
    private Map<Long, Application> mapApplicationNames = new HashMap<>();
    // Ancestors above application(ID), from left to right
    private Map<Long, List<Long>> upAncestors = new HashMap<>();
    // Ancestors below application(ID), from left to right
    private Map<Long, List<Long>> downAncestors = new HashMap<>();

    public Map<String, PositionAndSize> getMapApplicationPosition() {
        return mapApplicationPosition;
    }

    public PLantumlToDrawioPositioner(String svgXML, Collection<Application> applications, Collection<Edge> edges)
        throws SAXException, IOException, ParserConfigurationException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document docSvgXML = db.parse(new InputSource(new StringReader(svgXML)));
        NodeList textNodes = docSvgXML.getElementsByTagName("text");
        NodeList rectNodes = docSvgXML.getElementsByTagName("rect");
        populateMaps(textNodes, rectNodes, applications);
        populateUpAndDownAncestors(applications, edges);
    }

    public Document addPositions(Document drawDocument) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        Document copiedDocument;
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Node originalRoot = drawDocument.getDocumentElement();
            copiedDocument = db.newDocument();
            Node copiedRoot = copiedDocument.importNode(originalRoot, true);
            copiedDocument.appendChild(copiedRoot);
            _addPositions(copiedDocument);
            return copiedDocument;
        } catch (XPathExpressionException | ParserConfigurationException e) {
            e.printStackTrace();
            return drawDocument;
        }
    }

    private void _addPositions(Document doc) throws XPathExpressionException {
        XPathFactory xpathfactory = XPathFactory.newInstance();
        XPath xpath = xpathfactory.newXPath();
        NodeList nodeList = (NodeList) xpath.evaluate(
            "//mxCell[starts-with(@elementId,'" + MXFileSerializer.APP_ID_PREFIX + "')]",
            doc,
            XPathConstants.NODESET
        );
        for (int i = 0; i < nodeList.getLength(); i++) {
            Element mxcellElement = ((Element) nodeList.item(i));
            String applicationName = mxcellElement.getAttribute("value");
            PositionAndSize position = this.mapApplicationPosition.get(applicationName);

            NodeList textNodeList = mxcellElement.getElementsByTagName("mxGeometry");
            Element mxGeometryElement = extractFirstChild(textNodeList);
            if (mxGeometryElement != null && position != null) {
                mxGeometryElement.setAttribute("x", position.getX().toString());
                mxGeometryElement.setAttribute("y", position.getY().toString());
                mxGeometryElement.setAttribute("width", position.getWidth().toString());
                mxGeometryElement.setAttribute("height", position.getHeight().toString());
            }
        }
    }

    public Document addConnectionPoints(Document doc, Collection<Edge> edges) throws XPathExpressionException {
        for (Edge edge : edges) {
            double entryY = 0, entryX = 0, exitX = 0, exitY = 0;

            boolean upToDown = (downAncestors.get(edge.getSourceId()).indexOf(edge.getTargetId()) > -1);

            System.out.println(edge.getSourceId() + " -> " + mapApplicationNames.get(edge.getSourceId()).getName());
            System.out.println(edge.getTargetId() + " -> " + mapApplicationNames.get(edge.getTargetId()).getName());
            System.out.println(upToDown);

            if (upToDown) {
                // source up
                // target down

                entryY = 0; // downside
                exitY = 1; //upside

                List<Long> upAppli = upAncestors.get(edge.getTargetId());
                List<Long> downAppli = downAncestors.get(edge.getSourceId());
                int indexEntry = upAppli.indexOf(edge.getSourceId()) + 1;
                int indexExit = downAppli.indexOf(edge.getTargetId()) + 1;

                exitX = (double) indexExit / (downAppli.size() + 1);
                entryX = (double) indexEntry / (upAppli.size() + 1);

                System.out.println(entryX + " " + exitX);
            } else {
                // source down
                // target up

                entryY = 1; // downside
                exitY = 0; //upside

                List<Long> upAppli = upAncestors.get(edge.getSourceId());
                List<Long> downAppli = downAncestors.get(edge.getTargetId());
                int indexEntry = downAppli.indexOf(edge.getSourceId()) + 1;
                int indexExit = upAppli.indexOf(edge.getTargetId()) + 1;

                exitX = (double) indexExit / (upAppli.size() + 1);
                entryX = (double) indexEntry / (downAppli.size() + 1);

                System.out.println(entryX + " " + exitX);
            }
            XPathFactory xpathfactory = XPathFactory.newInstance();
            XPath xpath = xpathfactory.newXPath();
            Element elem = (Element) xpath.evaluate(
                "//mxCell[@elementId='" + MXFileSerializer.EDGE_ID_PREFIX + edge.getId() + "']",
                doc,
                XPathConstants.NODE
            );
            String styleValue = elem.getAttribute("style");
            elem.setAttribute(
                "style",
                styleValue +
                "jumpStyle=arc;edgeStyle=elbowEdgeStyle;elbow=vertical;entryX=" +
                entryX +
                ";entryY=" +
                entryY +
                ";entryDx=0;entryDy=0;exitX=" +
                exitX +
                ";exitY=" +
                exitY +
                ";exitDx=0;exitDy=0;"
            );
        }
        return doc;
    }

    //
    // Init methods
    //

    private void populateMaps(NodeList textNodes, NodeList rectNodes, Collection<Application> applications) {
        Set<String> appliNames = applications.stream().map(a -> a.getName()).collect(Collectors.toSet());
        applications.stream().forEach(a -> this.mapApplicationNames.put(a.getId(), a));
        Map<String, PositionAndSize> textPostion = new HashMap<>();
        for (int i = 0; i < textNodes.getLength(); i++) {
            Element textElement = (Element) textNodes.item(i);
            String applicationName = textElement.getTextContent();

            if (applicationName != null) {
                applicationName = applicationName.replaceAll("[\n|\r]", " ").replaceAll(" +", " ");
            }

            if (appliNames.contains(applicationName)) {
                String _x = textElement.getAttribute("x");
                Double x = Double.parseDouble(_x);
                String _y = textElement.getAttribute("y");
                Double y = Double.parseDouble(_y);
                String _textLength = textElement.getAttribute("textLength");
                Double textLength = Double.parseDouble(_textLength);

                textPostion.put(applicationName, new PositionAndSize(x + textLength / 2, y, null, null));
            }
        }

        for (String app : textPostion.keySet()) {
            PositionAndSize txtPos = textPostion.get(app);

            int i = 0;
            boolean found = false;
            while (i < rectNodes.getLength() && !found) {
                Element rectElement = (Element) rectNodes.item(i);
                String _x = rectElement.getAttribute("x");
                String _y = rectElement.getAttribute("y");
                String _width = rectElement.getAttribute("width");
                String _height = rectElement.getAttribute("height");
                PositionAndSize rectPos = new PositionAndSize(_x, _y, _width, _height);

                if (
                    txtPos.getX() > rectPos.getX() &&
                    txtPos.getX() < rectPos.getX() + rectPos.getWidth() &&
                    txtPos.getY() > rectPos.getY() &&
                    txtPos.getY() < rectPos.getY() + rectPos.getHeight()
                ) {
                    this.mapApplicationPosition.put(app, rectPos);
                    //this.mapApplicationNames.put(app.get, _height)
                }
                i++;
            }
        }
    }

    private static Element extractFirstChild(NodeList nodeList) {
        if (nodeList != null && nodeList.getLength() > 0) {
            Element element = (Element) nodeList.item(0);
            return element;
        }
        return null;
    }

    private void populateUpAndDownAncestors(Collection<Application> applications, Collection<Edge> edges) {
        for (Application application : applications) {
            PositionAndSize applicationPosition = mapApplicationPosition.get(application.getName());

            List<Application> connectedApplication = edges
                .stream()
                .filter(e -> e.getSourceId().equals(application.getId()) || e.getTargetId().equals(application.getId())) // edges connected to appli
                .map(e -> e.getSourceId().equals(application.getId()) ? e.getTargetId() : e.getSourceId()) // keep source or target if not appli
                .map(id -> mapApplicationNames.get(id)) // get position
                .collect(Collectors.toList());

            List<Application> upAppli = connectedApplication
                .stream()
                .filter(a -> mapApplicationPosition.get(a.getName()).getY() < applicationPosition.getY())
                .collect(Collectors.toList());
            upAppli.sort((appli1, appli2) ->
                mapApplicationPosition.get(appli1.getName()).getX().compareTo(mapApplicationPosition.get(appli2.getName()).getX())
            );

            this.upAncestors.put(application.getId(), upAppli.stream().map(a -> a.getId()).collect(Collectors.toList()));

            List<Application> downAppli = connectedApplication
                .stream()
                .filter(a -> mapApplicationPosition.get(a.getName()).getY() >= applicationPosition.getY())
                .collect(Collectors.toList());
            downAppli.sort((appli1, appli2) ->
                mapApplicationPosition.get(appli1.getName()).getX().compareTo(mapApplicationPosition.get(appli2.getName()).getX())
            );

            this.downAncestors.put(application.getId(), downAppli.stream().map(a -> a.getId()).collect(Collectors.toList()));
        }
    }
}
