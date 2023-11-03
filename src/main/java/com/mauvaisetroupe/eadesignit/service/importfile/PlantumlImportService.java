package com.mauvaisetroupe.eadesignit.service.importfile;

import com.mauvaisetroupe.eadesignit.domain.Application;
import com.mauvaisetroupe.eadesignit.domain.DataFlow;
import com.mauvaisetroupe.eadesignit.domain.FlowGroup;
import com.mauvaisetroupe.eadesignit.domain.FlowInterface;
import com.mauvaisetroupe.eadesignit.domain.FunctionalFlow;
import com.mauvaisetroupe.eadesignit.domain.FunctionalFlowStep;
import com.mauvaisetroupe.eadesignit.domain.LandscapeView;
import com.mauvaisetroupe.eadesignit.domain.Protocol;
import com.mauvaisetroupe.eadesignit.repository.ApplicationRepository;
import com.mauvaisetroupe.eadesignit.repository.DataFlowRepository;
import com.mauvaisetroupe.eadesignit.repository.FlowGroupRepository;
import com.mauvaisetroupe.eadesignit.repository.FlowInterfaceRepository;
import com.mauvaisetroupe.eadesignit.repository.FunctionalFlowRepository;
import com.mauvaisetroupe.eadesignit.repository.FunctionalFlowStepRepository;
import com.mauvaisetroupe.eadesignit.repository.LandscapeViewRepository;
import com.mauvaisetroupe.eadesignit.repository.ProtocolRepository;
import com.mauvaisetroupe.eadesignit.service.LandscapeViewService;
import com.mauvaisetroupe.eadesignit.service.diagram.plantuml.PlantUMLBuilder;
import com.mauvaisetroupe.eadesignit.service.diagram.plantuml.PlantUMLBuilder.Layout;
import com.mauvaisetroupe.eadesignit.service.dto.FlowImport;
import com.mauvaisetroupe.eadesignit.service.dto.FlowImportLine;
import com.mauvaisetroupe.eadesignit.service.identitier.IdentifierGenerator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import net.sourceforge.plantuml.BlockUml;
import net.sourceforge.plantuml.SourceStringReader;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.sequencediagram.Event;
import net.sourceforge.plantuml.sequencediagram.Message;
import net.sourceforge.plantuml.sequencediagram.Note;
import net.sourceforge.plantuml.sequencediagram.Participant;
import net.sourceforge.plantuml.sequencediagram.SequenceDiagram;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
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
    protected PlantUMLBuilder plantUMLBuilder;

    @Autowired
    private FlowGroupRepository flowGroupRepository;

    private static final String START_UML = "@startuml\n";
    public static final String END_UML = "@enduml";
    private static final String EQUAL_CHARACTER = "=";
    private final Logger log = LoggerFactory.getLogger(PlantumlImportService.class);

    public String getPlantUMLSourceForEdition(
        String plantuml,
        boolean removeHeaderAndFooter,
        boolean protocolAsComment,
        Queue<String> interfaces
    ) {
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
        // [["/functional-flow/1751/view" Scan document and archive]] group
        // Ophan group ID=3351
        plantuml = plantuml.replaceAll("\ngroup \\[\\[\".*?\"\\s*(.*?) ##.*?\\]\\] group\n", "\ngroup $1\n");

        StringBuilder builder = new StringBuilder();
        String[] lines = plantuml.split("\n");
        List<String> linesss = new ArrayList<>(Arrays.asList(lines))
            .stream()
            .filter(line -> !line.trim().isEmpty())
            .collect(Collectors.toList());
        int interfaceIndex = 0;
        int index = 0;
        boolean inGroup = false;
        boolean inNote = false;

        if (interfaces != null) {
            //clone to avoid side effects
            interfaces = new LinkedList<>(interfaces);

            int interfacesSize = interfaces.size();
            while (index < linesss.size()) {
                String line = linesss.get(index);
                if (line.startsWith("group")) {
                    inGroup = true;
                    builder.append(line + "\n");
                } else if (line.trim().equals("end")) {
                    Assert.isTrue(inGroup, "should not find end without 'group' first");
                    inGroup = false;
                    builder.append(line + "\n");
                } else if (line.startsWith("note")) {
                    inNote = true;
                    builder.append(line + "\n");
                } else if (inNote && line.startsWith("end note")) {
                    inNote = false;
                    builder.append(line + "\n");
                } else if (inNote) {
                    builder.append(line + "\n");
                } else if (!inNote) {
                    interfaceIndex++;
                    builder.append(line + " ##" + interfaces.poll() + "\n");
                }
                index++;
            }
            Assert.isTrue(
                interfaceIndex == interfacesSize,
                "We should haveas many interfaces (" + interfacesSize + ") as number of lines (" + interfaceIndex + ") in plantuml"
            );
            plantuml = builder.toString();
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
            plantUMLBuilder.getPlantumlHeader(builder, Layout.smetana, false);
            builder.append(plantUMLSource);
            plantUMLBuilder.getPlantumlFooter(builder);
            plantUMLSource = builder.toString();
        }
        // transform // API in note
        plantUMLSource = plantUMLSource.replaceAll(" // (.*)\n", "\nnote right\n$1\nend note\n");
        // remove in liline alias name ##interfaceAlias
        plantUMLSource = plantUMLSource.replaceAll(" ##([^#]*?)\\s*\n", "\n");
        //System.out.println(plantUMLSource);
        return plantUMLSource;
    }

    public FlowImport importPlantuml(String plantUMLSource) {
        FlowImport flowImport = new FlowImport();

        // Find group, o idea how to find from plantuml sequenceDiagram
        if (plantUMLSource.endsWith(EQUAL_CHARACTER)) {
            plantUMLSource = plantUMLSource.substring(0, plantUMLSource.length() - 1);
        }
        String[] lines = plantUMLSource.split("\n");
        List<String> linesss = new ArrayList<>(Arrays.asList(lines))
            .stream()
            .filter(line -> !line.trim().isEmpty())
            .collect(Collectors.toList());
        Queue<Grouping> groupings = new LinkedList<>();
        int interfaceIndex = 0;
        int index = 0;
        Grouping grouping = new Grouping();
        boolean inGroup = false;
        Queue<String> interfaces = new LinkedList<>();
        String regex = "##([^#\\s]+)";
        Pattern pattern = Pattern.compile(regex);

        while (index < linesss.size()) {
            String line = linesss.get(index);
            if (line.startsWith("group")) {
                inGroup = true;
                grouping.start = interfaceIndex;
                grouping.flowAlias = line.replaceAll("group\\s*(.*)\\s*", "$1");
            } else if (line.trim().equals("end")) {
                Assert.isTrue(inGroup, "should not find end without 'group' first");
                grouping.end = interfaceIndex - 1;
                groupings.add(grouping);
                grouping = new Grouping();
                inGroup = false;
            } else {
                interfaceIndex++;
                String _interface = null;
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    _interface = matcher.group(1);
                }
                interfaces.add(_interface);
            }
            index++;
        }
        plantUMLSource = preparePlantUMLSource(plantUMLSource);
        SourceStringReader reader = new SourceStringReader(plantUMLSource);
        BlockUml blockUml = reader.getBlocks().get(0);
        Diagram diagram = blockUml.getDiagram();

        String title = displayToString(diagram.getTitleDisplay());
        flowImport.setDescription(title);

        SequenceDiagram sequenceDiagram = (SequenceDiagram) diagram;

        List<Event> events = sequenceDiagram.events();
        int stepOrder = 0;

        Grouping currentGrouping = groupings.poll();

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
                    String _note = displayToString(note.getDisplay());
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
                if (currentGrouping != null) {
                    if (stepOrder >= currentGrouping.start && stepOrder <= currentGrouping.end) {
                        flowImportLine.setGroupOrder(stepOrder - currentGrouping.start + 1);
                        Optional<FunctionalFlow> optionalReferencedFlow = functionalFlowRepository.findByAlias(currentGrouping.flowAlias);
                        if (optionalReferencedFlow.isPresent()) {
                            flowImportLine.setGroupFlowAlias(currentGrouping.flowAlias);
                        }
                    } else if (stepOrder > currentGrouping.end) {
                        currentGrouping = groupings.poll();
                    }
                }
                String interfaceAlias = interfaces.poll();
                FlowInterface flowInterface = interfaceRepository.findByAlias(interfaceAlias).orElse(null);
                if (flowInterface != null) {
                    flowImportLine.setSelectedInterface(flowInterface);
                } else {
                    flowImportLine.setInterfaceAlias(interfaceAlias);
                }

                flowImportLine.setOrder(stepOrder++);
                flowImport.addLine(flowImportLine);
            }
        }

        List<String> interfacesAliases = interfaceRepository.findAlias();
        IdentifierGenerator interfaceIdgenerator = new IdentifierGenerator(interfacesAliases);
        flowImport.setPotentialIdentifier(interfaceIdgenerator.getPotentialIdentifiers());

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
            functionalFlow = functionalFlowRepository.findById(flowImport.getId()).orElseThrow();
        } else {
            functionalFlow = new FunctionalFlow();
            functionalFlowRepository.save(functionalFlow);
        }
        functionalFlow = copyFlowImportToFunctionalFlow(flowImport, functionalFlow);
        functionalFlowRepository.save(functionalFlow);

        if (landscapeId != null) {
            LandscapeView landscape = landscapeViewRepository.getById(landscapeId);
            if (landscape != null) {
                landscape.addFlows(functionalFlow);
                functionalFlow.addLandscape(landscape);
                landscapeViewRepository.save(landscape);
                functionalFlowRepository.save(functionalFlow);
            }
        }
        return functionalFlow;
    }

    private FunctionalFlow copyFlowImportToFunctionalFlow(FlowImport flowImport, FunctionalFlow functionalFlow) {
        functionalFlow.setAlias(flowImport.getAlias());
        functionalFlow.setDescription(flowImport.getDescription());
        functionalFlow.setComment(flowImport.getComment());
        functionalFlow.setStatus(flowImport.getStatus());
        functionalFlow.setDocumentationURL(flowImport.getDocumentationURL());
        functionalFlow.setDocumentationURL2(flowImport.getDocumentationURL2());
        functionalFlow.setStartDate(flowImport.getStartDate());
        functionalFlow.setEndDate(flowImport.getEndDate());
        functionalFlow.setDescription(flowImport.getDescription());

        Set<FlowGroup> groupsToDelete = new HashSet<>();
        for (FunctionalFlowStep step : new HashSet<>(functionalFlow.getSteps())) {
            FlowInterface inter = step.getFlowInterface();
            inter.removeSteps(step);
            functionalFlow.removeSteps(step);
            interfaceRepository.save(inter);
            functionalFlowRepository.save(functionalFlow);
            if (step.getGroup() != null) {
                groupsToDelete.add(step.getGroup());
                step.getGroup().removeSteps(step);
            }
            flowStepRepository.delete(step);
        }
        for (FlowGroup flowGroup : new HashSet<>(groupsToDelete)) {
            flowGroupRepository.delete(flowGroup);
        }

        int order = 1;
        FlowGroup flowGroup = null;
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

            if (flowImportLine.getGroupOrder() == 1) {
                //start the group
                flowGroup = new FlowGroup();
            }
            if (flowGroup != null) {
                if (flowImportLine.getGroupOrder() == 0) {
                    // close de group
                    flowGroup = null;
                } else {
                    flowGroup.addSteps(step);
                    flowStepRepository.save(step);
                    flowGroupRepository.save(flowGroup);

                    if (flowImportLine.getGroupFlowAlias() != null) {
                        FunctionalFlow flow = functionalFlowRepository.findByAlias(flowImportLine.getGroupFlowAlias()).orElse(null);
                        if (flow != null) {
                            flowGroup.setFlow(flow);
                            flowStepRepository.save(step);
                            flowGroupRepository.save(flowGroup);
                        } else {
                            flowGroup.setDescription(LandscapeViewService.SHOULD_BE_LINKED_TO + flowImportLine.getGroupFlowAlias());
                        }
                    }
                }
            }
        }
        return functionalFlow;
    }

    private class Grouping {

        int start;
        int end;
        String flowAlias;
    }
}
