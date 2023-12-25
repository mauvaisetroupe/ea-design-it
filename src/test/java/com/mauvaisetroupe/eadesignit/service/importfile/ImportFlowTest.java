package com.mauvaisetroupe.eadesignit.service.importfile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.mauvaisetroupe.eadesignit.IntegrationTest;
import com.mauvaisetroupe.eadesignit.domain.Capability;
import com.mauvaisetroupe.eadesignit.domain.CapabilityApplicationMapping;
import com.mauvaisetroupe.eadesignit.domain.FlowInterface;
import com.mauvaisetroupe.eadesignit.domain.FunctionalFlow;
import com.mauvaisetroupe.eadesignit.domain.LandscapeView;
import com.mauvaisetroupe.eadesignit.repository.ApplicationRepository;
import com.mauvaisetroupe.eadesignit.repository.BusinessObjectRepository;
import com.mauvaisetroupe.eadesignit.repository.CapabilityApplicationMappingRepository;
import com.mauvaisetroupe.eadesignit.repository.CapabilityRepository;
import com.mauvaisetroupe.eadesignit.repository.DataObjectRepository;
import com.mauvaisetroupe.eadesignit.repository.FlowInterfaceRepository;
import com.mauvaisetroupe.eadesignit.repository.FunctionalFlowRepository;
import com.mauvaisetroupe.eadesignit.repository.LandscapeViewRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

@IntegrationTest
public class ImportFlowTest {

    @Autowired
    FlowImportService flowImportService;

    @Autowired
    ApplicationImportService applicationImportService;

    @Autowired
    LandscapeViewRepository landscapeViewRepository;

    @Autowired
    FunctionalFlowRepository functionalFlowRepository;

    @Autowired
    ApplicationRepository applicationRepository;

    @Autowired
    FlowInterfaceRepository flowInterfaceRepository;

    @Autowired
    CapabilityImportService capabilityImportService;

    @Autowired
    CapabilityRepository capabilityRepository;

    @Autowired
    CapabilityApplicationMappingRepository capabilityApplicationMappingRepository;

    @Autowired
    ApplicationCapabilityImportService applicationCapabilityImportService;

    @Autowired
    DataObjectImportService dataObjectImportService;

    @Autowired
    DataObjectRepository dataObjectRepository;

    @Autowired
    BusinessObjectRepository businessObjectRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    protected List<LandscapeView> checkNbLandscapes(int nbLandscape) {
        List<LandscapeView> landscapes;
        landscapes = landscapeViewRepository.findAllWithEagerRelationships();
        assertEquals(nbLandscape, landscapes.size());
        return landscapes;
    }

    protected List<Capability> checkNbCapabilities(int nbCapabilities) {
        List<Capability> capabilities = capabilityRepository.findAll();
        assertEquals(nbCapabilities, capabilities.size());
        return capabilities;
    }

    protected List<CapabilityApplicationMapping> checkNbCapabilityMappings(int nbMappings) {
        List<CapabilityApplicationMapping> applicationMappings = capabilityApplicationMappingRepository.findAll();
        assertEquals(nbMappings, applicationMappings.size());
        return applicationMappings;
    }

    protected void checkNbFlows(LandscapeView landscapeView, int nbFlows) {
        assertEquals(nbFlows, landscapeView.getFlows().size());
    }

    protected void checkNbFlows(int nbFlows) {
        List<FunctionalFlow> flows = functionalFlowRepository.findAll();
        assertEquals(nbFlows, flows.size());
    }

    protected FunctionalFlow checkNbsteps(String flowAlias, int nbSteps) {
        FunctionalFlow flow = functionalFlowRepository.findByAlias(flowAlias).orElseThrow();
        assertEquals(nbSteps, flow.getSteps().size());
        return flow;
    }

    protected List<FlowInterface> checkNbInterfaces(int nbInterfaces) {
        List<FlowInterface> interfaces = flowInterfaceRepository.findAll();
        assertEquals(nbInterfaces, interfaces.size());
        return interfaces;
    }

    protected void checkInterfaceExists(String alias, List<FlowInterface> interfaces) {
        boolean found = false;
        for (FlowInterface inter : interfaces) {
            if (alias.equals(inter.getAlias())) return;
        }
        assertTrue(found, "Interface not found : " + alias);
    }

    public List<String> getAllTableNames() {
        // Query to get all table names
        String query = "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'PUBLIC'";
        return jdbcTemplate.queryForList(query, String.class);
    }

    @AfterEach
    @BeforeEach
    @Transactional
    public void clearDatabase() {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

        //SELECT 'jdbcTemplate.execute("TRUNCATE TABLE ' ||  table_schema || '.' || table_name || ';");' AS sql_statement FROM information_schema.tables WHERE table_schema = 'PUBLIC' ORDER BY table_name;
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");

        List<String> tableNames = getAllTableNames();

        // Truncate tables in reverse order
        for (int i = tableNames.size() - 1; i >= 0; i--) {
            String tableName = tableNames.get(i);
            String truncateQuery = "TRUNCATE TABLE " + tableName;
            jdbcTemplate.execute(truncateQuery);
        }

        // jdbcTemplate.execute("TRUNCATE TABLE PUBLIC.APPLICATION;");
        // jdbcTemplate.execute("TRUNCATE TABLE PUBLIC.APPLICATION_CATEGORY;");
        // jdbcTemplate.execute("TRUNCATE TABLE PUBLIC.BUSINESS_OBJECT;");
        // jdbcTemplate.execute("TRUNCATE TABLE PUBLIC.CAPABILITY;");
        // jdbcTemplate.execute("TRUNCATE TABLE PUBLIC.CAPABILITY_APPLICATION_MAPPING;");
        // jdbcTemplate.execute("TRUNCATE TABLE PUBLIC.COMPONENT;");
        // jdbcTemplate.execute("TRUNCATE TABLE PUBLIC.DATABASECHANGELOG;");
        // jdbcTemplate.execute("TRUNCATE TABLE PUBLIC.DATABASECHANGELOGLOCK;");
        // jdbcTemplate.execute("TRUNCATE TABLE PUBLIC.DATAFLOW;");
        // jdbcTemplate.execute("TRUNCATE TABLE PUBLIC.DATA_FLOW_ITEM;");
        // jdbcTemplate.execute("TRUNCATE TABLE PUBLIC.DATA_FORMAT;");
        // jdbcTemplate.execute("TRUNCATE TABLE PUBLIC.DATA_OBJECT;");
        // jdbcTemplate.execute("TRUNCATE TABLE PUBLIC.EXTERNAL_REFERENCE;");
        // jdbcTemplate.execute("TRUNCATE TABLE PUBLIC.EXTERNAL_SYSTEM;");
        // jdbcTemplate.execute("TRUNCATE TABLE PUBLIC.FLOW;");
        // jdbcTemplate.execute("TRUNCATE TABLE PUBLIC.FLOW_GROUP;");
        // jdbcTemplate.execute("TRUNCATE TABLE PUBLIC.INTERFACE;");
        // jdbcTemplate.execute("TRUNCATE TABLE PUBLIC.JHI_AUTHORITY;");
        // jdbcTemplate.execute("TRUNCATE TABLE PUBLIC.JHI_USER;");
        // jdbcTemplate.execute("TRUNCATE TABLE PUBLIC.JHI_USER_AUTHORITY;");
        // jdbcTemplate.execute("TRUNCATE TABLE PUBLIC.LANDSCAPE_VIEW;");
        // jdbcTemplate.execute("TRUNCATE TABLE PUBLIC.OWNER;");
        // jdbcTemplate.execute("TRUNCATE TABLE PUBLIC.PROTOCOL;");
        // jdbcTemplate.execute("TRUNCATE TABLE PUBLIC.REL_APPLICATION__CATEGORIES;");
        // jdbcTemplate.execute("TRUNCATE TABLE PUBLIC.REL_APPLICATION__EXTERNALIDS;");
        // jdbcTemplate.execute("TRUNCATE TABLE PUBLIC.REL_APPLICATION__TECHNOLOGIES;");
        // jdbcTemplate.execute("TRUNCATE TABLE PUBLIC.REL_CAPABILITY_AP__LANDSCAP_B2;");
        // jdbcTemplate.execute("TRUNCATE TABLE PUBLIC.REL_COMPONENT__CATEGORIES;");
        // jdbcTemplate.execute("TRUNCATE TABLE PUBLIC.REL_COMPONENT__EXTERNALIDS;");
        // jdbcTemplate.execute("TRUNCATE TABLE PUBLIC.REL_COMPONENT__TECHNOLOGIES;");
        // jdbcTemplate.execute("TRUNCATE TABLE PUBLIC.REL_DATAFLOW__FUNCTIONAL_FLOWS;");
        // jdbcTemplate.execute("TRUNCATE TABLE PUBLIC.REL_DATA_OBJECT__LANDSCAPES;");
        // jdbcTemplate.execute("TRUNCATE TABLE PUBLIC.REL_DATA_OBJECT__TECHNOLOGIES;");
        // jdbcTemplate.execute("TRUNCATE TABLE PUBLIC.REL_FLOW__INTERFACES;");
        // jdbcTemplate.execute("TRUNCATE TABLE PUBLIC.REL_LANDSCAPE_VIEW__FLOWS;");
        // jdbcTemplate.execute("TRUNCATE TABLE PUBLIC.REL_OWNER__USERS;");
        // jdbcTemplate.execute("TRUNCATE TABLE PUBLIC.TECHNOLOGY;");
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");
    }
}
