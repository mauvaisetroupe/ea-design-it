---
title: "PLantuml Import"
layout: page
permalink: /import-plantuml/flow
nav_order: 4
---

# Import Plantuml - FunctionalFlow

A convenient way to import a Functional Flow is using plantuml
You can describe your Functional Flow as a Sequence Diagarm

![Export Sequece Diagram](./sample.png)

```
@startuml
participant webbanking
participant "payment" 
participant "CBS System" as cbs

webbanking -> payment: first step
payment -> cbs: first step
note right:API

@enduml
```

![Export Sequece Diagram](./sample-02.png)