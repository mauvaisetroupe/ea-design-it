package com.mauvaisetroupe.eadesignit.web.rest;

import com.mauvaisetroupe.eadesignit.domain.Application;
import com.mauvaisetroupe.eadesignit.domain.BusinessObject;
import com.mauvaisetroupe.eadesignit.domain.DataObject;
import com.mauvaisetroupe.eadesignit.domain.FunctionalFlow;
import com.mauvaisetroupe.eadesignit.domain.IFlowInterface;
import com.mauvaisetroupe.eadesignit.domain.LandscapeView;
import com.mauvaisetroupe.eadesignit.repository.ApplicationRepository;
import com.mauvaisetroupe.eadesignit.repository.BusinessObjectRepository;
import com.mauvaisetroupe.eadesignit.repository.DataObjectRepository;
import com.mauvaisetroupe.eadesignit.repository.FlowInterfaceRepository;
import com.mauvaisetroupe.eadesignit.repository.FunctionalFlowRepository;
import com.mauvaisetroupe.eadesignit.repository.LandscapeViewRepository;
import com.mauvaisetroupe.eadesignit.service.diagram.plantuml.LandscapeWithDataObjectPlantUMLBuilder;
import com.mauvaisetroupe.eadesignit.service.diagram.plantuml.PlantUMLBuilder.Layout;
import com.mauvaisetroupe.eadesignit.service.diagram.plantuml.PlantUMLService;
import com.mauvaisetroupe.eadesignit.service.diagram.plantuml.PlantUMLService.DiagramType;
import com.mauvaisetroupe.eadesignit.service.dto.PlantumlDTO;
import com.mauvaisetroupe.eadesignit.service.importfile.PlantumlImportService;
import io.undertow.util.BadRequestException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.SortedSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api")
@Transactional
public class PlantUMLResource {

    private final LandscapeViewRepository landscapeViewRepository;
    private final FunctionalFlowRepository functionalFlowRepository;
    private final FlowInterfaceRepository flowInterfaceRepository;
    private final ApplicationRepository applicationRepository;
    private final PlantUMLService plantUMLSerializer;
    private final PlantumlImportService plantumlImportService;
    private final BusinessObjectRepository businessObjectRepository;
    private final DataObjectRepository dataObjectRepository;
    private final LandscapeWithDataObjectPlantUMLBuilder landscapeWithDataObjectPlantUMLBuilder;

    private final Logger log = LoggerFactory.getLogger(PlantUMLResource.class);

    public PlantUMLResource(
        LandscapeViewRepository landscapeViewRepository,
        FunctionalFlowRepository functionalFlowRepository,
        ApplicationRepository applicationRepository,
        FlowInterfaceRepository flowInterfaceRepository,
        PlantUMLService plantUMLSerializer,
        PlantumlImportService plantumlImportService,
        BusinessObjectRepository businessObjectRepository,
        DataObjectRepository dataObjectRepository,
        LandscapeWithDataObjectPlantUMLBuilder landscapeWithDataObjectPlantUMLBuilder
    ) {
        this.landscapeViewRepository = landscapeViewRepository;
        this.functionalFlowRepository = functionalFlowRepository;
        this.applicationRepository = applicationRepository;
        this.flowInterfaceRepository = flowInterfaceRepository;
        this.plantUMLSerializer = plantUMLSerializer;
        this.plantumlImportService = plantumlImportService;
        this.businessObjectRepository = businessObjectRepository;
        this.dataObjectRepository = dataObjectRepository;
        this.landscapeWithDataObjectPlantUMLBuilder = landscapeWithDataObjectPlantUMLBuilder;
    }

    @GetMapping(value = "plantuml/landscape-view/get-svg/{id}")
    public @ResponseBody PlantumlDTO getLandscapeSVG(
        @PathVariable Long id,
        @RequestParam(defaultValue = "smetana") Layout layout,
        @RequestParam(defaultValue = "true") boolean groupComponents,
        @RequestParam(defaultValue = "true") boolean showLabels,
        @RequestParam(defaultValue = "-1") int showLabelIfNumberapplicationsLessThan
    ) throws IOException, BadRequestException {
        LandscapeView landscape = landscapeViewRepository.findOneWithEagerRelationships(id).orElseThrow();
        if (!showLabels && showLabelIfNumberapplicationsLessThan > -1) {
            int nbApplicationsInInterfaces = getApplicationsCount(landscape);
            if (nbApplicationsInInterfaces <= showLabelIfNumberapplicationsLessThan) {
                showLabels = true;
            }
        }
        return new PlantumlDTO(
            this.plantUMLSerializer.getLandscapeDiagramSVG(landscape, layout, groupComponents, true, showLabels),
            null,
            null,
            showLabels
        );
    }

    private int getApplicationsCount(LandscapeView landscape) {
        Set<Application> result = new HashSet<Application>();
        if (landscape != null && landscape.getFlows() != null) {
            landscape
                .getFlows()
                .forEach(flow -> {
                    flow
                        .getInterfaces()
                        .forEach(i -> {
                            result.add(i.getSource());
                            result.add(i.getTarget());
                        });
                });
        }
        return result.size();
    }

    @GetMapping(value = "plantuml/landscape-view/data-objects/get-svg/{id}")
    public @ResponseBody String getLandscapeWithDataObjectsSVG(@PathVariable Long id) throws IOException, BadRequestException {
        LandscapeView landscape = landscapeViewRepository.findOneWithEagerRelationships(id).orElseThrow();
        return landscapeWithDataObjectPlantUMLBuilder.getLandscapeDiagramSVG(landscape);
    }

    @GetMapping(value = "plantuml/landscape-view/get-source/{id}")
    public ResponseEntity<Resource> getLandscapeSource(@PathVariable Long id, @RequestParam(defaultValue = "true") boolean showLabels)
        throws IOException, BadRequestException {
        LandscapeView landscape = landscapeViewRepository.findById(id).orElseThrow();
        String source = this.plantUMLSerializer.getLandscapeDiagramSource(landscape, Layout.elk, showLabels);
        InputStreamResource inputStreamResource = new InputStreamResource(new ByteArrayInputStream(source.getBytes()));
        return ResponseEntity
            .ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + landscape.getDiagramName() + ".txt")
            .contentType(MediaType.parseMediaType("text/plain"))
            .body(inputStreamResource);
    }

    @GetMapping(value = "plantuml/functional-flow/get-svg/{id}")
    public @ResponseBody String getFunctionalFlowSVG(
        @PathVariable Long id,
        @RequestParam(required = false, defaultValue = "true") DiagramType diagramType
    ) throws IOException, BadRequestException {
        FunctionalFlow functionalFlow = functionalFlowRepository.findById(id).orElseThrow();
        return this.plantUMLSerializer.getFunctionalFlowDiagramSVG(functionalFlow, diagramType);
    }

    @GetMapping(value = "plantuml/functional-flow/get-source/{id}")
    public ResponseEntity<Resource> getFunctionalFlowSource(
        @PathVariable Long id,
        @RequestParam(required = false, defaultValue = "true") DiagramType diagramType,
        @RequestParam(required = false, defaultValue = "false") boolean preparedForEdition
    ) throws IOException, BadRequestException {
        FunctionalFlow functionalFlow = functionalFlowRepository.findById(id).orElseThrow();

        String source = this.plantUMLSerializer.getFunctionalFlowDiagramSource(functionalFlow, diagramType);
        if (preparedForEdition) {
            Queue<String> interfaces = new LinkedList<>();
            functionalFlow.getSteps().forEach(step -> interfaces.add(step.getFlowInterface().getAlias()));
            source = plantumlImportService.getPlantUMLSourceForEdition(source, true, true, interfaces);
        }
        InputStreamResource inputStreamResource = new InputStreamResource(new ByteArrayInputStream(source.getBytes()));
        return ResponseEntity
            .ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=applications.txt")
            .contentType(MediaType.parseMediaType("text/plain"))
            .body(inputStreamResource);
    }

    @GetMapping(value = "plantuml/application/get-svg/{id}")
    public @ResponseBody PlantumlDTO getApplicationSVG(
        @PathVariable Long id,
        @RequestParam(defaultValue = "elk") Layout layout,
        @RequestParam(defaultValue = "true") boolean groupComponents,
        @RequestParam(defaultValue = "false") boolean showLabels,
        @RequestParam(defaultValue = "-1") int showLabelIfNumberapplicationsLessThan
    ) throws IOException, BadRequestException {
        Application appli = applicationRepository.findById(id).orElseThrow();
        SortedSet<IFlowInterface> interfaces = flowInterfaceRepository.findBySource_NameOrTarget_Name(appli.getName(), appli.getName());
        if (!showLabels && showLabelIfNumberapplicationsLessThan > -1) {
            int nbApplicationsInInterfaces = getApplicationsCount(interfaces);
            if (nbApplicationsInInterfaces <= showLabelIfNumberapplicationsLessThan) {
                showLabels = true;
            }
        }
        SortedSet<FunctionalFlow> flows = functionalFlowRepository.findFunctionalFlowsForInterfacesIn(appli);
        return new PlantumlDTO(
            this.plantUMLSerializer.getInterfacesCollectionDiagramSVG(interfaces, layout, groupComponents, showLabels),
            interfaces,
            flows,
            showLabels
        );
    }

    private int getApplicationsCount(Set<IFlowInterface> interfaces) {
        Set<Application> result = new HashSet<Application>();
        if (interfaces != null) {
            interfaces.forEach(i -> {
                result.add(i.getSource());
                result.add(i.getTarget());
            });
        }
        return result.size();
    }

    @GetMapping(value = "plantuml/application/structure/get-svg/{id}")
    public @ResponseBody String getApplicationStructureSVG(@PathVariable Long id) throws IOException, BadRequestException {
        Application appli = applicationRepository.findOneWithEagerRelationships(id).orElseThrow();
        return this.plantUMLSerializer.getApplicationStructureSVG(appli);
    }

    @GetMapping(value = "plantuml/applications/get-svg")
    public @ResponseBody String getApplicationsSVG(
        @RequestParam(value = "ids[]") Long[] ids,
        @RequestParam(defaultValue = "false") boolean showLabels
    ) throws IOException, BadRequestException {
        SortedSet<IFlowInterface> interfaces = flowInterfaceRepository.findBySourceIdInAndTargetIdIn(ids, ids);
        return this.plantUMLSerializer.getInterfacesCollectionDiagramSVG(interfaces, Layout.smetana, false, showLabels);
    }

    @GetMapping(value = "plantuml/applications/get-source")
    public @ResponseBody String getApplicationsSource(@RequestParam(value = "ids[]") Long[] ids) throws IOException, BadRequestException {
        SortedSet<IFlowInterface> interfaces = flowInterfaceRepository.findBySourceIdInAndTargetIdIn(ids, ids);
        return this.plantUMLSerializer.getInterfacesCollectionDiagramSource(interfaces);
    }

    @PostMapping(value = "plantuml/sequence-diagram/get-svg")
    public @ResponseBody String getSequenceDiagramSVG(@RequestBody String plantumlSource) throws IOException {
        plantumlSource = plantumlImportService.preparePlantUMLSource(plantumlSource);
        return this.plantUMLSerializer.getSVGFromSource(plantumlSource);
    }

    @GetMapping(value = "plantuml/business-object/get-svg/{id}")
    public @ResponseBody String getBusinessObjectSVG(@PathVariable Long id) throws IOException, BadRequestException {
        BusinessObject bo = businessObjectRepository
            .findOneWithAllChildrens(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return this.plantUMLSerializer.getDatObjectSVG(bo);
    }

    @GetMapping(value = "plantuml/data-object/get-svg/{id}")
    public @ResponseBody String getDataObjectSVG(@PathVariable Long id) throws IOException, BadRequestException {
        DataObject bo = dataObjectRepository
            .findOneWithAllChildrens(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return this.plantUMLSerializer.getDatObjectSVG(bo);
    }
}
