package com.mauvaisetroupe.eadesignit.service.plantuml;

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

    public String getPlantumlRelationShip(String plantUMLSource, String source, String target, String label, String URL) {
        plantUMLSource += "[" + source + "] --> [" + target + "] :";
        if (URL == null) return plantUMLSource += " " + label + "\n"; else return plantUMLSource += "[[ " + URL + " " + label + " ]]\n";
    }

    public String getPlantumlFooter(String plantUMLSource) {
        return plantUMLSource + "@enduml\n";
    }

    @Cacheable(cacheNames = PLANTUML_SVG_CACHE)
    public String getSVGFromSource(String plantUMLSource) throws IOException {
        System.out.println(plantUMLSource);
        System.out.println("XXXXX" + plantUMLSource.hashCode() + "YYYY");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        SourceStringReader reader = new SourceStringReader(plantUMLSource);
        DiagramDescription diagramDescription = reader.outputImage(byteArrayOutputStream, new FileFormatOption(FileFormat.SVG));
        byteArrayOutputStream.close();
        log.debug(diagramDescription.getDescription());
        return new String(byteArrayOutputStream.toByteArray(), Charset.forName("UTF-8"));
    }
}
