package com.mauvaisetroupe.eadesignit.service.plantuml;

import com.mauvaisetroupe.eadesignit.domain.FlowInterface;
import com.mauvaisetroupe.eadesignit.domain.FunctionalFlow;
import com.mauvaisetroupe.eadesignit.domain.LandscapeView;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;
import net.sourceforge.plantuml.core.DiagramDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlantUMLSerializer {

    private final Logger log = LoggerFactory.getLogger(PlantUMLSerializer.class);

    public String getSVG(LandscapeView landscapeView) throws IOException {
        String plantUMLSource = "@startuml\n";
        plantUMLSource += "!pragma layout smetana\n";
        plantUMLSource += "skinparam componentStyle rectangle\n";
        plantUMLSource += "skinparam hyperlinkColor #000000\n";
        plantUMLSource += "skinparam hyperlinkUnderline false\n";
        for (FunctionalFlow functionalFlow : landscapeView.getFlows()) {
            for (FlowInterface flowInterface : functionalFlow.getInterfaces()) {
                plantUMLSource +=
                    "[" +
                    flowInterface.getSource().getName() +
                    "] --> [" +
                    flowInterface.getTarget().getName() +
                    "] : [[/functional-flow/" +
                    functionalFlow.getId() +
                    "/view " +
                    functionalFlow.getAlias() +
                    "]]\n\r";
            }
        }
        plantUMLSource += "@enduml\n";

        System.out.println(plantUMLSource);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        SourceStringReader reader = new SourceStringReader(plantUMLSource);
        DiagramDescription diagramDescription = reader.outputImage(byteArrayOutputStream, new FileFormatOption(FileFormat.SVG));
        byteArrayOutputStream.close();
        log.debug(diagramDescription.getDescription());
        return new String(byteArrayOutputStream.toByteArray(), Charset.forName("UTF-8"));
    }

    public String getSVG(FunctionalFlow functionalFlow) throws IOException {
        String plantUMLSource = "@startuml\n";
        plantUMLSource += "!pragma layout smetana\n";
        plantUMLSource += "skinparam componentStyle rectangle\n";
        for (FlowInterface flowInterface : functionalFlow.getInterfaces()) {
            plantUMLSource +=
                "[" +
                flowInterface.getSource().getName() +
                "] --> [" +
                flowInterface.getTarget().getName() +
                "] : " +
                flowInterface.getAlias() +
                "\n\r";
        }
        plantUMLSource += "@enduml\n";

        System.out.println(plantUMLSource);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        SourceStringReader reader = new SourceStringReader(plantUMLSource);
        DiagramDescription diagramDescription = reader.outputImage(byteArrayOutputStream, new FileFormatOption(FileFormat.SVG));
        byteArrayOutputStream.close();
        log.debug(diagramDescription.getDescription());
        return new String(byteArrayOutputStream.toByteArray(), Charset.forName("UTF-8"));
    }
}
