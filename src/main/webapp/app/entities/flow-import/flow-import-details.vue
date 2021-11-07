<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="flowImport">
        <h2 class="jh-entity-heading" data-cy="flowImportDetailsHeading"><span>FlowImport</span> {{ flowImport.id }}</h2>
        <dl class="row jh-entity-details">
          <dt>
            <span>Id Flow From Excel</span>
          </dt>
          <dd>
            <span>{{ flowImport.idFlowFromExcel }}</span>
          </dd>
          <dt>
            <span>Flow Alias</span>
          </dt>
          <dd>
            <span>{{ flowImport.flowAlias }}</span>
          </dd>
          <dt>
            <span>Source Element</span>
          </dt>
          <dd>
            <span>{{ flowImport.sourceElement }}</span>
          </dd>
          <dt>
            <span>Target Element</span>
          </dt>
          <dd>
            <span>{{ flowImport.targetElement }}</span>
          </dd>
          <dt>
            <span>Description</span>
          </dt>
          <dd>
            <span>{{ flowImport.description }}</span>
          </dd>
          <dt>
            <span>Integration Pattern</span>
          </dt>
          <dd>
            <span>{{ flowImport.integrationPattern }}</span>
          </dd>
          <dt>
            <span>Frequency</span>
          </dt>
          <dd>
            <span>{{ flowImport.frequency }}</span>
          </dd>
          <dt>
            <span>Format</span>
          </dt>
          <dd>
            <span>{{ flowImport.format }}</span>
          </dd>
          <dt>
            <span>Swagger</span>
          </dt>
          <dd>
            <span>{{ flowImport.swagger }}</span>
          </dd>
          <dt>
            <span>Blueprint</span>
          </dt>
          <dd>
            <span>{{ flowImport.blueprint }}</span>
          </dd>
          <dt>
            <span>Blueprint Status</span>
          </dt>
          <dd>
            <span>{{ flowImport.blueprintStatus }}</span>
          </dd>
          <dt>
            <span>Flow Status</span>
          </dt>
          <dd>
            <span>{{ flowImport.flowStatus }}</span>
          </dd>
          <dt>
            <span>Comment</span>
          </dt>
          <dd>
            <span>{{ flowImport.comment }}</span>
          </dd>
          <dt>
            <span>Document Name</span>
          </dt>
          <dd>
            <span>{{ flowImport.documentName }}</span>
          </dd>
          <dt>
            <span>Import Interface Status</span>
          </dt>
          <dd>
            <span>{{ flowImport.importInterfaceStatus }}</span>
          </dd>
          <dt>
            <span>Import Functional Flow Status</span>
          </dt>
          <dd>
            <span>{{ flowImport.importFunctionalFlowStatus }}</span>
          </dd>
          <dt>
            <span>Import Data Flow Status</span>
          </dt>
          <dd>
            <span>{{ flowImport.importDataFlowStatus }}</span>
          </dd>
          <dt>
            <span>Import Status Message</span>
          </dt>
          <dd>
            <span>{{ flowImport.importStatusMessage }}</span>
          </dd>
          <dt>
            <span>Existing Application ID</span>
          </dt>
          <dd>
            <span>{{ flowImport.existingApplicationID }}</span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span> Back</span>
        </button>
        <router-link
          v-if="flowImport.id"
          :to="{ name: 'FlowImportEdit', params: { flowImportId: flowImport.id } }"
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

<script lang="ts" src="./flow-import-details.component.ts"></script>
