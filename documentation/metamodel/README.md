# Introduction
This application is tool to model application architecture building diagrams to document applications and interactions between applications (data flows).

# Teminology 

## Viewpoint
IT lanscape, composed by hundred of applications and components is difficult to model. A single and comprehensive model is often too complex to be understood. 

That's why it's important to introduce views and viewpoints.

Using TOGAF terminology, A **view** is a representation of a whole system from the perspective of a related set of concerns. A **viewpoint** defines the perspective from which a view is taken. Viepoint a kind of template, and View is an instantiation of a a specific viewpoint

This first version of the tool implement a unique ViewPoint : Application **Landscape**

## Application and Application Components
What an application is? There is no obvious reponse. But anyway, you need a abstraction in order to have a high-level view of your landscape. A pragmatic approach is to decide that an Application is an homogenous set of Application Components seen as one from an external point of view. 

<br/>

> In a bank for example, an Application could be the web 
> banking. Customers use the web banking 
> as their main bank application. 
> From DEV & IT-OPS teams perspective, web bankink application 
> is decomposed into in 3 ou 4 different Application Components
> separately deployable, that together create the web banking 
> application

## Functional Flows

In the context of a **Landscape**, a Functional Flow represents a functional information exchange between two applications

<br/>

> FrontEnd component need to push data to its BackEnd, 
> through a Microservice for creating a new customer. 
> This is a 'createCustomer' functional flow.
>
> ![flow view](png/plantuml-functionalflow/plantuml-functionalflow.png)


## Interfaces

A Functional Flow is implemented through one or more **Interface** to transfer information between source and target.

Interface is a "pipeline" between two applications and it's fully defined by :
- a source **Application**
- a target **Applications**
- a protocol (API, Files, Events, etc.)

<br/>

> A single Functional Flow (createCustomer) could be implemented 
> trough two different Interfaces:
> - an REST API between FrontEnd and Microservice
> - an SFTP file transfer between Microservice et Backend
>
> ![interface view](png/plantuml-interface/plantuml-interface.png)

<br/>

On the other hand, an Interfcae could be used to implement different Functional Flows, even in different Landscapes.

## DataFlow

In the context of a Function Flow, exchange data through a specific Interface is implemented by **DataFlow** 

Concretly, DataFlow is one of the following artifact:
- a **File**, 
- an **Event** 
- or **API**

<br/>


> FunctionalFlow createCustomer is implemented trhough two 
> Interfcaces, and then via multiple DataFlow transfer in each 
> Interface :
> - two API call between FrontEnd and MicroService
> - then by SFTP transfer with many Files (customer-core.cvs &  address.csv) between MicroService and BackEnd
> 
> ![dataflow view](png/plantuml-dataflow/plantuml-dataflow.png)


# Meta-Model

![meta model](png/plantuml-eadesignit/plantuml-eadesignit.png)















