package com.mauvaisetroupe.eadesignit.web.rest;

import com.mauvaisetroupe.eadesignit.domain.FunctionalFlow;
import com.mauvaisetroupe.eadesignit.domain.LandscapeView;
import com.mauvaisetroupe.eadesignit.repository.FunctionalFlowRepository;
import com.mauvaisetroupe.eadesignit.repository.LandscapeViewRepository;
import com.mauvaisetroupe.eadesignit.service.plantuml.PlantUMLSerializer;
import io.undertow.util.BadRequestException;
import java.io.IOException;
import java.util.Optional;
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
            PlantUMLSerializer plantUMLSerializer = new PlantUMLSerializer();
            return plantUMLSerializer.getSVG(landscapeViewOptional.get());
        } else {
            throw new BadRequestException("Cannot find landscape View");
        }
    }

    @GetMapping(value = "plantuml/functional-flow/get-svg/{id}")
    public @ResponseBody String getFunctionalFlowSVG(@PathVariable Long id) throws IOException, BadRequestException {
        Optional<FunctionalFlow> functionalFlowOptional = functionalFlowRepository.findById(id);

        if (functionalFlowOptional.isPresent()) {
            PlantUMLSerializer plantUMLSerializer = new PlantUMLSerializer();
            return plantUMLSerializer.getSVG(functionalFlowOptional.get());
        } else {
            throw new BadRequestException("Cannot find landscape View");
        }
    }
}
