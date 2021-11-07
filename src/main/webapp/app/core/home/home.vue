<template>
  <div class="home row">
    <div class="col-md-3">
      <span class="hipster img-fluid rounded"></span>
    </div>
    <div class="col-md-9">
      <h1 class="display-4">Welcome, Java Hipster!</h1>
      <p class="lead">This is your homepage</p>

      <div>
        <div class="alert alert-success">
          <span v-if="username">You are logged in as user "{{ username }}"</span>
        </div>

        <div class="alert alert-warning" v-if="!authenticated">
          <span>If you want to </span>
          <a class="alert-link" v-on:click="openLogin()">sign in</a
          ><span
            >, you can try the default accounts:<brv-if ="authenticated" />- Administrator (login="admin" and password="admin") <br />- User
            (login="user" and password="user").</span
          >
        </div>
        <div class="alert alert-warning" v-if="!authenticated">
          <span>You don't have an account yet?</span>&nbsp;
          <router-link class="alert-link" to="/register">Register a new account</router-link>
        </div>
        <div v-if="authenticated">
          <p>Import Excel files</p>
          <ul>
            <li><a href="/application-import-upload-file">Import Excel applications file</a></li>
            <li><a href="/flow-import-upload-file">Import Excel flows file</a></li>
          </ul>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./home.component.ts"></script>
