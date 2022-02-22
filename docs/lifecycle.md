---
layout: page
title: Lifecycle
permalink: /lifecycle/
nav_order: 4
---

# Landscape As-Is, Landscape To-Be

<span class="fs-2">Implemented</span>{: .label .label-green }
The start date of validity (easy) and the end date of validity (much more difficult to estimate) of the life cycle of each object can be entered.


```java
entity Application {
    ...
    startDate LocalDate
    endDate LocalDate
    ...
}
```
You will find these two attributes on the following entities :
 - Functional Flow
 - Interface
 - Application
 - Application Component
 - Data Flow
 - and Data Flow Item


<span class="fs-2">Not Implemented</span>{: .label .label-red } 
By default, all objects are displayed, but it is possible to indicate a date in the optional time filter. By indicating the current date, the As-Is architecture is displayed. To-Be architecture can be displayed by indicating a date in the future. This approach requires only one repository to store objects from the past, present and future.

