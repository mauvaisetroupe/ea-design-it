package com.mauvaisetroupe.eadesignit.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public interface IFlowInterface {
    Long getId();

    String getAlias();

    @JsonIgnoreProperties(
        value = {
            "description",
            "comment",
            "documentationURL",
            "categories",
            "technologies",
            "owner",
            "itOwner",
            "businessOwner",
            "applicationsLists",
            "capabilityApplicationMappings",
            "externalIDS",
            "nickname",
            "startDate",
            "endDate",
            "applicationType",
            "softwareType",
        }
    )
    Application getSource();

    @JsonIgnoreProperties(
        value = {
            "description",
            "comment",
            "documentationURL",
            "categories",
            "technologies",
            "owner",
            "itOwner",
            "businessOwner",
            "applicationsLists",
            "capabilityApplicationMappings",
            "externalIDS",
            "nickname",
            "startDate",
            "endDate",
            "applicationType",
            "softwareType",
        }
    )
    Application getTarget();

    ApplicationComponent getSourceComponent();

    ApplicationComponent getTargetComponent();

    @JsonIgnoreProperties(value = { "type", "description", "scope" })
    Protocol getProtocol();
}
