<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="applicationImport">
        <h2 class="jh-entity-heading" data-cy="applicationImportDetailsHeading">
          <span>ApplicationImport</span> {{ applicationImport.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span>Import Id</span>
          </dt>
          <dd>
            <span>{{ applicationImport.importId }}</span>
          </dd>
          <dt>
            <span>Excel File Name</span>
          </dt>
          <dd>
            <span>{{ applicationImport.excelFileName }}</span>
          </dd>
          <dt>
            <span>Id From Excel</span>
          </dt>
          <dd>
            <span>{{ applicationImport.idFromExcel }}</span>
          </dd>
          <dt>
            <span>Name</span>
          </dt>
          <dd>
            <span>{{ applicationImport.name }}</span>
          </dd>
          <dt>
            <span>Description</span>
          </dt>
          <dd>
            <span>{{ applicationImport.description }}</span>
          </dd>
          <dt>
            <span>Type</span>
          </dt>
          <dd>
            <span>{{ applicationImport.type }}</span>
          </dd>
          <dt>
            <span>Technology</span>
          </dt>
          <dd>
            <span>{{ applicationImport.technology }}</span>
          </dd>
          <dt>
            <span>Comment</span>
          </dt>
          <dd>
            <span>{{ applicationImport.comment }}</span>
          </dd>
          <dt>
            <span>Import Status</span>
          </dt>
          <dd>
            <span>{{ applicationImport.importStatus }}</span>
          </dd>
          <dt>
            <span>Import Status Message</span>
          </dt>
          <dd>
            <span>{{ applicationImport.importStatusMessage }}</span>
          </dd>
          <dt>
            <span>Existing Application ID</span>
          </dt>
          <dd>
            <span>{{ applicationImport.existingApplicationID }}</span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span> Back</span>
        </button>
        <router-link
          v-if="applicationImport.id"
          :to="{ name: 'ApplicationImportEdit', params: { applicationImportId: applicationImport.id } }"
          custom
          v-slot="{ navigate }"
        >
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./application-import-details.component.ts"></script>
