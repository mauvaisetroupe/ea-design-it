package com.mauvaisetroupe.eadesignit.service.importfile;

import com.mauvaisetroupe.eadesignit.config.DatabaseConfiguration;
import com.mauvaisetroupe.eadesignit.domain.Application;
import com.mauvaisetroupe.eadesignit.domain.DataFlow;
import com.mauvaisetroupe.eadesignit.domain.FlowInterface;
import com.mauvaisetroupe.eadesignit.domain.FunctionalFlow;
import com.mauvaisetroupe.eadesignit.domain.FunctionalFlowStep;
import com.mauvaisetroupe.eadesignit.domain.LandscapeView;
import com.mauvaisetroupe.eadesignit.domain.Protocol;
import com.mauvaisetroupe.eadesignit.repository.ApplicationRepository;
import com.mauvaisetroupe.eadesignit.repository.DataFlowRepository;
import com.mauvaisetroupe.eadesignit.repository.FlowInterfaceRepository;
import com.mauvaisetroupe.eadesignit.repository.FunctionalFlowRepository;
import com.mauvaisetroupe.eadesignit.repository.FunctionalFlowStepRepository;
import com.mauvaisetroupe.eadesignit.repository.LandscapeViewRepository;
import com.mauvaisetroupe.eadesignit.repository.ProtocolRepository;
import com.mauvaisetroupe.eadesignit.service.diagram.plantuml.PlantUMLBuilder;
import com.mauvaisetroupe.eadesignit.service.diagram.plantuml.PlantUMLBuilder.Layout;
import com.mauvaisetroupe.eadesignit.service.dto.FlowImport;
import com.mauvaisetroupe.eadesignit.service.dto.FlowImportLine;
import com.mauvaisetroupe.eadesignit.service.identitier.IdentifierGenerator;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import net.sourceforge.plantuml.BlockUml;
import net.sourceforge.plantuml.SourceStringReader;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.sequencediagram.Event;
import net.sourceforge.plantuml.sequencediagram.Message;
import net.sourceforge.plantuml.sequencediagram.Note;
import net.sourceforge.plantuml.sequencediagram.Participant;
import net.sourceforge.plantuml.sequencediagram.SequenceDiagram;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Service
public class PlantumlImportService {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private FlowInterfaceRepository interfaceRepository;

    @Autowired
    private ProtocolRepository protocolRepository;

    @Autowired
    private DataFlowRepository dataFlowRepository;

    @Autowired
    private FunctionalFlowRepository functionalFlowRepository;

    @Autowired
    private FunctionalFlowStepRepository flowStepRepository;

    @Autowired
    private LandscapeViewRepository landscapeViewRepository;

    @Autowired
    private PlantUMLBuilder plantUMLBuilder;

    private static final String START_UML = "@startuml\n";
    public static final String END_UML = "@enduml";
    private static final String EQUAL_CHARACTER = "=";
    private final Logger log = LoggerFactory.getLogger(PlantumlImportService.class);

    public String getPlantUMLSourceForEdition(String plantuml, boolean removeHeaderAndFooter, boolean protocolAsComment) {
        if (removeHeaderAndFooter) {
            // remove header... everithing end of hedaer
            plantuml =
                plantuml.substring(
                    plantuml.indexOf(PlantUMLBuilder.END_OF_HEADER) + PlantUMLBuilder.END_OF_HEADER.length(),
                    plantuml.length()
                );
            //remove footer
            plantuml = plantuml.substring(0, plantuml.indexOf(END_UML));
        }
        if (protocolAsComment) {
            //remove note
            plantuml = plantuml.replaceAll("\nnote right\n(.*)\nend note", " // $1");
        }
        return plantuml;
    }

    public String preparePlantUMLSource(String plantUMLSource) {
        // Remove strange '=' at the end of the string
        if (plantUMLSource.endsWith(EQUAL_CHARACTER)) {
            plantUMLSource = plantUMLSource.substring(0, plantUMLSource.length() - 1);
        }
        // Add header and footer if needed

        if (!plantUMLSource.startsWith(START_UML)) {
            StringBuilder builder = new StringBuilder();
            plantUMLBuilder.getPlantumlHeader(builder, Layout.smetana);
            builder.append(plantUMLSource);
            plantUMLBuilder.getPlantumlFooter(builder);
            plantUMLSource = builder.toString();
        }
        // transform // API in note
        plantUMLSource = plantUMLSource.replaceAll(" // (.*)\n", "\nnote right\n$1\nend note\n");
        System.out.println(plantUMLSource);
        return plantUMLSource;
    }

    public FlowImport importPlantuml(String plantUMLSource) {
        FlowImport flowImport = new FlowImport();
        plantUMLSource = preparePlantUMLSource(plantUMLSource);
        SourceStringReader reader = new SourceStringReader(plantUMLSource);
        BlockUml blockUml = reader.getBlocks().get(0);
        Diagram diagram = blockUml.getDiagram();

        String title = displayToString(diagram.getTitleDisplay());
        flowImport.setDescription(title);

        SequenceDiagram sequenceDiagram = (SequenceDiagram) diagram;

        List<Event> events = sequenceDiagram.events();
        int stepOrder = 0;
        for (Event event : events) {
            if (event instanceof Message) {
                Message message = (Message) event;
                FlowImportLine flowImportLine = new FlowImportLine();
                Application source = checkApplicationExists(message.getParticipant1());
                if (source == null) flowImport.setOnError(true);
                Application target = checkApplicationExists(message.getParticipant2());
                if (target == null) flowImport.setOnError(true);
                Protocol protocol = null;
                List<FlowInterface> potentialInterfaces = null;
                flowImportLine.setSource(source);
                flowImportLine.setTarget(target);

                List<Note> notes = message.getNoteOnMessages();
                for (Note note : notes) {
                    String _note = displayToString(note.getStrings());
                    String[] _string = _note.split(",");
                    for (int i = 0; i < _string.length; i++) {
                        //Protocol is the first item
                        if (i == 0) {
                            String[] protocolAndUrl = getProtocolAndUrl(_string[i]);
                            String protocolName = protocolAndUrl[0];
                            String url = protocolAndUrl[1];

                            if (protocolName != null) {
                                protocol = protocolRepository.findByNameIgnoreCase(protocolName);
                                flowImportLine.setProtocol(protocol);
                            }

                            if (url != null) {
                                Set<DataFlow> potentialDataFlows = dataFlowRepository.findByContractURLIgnoreCase(url);
                                if (CollectionUtils.isEmpty(potentialDataFlows)) {
                                    potentialDataFlows = dataFlowRepository.findByDocumentationURLIgnoreCase(url);
                                }
                            }
                        } else {
                            throw new RuntimeException("Not implemented");
                        }
                    }
                }
                if (source != null && target != null) {
                    if (protocol != null) {
                        potentialInterfaces =
                            interfaceRepository.findBySourceIdAndTargetIdAndProtocolId(source.getId(), target.getId(), protocol.getId());
                    } else {
                        potentialInterfaces = interfaceRepository.findBySourceIdAndTargetId(source.getId(), target.getId());
                    }
                    if (potentialInterfaces != null) {
                        flowImportLine.setPotentialInterfaces(potentialInterfaces);
                        if (potentialInterfaces.size() == 1) {
                            FlowInterface selectedInterface = potentialInterfaces.get(0);
                            flowImportLine.setSelectedInterface(selectedInterface);
                            flowImportLine.setProtocol(selectedInterface.getProtocol());
                        }
                    }
                }
                flowImportLine.setDescription(displayToString(message.getLabel()));
                flowImportLine.setOrder(stepOrder++);
                flowImport.addLine(flowImportLine);
            }
        }
        return flowImport;
    }

    private String[] getProtocolAndUrl(String _string) {
        String protocolName = null, url = null;
        if (_string.contains("[[")) {
            _string = _string.replace("[[", "");
            _string = _string.replace("]]", "");
            String[] _noteArrray = _string.split(" ");
            protocolName = _noteArrray[1];
            url = _noteArrray[0];
        } else {
            protocolName = _string;
        }
        return new String[] { protocolName, url };
    }

    private Application checkApplicationExists(Participant participant) {
        Application application = null;
        String appName = displayToString(participant.getDisplay(false));
        if (appName != null) {
            application = applicationRepository.findByNameIgnoreCase(appName);
        }
        return application;
    }

    private String displayToString(Display display) {
        if (display != null && display.size() > 0) {
            if (display.get(0) != null) {
                String _string = display.iterator().next().toString();
                if (StringUtils.hasText(_string)) {
                    _string = _string.replaceAll("\n", "");
                    _string = _string.replace("\n", "");
                }
                return _string;
            }
        }
        return null;
    }

    public FunctionalFlow saveImport(FlowImport flowImport, Long landscapeId) {
        FunctionalFlow functionalFlow = null;
        if (flowImport.getId() != null) {
            Optional<FunctionalFlow> optional = functionalFlowRepository.findById(flowImport.getId());
            if (optional.isPresent()) {
                functionalFlow = optional.get();
            } else {
                log.error("Could not find functionalFlow with ID = " + flowImport.getId());
            }
        }
        if (functionalFlow == null) {
            functionalFlow = new FunctionalFlow();
            functionalFlowRepository.save(functionalFlow);
        }

        functionalFlow = copyFlowImportToFunctionalFlow(flowImport, functionalFlow);
        functionalFlowRepository.save(functionalFlow);
        return functionalFlow;
    }

    private FunctionalFlow copyFlowImportToFunctionalFlow(FlowImport flowImport, FunctionalFlow functionalFlow) {
        functionalFlow.setId(flowImport.getId());
        functionalFlow.setAlias(flowImport.getAlias());
        functionalFlow.setDescription(flowImport.getDescription());
        functionalFlow.setComment(flowImport.getComment());
        functionalFlow.setStatus(flowImport.getStatus());
        functionalFlow.setDocumentationURL(flowImport.getDocumentationURL());
        functionalFlow.setDocumentationURL2(flowImport.getDocumentationURL2());
        functionalFlow.setStartDate(flowImport.getStartDate());
        functionalFlow.setEndDate(flowImport.getEndDate());
        functionalFlow.setDescription(flowImport.getDescription());

        for (FunctionalFlowStep step : new HashSet<>(functionalFlow.getSteps())) {
            FlowInterface inter = step.getFlowInterface();
            inter.removeSteps(step);
            functionalFlow.removeSteps(step);
            interfaceRepository.save(inter);
            functionalFlowRepository.save(functionalFlow);
            flowStepRepository.delete(step);
        }

        int order = 1;
        for (FlowImportLine flowImportLine : flowImport.getFlowImportLines()) {
            FlowInterface interface1;
            if (flowImportLine.getSelectedInterface() != null && StringUtils.hasText(flowImportLine.getSelectedInterface().getAlias())) {
                interface1 = interfaceRepository.getById(flowImportLine.getSelectedInterface().getId());
            } else {
                interface1 = new FlowInterface();
                interface1.setSource(applicationRepository.getById(flowImportLine.getSource().getId()));
                interface1.setTarget(applicationRepository.getById(flowImportLine.getTarget().getId()));
                interface1.setAlias(flowImportLine.getInterfaceAlias());
                interfaceRepository.save(interface1);
                if (flowImportLine.getProtocol() != null) {
                    interface1.setProtocol(protocolRepository.getById(flowImportLine.getProtocol().getId()));
                }
            }
            FunctionalFlowStep step = new FunctionalFlowStep();
            step.setDescription(flowImportLine.getDescription());
            step.stepOrder(order++);
            functionalFlow.addSteps(step);
            interface1.addSteps(step);
            flowStepRepository.save(step);
        }
        return functionalFlow;
    }
}
