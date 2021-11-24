<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="dataFlowImport">
        <h2 class="jh-entity-heading" data-cy="dataFlowImportDetailsHeading"><span>DataFlowImport</span> {{ dataFlowImport.id }}</h2>
        <dl class="row jh-entity-details">
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
            <span>Data Documentation URL</span>
          </dt>
          <dd>
            <span>{{ dataFlowImport.dataDocumentationURL }}</span>
          </dd>
          <dt>
            <span>Data Item Resource Name</span>
          </dt>
          <dd>
            <span>{{ dataFlowImport.dataItemResourceName }}</span>
          </dd>
          <dt>
            <span>Data Item Resource Type</span>
          </dt>
          <dd>
            <span>{{ dataFlowImport.dataItemResourceType }}</span>
          </dd>
          <dt>
            <span>Data Item Description</span>
          </dt>
          <dd>
            <span>{{ dataFlowImport.dataItemDescription }}</span>
          </dd>
          <dt>
            <span>Data Item Documentation URL</span>
          </dt>
          <dd>
            <span>{{ dataFlowImport.dataItemDocumentationURL }}</span>
          </dd>
          <dt>
            <span>Frequency</span>
          </dt>
          <dd>
            <span>{{ dataFlowImport.frequency }}</span>
          </dd>
          <dt>
            <span>Format</span>
          </dt>
          <dd>
            <span>{{ dataFlowImport.format }}</span>
          </dd>
          <dt>
            <span>Contract URL</span>
          </dt>
          <dd>
            <span>{{ dataFlowImport.contractURL }}</span>
          </dd>
          <dt>
            <span>Import Data Flow Status</span>
          </dt>
          <dd>
            <span>{{ dataFlowImport.importDataFlowStatus }}</span>
          </dd>
          <dt>
            <span>Import Data Flow Item Status</span>
          </dt>
          <dd>
            <span>{{ dataFlowImport.importDataFlowItemStatus }}</span>
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
