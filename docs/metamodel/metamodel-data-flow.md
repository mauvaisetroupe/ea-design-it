---
title: "Data Flow"
layout: page
permalink: /metamodel/data-flow/
nav_order: 5
parent: MetaModel
---


# DataFlow

## Teminology 

In the context of a Function Flow, exchange data through a specific Interface is implemented by a **DataFlow** 

Concretly, DataFlow is one of the following artifact:
- a **File**, 
- an **event** or an **Event topic** 
- or **API**

A dataflow is implemented through an Interface, hence dataflow ownership is define by Interface owner.

If you wnat a second level of decription for your data exhange (more detailed), use [Data Flow Item](../data-flow-item)


## Example

FunctionalFlow "Customer Synchronization" is implemented trhough two 
Interfaces :
 - TRAD.006, an Event synchronization between Core Banking Sysetm and Trading Platform
 - TRAD.007, a Batch file symchronization betwwen Core Banking Sysetm and an Account Microservice
 
![Flow Interface](./png/dataflow1.png)

Interface TRAD.006, is implemented via a Data item (typically a kafka topic) : /EVT/CUSTOMER
 
![Interface and its Data Flows](./png/dataflow2.png)

