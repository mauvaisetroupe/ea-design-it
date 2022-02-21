---
title: "MetaModel"
layout: page
permalink: /metamodel/
---
# Data Model - Introduction
This application is tool to model application architecture building diagrams to document applications and interactions between applications (data flows).

# Teminology 

## Landscape Viewpoint
IT lanscape, composed by hundred of applications and components is difficult to model. A single and comprehensive model is often too complex to be understood. 

That's why it's important to introduce views and viewpoints.

Using TOGAF terminology, A **view** is a representation of a whole system from the perspective of a related set of concerns. A **viewpoint** defines the perspective from which a view is taken. Viepoint a kind of template, and View is an instantiation of a a specific viewpoint

This first version of the tool implement a unique ViewPoint : Application **Landscape**

> ![landscape](./png/landscape.png)

A Landscape is a set of **Functional Flow** that together gives for example a big picture of specific domain.

> ![landscape as set of flows](./png/landscape-flows.png)

## Functional Flows

In the context of a **Landscape**, a Functional Flow represents a functional information exchange between two or more applications



> - Securities Partner send prices to Trading Platfom via the Security Master File
> - Trading Platform synchronize the prices to the Core Banking Sysetm
> - Core Banking Sysetm confirm the reception of prices to the Trading Platform
>
> ![landscape as set of flows](./png/flow.png)


FunctionalFlow is a list of **steps**, each step leverages on an **Interface**

> ![landscape as set of flows](./png/flow-steps.png)


## Interfaces

A Functional Flow is implemented through one or more **Interfaces** to transfer information between source and target.

Interface is a "pipeline" between two applications and it's fully defined by :
- a source **Application**
- a target **Applications**
- a protocol (API, Files, Events, etc.)


> A single Functional Flow (Instrument prices   ) could be implemented 
> trough two different Interfaces:
> - an REST API between Securities Partner and Security Master File
> - an REST API between Security Master File and Trading Platform
> - etc.
>
> ![interface view](png/interfaces.png)


On the other hand, an Interface could be used to implement different Functional Flows, even in different Landscapes.

But an Interface has a unique owner (a DEVOPS team)


## DataFlow

In the context of a Function Flow, exchange data through a specific Interface is implemented by a **DataFlow** 


Concretly, DataFlow is one of the following artifact:
- a **File**, 
- an **Event** 
- or **API**

> FunctionalFlow createCustomer is implemented trhough two 
> Interfcaces, and then via multiple DataFlow transfer in each 
> Interface :
> - two API call between FrontEnd and MicroService
> - then by SFTP transfer with many Files (customer-core.cvs &  address.csv) between MicroService and BackEnd
> 
> ![dataflow view](png/plantuml-dataflow/plantuml-dataflow.png)


A dataflow is implemented through an Interface, hence dataflow ownership is define by Interface owner.

## DataFlowItem

DataFlowItem is used for model a more fined-grained data exchange
We could follow for example the following convention :

| Protocol               | DataFlow                             | DataFlowItem
|------------------------|--------------------------------------|-------------
| API                    | A service exposed through a Swagger  | An operation of the service
| File                   | A fhysical File                      | A substructure in the File
| Event                  | A topic                              | An Event in the topic


> FunctionalFlow createCustomer is implemented with 
> Interfcace between MicroSercie and BackEnd
> - DataFlow is of type Event, and it's a topic (typically Kafka topic), named for exampe /EVT/TOPIC/CUSTOMER
> - In the topic, we have 2 Events (CUST_CORE and CUST_ADDRESS)
> 
> ![dataflow view](png/plantuml-dataflowitem/plantuml-dataflowitem.png)


# Meta-Model

Here is the meta-model used in the tool.

![meta model](png/plantuml-eadesignit/plantuml-eadesignit.png)

# JHipster JDL


You can also consult the [JDL file](https://github.com/mauvaisetroupe/ea-design-it/blob/main/jhipster-jdl-metamodel.jdl) for a more accurate and detailed view of the entities and their relationships (you can use [jdl studio](https://start.jhipster.tech/jdl-studio/) and import JDL file).

[![meta model](../images/jdl.png){: width="500"}](https://github.com/mauvaisetroupe/ea-design-it/blob/main/jhipster-jdl-metamodel.jdl)













