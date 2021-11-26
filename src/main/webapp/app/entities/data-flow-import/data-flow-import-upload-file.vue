<template>
  <div>
    <h2 id="page-heading" data-cy="FlowImportHeading">
      <span id="flow-import-heading">Data & Data Item Imports</span>
      <div class="d-flex justify-content-end" v-if="rowsLoaded || isFetching">
        <button class="btn btn-info mr-2" v-on:click="filterErrors" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Filter Errors</span>
        </button>
        <button class="btn btn-info mr-2" v-on:click="exportErrors" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Export Errors Report</span>
        </button>
      </div>
    </h2>

    <div v-if="!rowsLoaded">
      <div class="form-group">
        <div class="custom-file">
          <input type="file" id="customFile" @change="handleFileUpload($event)" />
          <label class="custom-file-label" for="customFile">{{ excelFileName }}</label>
        </div>
      </div>
      <div class="form-group" v-if="excelFile">
        <button type="submit" class="btn btn-primary mb-2" v-on:click="submitFile()">Submit File</button>
      </div>
    </div>

    <br />
    <div class="alert alert-warning" v-if="!isFetching && dataFlowImports && dataFlowImports.length === 0">
      <span>No DataFlow found</span>
    </div>

    <div class="table-responsive" v-if="dataFlowImports && dataFlowImports.length > 0">
      <table class="table table-striped" aria-describedby="dataFlowImports">
        <thead>
          <tr>
            <th scope="row"><span>ID</span></th>
            <th scope="row"><span>Data Resource Name</span></th>
            <th scope="row"><span>Data Resource Type</span></th>
            <th scope="row"><span>Data Description</span></th>
            <th scope="row"><span>Data Documentation URL</span></th>
            <th scope="row"><span>Data Item Resource Name</span></th>
            <th scope="row"><span>Data Item Resource Type</span></th>
            <th scope="row"><span>Data Item Description</span></th>
            <th scope="row"><span>Data Item Documentation URL</span></th>
            <th scope="row"><span>Frequency</span></th>
            <th scope="row"><span>Format</span></th>
            <th scope="row"><span>Contract URL</span></th>
            <th scope="row"><span>Import Data Flow Status</span></th>
            <th scope="row"><span>Import Data Flow Item Status</span></th>
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
            <td>{{ dataFlowImport.dataResourceName }}</td>
            <td>{{ dataFlowImport.dataResourceType }}</td>
            <td>{{ dataFlowImport.dataDescription }}</td>
            <td>{{ dataFlowImport.dataDocumentationURL }}</td>
            <td>{{ dataFlowImport.dataItemResourceName }}</td>
            <td>{{ dataFlowImport.dataItemResourceType }}</td>
            <td>{{ dataFlowImport.dataItemDescription }}</td>
            <td>{{ dataFlowImport.dataItemDocumentationURL }}</td>
            <td>{{ dataFlowImport.frequency }}</td>
            <td>{{ dataFlowImport.format }}</td>
            <td>{{ dataFlowImport.contractURL }}</td>
            <td>{{ dataFlowImport.importDataFlowStatus }}</td>
            <td>{{ dataFlowImport.importDataFlowItemStatus }}</td>
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
  </div>
</template>

<script lang="ts" src="./data-flow-import-upload-file.component.ts"></script>
<style>
.rederror {
  background-color: red;
  color: white;
}
</style>
