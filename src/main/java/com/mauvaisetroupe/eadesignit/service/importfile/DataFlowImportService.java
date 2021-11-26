package com.mauvaisetroupe.eadesignit.service.importfile;

import com.mauvaisetroupe.eadesignit.domain.DataFlow;
import com.mauvaisetroupe.eadesignit.domain.DataFlowImport;
import com.mauvaisetroupe.eadesignit.domain.DataFlowItem;
import com.mauvaisetroupe.eadesignit.repository.DataFlowImportRepository;
import com.mauvaisetroupe.eadesignit.repository.DataFlowItemRepository;
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

    private static final String DATA_RESOURCE_NAME = "Resource name";
    private static final String DATA_RESOURCE_TYPE = "Resource type";
    private static final String DATA_DESCRIPTION = "Data description";
    private static final String DATA_SOURCE = "Source";
    private static final String DATA_TARGET = "Target";
    private static final String DATA_ID_INTERFACE = "Id flow";
    private static final String DATA_CONTRACT_URL = "Contract URL";
    private static final String DATA_PARENT_RESOURCE_NAME = "Parent resource name";

    private static final String DATAFLOW_SHEET_NAME = "Data";
    private static final String DATAFLOWITEM_SHEET_NAME = "DataItem";

    private final Logger log = LoggerFactory.getLogger(ApplicationImportService.class);

    private final FlowInterfaceRepository flowInterfaceRepository;
    private final DataFlowImportRepository dataFlowImportRepository;
    private final DataFlowItemRepository dataFlowItemRepository;

    public DataFlowImportService(
        FlowInterfaceRepository flowInterfaceRepository,
        DataFlowImportRepository dataFlowImportRepository,
        DataFlowItemRepository dataFlowItemRepository
    ) {
        this.flowInterfaceRepository = flowInterfaceRepository;
        this.dataFlowImportRepository = dataFlowImportRepository;
        this.dataFlowItemRepository = dataFlowItemRepository;
    }

    public List<DataFlowImport> importExcel(InputStream excel, String sheetName) throws Exception {
        ExcelReader datFlowExcelReader = new ExcelReader(excel, DATAFLOW_SHEET_NAME);
        ExcelReader datFlowItenExcelReader = new ExcelReader(excel, DATAFLOWITEM_SHEET_NAME);

        // DataFlow

        List<Map<String, Object>> dataflows = datFlowExcelReader.getExcelDF();
        List<Map<String, Object>> dataflowItems = datFlowItenExcelReader.getExcelDF();

        List<DataFlowImport> result = new ArrayList<DataFlowImport>();
        for (Map<String, Object> map : dataflows) {
            DataFlowImport dataFlowImport = mapArrayToImportDataFlowImport(map);
            System.out.println(dataFlowImport);
        }

        for (Map<String, Object> map : dataflowItems) {
            DataFlowImport dataFlowImport = mapArrayToImportDataFlowImport(map);
            System.out.println(dataFlowImport);
        }

        return result;
    }

    private DataFlowImport mapArrayToImportDataFlowImport(Map<String, Object> map) {
        DataFlowImport dataFlowImport = new DataFlowImport();
        dataFlowImport.setDataResourceName(DATA_RESOURCE_NAME);
        dataFlowImport.setDataResourceType(DATA_RESOURCE_TYPE);
        dataFlowImport.setDataDescription(DATA_DESCRIPTION);

        return null;
    }

    public DataFlow mapImportToDataFlow(DataFlowImport dataFlowImport) {
        return null;
    }

    public DataFlowItem mapImportToDataFlowItem(DataFlowImport dataFlowImport) {
        return null;
    }
}
