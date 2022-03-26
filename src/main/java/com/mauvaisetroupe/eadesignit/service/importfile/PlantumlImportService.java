package com.mauvaisetroupe.eadesignit.service.importfile;

import com.mauvaisetroupe.eadesignit.domain.Application;
import com.mauvaisetroupe.eadesignit.domain.DataFlow;
import com.mauvaisetroupe.eadesignit.domain.FlowInterface;
import com.mauvaisetroupe.eadesignit.domain.FunctionalFlow;
import com.mauvaisetroupe.eadesignit.domain.FunctionalFlowStep;
import com.mauvaisetroupe.eadesignit.domain.Protocol;
import com.mauvaisetroupe.eadesignit.repository.ApplicationRepository;
import com.mauvaisetroupe.eadesignit.repository.FlowInterfaceRepository;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.List;
import net.sourceforge.plantuml.BlockUml;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.sequencediagram.Event;
import net.sourceforge.plantuml.sequencediagram.Message;
import net.sourceforge.plantuml.sequencediagram.Note;
import net.sourceforge.plantuml.sequencediagram.Participant;
import net.sourceforge.plantuml.sequencediagram.SequenceDiagram;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlantumlImportService {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private FlowInterfaceRepository interfaceRepository;

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

    public FunctionalFlow importPlantuml(String plantUMLSource) {
        FunctionalFlow flow = new FunctionalFlow();

        SourceStringReader reader = new SourceStringReader(plantUMLSource);
        BlockUml blockUml = reader.getBlocks().get(0);
        Diagram diagram = blockUml.getDiagram();
        SequenceDiagram sequenceDiagram = (SequenceDiagram) diagram;
        List<Event> events = sequenceDiagram.events();
        int stepOrder = 0;
        for (Event event : events) {
            Message message = (Message) event;
            FlowInterface interface1 = new FlowInterface();
            Application source = new Application();
            source.setName(message.getParticipant1().getDisplay(false).get(0).toString());
            Application target = new Application();
            target.setName(message.getParticipant2().getDisplay(false).get(0).toString());
            interface1.setSource(source);
            interface1.setTarget(target);
            FunctionalFlowStep step = new FunctionalFlowStep();
            step.setFlow(flow);
            step.setFlowInterface(interface1);
            step.setStepOrder(stepOrder++);
            step.setDescription(message.getLabel().get(0).toString());
            flow.addSteps(step);

            List<Note> notes = message.getNoteOnMessages();
            DataFlow dataFlow = new DataFlow();
            for (Note note : notes) {
                String _note = note.getStrings().get(0).toString();
                String[] _string = _note.split(",");
                for (int i = 0; i < _string.length; i++) {
                    if (i == 0) {
                        Protocol protocol = new Protocol();

                        String name = null, url = null;
                        if (_string[i].contains("[[")) {
                            _string[i] = _string[i].replace("[[", "");
                            _string[i] = _string[i].replace("]]", "");
                            String[] _noteArrray = _string[i].split(" ");
                            url = _noteArrray[0];
                            name = _noteArrray[1];
                            dataFlow.setContractURL(url);
                            dataFlow.setId(1111L);
                            interface1.addDataFlows(dataFlow);
                        } else {
                            name = _string[i];
                        }
                        protocol.setName(name);
                        interface1.setProtocol(protocol);
                    } else {
                        throw new RuntimeException("Not implemented");
                    }
                }
            }
        }
        return flow;
    }
}
