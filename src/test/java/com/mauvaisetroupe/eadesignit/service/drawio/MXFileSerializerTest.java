package com.mauvaisetroupe.eadesignit.service.drawio;

import static org.assertj.core.api.Assertions.*;

import com.mauvaisetroupe.eadesignit.domain.Application;
import com.mauvaisetroupe.eadesignit.domain.FlowInterface;
import com.mauvaisetroupe.eadesignit.domain.FunctionalFlow;
import com.mauvaisetroupe.eadesignit.domain.FunctionalFlowStep;
import com.mauvaisetroupe.eadesignit.domain.LandscapeView;
import com.mauvaisetroupe.eadesignit.service.diagram.drawio.MXFileSerializer;
import java.io.IOException;
import java.io.StringReader;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class MXFileSerializerTest {

    @Test
    public void test() throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
        LandscapeView landscapeView = new LandscapeView();
        landscapeView.setId(1L);
        landscapeView.setDiagramName("diagramName");
        FunctionalFlow functionalFlow = new FunctionalFlow();
        functionalFlow.setAlias("I.01");
        functionalFlow.setId(01L);
        Application app1 = getApplication("APP1");
        Application app2 = getApplication("APP2");
        Application app3 = getApplication("APP3");
        Application app4 = getApplication("APP4");

        functionalFlow.addSteps(creteInterface(1L, functionalFlow, app1, app2));
        functionalFlow.addSteps(creteInterface(2L, functionalFlow, app3, app2));
        functionalFlow.addSteps(creteInterface(3L, functionalFlow, app1, app4));
        landscapeView.addFlows(functionalFlow);

        MXFileSerializer mxFileSerializer = new MXFileSerializer(landscapeView);
        System.out.println(mxFileSerializer.createMXFileXML(null));
    }

    @Test
    public void testUpdateEncrypted() throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
        String xml =
            "<mxfile host=\"embed.diagrams.net\" modified=\"2021-11-16T21:16:18.886Z\" agent=\"5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/95.0.4638.69 Safari/537.36\" version=\"15.7.4\" etag=\"4h5OxQa3-R7l09y9BmRa\" type=\"embed\"><diagram id=\"0\" name=\"Page-1\">7ZhBk9ogFMc/TY7tQDC6Hq3aXQ87s23a6dFhkteEmSRkEFftpy9sIAmiu26d6h48Gf7A470/PyEakGm5vRe0zh95CkUQonQbkFkQhjgaYfWhlV2j3JGoETLBUjOoE2L2B4yIjLpmKaycgZLzQrLaFRNeVZBIR6NC8I077Dcv3FVrmoEnxAktfPUXS2VuqghHnf4ALMvtyng4bnpKagebSlY5TfmmJ5F5QKaCc9k8ldspFNo860sz7+uR3jYxAZU8ZQKt6yVWmTfznmmxNiXGkyclPHw3acqdrV3wdZWCno4C8mWTMwlxTRPdu1G7rbRcloVqYfXop2MyhAJKpS1s3l0KICRse2NN3vfAS5Bip4aY3tZDAxGxdGy6LRkZKe/tRmQ0aiDI2sidT+rBWPWabYh4tv0IB5dyrF39ZMfInmPhxR0b33mOLRbflLBIVW1M6mR1+zIOttn8M3P40g6Soe/gJJHsGZQ2Y0KddtyE/f8GdsmcbCC+IoKQZrDE0SBc4iEaezY+fkYqd6TDhWj6M/6EEMKek1ClE32DqFZS0NWKJa55rtMVn6tFYzP9VXNXfC0SOHQqSyoykIcOHl3RgSCL2fEwbl8TyN8tAQV9gcq5OQ94b6Y+cabKObrL7dfEhmjyNLP619N7AzVFeYFeUGjreRcdRNFhV3HpCPfpCD8mHfZYO5uOJtCNjh4dA00HPkQH2aeDXJMOy8CZZ4cb5nZ2vEFHpOnw36UVHYN9OvzXxI9AR3ujn0uHCXSjo0fHUNPh/2RQdET7dETXpMOeEGfeLG6Y283SNrtf983w7j8SMv8L</diagram></mxfile>";

        // <mxCell id="0"/>
        // <mxCell id="1" parent="0"/>
        // <mxCell elementId="1282" id="app_1282" parent="1" style="rounded=0;whiteSpace=wrap;html=1;" value="SAP HR" vertex="1">
        // <mxCell elementId="1203" id="app_1203" parent="1" style="rounded=0;whiteSpace=wrap;html=1;" value="T24" vertex="1">
        // <mxCell elementId="1298" id="app_1298" parent="1" style="rounded=0;whiteSpace=wrap;html=1;" value="IIQ Identity IQ" vertex="1">
        // <mxCell elementId="1368" id="app_1368" parent="1" style="rounded=0;whiteSpace=wrap;html=1;" value="Active Directory" vertex="1">
        // <mxCell edge="1" id="edge_1542_1609" parent="1" source="app_1282" sourceID="app_1282" target="app_1203" targetID="app_1203" value="M.01 / CUS-0001">
        // <mxCell edge="1" id="edge_1543_1610" parent="1" source="app_1282" sourceID="app_1282" target="app_1298" targetID="app_1298" value="M.02 / CUS-0002">
        // <mxCell edge="1" id="edge_1544_1611" parent="1" source="app_1298" sourceID="app_1298" target="app_1203" targetID="app_1203" value="M.03 / CUS-0003">
        // <mxCell edge="1" id="edge_1545_1612" parent="1" source="app_1298" sourceID="app_1298" target="app_1368" targetID="app_1368" value="M.04 / CUS-0004">
        // <mxCell edge="1" id="edge_1546_1613" parent="1" source="app_1203" sourceID="app_1203" target="app_1298" targetID="app_1298" value="M.05 / CUS-0005">

        LandscapeView landscapeView = new LandscapeView();
        landscapeView.setId(1L);
        landscapeView.setDiagramName("diagramName");
        FunctionalFlow functionalFlow = new FunctionalFlow();
        functionalFlow.setAlias("I.01");
        functionalFlow.setId(01L);
        Application app1 = getApplication("APP1");
        Application app2 = getApplication("APP2");
        Application app3 = getApplication("APP3");
        Application app4 = getApplication("APP4");

        functionalFlow.addSteps(creteInterface(1L, functionalFlow, app1, app2));
        functionalFlow.addSteps(creteInterface(2L, functionalFlow, app2, app3));
        functionalFlow.addSteps(creteInterface(3L, functionalFlow, app3, app4));
        landscapeView.addFlows(functionalFlow);
        landscapeView.setCompressedDrawXML(xml);
        ////////////////
        // Before update
        ////////////////
        MXFileSerializer fileSerializer = new MXFileSerializer(landscapeView);
        Document doc = fileSerializer.getDecodedExistingXML();
        XPath xPath = XPathFactory.newInstance().newXPath();
        NodeList nodeList = (NodeList) xPath.evaluate("//mxCell", doc, XPathConstants.NODESET);
        assertThat(nodeList.getLength()).isEqualTo(11);

        nodeList =
            (NodeList) xPath.evaluate("//mxCell[starts-with(@id,'" + MXFileSerializer.APP_ID_PREFIX + "')]", doc, XPathConstants.NODESET);
        assertThat(nodeList.getLength()).isEqualTo(4);

        nodeList =
            (NodeList) xPath.evaluate("//mxCell[starts-with(@id,'" + MXFileSerializer.EDGE_ID_PREFIX + "')]", doc, XPathConstants.NODESET);
        assertThat(nodeList.getLength()).isEqualTo(5);

        String updatedXML = fileSerializer.updateMXFileXML();
        assertThat(updatedXML).isNotNull();
        InputSource inputXML = new InputSource(new StringReader(updatedXML));

        nodeList =
            (NodeList) xPath.evaluate(
                "//mxCell[starts-with(@id,'" + MXFileSerializer.APP_ID_PREFIX + "')]",
                inputXML,
                XPathConstants.NODESET
            );
        assertThat(nodeList.getLength()).isEqualTo(8);

        inputXML = new InputSource(new StringReader(updatedXML));
        nodeList =
            (NodeList) xPath.evaluate(
                "//mxCell[starts-with(@id,'" + MXFileSerializer.EDGE_ID_PREFIX + "')]",
                inputXML,
                XPathConstants.NODESET
            );
        assertThat(nodeList.getLength()).isEqualTo(5);
    }

    private FunctionalFlowStep creteInterface(Long id, FunctionalFlow functionalFlow, Application source, Application target) {
        FunctionalFlowStep flowStep = new FunctionalFlowStep();
        FlowInterface flowInterface = new FlowInterface();
        flowInterface.setId(id);
        flowInterface.setSource(source);
        flowInterface.setTarget(target);
        flowStep.setFlowInterface(flowInterface);
        return flowStep;
    }

    private Application getApplication(String applicationName) {
        Application a = new Application();
        a.setName(applicationName);
        a.setId(0L + applicationName.hashCode());
        return a;
    }
}
