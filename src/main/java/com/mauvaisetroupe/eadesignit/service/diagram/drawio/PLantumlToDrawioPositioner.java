package com.mauvaisetroupe.eadesignit.service.diagram.drawio;

import com.mauvaisetroupe.eadesignit.service.diagram.dto.Application;
import com.mauvaisetroupe.eadesignit.service.diagram.dto.Edge;
import com.mauvaisetroupe.eadesignit.service.diagram.dto.PositionAndSize;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
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
    private Map<Long, List<Long>> topConnected = new HashMap<>();
    // Ancestors below application(ID), from left to right
    private Map<Long, List<Long>> bottomConnected = new HashMap<>();

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
        //     WEBBANKING (EXIT)
        //       |
        //       |
        //     ACCOUNTS   (ENTRY)

        //     <mxGraphModel dx="1401" dy="762" grid="1" gridSize="10" guides="1" tooltips="1" connect="1" arrows="1" fold="1" page="1" pageScale="1" pageWidth="827" pageHeight="1169" math="0" shadow="0">
        //     <root>
        //       <mxCell id="7" style="
        //              entryX=0.5;  entryY=0.0;  entryDx=0;  entryDy=0;
        //              exitX=0.5;   exitY=1.0;   exitDx=0;   exitDy=0;"
        //              parent="1" source="app_1661" target="app_1693" edge="1" elementId="edge_1661-1693" sourceId="app_1661" targetId="app_1693">
        //         <mxGeometry relative="1" as="geometry">
        //           <mxPoint as="sourcePoint" />
        //           <mxPoint as="targetPoint" />
        //         </mxGeometry>
        //       </mxCell>
        //       <mxCell id="app_1661" value="WEBBANKING" style="rounded=0;whiteSpace=wrap;html=1;" parent="1" elementId="app_1661" vertex="1">
        //         <mxGeometry x="185.5" y="12" width="62" height="41.6406" as="geometry" />
        //       </mxCell>
        //       <mxCell id="app_1693" value="ACCOUNTS" style="rounded=0;whiteSpace=wrap;html=1;" parent="1" elementId="app_1693" vertex="1">
        //         <mxGeometry x="160" y="150.0034" width="113" height="41.6406" as="geometry" />
        //       </mxCell>
        //     </root>
        //   </mxGraphModel>

        for (Edge edge : edges) {
            boolean fromTopToBottom = (bottomConnected.get(edge.getSourceId()).indexOf(edge.getTargetId()) > -1);

            // identify which of the source or target ones is the top application and the bottom application
            Long topApplicationId = fromTopToBottom ? edge.getSourceId() : edge.getTargetId();
            Application topApplication = mapApplicationNames.get(topApplicationId);

            Long bottomApplicationId = fromTopToBottom ? edge.getTargetId() : edge.getSourceId();
            Application bottomApplication = mapApplicationNames.get(bottomApplicationId);

            //     (0,0) --------------------------- (1,0)
            //           |                         |
            //           |                         |
            //     (0,1) --------------------------- (1,1)

            // define vertical (Y) connection point
            double originY = fromTopToBottom ? 1 : 0;
            double destinationY = fromTopToBottom ? 0 : 1;

            // sorted list of applications connected to topApplication and lower than it
            List<Long> bottomAppliList = bottomConnected.get(topApplicationId);
            // sorted list of applications connected to bottomApplication and higherr than it
            List<Long> topAppliList = topConnected.get(bottomApplicationId);

            // Define Horiontal (X) connection points
            int indexTop = bottomAppliList.indexOf(bottomApplicationId);
            int totalTop = topAppliList.size();
            int indexBottom = topAppliList.indexOf(topApplicationId);
            int totalBottom = bottomAppliList.size();
            double topRatio = (double) (indexTop + 1) / (totalBottom + 1);
            double bottomRatio = (double) (indexBottom + 1) / (totalTop + 1);

            double originX = fromTopToBottom ? topRatio : bottomRatio;
            double destinationX = fromTopToBottom ? bottomRatio : topRatio;

            // Write horizontal and vertical position points in XML
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
                "jumpStyle=arc;edgeStyle=elbowEdgeStyle;elbow=vertical;" +
                "entryX=" +
                destinationX +
                ";entryY=" +
                destinationY +
                ";entryDx=0;entryDy=0;" +
                "exitX=" +
                originX +
                ";exitY=" +
                originY +
                ";exitDx=0;exitDy=0;"
            );

            // Add Point on edge itself to avoid an alignememt on all edge

            PositionAndSize topPos = mapApplicationPosition.get(topApplication.getName());
            PositionAndSize bottomPos = mapApplicationPosition.get(bottomApplication.getName());

            Element geometry = (Element) elem.getFirstChild();

            Element array = (Element) geometry.appendChild(doc.createElement("Array"));
            array.setAttribute("as", "points");

            double deltaX = Math.abs((topPos.getX() + topPos.getWidth() / 2) - (bottomPos.getX() + bottomPos.getWidth() / 2));
            Element mxPoint3 = (Element) array.appendChild(doc.createElement("mxPoint"));
            mxPoint3.setAttribute("x", "" + (Math.min(topPos.getX(), bottomPos.getX()) + deltaX / 2));
            mxPoint3.setAttribute("y", "" + (topPos.getY() + topPos.getHeight() + (indexTop + 1) * 10));
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
                applicationName = applicationName.replaceAll("[\n|\r]", " ").replaceAll(" +", " ").replaceAll("-", "–");
            }

            if (appliNames.contains(applicationName)) {
                String _x = textElement.getAttribute("x");
                Double x = Double.parseDouble(_x);
                String _y = textElement.getAttribute("y");
                Double y = Double.parseDouble(_y);
                String _textLength = textElement.getAttribute("textLength");
                Double textLength = Double.parseDouble(_textLength);

                textPostion.put(applicationName, new PositionAndSize(x + textLength / 2, y, null, null));
            } else {
                System.out.println("Pb with : " + applicationName);
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
                .filter(a -> mapApplicationPosition.get(a.getName()) != null)
                .filter(a -> mapApplicationPosition.get(a.getName()).getY() < applicationPosition.getY())
                .collect(Collectors.toList());

            upAppli.sort((appli1, appli2) ->
                mapApplicationPosition.get(appli1.getName()).getX().compareTo(mapApplicationPosition.get(appli2.getName()).getX())
            );

            if (upAppli != null) {
                this.topConnected.put(application.getId(), upAppli.stream().map(a -> a.getId()).collect(Collectors.toList()));
            }

            List<Application> downAppli = connectedApplication
                .stream()
                .filter(a -> mapApplicationPosition.get(a.getName()) != null)
                .filter(a -> mapApplicationPosition.get(a.getName()).getY() >= applicationPosition.getY())
                .collect(Collectors.toList());

            downAppli.sort((appli1, appli2) ->
                mapApplicationPosition.get(appli1.getName()).getX().compareTo(mapApplicationPosition.get(appli2.getName()).getX())
            );

            this.bottomConnected.put(application.getId(), downAppli.stream().map(a -> a.getId()).collect(Collectors.toList()));
        }
    }
}
