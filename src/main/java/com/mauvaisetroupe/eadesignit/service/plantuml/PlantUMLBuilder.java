package com.mauvaisetroupe.eadesignit.service.plantuml;

import com.mauvaisetroupe.eadesignit.domain.Application;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;
import net.sourceforge.plantuml.core.DiagramDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class PlantUMLBuilder {

    private final Logger log = LoggerFactory.getLogger(PlantUMLSerializer.class);
    public static final String PLANTUML_SVG_CACHE = "com.mauvaisetroupe.eadesignit.service.plantuml.PlantUMLSerializer.svg";

    public String getPlantumlHeader() {
        String plantUMLSource = "@startuml\n";
        plantUMLSource += "!pragma layout smetana\n";
        plantUMLSource += "skinparam componentStyle rectangle\n";
        plantUMLSource += "skinparam hyperlinkColor #000000\n";
        plantUMLSource += "skinparam hyperlinkUnderline false\n";
        return plantUMLSource;
    }

    public String getPlantumlRelationShip(String plantUMLSource, Application source, Application target, String label, String URL) {
        plantUMLSource += "component [" + source.getName() + "] as C" + source.getId() + "\n";
        plantUMLSource += "url of C" + source.getId() + " is [[/application/" + source.getId() + "/view]]\n";
        plantUMLSource += "component [" + target.getName() + "] as C" + target.getId() + "\n";
        plantUMLSource += "url of C" + target.getId() + " is [[/application/" + target.getId() + "/view]]\n";
        plantUMLSource += "C" + source.getId() + " --> C" + target.getId() + " :";
        if (URL == null) return plantUMLSource += " " + label + "\n"; else return plantUMLSource += "[[ " + URL + " " + label + " ]]\n";
    }

    public String getPlantumlFooter(String plantUMLSource) {
        return plantUMLSource + "@enduml\n";
    }

    @Cacheable(cacheNames = PLANTUML_SVG_CACHE)
    public String getSVGFromSource(String plantUMLSource) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        SourceStringReader reader = new SourceStringReader(plantUMLSource);
        DiagramDescription diagramDescription = reader.outputImage(byteArrayOutputStream, new FileFormatOption(FileFormat.SVG));
        byteArrayOutputStream.close();
        log.debug(diagramDescription.getDescription());
        return new String(byteArrayOutputStream.toByteArray(), Charset.forName("UTF-8"));
    }
}
