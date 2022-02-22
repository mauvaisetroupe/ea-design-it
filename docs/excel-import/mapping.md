---
title: "Excel column mapping"
layout: page
permalink: /excel-mapping/
parent: Excel import/export
nav_order: 3
---

# Mapping Excel / Data Model

Find below the mapping between Excel column names and Entities field names

## Applications

This mapping concerns import for [Application via Excel](./samples/applications.xlsx)

| Excel Column Name            | FlowImport setter | Application setters
|------------------------------|----------------|-------|
| application.id               | setIdFromExcel | setId |
| application.name             | setName        | setName |
| application.description      | setDescription | setDescription |
| application.comment          | setComment     | setComment  |
| application.type             | setType        | setType |


## Landscape, Functional flow, Interfaces 

This mapping concerns import [Landscape via Excel](./samples/Invest_And_Securities_Landscape.xlsx)


| Excel Column Name            | FlowImport setter            | functionalFlow setter | FlowInterface setters | DataFlow  |
|------------------------------|------------------------------|-----------------------|-----------------------|-----------|
| Id flow                      | setIdFlowFromExcel           |                       | setId | |
| Alias flow                   | setFlowAlias                 | setId                 | | |
| Source Element               | setSourceElement             |                       | setSource | |
| Target Element               | setTargetElement             |                       | setTarget | |
| Description                  | setDescription               | setDescription        | | |
| Integration pattern          | setIntegrationPattern        |                       | setProtocol | |
| Frequency                    | setFrequency                 |                       | | |
| Format                       | setFormat                    |                       | | |
| Swagger                      | setSwagger                   |                       | | |
| Blueprint From Source        | setSourceURLDocumentation    |                       | | |
| Blueprint From Target        | setTargetURLDocumentation    |                       | | |
| Status Blueprint From Source | setSourceDocumentationStatus |                       | | |
| Status Blueprint From Target | setTargetDocumentationStatus |                       | | |
| Status flow                  | setFlowStatus                | setStatus             | | |
| Comment                      | setComment                   | setComment            | | |


| Excel FileName    | FlowImport setter  | Object setters               |
|-------------------|--------------------|------------------------------|
| file name         |                    | landscapeView.setDiagramName | 

## Data flow, Data Flow Items

This mapping concerns import [Data & Data Items via Excel](./samples/data-data-item.xlsx)

// TODO

## Capabilities & Mapping Capabilities/Applications

 This mapping concerns import [Capabilities & sub-capabilities](./samples/capabilities.xlsx) and [Mapping between Capabilities & Applications](./samples/capabilities-applications.xlsx)

 // TODO