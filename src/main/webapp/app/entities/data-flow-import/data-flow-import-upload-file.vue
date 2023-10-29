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
          <input type="file" id="customFile" @change="handleFileUpload()" ref="excelFile" />
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
            <th scope="row"><span>Data Id</span></th>
            <th scope="row"><span>Import Data Status</span></th>
            <th scope="row"><span>Import Data Item Status</span></th>
            <th scope="row"><span>Import Status Message</span></th>
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
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="dataFlowImport in dataFlowImports" :key="dataFlowImport.id" data-cy="entityTable">
            <td>
              {{ dataFlowImport.id }}
            </td>
            <td>{{ dataFlowImport.dataId }}</td>
            <td>
              <span v-bind:class="[dataFlowImport.importDataStatus === 'ERROR' ? 'rederror' : '']">{{
                dataFlowImport.importDataStatus
              }}</span>
            </td>
            <td>
              <span v-bind:class="[dataFlowImport.importDataItemStatus === 'ERROR' ? 'rederror' : '']">{{
                dataFlowImport.importDataItemStatus
              }}</span>
            </td>
            <td>{{ dataFlowImport.importStatusMessage }}</td>
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
