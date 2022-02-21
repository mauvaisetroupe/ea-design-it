---
title: "Excel Import"
layout: page
permalink: /import/
---

# Import Excel - Templates & Samples

A convenient way to populate your database is importing your landscape via Excel File.

Here an example/templates of files for :
 - importing [Application via Excel](./samples/applications.xlsx)  - sheet named "*Application*"
 - importing [Landscape via Excel](./samples/Invest_And_Securities_Landscape.xlsx) - sheet named "*Message_Flow*"
 - importing [Data & Data Items via Excel](./samples/data-data-item.xlsx) - sheet named "*Data*" and "*DataItem*"
 - importing [Capabilities & sub-capabilities](./samples/capabilities.xlsx) - sheet named "*Capabilities*"
 - importing [Mapping between Capabilities & Applications](./samples/capabilities-applications.xlsx) - all sheets with a name "*ADD_XXX*"

# How to import
## How to find the URLs?

If you have enough access, you will have different links to import Excel files on the Home page.

![Export Excel](./selection_002.png)

> Importing en Excel file requires **ROLE_WRITE** access

## Import an existing Landscape

For importing an existing Landscape, you should first delete the existing Landscape, then import it via the new Excel file. 

![Export Excel](./delete-landscape.png)

<br/>
> Deleting a entity requires **ROLE_HARD_DELETE** access


**During Landscape deletion, orhpan entities (FunctionnalFlows, Interfaces, DataFlows, DataFlowItems) not used in another landscape will be deleted.** 

# Exporting Excel from EADesignIt

You also can export Excel directly from the application.

![Export Excel](./selection_001.png)

# Excel as Golden source?!?

As it's both possible to import and export a Landscape (with Functional Flows and Interfaces) via an Excel file, the mechanism is ready if you want to consider Excel as your "golden source".

A excel-centric process could be to:
- export the database content in Excel, 
- modify the content in Excel file, 
- and then re-import the full data from the Excel file.

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