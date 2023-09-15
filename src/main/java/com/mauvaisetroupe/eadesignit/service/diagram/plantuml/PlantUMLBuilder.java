package com.mauvaisetroupe.eadesignit.service.diagram.plantuml;

import com.mauvaisetroupe.eadesignit.domain.Capability;
import com.mauvaisetroupe.eadesignit.service.diagram.dto.Application;
import com.mauvaisetroupe.eadesignit.service.diagram.dto.EdgeGroup;
import com.mauvaisetroupe.eadesignit.service.diagram.dto.GraphBuilder;
import com.mauvaisetroupe.eadesignit.service.diagram.dto.Label;
import com.mauvaisetroupe.eadesignit.service.diagram.plantuml.PlantUMLService.DiagramType;
import com.mauvaisetroupe.eadesignit.service.dto.CapabilityDTO;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedSet;
import net.sourceforge.plantuml.BlockUml;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.core.DiagramDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Component
public class PlantUMLBuilder {

    private final Logger log = LoggerFactory.getLogger(PlantUMLService.class);
    public static final String PLANTUML_SVG_CACHE = "com.mauvaisetroupe.eadesignit.service.plantuml.PlantUMLSerializer.svg";

    public enum Layout {
        smetana,
        elk,
        none,
    }

    public void getPlantumlHeader(StringBuilder plantUMLSource) {
        getPlantumlHeader(plantUMLSource, Layout.smetana);
    }

    public void getPlantumlHeader(StringBuilder plantUMLSource, Layout layout) {
        plantUMLSource.append("@startuml\n");
        if (layout != Layout.none) {
            plantUMLSource.append("!pragma layout " + layout.toString() + "\n");
        }
        plantUMLSource.append("skinparam componentStyle rectangle\n");
        plantUMLSource.append("skinparam hyperlinkColor #000000\n");
        plantUMLSource.append("skinparam hyperlinkUnderline false\n");
        plantUMLSource.append("skinparam monochrome true\n");
        plantUMLSource.append("skinparam shadowing false\n");
        plantUMLSource.append("skinparam padding 5\n");
        plantUMLSource.append("skinparam rectangle {\n");
        plantUMLSource.append("  backgroundColor #A9DCDF\n");
        plantUMLSource.append("}\n");
        plantUMLSource.append("skinparam component {\n");
        plantUMLSource.append("  backgroundColor white\n");
        plantUMLSource.append("  borderColor black\n");
        plantUMLSource.append("}\n");
        plantUMLSource.append("skinparam rectangleFontSize 10\n");
        //plantUMLSource.append("skinparam svgDimensionStyle false\n");
        plantUMLSource.append("hide footbox\n");
    }

    public void getPlantumlRelationShip(
        StringBuilder plantUMLSource,
        Application source,
        Application target,
        SortedSet<Label> labels,
        DiagramType diagramType,
        boolean useID,
        boolean addURL,
        EdgeGroup startGroup,
        EdgeGroup endGroup
    ) {
        if (diagramType == DiagramType.SEQUENCE_DIAGRAM && startGroup != null) {
            String title = "";
            if (startGroup.getTitle() != null) {
                title = startGroup.getTitle();
            }

            if (startGroup.getUrl() != null) {
                plantUMLSource.append("group [[\"" + startGroup.getUrl() + "\" " + title + "]] group\n");
            } else {
                plantUMLSource.append("group " + title + "\n");
            }
        }
        plantUMLSource.append(
            getComponentByNameOrId(source, useID, diagramType) + " --> " + getComponentByNameOrId(target, useID, diagramType)
        );
        if (!isEmpty(labels)) {
            plantUMLSource.append(" :");
            String sepaString = "";
            int nbLabel = 0;
            for (Label label : labels) {
                if (StringUtils.hasText(label.getLabel())) {
                    String _label = label.getLabel().replaceAll("\n", "\\\\n");
                    plantUMLSource.append(sepaString);
                    if (label.getUrl() == null || !addURL) {
                        plantUMLSource.append(" " + _label);
                    } else {
                        plantUMLSource.append("[[ " + label.getUrl() + " " + _label + " ]]");
                    }
                    nbLabel++;
                    sepaString = (nbLabel % 3 == 0) ? ",\\n" : ",";
                }
            }
        }
        plantUMLSource.append("\n");
        if (diagramType == DiagramType.SEQUENCE_DIAGRAM) {
            if (hasMetadata(labels)) {
                plantUMLSource.append("note right\n");
                for (Label label : labels) {
                    if (!CollectionUtils.isEmpty(label.getMetadata())) {
                        for (Entry<String, String> metadata : label.getMetadata().entrySet()) {
                            if (GraphBuilder.KEY_PROTOCOL.equals(metadata.getKey())) {
                                plantUMLSource.append(metadata.getValue() + "\n");
                            } else {
                                plantUMLSource.append(metadata.getKey() + "=" + metadata.getValue() + "\n");
                            }
                        }
                    }
                }
                plantUMLSource.append("end note\n");
            }
        }
        if (diagramType == DiagramType.SEQUENCE_DIAGRAM && endGroup != null) {
            plantUMLSource.append("end \n");
        }
    }

    private boolean isEmpty(SortedSet<Label> labels) {
        if (labels == null) return true;
        if (labels.size() == 0) return true;
        for (Label label : labels) {
            if (label != null && StringUtils.hasText(label.getLabel())) {
                return false;
            }
        }
        return true;
    }

    private boolean hasMetadata(SortedSet<Label> labels) {
        for (Label label : labels) {
            if (!CollectionUtils.isEmpty(label.getMetadata())) {
                return true;
            }
        }
        return false;
    }

    private String getComponentByNameOrId(Application application, boolean useID, DiagramType diagramType) {
        if (useID) {
            return "C" + application.getId();
        }
        String open = (diagramType == DiagramType.SEQUENCE_DIAGRAM) ? "\"" : "[";
        String close = (diagramType == DiagramType.SEQUENCE_DIAGRAM) ? "\"" : "]";
        return open + application.getName() + close;
    }

    public void createComponentWithId(
        StringBuilder plantUMLSource,
        Application application,
        DiagramType diagramType,
        boolean addCapabilities
    ) {
        if (diagramType == DiagramType.SEQUENCE_DIAGRAM) {
            plantUMLSource.append("participant \"" + application.getName() + "\" as C" + application.getId() + "\n");
        } else {
            if (!addCapabilities || application.getCapabilities() == null || application.getCapabilities().size() == 0) {
                plantUMLSource.append("component \"" + application.getName() + "\" as C" + application.getId() + "\n");
            } else {
                plantUMLSource.append("component \"" + application.getName() + "\" as C" + application.getId() + "{\n");
                application
                    .getCapabilities()
                    .forEach(c -> {
                        plantUMLSource.append(" rectangle \"" + application.getId() + "." + c + "\"\n");
                    });
                plantUMLSource.append("}\n");
            }
        }
        plantUMLSource.append("url of C" + application.getId() + " is [[" + application.getUrl() + "]]\n");
    }

    public void getPlantumlFooter(StringBuilder plantUMLSource) {
        plantUMLSource.append("@enduml\n");
    }

    @Cacheable(cacheNames = PLANTUML_SVG_CACHE)
    public String getSVGFromSource(String plantUMLSource) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        SourceStringReader reader = new SourceStringReader(plantUMLSource);
        DiagramDescription diagramDescription = reader.outputImage(byteArrayOutputStream, new FileFormatOption(FileFormat.SVG));

        List<BlockUml> blocks = reader.getBlocks();
        if (blocks != null && !blocks.isEmpty()) {
            BlockUml blockUml = blocks.iterator().next();
            if (blockUml != null) {
                Diagram diagram = blockUml.getDiagram();
                String errorOrWarning = diagram.getWarningOrError();
                if (errorOrWarning != null) {
                    throw new RuntimeException("Error during plantuml redering");
                }
            }
        }

        byteArrayOutputStream.close();
        return new String(byteArrayOutputStream.toByteArray(), Charset.forName("UTF-8"));
    }

    public void getPlantumlCapabilities(StringBuilder plantUMLSource, Collection<Capability> capabilities) {
        for (Capability capability : capabilities) {
            getRectangle1(capability, plantUMLSource, "");
        }
    }

    public void getPlantumlCapabilitiesDTO(StringBuilder plantUMLSource, Collection<CapabilityDTO> capabilities) {
        for (CapabilityDTO capability : capabilities) {
            getRectangle2(capability, plantUMLSource, "");
        }
    }

    private void getRectangle1(Capability root, StringBuilder result, String tab) {
        result.append(
            tab +
            "rectangle \"" +
            root.getName().replaceAll("[\n\r]", " ") +
            "\" as C" +
            root.getId() +
            (root.getSubCapabilities().size() != 0 ? " {" : "") +
            " \n"
        );
        for (Capability dto : root.getSubCapabilities()) {
            getRectangle1(dto, result, tab + "   ");
        }
        if (root.getSubCapabilities().size() != 0) result.append(tab + "}\n");
        result.append(tab + "url of C" + root.getId() + " is [[/capability/" + root.getId() + "/view]]\n");
    }

    private void getRectangle2(CapabilityDTO root, StringBuilder result, String tab) {
        result.append(
            tab +
            "rectangle \"" +
            root.getName().replaceAll("[\n\r]", " ") +
            "\" as C" +
            root.getId() +
            (root.getSubCapabilities().size() != 0 ? " {" : "") +
            " \n"
        );
        for (CapabilityDTO dto : root.getSubCapabilities()) {
            getRectangle2(dto, result, tab + "   ");
        }
        if (root.getSubCapabilities().size() != 0) result.append(tab + "}\n");
        result.append(tab + "url of C" + root.getId() + " is [[/capability/" + root.getId() + "/view]]\n");
    }

    public void getLegend(StringBuilder plantUMLSource, List<String[]> legend) {
        plantUMLSource.append("Legend\n");
        boolean header = true;
        for (String[] row : legend) {
            for (String string : row) {
                if (header) {
                    plantUMLSource.append(" |= " + string);
                } else {
                    plantUMLSource.append(" | " + string);
                }
            }
            header = false;
            plantUMLSource.append(" |\n");
        }
        plantUMLSource.append("End Legend\n");
    }

    public void getPlantumlPackage(
        StringBuilder plantUMLSource,
        String packageName,
        List<Application> applications,
        boolean useID,
        boolean addCapabilities
    ) {
        plantUMLSource.append("package \"" + packageName + "\" {\n");
        for (Application application : applications) {
            if (useID) {
                createComponentWithId(plantUMLSource, application, DiagramType.COMPONENT_DIAGRAM, addCapabilities);
            } else {
                plantUMLSource.append(getComponentByNameOrId(application, useID, DiagramType.COMPONENT_DIAGRAM));
                plantUMLSource.append("\n");
            }
        }
        plantUMLSource.append("}\n");
    }
}
