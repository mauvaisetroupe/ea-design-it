<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2 id="eaDesignItApp.dataFlowImport.home.createOrEditLabel" data-cy="DataFlowImportCreateUpdateHeading">
          Create or edit a DataFlowImport
        </h2>
        <div>
          <div class="form-group" v-if="dataFlowImport.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="dataFlowImport.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="data-flow-import-dataId">Data Id</label>
            <input
              type="text"
              class="form-control"
              name="dataId"
              id="data-flow-import-dataId"
              data-cy="dataId"
              :class="{ valid: !$v.dataFlowImport.dataId.$invalid, invalid: $v.dataFlowImport.dataId.$invalid }"
              v-model="$v.dataFlowImport.dataId.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="data-flow-import-dataParentId">Data Parent Id</label>
            <input
              type="text"
              class="form-control"
              name="dataParentId"
              id="data-flow-import-dataParentId"
              data-cy="dataParentId"
              :class="{ valid: !$v.dataFlowImport.dataParentId.$invalid, invalid: $v.dataFlowImport.dataParentId.$invalid }"
              v-model="$v.dataFlowImport.dataParentId.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="data-flow-import-dataParentName">Data Parent Name</label>
            <input
              type="text"
              class="form-control"
              name="dataParentName"
              id="data-flow-import-dataParentName"
              data-cy="dataParentName"
              :class="{ valid: !$v.dataFlowImport.dataParentName.$invalid, invalid: $v.dataFlowImport.dataParentName.$invalid }"
              v-model="$v.dataFlowImport.dataParentName.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="data-flow-import-functionalFlowId">Functional Flow Id</label>
            <input
              type="text"
              class="form-control"
              name="functionalFlowId"
              id="data-flow-import-functionalFlowId"
              data-cy="functionalFlowId"
              :class="{ valid: !$v.dataFlowImport.functionalFlowId.$invalid, invalid: $v.dataFlowImport.functionalFlowId.$invalid }"
              v-model="$v.dataFlowImport.functionalFlowId.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="data-flow-import-flowInterfaceId">Flow Interface Id</label>
            <input
              type="text"
              class="form-control"
              name="flowInterfaceId"
              id="data-flow-import-flowInterfaceId"
              data-cy="flowInterfaceId"
              :class="{ valid: !$v.dataFlowImport.flowInterfaceId.$invalid, invalid: $v.dataFlowImport.flowInterfaceId.$invalid }"
              v-model="$v.dataFlowImport.flowInterfaceId.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="data-flow-import-dataType">Data Type</label>
            <input
              type="text"
              class="form-control"
              name="dataType"
              id="data-flow-import-dataType"
              data-cy="dataType"
              :class="{ valid: !$v.dataFlowImport.dataType.$invalid, invalid: $v.dataFlowImport.dataType.$invalid }"
              v-model="$v.dataFlowImport.dataType.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="data-flow-import-dataResourceName">Data Resource Name</label>
            <input
              type="text"
              class="form-control"
              name="dataResourceName"
              id="data-flow-import-dataResourceName"
              data-cy="dataResourceName"
              :class="{ valid: !$v.dataFlowImport.dataResourceName.$invalid, invalid: $v.dataFlowImport.dataResourceName.$invalid }"
              v-model="$v.dataFlowImport.dataResourceName.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="data-flow-import-dataResourceType">Data Resource Type</label>
            <input
              type="text"
              class="form-control"
              name="dataResourceType"
              id="data-flow-import-dataResourceType"
              data-cy="dataResourceType"
              :class="{ valid: !$v.dataFlowImport.dataResourceType.$invalid, invalid: $v.dataFlowImport.dataResourceType.$invalid }"
              v-model="$v.dataFlowImport.dataResourceType.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="data-flow-import-dataDescription">Data Description</label>
            <input
              type="text"
              class="form-control"
              name="dataDescription"
              id="data-flow-import-dataDescription"
              data-cy="dataDescription"
              :class="{ valid: !$v.dataFlowImport.dataDescription.$invalid, invalid: $v.dataFlowImport.dataDescription.$invalid }"
              v-model="$v.dataFlowImport.dataDescription.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="data-flow-import-dataFrequency">Data Frequency</label>
            <input
              type="text"
              class="form-control"
              name="dataFrequency"
              id="data-flow-import-dataFrequency"
              data-cy="dataFrequency"
              :class="{ valid: !$v.dataFlowImport.dataFrequency.$invalid, invalid: $v.dataFlowImport.dataFrequency.$invalid }"
              v-model="$v.dataFlowImport.dataFrequency.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="data-flow-import-dataFormat">Data Format</label>
            <input
              type="text"
              class="form-control"
              name="dataFormat"
              id="data-flow-import-dataFormat"
              data-cy="dataFormat"
              :class="{ valid: !$v.dataFlowImport.dataFormat.$invalid, invalid: $v.dataFlowImport.dataFormat.$invalid }"
              v-model="$v.dataFlowImport.dataFormat.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="data-flow-import-dataContractURL">Data Contract URL</label>
            <input
              type="text"
              class="form-control"
              name="dataContractURL"
              id="data-flow-import-dataContractURL"
              data-cy="dataContractURL"
              :class="{ valid: !$v.dataFlowImport.dataContractURL.$invalid, invalid: $v.dataFlowImport.dataContractURL.$invalid }"
              v-model="$v.dataFlowImport.dataContractURL.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="data-flow-import-dataDocumentationURL">Data Documentation URL</label>
            <input
              type="text"
              class="form-control"
              name="dataDocumentationURL"
              id="data-flow-import-dataDocumentationURL"
              data-cy="dataDocumentationURL"
              :class="{ valid: !$v.dataFlowImport.dataDocumentationURL.$invalid, invalid: $v.dataFlowImport.dataDocumentationURL.$invalid }"
              v-model="$v.dataFlowImport.dataDocumentationURL.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="data-flow-import-source">Source</label>
            <input
              type="text"
              class="form-control"
              name="source"
              id="data-flow-import-source"
              data-cy="source"
              :class="{ valid: !$v.dataFlowImport.source.$invalid, invalid: $v.dataFlowImport.source.$invalid }"
              v-model="$v.dataFlowImport.source.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="data-flow-import-target">Target</label>
            <input
              type="text"
              class="form-control"
              name="target"
              id="data-flow-import-target"
              data-cy="target"
              :class="{ valid: !$v.dataFlowImport.target.$invalid, invalid: $v.dataFlowImport.target.$invalid }"
              v-model="$v.dataFlowImport.target.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="data-flow-import-importDataStatus">Import Data Status</label>
            <select
              class="form-control"
              name="importDataStatus"
              :class="{ valid: !$v.dataFlowImport.importDataStatus.$invalid, invalid: $v.dataFlowImport.importDataStatus.$invalid }"
              v-model="$v.dataFlowImport.importDataStatus.$model"
              id="data-flow-import-importDataStatus"
              data-cy="importDataStatus"
            >
              <option v-for="importStatus in importStatusValues" :key="importStatus" v-bind:value="importStatus">{{ importStatus }}</option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="data-flow-import-importDataItemStatus">Import Data Item Status</label>
            <select
              class="form-control"
              name="importDataItemStatus"
              :class="{ valid: !$v.dataFlowImport.importDataItemStatus.$invalid, invalid: $v.dataFlowImport.importDataItemStatus.$invalid }"
              v-model="$v.dataFlowImport.importDataItemStatus.$model"
              id="data-flow-import-importDataItemStatus"
              data-cy="importDataItemStatus"
            >
              <option v-for="importStatus in importStatusValues" :key="importStatus" v-bind:value="importStatus">{{ importStatus }}</option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="data-flow-import-importStatusMessage">Import Status Message</label>
            <input
              type="text"
              class="form-control"
              name="importStatusMessage"
              id="data-flow-import-importStatusMessage"
              data-cy="importStatusMessage"
              :class="{ valid: !$v.dataFlowImport.importStatusMessage.$invalid, invalid: $v.dataFlowImport.importStatusMessage.$invalid }"
              v-model="$v.dataFlowImport.importStatusMessage.$model"
            />
          </div>
        </div>
        <div>
          <button type="button" id="cancel-save" data-cy="entityCreateCancelButton" class="btn btn-secondary" v-on:click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span>Cancel</span>
          </button>
          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="$v.dataFlowImport.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span>Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./data-flow-import-update.component.ts"></script>
