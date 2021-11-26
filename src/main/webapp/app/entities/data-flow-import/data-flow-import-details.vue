<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="dataFlowImport">
        <h2 class="jh-entity-heading" data-cy="dataFlowImportDetailsHeading"><span>DataFlowImport</span> {{ dataFlowImport.id }}</h2>
        <dl class="row jh-entity-details">
          <dt>
            <span>Data Id</span>
          </dt>
          <dd>
            <span>{{ dataFlowImport.dataId }}</span>
          </dd>
          <dt>
            <span>Data Parent Id</span>
          </dt>
          <dd>
            <span>{{ dataFlowImport.dataParentId }}</span>
          </dd>
          <dt>
            <span>Data Parent Name</span>
          </dt>
          <dd>
            <span>{{ dataFlowImport.dataParentName }}</span>
          </dd>
          <dt>
            <span>Functional Flow Id</span>
          </dt>
          <dd>
            <span>{{ dataFlowImport.functionalFlowId }}</span>
          </dd>
          <dt>
            <span>Flow Interface Id</span>
          </dt>
          <dd>
            <span>{{ dataFlowImport.flowInterfaceId }}</span>
          </dd>
          <dt>
            <span>Data Type</span>
          </dt>
          <dd>
            <span>{{ dataFlowImport.dataType }}</span>
          </dd>
          <dt>
            <span>Data Resource Name</span>
          </dt>
          <dd>
            <span>{{ dataFlowImport.dataResourceName }}</span>
          </dd>
          <dt>
            <span>Data Resource Type</span>
          </dt>
          <dd>
            <span>{{ dataFlowImport.dataResourceType }}</span>
          </dd>
          <dt>
            <span>Data Description</span>
          </dt>
          <dd>
            <span>{{ dataFlowImport.dataDescription }}</span>
          </dd>
          <dt>
            <span>Data Frequency</span>
          </dt>
          <dd>
            <span>{{ dataFlowImport.dataFrequency }}</span>
          </dd>
          <dt>
            <span>Data Format</span>
          </dt>
          <dd>
            <span>{{ dataFlowImport.dataFormat }}</span>
          </dd>
          <dt>
            <span>Data Contract URL</span>
          </dt>
          <dd>
            <span>{{ dataFlowImport.dataContractURL }}</span>
          </dd>
          <dt>
            <span>Data Documentation URL</span>
          </dt>
          <dd>
            <span>{{ dataFlowImport.dataDocumentationURL }}</span>
          </dd>
          <dt>
            <span>Source</span>
          </dt>
          <dd>
            <span>{{ dataFlowImport.source }}</span>
          </dd>
          <dt>
            <span>Target</span>
          </dt>
          <dd>
            <span>{{ dataFlowImport.target }}</span>
          </dd>
          <dt>
            <span>Import Data Status</span>
          </dt>
          <dd>
            <span>{{ dataFlowImport.importDataStatus }}</span>
          </dd>
          <dt>
            <span>Import Data Item Status</span>
          </dt>
          <dd>
            <span>{{ dataFlowImport.importDataItemStatus }}</span>
          </dd>
          <dt>
            <span>Import Status Message</span>
          </dt>
          <dd>
            <span>{{ dataFlowImport.importStatusMessage }}</span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span> Back</span>
        </button>
        <router-link
          v-if="dataFlowImport.id"
          :to="{ name: 'DataFlowImportEdit', params: { dataFlowImportId: dataFlowImport.id } }"
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

<script lang="ts" src="./data-flow-import-details.component.ts"></script>
