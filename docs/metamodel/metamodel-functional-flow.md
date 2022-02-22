---
title: "Functional Flow"
layout: page
permalink: /metamodel/functional-flow/
nav_order: 3
parent: MetaModel
---

# Functional Flow

## Teminology 

In the context of a **Landscape**, a Functional Flow represents a functional information exchange between two or more applications

FunctionalFlow is a list of **steps**, each step leverages on an [Interface](../interface)

## Example

**S.01** is FunctionalFlow that describes interactions between applications for "Instruments Prices Feeding".

It is composed of 3 steps :
- Securities Partner send prices to Trading Platfom via the Security Master File
- Trading Platform synchronize the prices to the Core Banking Sysetm
- Core Banking Sysetm confirm the reception of prices to the Trading Platform

![landscape as set of flows](./png/flow.png)



![landscape as set of flows](./png/flow-steps.png)

