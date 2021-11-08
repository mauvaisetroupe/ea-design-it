# Excel format

## Application Excel columns

| Excel Column Name            | FlowImport setter                       | Object setters
|------------------------------|-----------------------------------------|----------------|
| application.id               | applicationImport.setIdFromExcel | application.setId |
| application.name             | applicationImport.setName | application.setName |
| application.description      | applicationImport.setDescription | application.setDescription |
| application.comment          | applicationImport.setComment | application.setComment  |
| application.type             | applicationImport.setType | application.setType |


## Flow Excel columns

| Excel Column Name            | FlowImport setter                       | Object setters
|------------------------------|-----------------------------------------|----------------|
| Id flow                      | flowImport.setIdFlowFromExcel           | flowInterface.setId |
| Alias flow                   | flowImport.setFlowAlias                 | functionalFlow.setId|
| Source Element               | flowImport.setSourceElement             | flowInterface.setSource |
| Target Element               | flowImport.setTargetElement             | flowInterface.setTarget |
| Description                  | flowImport.setDescription               | functionalFlow.setDescription |
| Integration pattern          | flowImport.setIntegrationPattern        | flowInterface.setProtocol |
| Frequency                    | flowImport.setFrequency                 | |
| Format                       | flowImport.setFormat                    | |
| Swagger                      | flowImport.setSwagger                   | | 
| Blueprint From Source        | flowImport.setSourceURLDocumentation    | |
| Blueprint From Target        | flowImport.setTargetURLDocumentation    | |
| Status Blueprint From Source | flowImport.setSourceDocumentationStatus | |
| Status Blueprint From Target | flowImport.setTargetDocumentationStatus | |
| Status flow                  | flowImport.setFlowStatus                | functionalFlow.setStatus |
| Comment                      | flowImport.setComment                   | functionalFlow.setComment |
| ADD correspondent ID         | flowImport.setDocumentName              | #ignored |

| Excel FileName    | FlowImport setter  | Object setters               |
|-------------------|--------------------|------------------------------|
| file name         |                    | landscapeView.setDiagramName | 