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
            A <router-link to="landscape-view">Landscape</router-link> is an architecture diagram that represents a subset of
            <router-link to="application">Applications</router-link>, and their Functional Flows
          </li>
          <li>
            In the context of a Landscape, a <router-link to="functional-flow">Functional Flow</router-link> represents a functional
            information exchange between two ore more applications
          </li>
          <li>
            A Functional Flow is implemented through one or more <router-link to="flow-interface">Interfaces</router-link> to transfer
            information between a source and a target Application.
          </li>
          <li>
            Interface is a "pipeline" between two applications and it's fully defined by a source and a target Application and a
            <router-link to="protocol">Protocol</router-link>
          </li>
          <li>
            In the context of a Function Flow, exchange data through a specific Interface is implemented by a
            <router-link to="data-flow">Data Flow</router-link> (concretly, DataFlow is a File, an Event in a topic or API call of a
            swagger)
          </li>
          <li>
            <router-link to="data-flow-item">Data Flow Item</router-link> is used for model a more fined-grained data exchange (operation of
            swagger, substructure in the File, event of a topic)
          </li>
        </ul>
        <p>Please, refer to <a href="https://mauvaisetroupe.github.io/ea-design-it/">documentation</a> for more explanation</p>
      </div>
      <div v-if="$store.getters.writeAuthority">
        <h2>Import Excel files</h2>
        <ul>
          <li>Import Excel <router-link to="/application-import-upload-file"> Applications</router-link> file</li>
          <li>Import Excel <router-link to="flow-import-upload-file">Functional Flows &amp; Interfaces </router-link> file</li>
          <li>Import Excel <router-link to="event-import-upload-file">Data Flows &amp; Dat Flow Items</router-link> file</li>
          <li>Import Excel <router-link to="/capability-import-upload-file">Capabilities</router-link></li>
          <li>Import Excel mapping <router-link to="/application-capability-import-upload-file">Applications/Capabilities</router-link></li>
        </ul>
        <p>Please, refer to <a href="https://mauvaisetroupe.github.io/ea-design-it/import/">documentation</a> for more explanation</p>
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
    </div>
  </div>
</template>

<script lang="ts" src="./home.component.ts"></script>
