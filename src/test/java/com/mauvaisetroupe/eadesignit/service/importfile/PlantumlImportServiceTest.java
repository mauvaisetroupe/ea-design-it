package com.mauvaisetroupe.eadesignit.service.importfile;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;

public class PlantumlImportServiceTest {

    @Test
    public void testGetPlantUMLSourceForEdition() {
        PlantumlImportService importService = new PlantumlImportService();

        String umlString =
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

        String resource = importService.getPlantUMLSourceForEdition(umlString, true, false);

        String umlInteractionString =
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

        resource = importService.getPlantUMLSourceForEdition(umlString, true, true);

        String interactionsString =
            "\"Investor\" --> \"portfolio-analytics-tool\" : Request Investment Analysis // API\n" +
            "\"portfolio-analytics-tool\" --> \"investment-portfolio-manager\" : Retrieve Portfolio Data // API\n" +
            "\"investment-portfolio-manager\" --> \"portfolio-analytics-tool\" : Portfolio Data // API\n" +
            "\"portfolio-analytics-tool\" --> \"investment-risk-analyzer\" : Assess Investment Risk // Event\n" +
            "\"investment-risk-analyzer\" --> \"portfolio-analytics-tool\" : Risk Assessment // API\n" +
            "\"portfolio-analytics-tool\" --> \"Investor\" : Investment Advice // API\n";

        assertEquals(interactionsString, resource);
    }
}
