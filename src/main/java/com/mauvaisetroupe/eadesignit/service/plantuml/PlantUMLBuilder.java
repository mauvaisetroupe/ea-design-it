package com.mauvaisetroupe.eadesignit.service.plantuml;

import com.mauvaisetroupe.eadesignit.domain.Application;
import com.mauvaisetroupe.eadesignit.domain.Capability;
import com.mauvaisetroupe.eadesignit.service.importfile.dto.CapabilityDTO;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;
import net.sourceforge.plantuml.core.DiagramDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class PlantUMLBuilder {

    private final Logger log = LoggerFactory.getLogger(PlantUMLSerializer.class);
    public static final String PLANTUML_SVG_CACHE = "com.mauvaisetroupe.eadesignit.service.plantuml.PlantUMLSerializer.svg";

    public void getPlantumlHeader(StringBuilder plantUMLSource) {
        plantUMLSource.append("@startuml\n");
        plantUMLSource.append("!pragma layout smetana\n");
        plantUMLSource.append("skinparam componentStyle rectangle\n");
        plantUMLSource.append("skinparam hyperlinkColor #000000\n");
        plantUMLSource.append("skinparam hyperlinkUnderline false\n");
        plantUMLSource.append("skinparam monochrome true\n");
        plantUMLSource.append("skinparam shadowing false\n");
        //plantUMLSource.append("skinparam svgDimensionStyle false\n");
        plantUMLSource.append("hide footbox\n");
    }

    public void getPlantumlRelationShip(
        StringBuilder plantUMLSource,
        Application source,
        Application target,
        List<String[]> labelAndURL,
        boolean sequenceDiagram
    ) {
        createComponent(plantUMLSource, source, sequenceDiagram);
        createComponent(plantUMLSource, target, sequenceDiagram);
        plantUMLSource.append("C" + source.getId() + " --> C" + target.getId());
        String sepaString = "";
        for (String[] strings : labelAndURL) {
            String label = strings[0];
            String URL = strings[1];
            if (StringUtils.hasText(label)) {
                label = label.replaceAll("\n", "\\\\n");
                plantUMLSource.append(" :");
                plantUMLSource.append(sepaString);
                if (URL == null) plantUMLSource.append(" " + label); else plantUMLSource.append("[[ " + URL + " " + label + " ]]");
                sepaString = ",\\n";
            }
        }
        plantUMLSource.append("\n");
    }

    private void createComponent(StringBuilder plantUMLSource, Application application, boolean sequenceDiagram) {
        if (sequenceDiagram) {
            plantUMLSource.append("participant \"" + application.getName() + "\" as C" + application.getId() + "\n");
        } else {
            plantUMLSource.append("component [" + application.getName() + "] as C" + application.getId() + "\n");
        }
        plantUMLSource.append("url of C" + application.getId() + " is [[/application/" + application.getId() + "/view]]\n");
    }

    public void getPlantumlFooter(StringBuilder plantUMLSource) {
        plantUMLSource.append("@enduml\n");
    }

    @Cacheable(cacheNames = PLANTUML_SVG_CACHE)
    public String getSVGFromSource(String plantUMLSource) throws IOException {
        System.out.println(plantUMLSource);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        SourceStringReader reader = new SourceStringReader(plantUMLSource);
        DiagramDescription diagramDescription = reader.outputImage(byteArrayOutputStream, new FileFormatOption(FileFormat.SVG));
        byteArrayOutputStream.close();
        log.debug(diagramDescription.getDescription());
        return new String(byteArrayOutputStream.toByteArray(), Charset.forName("UTF-8"));
    }

    public void getPlantumlRelationShip(
        StringBuilder plantUMLSource,
        Application source,
        Application target,
        String alias,
        String url,
        boolean sequenceDiagram
    ) {
        List<String[]> labelAndURLs = new ArrayList<>();
        String[] labelAndURL = { alias, url };
        labelAndURLs.add(labelAndURL);
        getPlantumlRelationShip(plantUMLSource, source, target, labelAndURLs, sequenceDiagram);
    }

    public void getPlantumlCapabilities(StringBuilder plantUMLSource, Collection<Capability> capabilities) {
        for (Capability capability : capabilities) {
            getRectangle1(capability, plantUMLSource, "");
        }
    }

    public void getPlantumlCapabilitiesDTO(StringBuilder plantUMLSource, Collection<CapabilityDTO> capabilities) {
        for (CapabilityDTO capability : capabilities) {
            getRectangle2(capability, plantUMLSource, "");
        }
    }

    private void getRectangle1(Capability root, StringBuilder result, String tab) {
        result.append(
            tab +
            "rectangle \"" +
            root.getName().replaceAll("[\n\r]", " ") +
            "\" as C" +
            root.getId() +
            (root.getSubCapabilities().size() != 0 ? " {" : "") +
            " \n"
        );
        for (Capability dto : root.getSubCapabilities()) {
            getRectangle1(dto, result, tab + "   ");
        }
        if (root.getSubCapabilities().size() != 0) result.append(tab + "}\n");
        result.append(tab + "url of C" + root.getId() + " is [[/capability/" + root.getId() + "/view]]\n");
    }

    private void getRectangle2(CapabilityDTO root, StringBuilder result, String tab) {
        result.append(
            tab +
            "rectangle \"" +
            root.getName().replaceAll("[\n\r]", " ") +
            "\" as C" +
            root.getId() +
            (root.getSubCapabilities().size() != 0 ? " {" : "") +
            " \n"
        );
        for (CapabilityDTO dto : root.getSubCapabilities()) {
            getRectangle2(dto, result, tab + "   ");
        }
        if (root.getSubCapabilities().size() != 0) result.append(tab + "}\n");
        result.append(tab + "url of C" + root.getId() + " is [[/capability/" + root.getId() + "/view]]\n");
    }
}
