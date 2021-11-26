package com.mauvaisetroupe.eadesignit.service.importfile;

import com.mauvaisetroupe.eadesignit.domain.DataFlow;
import com.mauvaisetroupe.eadesignit.domain.DataFlowImport;
import com.mauvaisetroupe.eadesignit.domain.DataFlowItem;
import com.mauvaisetroupe.eadesignit.repository.DataFlowImportRepository;
import com.mauvaisetroupe.eadesignit.repository.DataFlowItemRepository;
import com.mauvaisetroupe.eadesignit.repository.DataFlowRepository;
import com.mauvaisetroupe.eadesignit.repository.FlowInterfaceRepository;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DataFlowImportService {

    private static final String DATA_ID = "Data id";
    private static final String DATA_PARENTT_ID = "Parent id";
    private static final String DATA_RESOURCE_NAME = "Resource name";
    private static final String DATA_RESOURCE_TYPE = "Resource type";
    private static final String DATA_DESCRIPTION = "Data description";
    private static final String DATA_SOURCE = "Source";
    private static final String DATA_TARGET = "Target";
    private static final String DATA_ID_INTERFACE = "Id flow";
    private static final String DATA_ID_FUNCTIONAL_FLOW = "Alias flow";
    private static final String DATA_CONTRACT_URL = "Contract URL";
    private static final String DATA_DOCUMENTATION_URL = "Documentation URL";
    private static final String DATA_PARENT_RESOURCE_NAME = "Parent resource name";

    private static final String DATAFLOW_SHEET_NAME = "Data";
    private static final String DATAFLOWITEM_SHEET_NAME = "DataItem";

    private final Logger log = LoggerFactory.getLogger(ApplicationImportService.class);

    private final FlowInterfaceRepository flowInterfaceRepository;
    private final DataFlowRepository dataFlowRepository;
    private final DataFlowItemRepository dataFlowItemRepository;

    public DataFlowImportService(
        FlowInterfaceRepository flowInterfaceRepository,
        DataFlowRepository dataFlowRepository,
        DataFlowItemRepository dataFlowItemRepository
    ) {
        this.flowInterfaceRepository = flowInterfaceRepository;
        this.dataFlowRepository = dataFlowRepository;
        this.dataFlowItemRepository = dataFlowItemRepository;
    }

    public List<DataFlowImport> importExcel(InputStream excel, String sheetName) throws Exception {
        ExcelReader datFlowExcelReader = new ExcelReader(excel);

        // DataFlow

        List<Map<String, Object>> dataflows = datFlowExcelReader.getSheet(DATAFLOW_SHEET_NAME);
        List<Map<String, Object>> dataflowItems = datFlowExcelReader.getSheet(DATAFLOWITEM_SHEET_NAME);

        List<DataFlowImport> result = new ArrayList<DataFlowImport>();
        for (Map<String, Object> map : dataflows) {
            DataFlowImport dataFlowImport = mapArrayToImportDataFlowImport(map);
            DataFlow dataFlow = mapImportToDataFlow(dataFlowImport);
            dataFlowRepository.save(dataFlow);
        }

        for (Map<String, Object> map : dataflowItems) {
            DataFlowImport dataFlowImport = mapArrayToImportDataFlowImport(map);
            DataFlowItem dataFlowItem = mapImportToDataFlowItem(dataFlowImport);
            DataFlow parent = dataFlowRepository.findByResourceNameIgnoreCase(dataFlowImport.getDataParentName());
            if (parent != null) {
                dataFlowItem.setDataFlow(parent);
                dataFlowItemRepository.save(dataFlowItem);
                dataFlowRepository.save(parent);
            }
        }
        return result;
    }

    private DataFlowImport mapArrayToImportDataFlowImport(Map<String, Object> map) {
        DataFlowImport dataFlowImport = new DataFlowImport();
        dataFlowImport.setDataResourceName(DATA_RESOURCE_NAME);
        dataFlowImport.setDataResourceType(DATA_RESOURCE_TYPE);
        dataFlowImport.setDataDescription(DATA_DESCRIPTION);
        dataFlowImport.setDataId(DATA_ID);
        dataFlowImport.setDataParentId(DATA_PARENTT_ID);
        dataFlowImport.setDataParentName(DATA_PARENT_RESOURCE_NAME);
        dataFlowImport.setDataContractURL(DATA_CONTRACT_URL);
        dataFlowImport.setDataDocumentationURL(DATA_DOCUMENTATION_URL);
        dataFlowImport.setFlowInterfaceId(DATA_ID_INTERFACE);
        dataFlowImport.setFunctionalFlowId(DATA_ID_FUNCTIONAL_FLOW);
        dataFlowImport.setSource(DATA_SOURCE);
        dataFlowImport.setTarget(DATA_TARGET);
        return dataFlowImport;
    }

    public DataFlow mapImportToDataFlow(DataFlowImport dataImport) {
        DataFlow dataFlow = dataFlowRepository.findByResourceNameIgnoreCase(dataImport.getDataResourceName());
        if (dataFlow == null) {
            dataFlow = new DataFlow();
        }
        dataFlow.setResourceName(dataImport.getDataResourceName());
        dataFlow.setResourceType(dataImport.getDataResourceType());
        dataFlow.setDescription(dataImport.getDataDescription());
        dataFlow.setContractURL(dataImport.getDataContractURL());
        dataFlow.setDocumentationURL(dataImport.getDataContractURL());

        return dataFlow;
    }

    public DataFlowItem mapImportToDataFlowItem(DataFlowImport dataImport) {
        DataFlowItem dataFlow = dataFlowItemRepository.findByResourceNameIgnoreCase(dataImport.getDataResourceName());
        if (dataFlow == null) {
            dataFlow = new DataFlowItem();
        }
        dataFlow.setResourceName(dataImport.getDataResourceName());
        dataFlow.setResourceType(dataImport.getDataResourceType());
        dataFlow.setDescription(dataImport.getDataDescription());
        dataFlow.setContractURL(dataImport.getDataContractURL());
        dataFlow.setDocumentationURL(dataImport.getDataContractURL());
        return dataFlow;
    }
}
