package com.mauvaisetroupe.eadesignit.service.importfile;

import static org.junit.Assert.assertEquals;

import com.mauvaisetroupe.eadesignit.service.diagram.plantuml.PlantUMLBuilder;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import org.junit.jupiter.api.Test;

public class PlantumlImportServiceTest {

    String rawPlantuml =
        "@startuml\n" +
        "!pragma layout smetana\n" +
        "skinparam componentStyle rectangle\n" +
        "skinparam hyperlinkColor #000000\n" +
        "skinparam hyperlinkUnderline false\n" +
        "skinparam monochrome true\n" +
        "skinparam shadowing false\n" +
        "skinparam padding 5\n" +
        "skinparam rectangle {\n" +
        "  backgroundColor #A9DCDF\n" +
        "}\n" +
        "skinparam component {\n" +
        "  backgroundColor white\n" +
        "  borderColor black\n" +
        "}\n" +
        "skinparam rectangleFontSize 10\n" +
        "hide footbox\n" +
        "' end of header \n" +
        "\"Investor\" --> \"portfolio-analytics-tool\" : Request Investment Analysis\n" +
        "note right\n" +
        "API\n" +
        "end note\n" +
        "\"portfolio-analytics-tool\" --> \"investment-portfolio-manager\" : Retrieve Portfolio Data\n" +
        "note right\n" +
        "API\n" +
        "end note\n" +
        "\"investment-portfolio-manager\" --> \"portfolio-analytics-tool\" : Portfolio Data\n" +
        "note right\n" +
        "API\n" +
        "end note\n" +
        "\"portfolio-analytics-tool\" --> \"investment-risk-analyzer\" : Assess Investment Risk\n" +
        "note right\n" +
        "Event\n" +
        "end note\n" +
        "\"investment-risk-analyzer\" --> \"portfolio-analytics-tool\" : Risk Assessment\n" +
        "note right\n" +
        "API\n" +
        "end note\n" +
        "\"portfolio-analytics-tool\" --> \"Investor\" : Investment Advice\n" +
        "note right\n" +
        "API\n" +
        "end note\n" +
        "@enduml";

    @Test
    public void test_ProtocolAsNote_NoInterface() {
        PlantumlImportService importService = new PlantumlImportService();

        String plantumlForEdition = importService.getPlantUMLSourceForEdition(rawPlantuml, true, false, null);

        String expectedPlantumlForEdition =
            "\"Investor\" --> \"portfolio-analytics-tool\" : Request Investment Analysis\n" +
            "note right\n" +
            "API\n" +
            "end note\n" +
            "\"portfolio-analytics-tool\" --> \"investment-portfolio-manager\" : Retrieve Portfolio Data\n" +
            "note right\n" +
            "API\n" +
            "end note\n" +
            "\"investment-portfolio-manager\" --> \"portfolio-analytics-tool\" : Portfolio Data\n" +
            "note right\n" +
            "API\n" +
            "end note\n" +
            "\"portfolio-analytics-tool\" --> \"investment-risk-analyzer\" : Assess Investment Risk\n" +
            "note right\n" +
            "Event\n" +
            "end note\n" +
            "\"investment-risk-analyzer\" --> \"portfolio-analytics-tool\" : Risk Assessment\n" +
            "note right\n" +
            "API\n" +
            "end note\n" +
            "\"portfolio-analytics-tool\" --> \"Investor\" : Investment Advice\n" +
            "note right\n" +
            "API\n" +
            "end note\n";

        assertEquals(expectedPlantumlForEdition, plantumlForEdition);

        importService.plantUMLBuilder = new PlantUMLBuilder();
        String afterEdition = importService.preparePlantUMLSource(plantumlForEdition).trim();
        assertEquals(rawPlantuml, afterEdition);
    }

    @Test
    public void test_InlineProtocol_NoInterface() {
        PlantumlImportService importService = new PlantumlImportService();

        String plantumlForEdition = importService.getPlantUMLSourceForEdition(rawPlantuml, true, true, null);

        String expectedPlantumlForEdition =
            "\"Investor\" --> \"portfolio-analytics-tool\" : Request Investment Analysis // API\n" +
            "\"portfolio-analytics-tool\" --> \"investment-portfolio-manager\" : Retrieve Portfolio Data // API\n" +
            "\"investment-portfolio-manager\" --> \"portfolio-analytics-tool\" : Portfolio Data // API\n" +
            "\"portfolio-analytics-tool\" --> \"investment-risk-analyzer\" : Assess Investment Risk // Event\n" +
            "\"investment-risk-analyzer\" --> \"portfolio-analytics-tool\" : Risk Assessment // API\n" +
            "\"portfolio-analytics-tool\" --> \"Investor\" : Investment Advice // API\n";

        assertEquals(expectedPlantumlForEdition, plantumlForEdition);

        importService.plantUMLBuilder = new PlantUMLBuilder();
        String afterEdition = importService.preparePlantUMLSource(plantumlForEdition).trim();
        assertEquals(rawPlantuml, afterEdition);
    }

    @Test
    public void test_InlineProtocol_Interface() {
        PlantumlImportService importService = new PlantumlImportService();

        Queue<String> interfaces = new LinkedList<String>(List.of("LCO.00041", "LCO-0120120", "L-65-32.36", "LCO", "LCO", "LCO2"));

        String plantumlForEdition = importService.getPlantUMLSourceForEdition(rawPlantuml, true, true, interfaces);

        String expectedPlantumlForEdition =
            "\"Investor\" --> \"portfolio-analytics-tool\" : Request Investment Analysis // API ##" +
            interfaces.poll() +
            "\n" +
            "\"portfolio-analytics-tool\" --> \"investment-portfolio-manager\" : Retrieve Portfolio Data // API ##" +
            interfaces.poll() +
            "\n" +
            "\"investment-portfolio-manager\" --> \"portfolio-analytics-tool\" : Portfolio Data // API ##" +
            interfaces.poll() +
            "\n" +
            "\"portfolio-analytics-tool\" --> \"investment-risk-analyzer\" : Assess Investment Risk // Event ##" +
            interfaces.poll() +
            "\n" +
            "\"investment-risk-analyzer\" --> \"portfolio-analytics-tool\" : Risk Assessment // API ##" +
            interfaces.poll() +
            "\n" +
            "\"portfolio-analytics-tool\" --> \"Investor\" : Investment Advice // API ##" +
            interfaces.poll() +
            "\n";

        assertEquals(expectedPlantumlForEdition, plantumlForEdition);

        importService.plantUMLBuilder = new PlantUMLBuilder();
        String afterEdition = importService.preparePlantUMLSource(plantumlForEdition).trim();
        assertEquals(rawPlantuml, afterEdition);
    }

    @Test
    public void test_ProtocolAsNote_Interface() {
        PlantumlImportService importService = new PlantumlImportService();

        Queue<String> interfaces = new LinkedList<String>(List.of("LCO.00041", "LCO-0120120", "L-65-32.36", "LCO", "LCO", "LCO2"));

        String plantumlForEdition = importService.getPlantUMLSourceForEdition(rawPlantuml, true, false, interfaces);

        String expectedPlantumlForEdition =
            "\"Investor\" --> \"portfolio-analytics-tool\" : Request Investment Analysis ##" +
            interfaces.poll() +
            "\n" +
            "note right\n" +
            "API\n" +
            "end note\n" +
            "\"portfolio-analytics-tool\" --> \"investment-portfolio-manager\" : Retrieve Portfolio Data ##" +
            interfaces.poll() +
            "\n" +
            "note right\n" +
            "API\n" +
            "end note\n" +
            "\"investment-portfolio-manager\" --> \"portfolio-analytics-tool\" : Portfolio Data ##" +
            interfaces.poll() +
            "\n" +
            "note right\n" +
            "API\n" +
            "end note\n" +
            "\"portfolio-analytics-tool\" --> \"investment-risk-analyzer\" : Assess Investment Risk ##" +
            interfaces.poll() +
            "\n" +
            "note right\n" +
            "Event\n" +
            "end note\n" +
            "\"investment-risk-analyzer\" --> \"portfolio-analytics-tool\" : Risk Assessment ##" +
            interfaces.poll() +
            "\n" +
            "note right\n" +
            "API\n" +
            "end note\n" +
            "\"portfolio-analytics-tool\" --> \"Investor\" : Investment Advice ##" +
            interfaces.poll() +
            "\n" +
            "note right\n" +
            "API\n" +
            "end note\n";

        assertEquals(expectedPlantumlForEdition, plantumlForEdition);

        importService.plantUMLBuilder = new PlantUMLBuilder();
        String afterEdition = importService.preparePlantUMLSource(plantumlForEdition).trim();
        assertEquals(rawPlantuml, afterEdition);
    }
}
