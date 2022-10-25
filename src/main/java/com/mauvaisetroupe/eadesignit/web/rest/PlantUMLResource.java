package com.mauvaisetroupe.eadesignit.web.rest;

import com.mauvaisetroupe.eadesignit.domain.Application;
import com.mauvaisetroupe.eadesignit.domain.Capability;
import com.mauvaisetroupe.eadesignit.domain.FunctionalFlow;
import com.mauvaisetroupe.eadesignit.domain.LandscapeView;
import com.mauvaisetroupe.eadesignit.repository.ApplicationRepository;
import com.mauvaisetroupe.eadesignit.repository.CapabilityRepository;
import com.mauvaisetroupe.eadesignit.repository.FlowInterfaceRepository;
import com.mauvaisetroupe.eadesignit.repository.FunctionalFlowRepository;
import com.mauvaisetroupe.eadesignit.repository.LandscapeViewRepository;
import com.mauvaisetroupe.eadesignit.repository.view.FlowInterfaceLight;
import com.mauvaisetroupe.eadesignit.service.dto.PlantumlDTO;
import com.mauvaisetroupe.eadesignit.service.plantuml.PlantUMLBuilder.Layout;
import com.mauvaisetroupe.eadesignit.service.plantuml.PlantUMLSerializer;
import com.mauvaisetroupe.eadesignit.service.plantuml.PlantUMLSerializer.DiagramType;
import io.undertow.util.BadRequestException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.SortedSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    private final CapabilityRepository capabilityRepository;

    private final PlantUMLSerializer plantUMLSerializer;

    private final Logger log = LoggerFactory.getLogger(PlantUMLResource.class);

    public PlantUMLResource(
        LandscapeViewRepository landscapeViewRepository,
        FunctionalFlowRepository functionalFlowRepository,
        ApplicationRepository applicationRepository,
        FlowInterfaceRepository flowInterfaceRepository,
        CapabilityRepository capabilityRepository,
        PlantUMLSerializer plantUMLSerializer
    ) {
        this.landscapeViewRepository = landscapeViewRepository;
        this.functionalFlowRepository = functionalFlowRepository;
        this.applicationRepository = applicationRepository;
        this.flowInterfaceRepository = flowInterfaceRepository;
        this.plantUMLSerializer = plantUMLSerializer;
        this.capabilityRepository = capabilityRepository;
    }

    @GetMapping(value = "plantuml/landscape-view/get-svg/{id}")
    public @ResponseBody String getLandscapeSVG(
        @PathVariable Long id,
        @RequestParam(required = false, defaultValue = "smetana") Layout layout
    ) throws IOException, BadRequestException {
        Optional<LandscapeView> landscapeViewOptional = landscapeViewRepository.findById(id);
        if (landscapeViewOptional.isPresent()) {
            return this.plantUMLSerializer.getLandscapeDiagramSVG(landscapeViewOptional.get(), layout);
        } else {
            throw new BadRequestException("Cannot find landscape View");
        }
    }

    @GetMapping(value = "plantuml/landscape-view/get-source/{id}")
    public ResponseEntity<Resource> getLandscapeSource(@PathVariable Long id) throws IOException, BadRequestException {
        Optional<LandscapeView> landscapeViewOptional = landscapeViewRepository.findById(id);
        if (landscapeViewOptional.isPresent()) {
            String source = this.plantUMLSerializer.getLandscapeDiagramSource(landscapeViewOptional.get());
            InputStreamResource inputStreamResource = new InputStreamResource(new ByteArrayInputStream(source.getBytes()));
            return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + landscapeViewOptional.get().getDiagramName() + ".txt")
                .contentType(MediaType.parseMediaType("text/plain"))
                .body(inputStreamResource);
        } else {
            throw new BadRequestException("Cannot find landscape View");
        }
    }

    @GetMapping(value = "plantuml/functional-flow/get-svg/{id}")
    public @ResponseBody String getFunctionalFlowSVG(
        @PathVariable Long id,
        @RequestParam(required = false, defaultValue = "true") DiagramType diagramType
    ) throws IOException, BadRequestException {
        Optional<FunctionalFlow> functionalFlowOptional = functionalFlowRepository.findById(id);

        if (functionalFlowOptional.isPresent()) {
            return this.plantUMLSerializer.getFunctionalFlowDiagramSVG(functionalFlowOptional.get(), diagramType);
        } else {
            throw new BadRequestException("Cannot find landscape View");
        }
    }

    @GetMapping(value = "plantuml/functional-flow/get-source/{id}")
    public ResponseEntity<Resource> getFunctionalFlowSource(
        @PathVariable Long id,
        @RequestParam(required = false, defaultValue = "true") DiagramType diagramType
    ) throws IOException, BadRequestException {
        Optional<FunctionalFlow> functionalFlowOptional = functionalFlowRepository.findById(id);

        if (functionalFlowOptional.isPresent()) {
            String source = this.plantUMLSerializer.getFunctionalFlowDiagramSource(functionalFlowOptional.get(), diagramType);
            InputStreamResource inputStreamResource = new InputStreamResource(new ByteArrayInputStream(source.getBytes()));
            return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=applications.txt")
                .contentType(MediaType.parseMediaType("text/plain"))
                .body(inputStreamResource);
        } else {
            throw new BadRequestException("Cannot find landscape View");
        }
    }

    @GetMapping(value = "plantuml/application/get-svg/{id}")
    public @ResponseBody PlantumlDTO getApplicationSVG(@PathVariable Long id) throws IOException, BadRequestException {
        Optional<Application> optional = applicationRepository.findById(id);

        if (optional.isPresent()) {
            SortedSet<FlowInterfaceLight> interfaces = flowInterfaceRepository.findBySource_NameOrTarget_Name(
                optional.get().getName(),
                optional.get().getName()
            );
            return new PlantumlDTO(this.plantUMLSerializer.getInterfacesCollectionDiagramSVG(interfaces), interfaces);
        } else {
            throw new BadRequestException("Cannot find landscape View");
        }
    }

    @GetMapping(value = "plantuml/application/capability/get-svg/{id}")
    public @ResponseBody String getApplicationCapabilitiesSVG(@PathVariable Long id) throws IOException, BadRequestException {
        Optional<Application> optional = applicationRepository.findById(id);
        if (optional.isPresent()) {
            return this.plantUMLSerializer.getCapabilitiesFromLeavesSVG(optional.get().getCapabilities());
        } else {
            throw new BadRequestException("Cannot find application");
        }
    }

    @GetMapping(value = "plantuml/applications/get-svg")
    public @ResponseBody String getApplicationsSVG(@RequestParam(value = "ids[]") Long[] ids) throws IOException, BadRequestException {
        SortedSet<FlowInterfaceLight> interfaces = flowInterfaceRepository.findBySourceIdInAndTargetIdIn(ids, ids);
        return this.plantUMLSerializer.getInterfacesCollectionDiagramSVG(interfaces);
    }

    @GetMapping(value = "plantuml/applications/get-source")
    public @ResponseBody String getApplicationsSource(@RequestParam(value = "ids[]") Long[] ids) throws IOException, BadRequestException {
        SortedSet<FlowInterfaceLight> interfaces = flowInterfaceRepository.findBySourceIdInAndTargetIdIn(ids, ids);
        return this.plantUMLSerializer.getInterfacesCollectionDiagramSource(interfaces);
    }

    @GetMapping(value = "plantuml/capabilities/get-svg/{id}")
    public @ResponseBody String getCapabilitiesSVG(@PathVariable Long id) throws IOException, BadRequestException {
        Optional<Capability> optional = capabilityRepository.findById(id);
        if (optional.isPresent()) {
            Capability capability = optional.get();
            List<Capability> tmp = new ArrayList<>();
            tmp.add(capability);
            return this.plantUMLSerializer.getCapabilitiesFromRootsSVG(tmp);
        } else {
            throw new BadRequestException("Cannot find application");
        }
    }

    @PostMapping(value = "plantuml/sequence-diagram/get-svg")
    public @ResponseBody String getSequenceDiagramSVG(@RequestBody String plantumlSource) throws IOException {
        plantumlSource = URLDecoder.decode(plantumlSource, StandardCharsets.UTF_8);
        plantumlSource = plantumlSource.replace("###CR##", "\n");
        return this.plantUMLSerializer.getSVGFromSource(plantumlSource);
    }
}
