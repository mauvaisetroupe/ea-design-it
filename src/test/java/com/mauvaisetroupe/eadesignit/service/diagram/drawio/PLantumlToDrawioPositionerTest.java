package com.mauvaisetroupe.eadesignit.service.diagram.drawio;

import static org.junit.Assert.assertEquals;

import com.mauvaisetroupe.eadesignit.service.diagram.dto.Application;
import com.mauvaisetroupe.eadesignit.service.diagram.dto.PositionAndSize;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.FileCopyUtils;
import org.xml.sax.SAXException;

public class PLantumlToDrawioPositionerTest {

    @Test
    void testGetPosition() throws ParserConfigurationException, SAXException, IOException, XPathExpressionException, URISyntaxException {
        Collection<Application> applications = new ArrayList<>();
        applications.add(new Application(1L, "Accounts Service", null, null));
        applications.add(new Application(2L, "Apple pay", null, null));
        applications.add(new Application(3L, "Apple push notification service", null, null));
        applications.add(new Application(4L, "BILnet", null, null));
        applications.add(new Application(6L, "Cards Service", null, null));
        applications.add(new Application(7L, "Decibel", null, null));
        applications.add(new Application(8L, "Financial instruments service", null, null));
        applications.add(new Application(9L, "Google Analytics", null, null));
        applications.add(new Application(10L, "Google Firebase", null, null));
        applications.add(new Application(11L, "IBM Campaign", null, null));
        applications.add(new Application(12L, "Identity manager", null, null));
        applications.add(new Application(13L, "Instant Payments service", null, null));
        applications.add(new Application(14L, "Investor profile service", null, null));
        applications.add(new Application(15L, "Kondor+", null, null));
        applications.add(new Application(16L, "Loans service", null, null));
        applications.add(new Application(17L, "Luxtrust", null, null));
        applications.add(new Application(18L, "Marketing Files, Forms, FactSet", null, null));
        applications.add(new Application(19L, "Party Service", null, null));
        applications.add(new Application(20L, "Payconiq", null, null));
        applications.add(new Application(201L, "Payconiq service", null, null));
        applications.add(new Application(21L, "Payments service", null, null));
        applications.add(new Application(22L, "Portfolios Service", null, null));
        applications.add(new Application(23L, "Product Mandates service", null, null));
        applications.add(new Application(24L, "PSD2 Payments Service", null, null));
        applications.add(new Application(25L, "Report Data Collector", null, null));
        applications.add(new Application(26L, "RT notif Service", null, null));
        applications.add(new Application(27L, "Secured message service", null, null));
        applications.add(new Application(28L, "Sirius", null, null));
        applications.add(new Application(29L, "Six Payments", null, null));
        applications.add(new Application(30L, "T24", null, null));
        applications.add(new Application(31L, "Trading service", null, null));
        applications.add(new Application(32L, "Trust Service", null, null));
        applications.add(new Application(33L, "User settings service", null, null));

        //InputStream stream = PLantumlToDrawioPositionerTest.class.getResourceAsStream("/com/mauvaisetroupe/eadesignit/service/diagram/drawio/svg.xml");
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        Resource resource = resourceLoader.getResource("classpath:/svg/svg.xml");
        String svg = asString(resource);

        PLantumlToDrawioPositioner drawioPositioner = new PLantumlToDrawioPositioner(svg, applications, null);
        Map<String, PositionAndSize> map = drawioPositioner.getMapApplicationPosition();

        // All application should have been found in <text>
        assertEquals(applications.size(), map.size());

        // All <text> should been included in <rect>
        assertEquals(applications.size(), map.values().stream().filter(p -> p != null).collect(Collectors.toList()).size());

        // <rect fill="#FFFFFF" height="86.6406" style="stroke:#000000;stroke-width:1.5;" width="205" x="4178.5" y="546.9766"></rect>

        assertEquals((Double) 205d, map.get("Trading service").getWidth());
        assertEquals((Double) 86.6406, map.get("Trading service").getHeight());
        assertEquals((Double) 4178.5, map.get("Trading service").getX());
        assertEquals((Double) 546.9766, map.get("Trading service").getY());
    }

    public static String asString(Resource resource) {
        try (Reader reader = new InputStreamReader(resource.getInputStream())) {
            return FileCopyUtils.copyToString(reader);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
