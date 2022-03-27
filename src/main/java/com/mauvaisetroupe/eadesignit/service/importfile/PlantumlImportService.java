package com.mauvaisetroupe.eadesignit.service.importfile;

import com.mauvaisetroupe.eadesignit.domain.Application;
import com.mauvaisetroupe.eadesignit.domain.DataFlow;
import com.mauvaisetroupe.eadesignit.domain.FlowInterface;
import com.mauvaisetroupe.eadesignit.domain.FunctionalFlow;
import com.mauvaisetroupe.eadesignit.domain.FunctionalFlowStep;
import com.mauvaisetroupe.eadesignit.domain.Protocol;
import com.mauvaisetroupe.eadesignit.repository.ApplicationRepository;
import com.mauvaisetroupe.eadesignit.repository.DataFlowRepository;
import com.mauvaisetroupe.eadesignit.repository.FlowInterfaceRepository;
import com.mauvaisetroupe.eadesignit.repository.FunctionalFlowRepository;
import com.mauvaisetroupe.eadesignit.repository.FunctionalFlowStepRepository;
import com.mauvaisetroupe.eadesignit.repository.ProtocolRepository;
import com.mauvaisetroupe.eadesignit.service.dto.FlowImport;
import com.mauvaisetroupe.eadesignit.service.dto.FlowImportLine;
import com.mauvaisetroupe.eadesignit.service.identitier.IdentifierGenerator;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import net.sourceforge.plantuml.BlockUml;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.sequencediagram.Event;
import net.sourceforge.plantuml.sequencediagram.Message;
import net.sourceforge.plantuml.sequencediagram.Note;
import net.sourceforge.plantuml.sequencediagram.Participant;
import net.sourceforge.plantuml.sequencediagram.SequenceDiagram;
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

    public static void main(String[] args) throws IOException {
        String plantUMLSource =
            "@startuml\n" +
            "!pragma layout smetana\n" +
            "participant APP1 as C1231\n" +
            "participant APP2 as C1103\n" +
            "C1231 --> C1103 : POPOPOPO\n" +
            "@enduml";

        SourceStringReader reader = new SourceStringReader(plantUMLSource);
        System.out.println(reader);
        DiagramDescription diagramDescription = reader.generateDiagramDescription();
        System.out.println(diagramDescription.getDescription());
        BlockUml blockUml = reader.getBlocks().get(0);
        System.out.println(blockUml);
        Diagram diagram = blockUml.getDiagram();
        System.out.println(diagram);
        SequenceDiagram sequenceDiagram = (SequenceDiagram) diagram;
        Collection<Participant> participants = sequenceDiagram.participants();
        for (Participant participant : participants) {
            System.out.println(participant.getCode());
            System.out.println(participant.getDisplay(true));
            System.out.println(participant.getDisplay(false));
        }

        System.out.println("PARTICIPANTS");
        List<Event> events = sequenceDiagram.events();
        for (Event event : events) {
            Message message = (Message) event;
            System.out.print(message.getParticipant1().getDisplay(false).get(0));
            System.out.print(" ");
            System.out.print(message.getParticipant1().getCode());
            System.out.print(" ----------> ");
            System.out.print(message.getParticipant2().getDisplay(false).get(0));
            System.out.print(" ");
            System.out.print(message.getParticipant2().getCode());
            System.out.print(" : ");
            System.out.print(message.getLabel().get(0));
            System.out.println("");

            List<Note> notes = message.getNoteOnMessages();
            for (Note note : notes) {
                System.out.println(note);
            }
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        diagram.exportDiagram(byteArrayOutputStream, 0, new FileFormatOption(FileFormat.SVG));
        //diagramDescription = reader.outputImage(byteArrayOutputStream, new FileFormatOption(FileFormat.SVG));
        byteArrayOutputStream.close();
        System.out.println(new String(byteArrayOutputStream.toByteArray(), Charset.forName("UTF-8")));
    }

    public FlowImport importPlantuml(String plantUMLSource) {
        FlowImport flowImport = new FlowImport();

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
                                flowImportLine.setPotentialDataFlows(new ArrayList<>(potentialDataFlows));
                                if (CollectionUtils.isEmpty(flowImportLine.getPotentialDataFlows())) {
                                    DataFlow dataFlow = new DataFlow();
                                    dataFlow.setContractURL(url);
                                    dataFlow.setId(-1L);
                                    flowImportLine.addPotentialDataFlow(dataFlow);
                                    flowImportLine.setSelectedDataFlow(dataFlow);
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
                            flowImportLine.addPotentialDataFlow(selectedInterface.getDataFlows());
                            flowImportLine.setProtocol(selectedInterface.getProtocol());
                        }
                    }
                }
                if (!CollectionUtils.isEmpty(flowImportLine.getPotentialDataFlows())) {
                    if (flowImportLine.getPotentialDataFlows().size() == 1) {
                        flowImportLine.setSelectedDataFlow(flowImportLine.getPotentialDataFlows().get(0));
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
        if (display != null) {
            if (display.iterator().hasNext()) {
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

    public FunctionalFlow saveImport(FlowImport flowImport) {
        FunctionalFlow functionalFlow = new FunctionalFlow();

        List<String> interfacesAliases = interfaceRepository.findAlias();
        IdentifierGenerator interfaceIdgenerator = new IdentifierGenerator(interfacesAliases);

        List<String> flowAliases = functionalFlowRepository.findAlias();
        IdentifierGenerator flowIdGenerator = new IdentifierGenerator(flowAliases);

        functionalFlowRepository.save(functionalFlow);
        functionalFlow.setDescription(flowImport.getDescription());
        int order = 1;
        for (FlowImportLine flowImportLine : flowImport.getFlowImportLines()) {
            FlowInterface interface1;
            if (flowImportLine.getSelectedInterface() != null) {
                interface1 = interfaceRepository.getById(flowImportLine.getSelectedInterface().getId());
            } else {
                interface1 = new FlowInterface();
                interface1.setSource(applicationRepository.getById(flowImportLine.getSource().getId()));
                interface1.setTarget(applicationRepository.getById(flowImportLine.getTarget().getId()));
                interface1.setAlias(interfaceIdgenerator.getNext("GEN-"));
                interfaceRepository.save(interface1);
                if (flowImportLine.getProtocol() != null) {
                    interface1.setProtocol(protocolRepository.getById(flowImportLine.getProtocol().getId()));
                }
                if (flowImportLine.getSelectedDataFlow() != null) {
                    DataFlow dataFlow = dataFlowRepository.getById(flowImportLine.getSelectedDataFlow().getId());
                    if (dataFlow == null) {
                        dataFlow = new DataFlow();
                        dataFlowRepository.save(dataFlow);
                        dataFlow.setFrequency(flowImportLine.getSelectedDataFlow().getFrequency());
                        dataFlow.setFormat(flowImportLine.getSelectedDataFlow().getFormat());
                        dataFlow.setContractURL(flowImportLine.getSelectedDataFlow().getContractURL());
                        dataFlow.setDocumentationURL(flowImportLine.getSelectedDataFlow().getDocumentationURL());
                    }
                    interface1.addDataFlows(dataFlow);
                }
            }
            FunctionalFlowStep step = new FunctionalFlowStep();
            step.setDescription(flowImportLine.getDescription());
            step.stepOrder(order++);
            functionalFlow.addSteps(step);
            interface1.addSteps(step);
            flowStepRepository.save(step);
        }
        functionalFlow.setAlias(flowIdGenerator.getNext("GEN."));
        TreeSet<String> aliases = new TreeSet<>();
        for (FunctionalFlowStep step : functionalFlow.getSteps()) {
            aliases.add(step.getFlowInterface().getAlias());
        }
        return functionalFlow;
    }
}
