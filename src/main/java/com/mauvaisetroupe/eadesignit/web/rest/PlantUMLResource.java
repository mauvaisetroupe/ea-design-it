package com.mauvaisetroupe.eadesignit.web.rest;

import com.mauvaisetroupe.eadesignit.domain.FlowInterface;
import com.mauvaisetroupe.eadesignit.domain.FunctionalFlow;
import com.mauvaisetroupe.eadesignit.domain.LandscapeView;
import com.mauvaisetroupe.eadesignit.repository.FunctionalFlowRepository;
import com.mauvaisetroupe.eadesignit.repository.LandscapeViewRepository;
import io.undertow.util.BadRequestException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.Optional;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;
import net.sourceforge.plantuml.core.DiagramDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Transactional
public class PlantUMLResource {

    private final LandscapeViewRepository landscapeViewRepository;
    private final FunctionalFlowRepository functionalFlowRepository;

    private final Logger log = LoggerFactory.getLogger(PlantUMLResource.class);

    public PlantUMLResource(LandscapeViewRepository landscapeViewRepository, FunctionalFlowRepository functionalFlowRepository) {
        this.landscapeViewRepository = landscapeViewRepository;
        this.functionalFlowRepository = functionalFlowRepository;
    }

    @GetMapping(value = "plantuml/landscape-view/get-svg/{id}")
    public @ResponseBody String getLandscapeSVG(@PathVariable Long id) throws IOException, BadRequestException {
        Optional<LandscapeView> landscapeViewOptional = landscapeViewRepository.findById(id);
        if (landscapeViewOptional.isPresent()) {
            String plantUMLSource = "@startuml\n";
            plantUMLSource += "!pragma layout smetana\n";
            for (FunctionalFlow functionalFlow : landscapeViewOptional.get().getFlows()) {
                for (FlowInterface flowInterface : functionalFlow.getInterfaces()) {
                    plantUMLSource +=
                        "[" +
                        flowInterface.getSource().getName() +
                        "] --> [" +
                        flowInterface.getTarget().getName() +
                        "] : " +
                        functionalFlow.getAlias() +
                        " / " +
                        flowInterface.getAlias() +
                        "\n\r";
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
        } else {
            throw new BadRequestException("Cannot find landscape View");
        }
    }

    @GetMapping(value = "plantuml/functional-flow/get-svg/{id}")
    public @ResponseBody String getFunctionalFlowSVG(@PathVariable Long id) throws IOException, BadRequestException {
        Optional<FunctionalFlow> functionalFlowOptional = functionalFlowRepository.findById(id);

        if (functionalFlowOptional.isPresent()) {
            FunctionalFlow functionalFlow = functionalFlowOptional.get();
            String plantUMLSource = "@startuml\n";
            plantUMLSource += "!pragma layout smetana\n";
            for (FlowInterface flowInterface : functionalFlow.getInterfaces()) {
                plantUMLSource +=
                    "[" +
                    flowInterface.getSource().getName() +
                    "] --> [" +
                    flowInterface.getTarget().getName() +
                    "] : " +
                    functionalFlow.getAlias() +
                    " / " +
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
        } else {
            throw new BadRequestException("Cannot find landscape View");
        }
    }
}
