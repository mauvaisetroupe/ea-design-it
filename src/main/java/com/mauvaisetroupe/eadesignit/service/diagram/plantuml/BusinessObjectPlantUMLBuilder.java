package com.mauvaisetroupe.eadesignit.service.diagram.plantuml;

import com.mauvaisetroupe.eadesignit.domain.Application;
import com.mauvaisetroupe.eadesignit.domain.BusinessObject;
import com.mauvaisetroupe.eadesignit.domain.DataObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.sourceforge.plantuml.BlockUml;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;
import net.sourceforge.plantuml.core.Diagram;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class BusinessObjectPlantUMLBuilder {

    private final Logger log = LoggerFactory.getLogger(PlantUMLService.class);

    public void getPlantumlHeader(StringBuilder plantUMLSource) {
        plantUMLSource.append("@startuml\n");
        plantUMLSource.append("!pragma layout smetana\n");
        plantUMLSource.append("skinparam hyperlinkColor #000000\n");
        plantUMLSource.append("skinparam hyperlinkUnderline false\n");
        plantUMLSource.append("skinparam componentStyle uml1\n");
        plantUMLSource.append("skinparam component {\n");
        plantUMLSource.append("  borderThickness 2\n");
        plantUMLSource.append("}\n");
    }

    public void getPlantumlFooter(StringBuilder plantUMLSource) {
        if (!plantUMLSource.toString().endsWith("\n")) {
            plantUMLSource.append("\n");
        }
        plantUMLSource.append("@enduml\n");
    }

    public void getBusinessObjectsBusiness(StringBuilder plantUMLSource, BusinessObject bo2) {
        Set<BusinessObject> allBusinessObjects = new HashSet<>();
        Set<DataObject> allDataObjects = new HashSet<>();
        Set<Application> allApplications = new HashSet<>();
        findBusinessObjectsAndDataObjectAndApplications(allBusinessObjects, allDataObjects, allApplications, bo2);
        writeApplicationsAndDataObjects(plantUMLSource, allApplications, allDataObjects);
        writeBusinessObjects(plantUMLSource, allBusinessObjects);
    }

    public void getDataObjectsBusiness(StringBuilder plantUMLSource, DataObject dataObj) {
        Set<Application> allApplications = new HashSet<>();
        Set<DataObject> allDataObjects = new HashSet<>();
        findDataObjectsAndApplications(allDataObjects, dataObj, allApplications);
        writeApplicationsAndDataObjects(plantUMLSource, allApplications, allDataObjects);
    }

    private void writeBusinessObjects(StringBuilder plantUMLSource, Set<BusinessObject> allBusinessObjects) {
        for (BusinessObject businessObject : allBusinessObjects) {
            plantUMLSource.append(
                "rectangle \"--\\n **" + businessObject.getName() + "**\" as BO" + businessObject.getId() + " #business\n"
            );
        }

        for (BusinessObject businessObject : allBusinessObjects) {
            // BO Specialization
            for (BusinessObject sp : businessObject.getSpecializations()) {
                plantUMLSource.append("BO" + businessObject.getId() + " <|-- BO" + sp.getId() + ": specializes \n");
            }
            // BO Composition
            for (BusinessObject component : businessObject.getComponents()) {
                plantUMLSource.append("BO" + businessObject.getId() + " *-- BO" + component.getId() + ": composed in \n");
            }
            // BO - DO
            for (DataObject dataObject : businessObject.getDataObjects()) {
                plantUMLSource.append("BO" + businessObject.getId() + " <|.. DO" + dataObject.getId() + ": realizes \n");
            }
        }
    }

    private void writeApplicationsAndDataObjects(
        StringBuilder plantUMLSource,
        Set<Application> allApplications,
        Set<DataObject> allDataObjects
    ) {
        for (Application application : allApplications) {
            plantUMLSource.append("component \"**" + application.getName() + "**\" as APPLI" + application.getId() + " #application\n");
        }

        for (DataObject dataObject : allDataObjects) {
            plantUMLSource.append(
                "rectangle \"--\\n **" +
                dataObject.getName() +
                "** \\n<<" +
                dataObject.getType() +
                ">>\" as DO" +
                dataObject.getId() +
                " #application\n"
            );
        }

        for (DataObject dataObject : allDataObjects) {
            // DO Composition
            for (DataObject child : dataObject.getComponents()) {
                plantUMLSource.append("DO" + dataObject.getId() + " *-- DO" + child.getId() + ": composed in \n");
            }
            // DO - APPLI
            plantUMLSource.append("DO" + dataObject.getId() + " <.. APPLI" + dataObject.getApplication().getId() + ": access \n");
        }
    }

    private void findBusinessObjectsAndDataObjectAndApplications(
        Set<BusinessObject> businessObjectSet,
        Set<DataObject> dataObjectSet,
        Set<Application> applicationSet,
        BusinessObject bo
    ) {
        businessObjectSet.add(bo);
        for (DataObject dataObject : bo.getDataObjects()) {
            findDataObjectsAndApplications(dataObjectSet, dataObject, applicationSet);
        }

        for (BusinessObject specialization : bo.getSpecializations()) {
            findBusinessObjectsAndDataObjectAndApplications(businessObjectSet, dataObjectSet, applicationSet, specialization);
        }
        for (BusinessObject component : bo.getComponents()) {
            findBusinessObjectsAndDataObjectAndApplications(businessObjectSet, dataObjectSet, applicationSet, component);
        }
    }

    private void findDataObjectsAndApplications(Set<DataObject> dataObjectSet, DataObject dataObject, Set<Application> applicationSet) {
        dataObjectSet.add(dataObject);
        applicationSet.add(dataObject.getApplication());
        for (DataObject component : dataObject.getComponents()) {
            findDataObjectsAndApplications(dataObjectSet, component, applicationSet);
        }
    }

    public String getSVGFromSource(String plantUMLSource) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        SourceStringReader reader = new SourceStringReader(plantUMLSource);
        reader.outputImage(byteArrayOutputStream, new FileFormatOption(FileFormat.SVG));

        List<BlockUml> blocks = reader.getBlocks();
        if (blocks != null && !blocks.isEmpty()) {
            BlockUml blockUml = blocks.iterator().next();
            if (blockUml != null) {
                Diagram diagram = blockUml.getDiagram();
                String errorOrWarning = diagram.getWarningOrError();
                if (errorOrWarning != null) {
                    log.error(plantUMLSource);
                    throw new RuntimeException("Error during plantuml redering");
                }
            }
        }

        byteArrayOutputStream.close();
        return new String(byteArrayOutputStream.toByteArray(), Charset.forName("UTF-8"));
    }
}
