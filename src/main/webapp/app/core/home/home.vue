<template>
  <div class="home row">
    <div class="col-md-3">
      <span class="hipster img-fluid rounded"></span>
    </div>

    <div class="col-md-9">
      <h1 class="display-4">Welcome to EA Design IT</h1>
      <div>
        <h2>Introduction</h2>
        <p>
          This application is tool to model application architecture building diagrams to document applications and interactions between
          applications (data flows).
        </p>
        <h2>Main concepts</h2>
        <ul>
          <li>
            <router-link to="landscape-view">Landscape</router-link> : architecture diagram that represents a subset of Applications, and
            their Functional Flows
          </li>
          <li>
            <router-link to="application">Application</router-link> : could be composed of
            <router-link to="application-component">Application Component</router-link>
          </li>
          <li>
            <router-link to="functional-flow">Functional Flow</router-link> : in the context of a Landscape, a Functional Flow represents a
            functional information exchange between two ore more applications
          </li>
          <li>
            <router-link to="flow-interface">Interfaces</router-link> : a Functional Flow is implemented through one or more Interfaces to
            transfer information between a source and a target Application.
          </li>
          <li>
            <router-link to="protocol">Protocol</router-link> : Interface is a "pipeline" between two applications and it's fully defined by
            a source and a target Application and a Protocol
          </li>
          <li>
            <router-link to="data-flow">Data Flow</router-link> : in the context of a Function Flow, exchange data through a specific
            Interface is implemented by a Data Flow (File, Event, API call)
          </li>
          <li>
            <router-link to="data-flow-item">Data Flow Item</router-link> is used for model a more fined-grained data exchange (operation of
            swagger, substructure in the File, event of a topic)
          </li>
          <li>
            <router-link to="capability/navigate">Capability Map</router-link>
          </li>
        </ul>
        <p>Please, refer to <a href="https://mauvaisetroupe.github.io/ea-design-it/">documentation</a> for more explanation</p>
      </div>

      <div class="alert alert-success" v-if="authenticated">
        <span v-if="username">You are logged in as user "{{ username }}"</span>
      </div>
      <div class="alert alert-warning" v-if="!authenticated">
        <span>Please </span><a class="alert-link" v-on:click="openLogin()">sign in</a>.
      </div>
      <div class="alert alert-warning" v-if="!authenticated">
        <span>You don't have an account yet?</span>&nbsp;
        <router-link class="alert-link" to="/register">Register a new account</router-link>
      </div>

      <div v-if="$store.getters.writeAuthority">
        <h2>Import</h2>
        <ol>
          <li>
            Import
            <router-link to="/external-system-upload-file" data-cy="import-excel-external-system">External System</router-link> Excel file
          </li>
          <li>
            Import
            <router-link to="/application-import-upload-file" data-cy="import-excel-applications">Applications</router-link> Excel file
          </li>
          <li>
            Import
            <router-link to="/component-import-upload-file" data-cy="import-excel-component">Application Components</router-link> Excel file
          </li>
          <li>
            Import
            <router-link to="flow-import-upload-file" data-cy="import-excel-flows">Landscape</router-link> Excel file
          </li>
          <li>
            Import
            <router-link to="flow-import-upload-multifile" data-cy="import-excel-flows">Multiple Landscapes</router-link> Excel file
          </li>
          <li>
            Import
            <router-link to="event-import-upload-file" data-cy="import-excel-data">Data Flows &amp; Dat Flow Items</router-link> Excel file
          </li>
          <li>
            Import <router-link to="/capability-import-upload-file" data-cy="import-excel-capabilities">Capabilities</router-link> Excel
            file
          </li>
          <li>
            Import
            <router-link to="/application-capability-import-upload-file" data-cy="import-excel-capabilities-mapping"
              >Applications/Capabilities mapping</router-link
            >
            Excel file
          </li>
          <li>
            Import
            <router-link to="/sequence-diagram/import" data-cy="import-sequence-diagram-mapping">Sequence diagram</router-link> Plantuml
            file
          </li>
        </ol>

        <h2>Export</h2>
        <ul>
          <li>
            <router-link to="/full-export" data-cy="full-export">Full Export</router-link>
          </li>
        </ul>
        <p>Please, refer to <a href="https://mauvaisetroupe.github.io/ea-design-it/import/">documentation</a> for more explanation</p>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./home.component.ts"></script>
