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
            <label class="form-control-label" for="data-flow-import-dataItemResourceName">Data Item Resource Name</label>
            <input
              type="text"
              class="form-control"
              name="dataItemResourceName"
              id="data-flow-import-dataItemResourceName"
              data-cy="dataItemResourceName"
              :class="{ valid: !$v.dataFlowImport.dataItemResourceName.$invalid, invalid: $v.dataFlowImport.dataItemResourceName.$invalid }"
              v-model="$v.dataFlowImport.dataItemResourceName.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="data-flow-import-dataItemResourceType">Data Item Resource Type</label>
            <input
              type="text"
              class="form-control"
              name="dataItemResourceType"
              id="data-flow-import-dataItemResourceType"
              data-cy="dataItemResourceType"
              :class="{ valid: !$v.dataFlowImport.dataItemResourceType.$invalid, invalid: $v.dataFlowImport.dataItemResourceType.$invalid }"
              v-model="$v.dataFlowImport.dataItemResourceType.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="data-flow-import-dataItemDescription">Data Item Description</label>
            <input
              type="text"
              class="form-control"
              name="dataItemDescription"
              id="data-flow-import-dataItemDescription"
              data-cy="dataItemDescription"
              :class="{ valid: !$v.dataFlowImport.dataItemDescription.$invalid, invalid: $v.dataFlowImport.dataItemDescription.$invalid }"
              v-model="$v.dataFlowImport.dataItemDescription.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="data-flow-import-dataItemDocumentationURL">Data Item Documentation URL</label>
            <input
              type="text"
              class="form-control"
              name="dataItemDocumentationURL"
              id="data-flow-import-dataItemDocumentationURL"
              data-cy="dataItemDocumentationURL"
              :class="{
                valid: !$v.dataFlowImport.dataItemDocumentationURL.$invalid,
                invalid: $v.dataFlowImport.dataItemDocumentationURL.$invalid,
              }"
              v-model="$v.dataFlowImport.dataItemDocumentationURL.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="data-flow-import-frequency">Frequency</label>
            <input
              type="text"
              class="form-control"
              name="frequency"
              id="data-flow-import-frequency"
              data-cy="frequency"
              :class="{ valid: !$v.dataFlowImport.frequency.$invalid, invalid: $v.dataFlowImport.frequency.$invalid }"
              v-model="$v.dataFlowImport.frequency.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="data-flow-import-format">Format</label>
            <input
              type="text"
              class="form-control"
              name="format"
              id="data-flow-import-format"
              data-cy="format"
              :class="{ valid: !$v.dataFlowImport.format.$invalid, invalid: $v.dataFlowImport.format.$invalid }"
              v-model="$v.dataFlowImport.format.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="data-flow-import-contractURL">Contract URL</label>
            <input
              type="text"
              class="form-control"
              name="contractURL"
              id="data-flow-import-contractURL"
              data-cy="contractURL"
              :class="{ valid: !$v.dataFlowImport.contractURL.$invalid, invalid: $v.dataFlowImport.contractURL.$invalid }"
              v-model="$v.dataFlowImport.contractURL.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="data-flow-import-importDataFlowStatus">Import Data Flow Status</label>
            <select
              class="form-control"
              name="importDataFlowStatus"
              :class="{ valid: !$v.dataFlowImport.importDataFlowStatus.$invalid, invalid: $v.dataFlowImport.importDataFlowStatus.$invalid }"
              v-model="$v.dataFlowImport.importDataFlowStatus.$model"
              id="data-flow-import-importDataFlowStatus"
              data-cy="importDataFlowStatus"
            >
              <option v-for="importStatus in importStatusValues" :key="importStatus" v-bind:value="importStatus">{{ importStatus }}</option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="data-flow-import-importDataFlowItemStatus">Import Data Flow Item Status</label>
            <select
              class="form-control"
              name="importDataFlowItemStatus"
              :class="{
                valid: !$v.dataFlowImport.importDataFlowItemStatus.$invalid,
                invalid: $v.dataFlowImport.importDataFlowItemStatus.$invalid,
              }"
              v-model="$v.dataFlowImport.importDataFlowItemStatus.$model"
              id="data-flow-import-importDataFlowItemStatus"
              data-cy="importDataFlowItemStatus"
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
