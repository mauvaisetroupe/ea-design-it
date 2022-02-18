# EADesignIt

# Introduction

EADesignIt is a Enterprise Architecture software.
It's a lightweight and an open source application that will allow to store in a database your applications, and interactions between applications.

It allows to build in a database a referential of :

- Applications, and Application Components
- Interfaces and Flows between Applications
- Capabilities covered by Applications

EADesignIt is intended for Enterprise Architects who need to document their applications landscape with minimal effort but in a structured and centralized way, replacing your unmaintanable Excel files.

Building an enterprise-wide application referential is a costly activity (but sometime mandatory, for compliance reason or other).
Even more, maintaining a fine-grained refential manually could turn into a nightmare.

So the idea is to :

1. create via a manually process a top-down high-level referential,
2. and give the possibility to complete the feeding of the database by any bottom-up tool (exampe : importing from an API gateway configuration, listening on the netowrk or an ESB, pluging into the build factory, etc.)

For these reasons, note than event if it's then possible to create all entities (applications, interfaces, etc.) via the GUI, the philosophy of the software is to be open :

- importing and exporting excel files
- offering an API over the database to add your own process/tooling to populate the database

If you want to have a better idea, please browse the [demo](https://ea-design-it.herokuapp.com/) on Heroku platform

# Data model

If you want to know if EADesignIt is appropriate for your requirements, the more important aspect is the data model.

Does the data model meet your requirements? So for more explanation of potential features, please read first the [description of data model](./documentation/metamodel).

You can also import the [JDL file](./jhipster-jdl-metamodel.jdl) in [jdl studio](https://start.jhipster.tech/jdl-studio/) for a more accurate and detailed view of the entities and their relationships.

# Diagram or not diagram ?

This application is not a architeture diagram tool.
It should be considered as a databse referential first.

Diagram capabilities come however in two forms :

- generated diagram using [plantuml](http://www.plantuml.com)
- generated and updatable diagram using [drawio](https://drawio-app.com/)

Example of generated plantuml diagram :

![interface view](./documentation/application/screenshot-plantuml.png)

Example of generated then modified draw.io diagram :

![interface view](./documentation/application/screenshot-drawio.png)

# Demo

You can find a [demo](https://ea-design-it.herokuapp.com/) on Heroku platform.

# Build the application

This application is build with Java, Spring Boot and VueJS.

It's based on JHipster to generate all entities. Please refer to [documentation](./documentation/jhipster).
