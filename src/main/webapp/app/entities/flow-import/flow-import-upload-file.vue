<template>
  <div>
    <h2 id="page-heading" data-cy="FlowImportHeading">
      <span id="flow-import-heading">Flow Imports</span>
    </h2>

    <div v-if="!rowsLoaded">
      <div class="form-group">
        <div class="custom-file">
          <input type="file" class="custom-file-input" id="customFile" @change="handleFileUpload($event)" />
          <label class="custom-file-label" for="customFile">{{ excelFileName }}</label>
        </div>
      </div>
      <div class="form-group" v-if="excelFile">
        <button type="submit" class="btn btn-primary mb-2" v-on:click="submitFile()">Submit File</button>
      </div>
    </div>

    <br />
    <div class="alert alert-warning" v-if="!isFetching && flowImports && flowImports.length === 0">
      <span>No flowImports found</span>
    </div>
    <div class="table-responsive" v-if="flowImports && flowImports.length > 0">
      <table class="table table-striped" aria-describedby="flowImports">
        <thead>
          <tr>
            <th scope="row"><span>ID</span></th>
            <th scope="row"><span>Id Flow From Excel</span></th>
            <th scope="row"><span>Flow Alias</span></th>
            <th scope="row"><span>Source Element</span></th>
            <th scope="row"><span>Target Element</span></th>
            <th scope="row"><span>Description</span></th>
            <th scope="row"><span>Integration Pattern</span></th>
            <th scope="row"><span>Frequency</span></th>
            <th scope="row"><span>Format</span></th>
            <th scope="row"><span>Swagger</span></th>
            <th scope="row"><span>Blueprint</span></th>
            <th scope="row"><span>Blueprint Status</span></th>
            <th scope="row"><span>Flow Status</span></th>
            <th scope="row"><span>Comment</span></th>
            <th scope="row"><span>Document Name</span></th>
            <th scope="row"><span>Import Interface Status</span></th>
            <th scope="row"><span>Import Functional Flow Status</span></th>
            <th scope="row"><span>Import Data Flow Status</span></th>
            <th scope="row"><span>Import Status Message</span></th>
            <th scope="row"><span>Existing Application ID</span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="flowImport in flowImports" :key="flowImport.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'FlowImportView', params: { flowImportId: flowImport.id } }">{{ flowImport.id }}</router-link>
            </td>
            <td>{{ flowImport.idFlowFromExcel }}</td>
            <td>{{ flowImport.flowAlias }}</td>
            <td>{{ flowImport.sourceElement }}</td>
            <td>{{ flowImport.targetElement }}</td>
            <td>{{ flowImport.description }}</td>
            <td>{{ flowImport.integrationPattern }}</td>
            <td>{{ flowImport.frequency }}</td>
            <td>{{ flowImport.format }}</td>
            <td>{{ flowImport.swagger }}</td>
            <td>{{ flowImport.blueprint }}</td>
            <td>{{ flowImport.blueprintStatus }}</td>
            <td>{{ flowImport.flowStatus }}</td>
            <td>{{ flowImport.comment }}</td>
            <td>{{ flowImport.documentName }}</td>
            <td>
              <span v-bind:class="[flowImport.importInterfaceStatus === 'ERROR' ? 'rederror' : '']">
                {{ flowImport.importInterfaceStatus }}</span
              >
            </td>
            <td>
              <span v-bind:class="[flowImport.importFunctionalFlowStatus === 'ERROR' ? 'rederror' : '']">{{
                flowImport.importFunctionalFlowStatus
              }}</span>
            </td>
            <td>
              <span v-bind:class="[flowImport.importDataFlowStatus === 'ERROR' ? 'rederror' : '']">{{
                flowImport.importDataFlowStatus
              }}</span>
            </td>
            <td>
              <span v-bind:class="[flowImport.importStatusMessage === 'ERROR' ? 'rederror' : '']">{{
                flowImport.importStatusMessage
              }}</span>
            </td>
            <td>{{ flowImport.existingApplicationID }}</td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'FlowImportView', params: { flowImportId: flowImport.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'FlowImportEdit', params: { flowImportId: flowImport.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(flowImport)"
                  variant="danger"
                  class="btn btn-sm"
                  data-cy="entityDeleteButton"
                  v-b-modal.removeEntity
                >
                  <font-awesome-icon icon="times"></font-awesome-icon>
                  <span class="d-none d-md-inline">Delete</span>
                </b-button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <b-modal ref="removeEntity" id="removeEntity">
      <span slot="modal-title"
        ><span id="eaDesignItApp.flowImport.delete.question" data-cy="flowImportDeleteDialogHeading">Confirm delete operation</span></span
      >
      <div class="modal-body">
        <p id="jhi-delete-flowImport-heading">Are you sure you want to delete this Flow Import?</p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-flowImport"
          data-cy="entityConfirmDeleteButton"
          v-on:click="removeFlowImport()"
        >
          Delete
        </button>
      </div>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./flow-import-upload-file.component.ts"></script>
<style>
.rederror {
  background-color: red;
  color: white;
}
</style>
