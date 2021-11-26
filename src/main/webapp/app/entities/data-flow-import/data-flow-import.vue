<template>
  <div>
    <h2 id="page-heading" data-cy="DataFlowImportHeading">
      <span id="data-flow-import-heading">Data Flow Imports</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refresh List</span>
        </button>
        <router-link :to="{ name: 'DataFlowImportCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-data-flow-import"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span> Create a new Data Flow Import </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && dataFlowImports && dataFlowImports.length === 0">
      <span>No dataFlowImports found</span>
    </div>
    <div class="table-responsive" v-if="dataFlowImports && dataFlowImports.length > 0">
      <table class="table table-striped" aria-describedby="dataFlowImports">
        <thead>
          <tr>
            <th scope="row"><span>ID</span></th>
            <th scope="row"><span>Data Id</span></th>
            <th scope="row"><span>Data Parent Id</span></th>
            <th scope="row"><span>Data Parent Name</span></th>
            <th scope="row"><span>Functional Flow Id</span></th>
            <th scope="row"><span>Flow Interface Id</span></th>
            <th scope="row"><span>Data Type</span></th>
            <th scope="row"><span>Data Resource Name</span></th>
            <th scope="row"><span>Data Resource Type</span></th>
            <th scope="row"><span>Data Description</span></th>
            <th scope="row"><span>Data Frequency</span></th>
            <th scope="row"><span>Data Format</span></th>
            <th scope="row"><span>Data Contract URL</span></th>
            <th scope="row"><span>Data Documentation URL</span></th>
            <th scope="row"><span>Source</span></th>
            <th scope="row"><span>Target</span></th>
            <th scope="row"><span>Import Data Status</span></th>
            <th scope="row"><span>Import Data Item Status</span></th>
            <th scope="row"><span>Import Status Message</span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="dataFlowImport in dataFlowImports" :key="dataFlowImport.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'DataFlowImportView', params: { dataFlowImportId: dataFlowImport.id } }">{{
                dataFlowImport.id
              }}</router-link>
            </td>
            <td>{{ dataFlowImport.dataId }}</td>
            <td>{{ dataFlowImport.dataParentId }}</td>
            <td>{{ dataFlowImport.dataParentName }}</td>
            <td>{{ dataFlowImport.functionalFlowId }}</td>
            <td>{{ dataFlowImport.flowInterfaceId }}</td>
            <td>{{ dataFlowImport.dataType }}</td>
            <td>{{ dataFlowImport.dataResourceName }}</td>
            <td>{{ dataFlowImport.dataResourceType }}</td>
            <td>{{ dataFlowImport.dataDescription }}</td>
            <td>{{ dataFlowImport.dataFrequency }}</td>
            <td>{{ dataFlowImport.dataFormat }}</td>
            <td>{{ dataFlowImport.dataContractURL }}</td>
            <td>{{ dataFlowImport.dataDocumentationURL }}</td>
            <td>{{ dataFlowImport.source }}</td>
            <td>{{ dataFlowImport.target }}</td>
            <td>{{ dataFlowImport.importDataStatus }}</td>
            <td>{{ dataFlowImport.importDataItemStatus }}</td>
            <td>{{ dataFlowImport.importStatusMessage }}</td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'DataFlowImportView', params: { dataFlowImportId: dataFlowImport.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">View</span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'DataFlowImportEdit', params: { dataFlowImportId: dataFlowImport.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(dataFlowImport)"
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
        ><span id="eaDesignItApp.dataFlowImport.delete.question" data-cy="dataFlowImportDeleteDialogHeading"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-dataFlowImport-heading">Are you sure you want to delete this Data Flow Import?</p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-dataFlowImport"
          data-cy="entityConfirmDeleteButton"
          v-on:click="removeDataFlowImport()"
        >
          Delete
        </button>
      </div>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./data-flow-import.component.ts"></script>
