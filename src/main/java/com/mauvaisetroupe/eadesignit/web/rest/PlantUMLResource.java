package com.mauvaisetroupe.eadesignit.web.rest;

import com.mauvaisetroupe.eadesignit.domain.Application;
import com.mauvaisetroupe.eadesignit.domain.FlowInterface;
import com.mauvaisetroupe.eadesignit.domain.FunctionalFlow;
import com.mauvaisetroupe.eadesignit.domain.LandscapeView;
import com.mauvaisetroupe.eadesignit.repository.ApplicationRepository;
import com.mauvaisetroupe.eadesignit.repository.FlowInterfaceRepository;
import com.mauvaisetroupe.eadesignit.repository.FunctionalFlowRepository;
import com.mauvaisetroupe.eadesignit.repository.LandscapeViewRepository;
import com.mauvaisetroupe.eadesignit.service.dto.PlantumlDTO;
import com.mauvaisetroupe.eadesignit.service.plantuml.PlantUMLSerializer;
import io.undertow.util.BadRequestException;
import java.io.IOException;
import java.util.Optional;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Transactional
public class PlantUMLResource {

    private final LandscapeViewRepository landscapeViewRepository;
    private final FunctionalFlowRepository functionalFlowRepository;
    private final FlowInterfaceRepository flowInterfaceRepository;
    private final ApplicationRepository applicationRepository;
    private final PlantUMLSerializer plantUMLSerializer;

    private final Logger log = LoggerFactory.getLogger(PlantUMLResource.class);

    public PlantUMLResource(
        LandscapeViewRepository landscapeViewRepository,
        FunctionalFlowRepository functionalFlowRepository,
        ApplicationRepository applicationRepository,
        FlowInterfaceRepository flowInterfaceRepository,
        PlantUMLSerializer plantUMLSerializer
    ) {
        this.landscapeViewRepository = landscapeViewRepository;
        this.functionalFlowRepository = functionalFlowRepository;
        this.applicationRepository = applicationRepository;
        this.flowInterfaceRepository = flowInterfaceRepository;
        this.plantUMLSerializer = plantUMLSerializer;
    }

    @GetMapping(value = "plantuml/landscape-view/get-svg/{id}")
    public @ResponseBody String getLandscapeSVG(@PathVariable Long id) throws IOException, BadRequestException {
        Optional<LandscapeView> landscapeViewOptional = landscapeViewRepository.findById(id);
        if (landscapeViewOptional.isPresent()) {
            return this.plantUMLSerializer.getSVG(landscapeViewOptional.get());
        } else {
            throw new BadRequestException("Cannot find landscape View");
        }
    }

    @GetMapping(value = "plantuml/functional-flow/get-svg/{id}")
    public @ResponseBody String getFunctionalFlowSVG(@PathVariable Long id) throws IOException, BadRequestException {
        Optional<FunctionalFlow> functionalFlowOptional = functionalFlowRepository.findById(id);

        if (functionalFlowOptional.isPresent()) {
            return this.plantUMLSerializer.getSVG(functionalFlowOptional.get());
        } else {
            throw new BadRequestException("Cannot find landscape View");
        }
    }

    @GetMapping(value = "plantuml/application/get-svg/{id}")
    public @ResponseBody PlantumlDTO getApplicationSVG(@PathVariable Long id) throws IOException, BadRequestException {
        Optional<Application> optional = applicationRepository.findById(id);

        if (optional.isPresent()) {
            Set<FlowInterface> interfaces = flowInterfaceRepository.findBySource_NameOrTarget_Name(
                optional.get().getName(),
                optional.get().getName()
            );
            return new PlantumlDTO(this.plantUMLSerializer.getSVG(interfaces), interfaces);
        } else {
            throw new BadRequestException("Cannot find landscape View");
        }
    }

    @GetMapping(value = "plantuml/application/capability/get-svg/{id}")
    public @ResponseBody String getApplicationCapabilitiesSVG(@PathVariable Long id) throws IOException, BadRequestException {
        Optional<Application> optional = applicationRepository.findById(id);
        if (optional.isPresent()) {
            return this.plantUMLSerializer.getCapabilitiesSVG(optional.get());
        } else {
            throw new BadRequestException("Cannot find landscape View");
        }
    }

    @GetMapping(value = "plantuml/applications/get-svg")
    public @ResponseBody String getApplicationsSVG(@RequestParam(value = "ids[]") Long[] ids) throws IOException, BadRequestException {
        Set<FlowInterface> interfaces = flowInterfaceRepository.findBySourceIdInAndTargetIdIn(ids, ids);
        return this.plantUMLSerializer.getSVG(interfaces);
    }
}
