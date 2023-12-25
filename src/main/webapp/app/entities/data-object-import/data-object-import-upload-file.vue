<template>
  <div>
    <h2 id="page-heading" data-cy="DataObjectImportHeading">
      <span id="data-object-import-heading">Business Objects / Data Objects Imports</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-if="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refreshing</span>
        </button>
      </div>
    </h2>

    <div v-if="!rowsLoaded">
      <div class="form-group">
        <div class="custom-file">
          <input type="file" class="custom-file-input" id="customFile" @change="handleFileUpload()" ref="excelFile" />
          <label class="custom-file-label" for="customFile">{{ excelFileName }}</label>
        </div>
      </div>

      <div class="form-group" v-if="excelFile">
        <button type="submit" class="btn btn-primary mb-2" v-on:click="submitFile()">Submit File</button>
      </div>
    </div>

    <br />
    <div class="alert alert-warning" v-if="rowsLoaded && dataObjectDTO.dtos && dataObjectDTO.dtos.length === 0">
      <span>No Data Objects found</span>
    </div>

    <div class="table-responsive" v-if="dataObjectDTO.dtos && dataObjectDTO.dtos.length > 0">
      <table class="table table-striped">
        <thead>
          <tr>
            <th scope="row"><span>Status</span></th>
            <th scope="row"><span>Error Message</span></th>
            <th scope="row"><span>Business Object</span></th>
            <th scope="row"><span>Data Object</span></th>
            <th scope="row"><span>Business Object abstract</span></th>
            <th scope="row"><span>Generalization</span></th>
            <th scope="row"><span>Application</span></th>
            <th scope="row"><span>Landscapes</span></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(dto, i) in dataObjectDTO.dtos" :key="i" data-cy="entityTable">
            <td>
              <span :class="[dto.importStatus === 'ERROR' ? 'rederror' : '']">{{ dto.importStatus }}</span>
            </td>
            <td>{{ dto.errorMessage }}</td>
            <td>{{ dto.businessobject }}</td>
            <td>{{ dto.dataobject }}</td>
            <td>{{ dto.abstractValue }}</td>
            <td>{{ dto.generalization }}</td>
            <td>{{ dto.application }}</td>
            <td>
              <span v-for="(landscape, i) in dto.landscapes" :key="i"><span v-if="i > 0">, </span>{{ landscape }}</span>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script lang="ts" src="./data-object-import-upload-file.component"></script>
<style>
.rederror {
  background-color: red;
  color: white;
}
</style>
