<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2 id="eaDesignItApp.flowImport.home.createOrEditLabel" data-cy="FlowImportCreateUpdateHeading">Create or edit a FlowImport</h2>
        <div>
          <div class="form-group" v-if="flowImport.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="flowImport.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="flow-import-idFlowFromExcel">Id Flow From Excel</label>
            <input
              type="text"
              class="form-control"
              name="idFlowFromExcel"
              id="flow-import-idFlowFromExcel"
              data-cy="idFlowFromExcel"
              :class="{ valid: !$v.flowImport.idFlowFromExcel.$invalid, invalid: $v.flowImport.idFlowFromExcel.$invalid }"
              v-model="$v.flowImport.idFlowFromExcel.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="flow-import-flowAlias">Flow Alias</label>
            <input
              type="text"
              class="form-control"
              name="flowAlias"
              id="flow-import-flowAlias"
              data-cy="flowAlias"
              :class="{ valid: !$v.flowImport.flowAlias.$invalid, invalid: $v.flowImport.flowAlias.$invalid }"
              v-model="$v.flowImport.flowAlias.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="flow-import-sourceElement">Source Element</label>
            <input
              type="text"
              class="form-control"
              name="sourceElement"
              id="flow-import-sourceElement"
              data-cy="sourceElement"
              :class="{ valid: !$v.flowImport.sourceElement.$invalid, invalid: $v.flowImport.sourceElement.$invalid }"
              v-model="$v.flowImport.sourceElement.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="flow-import-targetElement">Target Element</label>
            <input
              type="text"
              class="form-control"
              name="targetElement"
              id="flow-import-targetElement"
              data-cy="targetElement"
              :class="{ valid: !$v.flowImport.targetElement.$invalid, invalid: $v.flowImport.targetElement.$invalid }"
              v-model="$v.flowImport.targetElement.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="flow-import-description">Description</label>
            <input
              type="text"
              class="form-control"
              name="description"
              id="flow-import-description"
              data-cy="description"
              :class="{ valid: !$v.flowImport.description.$invalid, invalid: $v.flowImport.description.$invalid }"
              v-model="$v.flowImport.description.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="flow-import-integrationPattern">Integration Pattern</label>
            <input
              type="text"
              class="form-control"
              name="integrationPattern"
              id="flow-import-integrationPattern"
              data-cy="integrationPattern"
              :class="{ valid: !$v.flowImport.integrationPattern.$invalid, invalid: $v.flowImport.integrationPattern.$invalid }"
              v-model="$v.flowImport.integrationPattern.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="flow-import-frequency">Frequency</label>
            <input
              type="text"
              class="form-control"
              name="frequency"
              id="flow-import-frequency"
              data-cy="frequency"
              :class="{ valid: !$v.flowImport.frequency.$invalid, invalid: $v.flowImport.frequency.$invalid }"
              v-model="$v.flowImport.frequency.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="flow-import-format">Format</label>
            <input
              type="text"
              class="form-control"
              name="format"
              id="flow-import-format"
              data-cy="format"
              :class="{ valid: !$v.flowImport.format.$invalid, invalid: $v.flowImport.format.$invalid }"
              v-model="$v.flowImport.format.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="flow-import-swagger">Swagger</label>
            <input
              type="text"
              class="form-control"
              name="swagger"
              id="flow-import-swagger"
              data-cy="swagger"
              :class="{ valid: !$v.flowImport.swagger.$invalid, invalid: $v.flowImport.swagger.$invalid }"
              v-model="$v.flowImport.swagger.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="flow-import-sourceURLDocumentation">Source URL Documentation</label>
            <input
              type="text"
              class="form-control"
              name="sourceURLDocumentation"
              id="flow-import-sourceURLDocumentation"
              data-cy="sourceURLDocumentation"
              :class="{ valid: !$v.flowImport.sourceURLDocumentation.$invalid, invalid: $v.flowImport.sourceURLDocumentation.$invalid }"
              v-model="$v.flowImport.sourceURLDocumentation.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="flow-import-targetURLDocumentation">Target URL Documentation</label>
            <input
              type="text"
              class="form-control"
              name="targetURLDocumentation"
              id="flow-import-targetURLDocumentation"
              data-cy="targetURLDocumentation"
              :class="{ valid: !$v.flowImport.targetURLDocumentation.$invalid, invalid: $v.flowImport.targetURLDocumentation.$invalid }"
              v-model="$v.flowImport.targetURLDocumentation.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="flow-import-sourceDocumentationStatus">Source Documentation Status</label>
            <input
              type="text"
              class="form-control"
              name="sourceDocumentationStatus"
              id="flow-import-sourceDocumentationStatus"
              data-cy="sourceDocumentationStatus"
              :class="{
                valid: !$v.flowImport.sourceDocumentationStatus.$invalid,
                invalid: $v.flowImport.sourceDocumentationStatus.$invalid,
              }"
              v-model="$v.flowImport.sourceDocumentationStatus.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="flow-import-targetDocumentationStatus">Target Documentation Status</label>
            <input
              type="text"
              class="form-control"
              name="targetDocumentationStatus"
              id="flow-import-targetDocumentationStatus"
              data-cy="targetDocumentationStatus"
              :class="{
                valid: !$v.flowImport.targetDocumentationStatus.$invalid,
                invalid: $v.flowImport.targetDocumentationStatus.$invalid,
              }"
              v-model="$v.flowImport.targetDocumentationStatus.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="flow-import-flowStatus">Flow Status</label>
            <input
              type="text"
              class="form-control"
              name="flowStatus"
              id="flow-import-flowStatus"
              data-cy="flowStatus"
              :class="{ valid: !$v.flowImport.flowStatus.$invalid, invalid: $v.flowImport.flowStatus.$invalid }"
              v-model="$v.flowImport.flowStatus.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="flow-import-comment">Comment</label>
            <input
              type="text"
              class="form-control"
              name="comment"
              id="flow-import-comment"
              data-cy="comment"
              :class="{ valid: !$v.flowImport.comment.$invalid, invalid: $v.flowImport.comment.$invalid }"
              v-model="$v.flowImport.comment.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="flow-import-documentName">Document Name</label>
            <input
              type="text"
              class="form-control"
              name="documentName"
              id="flow-import-documentName"
              data-cy="documentName"
              :class="{ valid: !$v.flowImport.documentName.$invalid, invalid: $v.flowImport.documentName.$invalid }"
              v-model="$v.flowImport.documentName.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="flow-import-importInterfaceStatus">Import Interface Status</label>
            <select
              class="form-control"
              name="importInterfaceStatus"
              :class="{ valid: !$v.flowImport.importInterfaceStatus.$invalid, invalid: $v.flowImport.importInterfaceStatus.$invalid }"
              v-model="$v.flowImport.importInterfaceStatus.$model"
              id="flow-import-importInterfaceStatus"
              data-cy="importInterfaceStatus"
            >
              <option v-for="importStatus in importStatusValues" :key="importStatus" v-bind:value="importStatus">{{ importStatus }}</option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="flow-import-importFunctionalFlowStatus">Import Functional Flow Status</label>
            <select
              class="form-control"
              name="importFunctionalFlowStatus"
              :class="{
                valid: !$v.flowImport.importFunctionalFlowStatus.$invalid,
                invalid: $v.flowImport.importFunctionalFlowStatus.$invalid,
              }"
              v-model="$v.flowImport.importFunctionalFlowStatus.$model"
              id="flow-import-importFunctionalFlowStatus"
              data-cy="importFunctionalFlowStatus"
            >
              <option v-for="importStatus in importStatusValues" :key="importStatus" v-bind:value="importStatus">{{ importStatus }}</option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="flow-import-importDataFlowStatus">Import Data Flow Status</label>
            <select
              class="form-control"
              name="importDataFlowStatus"
              :class="{ valid: !$v.flowImport.importDataFlowStatus.$invalid, invalid: $v.flowImport.importDataFlowStatus.$invalid }"
              v-model="$v.flowImport.importDataFlowStatus.$model"
              id="flow-import-importDataFlowStatus"
              data-cy="importDataFlowStatus"
            >
              <option v-for="importStatus in importStatusValues" :key="importStatus" v-bind:value="importStatus">{{ importStatus }}</option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="flow-import-importStatusMessage">Import Status Message</label>
            <input
              type="text"
              class="form-control"
              name="importStatusMessage"
              id="flow-import-importStatusMessage"
              data-cy="importStatusMessage"
              :class="{ valid: !$v.flowImport.importStatusMessage.$invalid, invalid: $v.flowImport.importStatusMessage.$invalid }"
              v-model="$v.flowImport.importStatusMessage.$model"
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
            :disabled="$v.flowImport.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span>Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./flow-import-update.component.ts"></script>
