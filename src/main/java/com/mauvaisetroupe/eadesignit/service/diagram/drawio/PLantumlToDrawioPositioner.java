package com.mauvaisetroupe.eadesignit.service.diagram.drawio;

import com.mauvaisetroupe.eadesignit.service.diagram.dto.Application;
import com.mauvaisetroupe.eadesignit.service.diagram.dto.PositionAndSize;
import java.io.IOException;
import java.io.StringReader;
import java.util.Collection;
import java.util.HashMap;
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

    // public static void main(String[] args) throws Exception {

    //     PLantumlToDrawioPositioner app = new PLantumlToDrawioPositioner();

    //     ClassLoader classLoader = PLantumlToDrawioPositioner.class.getClassLoader();
    //     File svgFile = new File(classLoader.getResource("svg-formatted.svg").getFile());
    //     Document svgDoc = createDocFromFile(svgFile);
    //     Map<String, Point> pointFromSVGMap = app.getPointFromSVG(svgDoc);

    //     File drawioFile = new File(classLoader.getResource("draw-io.xml").getFile());
    //     Document drawioDoc = createDocFromFile(drawioFile);
    //     app.addPosition(drawioDoc, pointFromSVGMap);

    //     FileOutputStream output = new FileOutputStream("c:\\workspaces\\staff-dom.xml");
    //     writeXml(drawioDoc, output);

    // }

    public Document addPositions(Document drawDocument, String svgXML, Collection<Application> applications) {
        if (svgXML == null) return drawDocument;

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        Document copiedDocument;
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Node originalRoot = drawDocument.getDocumentElement();
            copiedDocument = db.newDocument();
            Node copiedRoot = copiedDocument.importNode(originalRoot, true);
            copiedDocument.appendChild(copiedRoot);
        } catch (ParserConfigurationException e) {
            return drawDocument;
        }

        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document docSvgXML = db.parse(new InputSource(new StringReader(svgXML)));

            Map<String, PositionAndSize> pointFromSVGMap = getPointFromSVG(docSvgXML, applications);
            addPositions(copiedDocument, pointFromSVGMap);
            return copiedDocument;
        } catch (XPathExpressionException | ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
            return drawDocument;
        }
    }

    private void addPositions(Document doc, Map<String, PositionAndSize> pointFromSVGMap) throws XPathExpressionException {
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
            PositionAndSize position = pointFromSVGMap.get(applicationName);

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

    // private static Document createDocFromFile(File file)
    //         throws ParserConfigurationException, SAXException, IOException {
    //     DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    //     DocumentBuilder db = factory.newDocumentBuilder();
    //     Document doc = db.parse(file);
    //     return doc;
    // }

    protected Map<String, PositionAndSize> getPointFromSVG(Document doc, Collection<Application> applications)
        throws XPathExpressionException {
        Map<String, PositionAndSize> map = new HashMap<>();

        XPathFactory xpathfactory = XPathFactory.newInstance();
        XPath xpath = xpathfactory.newXPath();

        // <g id="elem_Trust Service">
        // <rect fill="#F1F1F1" height="46.2969" rx="2.5" ry="2.5"
        // style="stroke:#181818;stroke-width:0.5;" width="124" x="2986.5" y="463.543"/>
        // <text fill="#000000" font-family="sans-serif" font-size="14"
        // lengthAdjust="spacing" textLength="94" x="3001.5"
        // y="491.5381">TrustService</text>
        // </g>

        NodeList textNodes = doc.getElementsByTagName("text");
        NodeList rectNodes = doc.getElementsByTagName("rect");
        populateMap(map, textNodes, rectNodes, applications);
        return map;
    }

    private void populateMap(
        Map<String, PositionAndSize> mapToComplete,
        NodeList textNodes,
        NodeList rectNodes,
        Collection<Application> applications
    ) {
        Set<String> appliNames = applications.stream().map(a -> a.getName()).collect(Collectors.toSet());
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
                    mapToComplete.put(app, rectPos);
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
}
