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
            A <a href="/landscape-view">Landscape</a> is an architecture diagram that represents a subset of
            <a href="/application">Application</a>, and their Functional Flow
          </li>
          <li>
            In the context of a Landscape, a <a href="/functional-flow">Functional Flow</a> represents a functional information exchange
            between two applications
          </li>
          <li>
            A Functional Flow is implemented through one or more <a href="/flow-interface">Interface</a> to transfer information between
            source and target.
          </li>
          <li>
            Interface is a "pipeline" between two applications and it's fully defined by a source and a target Application and a
            <a href="/protocol">Protocol</a>
          </li>
          <li>
            In the context of a Function Flow, exchange data through a specific Interface is implemented by a
            <a href="/data-flow">Data Flow</a> (concretly, DataFlow is a File, an Event in a topic or API call of a swagger)
          </li>
          <li>
            <a href="/data-flow-item">Data Flow Item</a> is used for model a more fined-grained data exchange (operation of swagger,
            substructure in the File, event of a topic)
          </li>
        </ul>
        <p>
          Please, refer to <a href="https://github.com/mauvaisetroupe/EADesignIt/tree/main/documentation/metamodel">documentation</a> for
          more explanation
        </p>
      </div>
      <div v-if="$store.getters.writeAuthority">
        <h2>Import Excel files</h2>
        <ul>
          <li>Import Excel <a href="/application-import-upload-file">Applications</a> file</li>
          <li>Import Excel <a href="/flow-import-upload-file">Functional Flows &amp; Interfaces </a> file</li>
          <li>Import Excel <a href="/event-import-upload-file">Data Flows &amp; Dat Flow Items</a> file</li>
          <li>Import Excel <router-link to="/capability-import-upload-file">Capabilities</router-link></li>
          <li>Import Excel mapping <router-link to="/application-capability-import-upload-file">Applications/Capabilities</router-link></li>
        </ul>
        <p>
          Please, refer to <a href="https://github.com/mauvaisetroupe/EADesignIt/tree/main/documentation/excel-import">documentation</a> for
          more explanation
        </p>
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
