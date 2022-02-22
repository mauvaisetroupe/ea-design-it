---
title: "Interface"
layout: page
permalink: /metamodel/interface/
nav_order: 4
parent: MetaModel
---

# Interfaces

## Teminology 

Interface is a "pipeline" between two [Applications](../application/) and it's fully defined by :
- a source **Application**
- a target **Applications**
- a protocol (API, Files, Events, etc.)

A **Functional Flow** is implemented through one or more **Interfaces**.

On the other hand, an **Interface** could be used to implement different **Functional Flows**, even in different Landscapes.

Interface has a unique owner (a DEVOPS team that develop & opearate the integration)

Data that go through an Interface piepeline is named [Data Flow](../data-flow)

## Example 

A single Functional Flow (Instrument prices   ) could be implemented 
trough two different Interfaces:
 - an REST API between Securities Partner and Security Master File
 - an REST API between Security Master File and Trading Platform
 - etc.

![interface view](png/interfaces.png)



