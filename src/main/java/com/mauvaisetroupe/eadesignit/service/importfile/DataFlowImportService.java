package com.mauvaisetroupe.eadesignit.service.importfile;

import com.mauvaisetroupe.eadesignit.domain.DataFlow;
import com.mauvaisetroupe.eadesignit.domain.DataFlowImport;
import com.mauvaisetroupe.eadesignit.domain.DataFlowItem;
import com.mauvaisetroupe.eadesignit.domain.FlowInterface;
import com.mauvaisetroupe.eadesignit.domain.enumeration.ImportStatus;
import com.mauvaisetroupe.eadesignit.repository.DataFlowImportRepository;
import com.mauvaisetroupe.eadesignit.repository.DataFlowItemRepository;
import com.mauvaisetroupe.eadesignit.repository.DataFlowRepository;
import com.mauvaisetroupe.eadesignit.repository.FlowInterfaceRepository;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.apache.poi.EncryptedDocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

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

    public List<DataFlowImport> importExcel(InputStream excel, String sheetName) throws EncryptedDocumentException, IOException {
        ExcelReader datFlowExcelReader = new ExcelReader(excel);

        // DataFlow

        List<Map<String, Object>> dataflows = datFlowExcelReader.getSheet(DATAFLOW_SHEET_NAME);
        List<Map<String, Object>> dataflowItems = datFlowExcelReader.getSheet(DATAFLOWITEM_SHEET_NAME);

        List<DataFlowImport> result = new ArrayList<DataFlowImport>();
        for (Map<String, Object> map : dataflows) {
            DataFlowImport dataFlowImport = mapArrayToImportDataFlowImport(map);
            DataFlow dataFlow = null;
            try {
                dataFlow = mapImportToDataFlow(dataFlowImport);

                // DataFlow is mandatory linked to Interface
                if (StringUtils.hasText(dataFlowImport.getFlowInterfaceId())) {
                    Optional<FlowInterface> optional = flowInterfaceRepository.findByAlias(dataFlowImport.getFlowInterfaceId());
                    if (optional.isPresent()) {
                        FlowInterface flowInterface = optional.get();
                        dataFlow.setFlowInterface(flowInterface);
                        dataFlowRepository.save(dataFlow);
                    } else {
                        dataFlow = null;
                        dataFlowImport.setImportDataStatus(ImportStatus.ERROR);
                        dataFlowImport.setImportStatusMessage("No ID flow found");
                    }
                } else {
                    dataFlow = null;
                    dataFlowImport.setImportDataStatus(ImportStatus.ERROR);
                    dataFlowImport.setImportStatusMessage("No ID flow found");
                }
            } catch (Exception e) {
                dataFlow = null;
                dataFlowImport.setImportDataItemStatus(ImportStatus.ERROR);
                dataFlowImport.setImportStatusMessage(e.getMessage());
            }
            result.add(dataFlowImport);
        }

        for (Map<String, Object> map : dataflowItems) {
            DataFlowImport dataFlowImport = mapArrayToImportDataFlowImport(map);
            DataFlowItem dataFlowItem = mapImportToDataFlowItem(dataFlowImport);
            try {
                Set<DataFlow> parents = dataFlowRepository.findByResourceNameIgnoreCase(dataFlowImport.getDataParentName());
                if (parents != null && !parents.isEmpty()) {
                    DataFlow parent = null;
                    if (parents.size() == 1) {
                        dataFlowImport.setImportDataItemStatus(ImportStatus.EXISTING);
                        parent = parents.iterator().next();
                    } else {
                        throw new Exception("Many dataflow for same resource name ");
                    }
                    dataFlowItem.setDataFlow(parent);
                    dataFlowItemRepository.save(dataFlowItem);
                    dataFlowRepository.save(parent);
                } else {
                    dataFlowItem = null;
                    dataFlowImport.setImportDataItemStatus(ImportStatus.ERROR);
                    dataFlowImport.setImportStatusMessage("No parent found");
                }
            } catch (Exception e) {
                dataFlowItem = null;
                dataFlowImport.setImportDataItemStatus(ImportStatus.ERROR);
                dataFlowImport.setImportStatusMessage(e.getMessage());
            }
            result.add(dataFlowImport);
        }
        return result;
    }

    private DataFlowImport mapArrayToImportDataFlowImport(Map<String, Object> map) {
        DataFlowImport dataFlowImport = new DataFlowImport();
        dataFlowImport.setDataResourceName(clean(map.get(DATA_RESOURCE_NAME)));
        dataFlowImport.setDataResourceType(clean(map.get(DATA_RESOURCE_TYPE)));
        dataFlowImport.setDataDescription(clean(map.get(DATA_DESCRIPTION)));
        dataFlowImport.setDataId(clean(map.get(DATA_ID)));
        dataFlowImport.setDataParentId(clean(map.get(DATA_PARENTT_ID)));
        dataFlowImport.setDataParentName(clean(map.get(DATA_PARENT_RESOURCE_NAME)));
        dataFlowImport.setDataContractURL(clean(map.get(DATA_CONTRACT_URL)));
        dataFlowImport.setDataDocumentationURL(clean(map.get(DATA_DOCUMENTATION_URL)));
        dataFlowImport.setFlowInterfaceId(clean(map.get(DATA_ID_INTERFACE)));
        dataFlowImport.setFunctionalFlowId(clean(map.get(DATA_ID_FUNCTIONAL_FLOW)));
        dataFlowImport.setSource(clean(map.get(DATA_SOURCE)));
        dataFlowImport.setTarget(clean(map.get(DATA_TARGET)));

        return dataFlowImport;
    }

    private String clean(Object value) {
        if (value == null) return null;
        if (!(value instanceof String)) return null;
        return value.toString();
    }

    public DataFlow mapImportToDataFlow(DataFlowImport dataImport) throws Exception {
        Set<DataFlow> dataFlows = dataFlowRepository.findByResourceNameIgnoreCase(dataImport.getDataResourceName());
        DataFlow dataFlow = null;
        if (dataFlows == null || dataFlows.isEmpty()) {
            dataFlow = new DataFlow();
            dataImport.setImportDataItemStatus(ImportStatus.NEW);
        } else {
            if (dataFlows.size() == 1) {
                dataFlow = dataFlows.iterator().next();
                dataImport.setImportDataItemStatus(ImportStatus.EXISTING);
            } else {
                throw new Exception("Many dataflow for same resource name");
            }
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
            dataImport.setImportDataItemStatus(ImportStatus.NEW);
        } else {
            dataImport.setImportDataItemStatus(ImportStatus.EXISTING);
        }
        dataFlow.setResourceName(dataImport.getDataResourceName());
        dataFlow.setResourceType(dataImport.getDataResourceType());
        dataFlow.setDescription(dataImport.getDataDescription());
        dataFlow.setContractURL(dataImport.getDataContractURL());
        dataFlow.setDocumentationURL(dataImport.getDataContractURL());
        return dataFlow;
    }
}
