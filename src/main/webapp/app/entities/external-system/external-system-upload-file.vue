<template>
  <div>
    <h2 id="page-heading" data-cy="FlowImportHeading">
      <span id="flow-import-heading">External System Import </span>
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
    <div class="alert alert-warning" v-if="!isFetching && externalSystems && externalSystems.length === 0">
      <span>No flowImports found</span>
    </div>

    <table class="table table-striped" aria-describedby="dto.flowImports" v-if="externalSystems && externalSystems.length > 0">
      <thead>
        <tr>
          <th scope="row"><span>Id</span></th>
          <th scope="row"><span>External System id</span></th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="flowImport in externalSystems" :key="flowImport.id" data-cy="entityTable">
          <td>{{ flowImport.id }}</td>
          <td>{{ flowImport.externalSystemID }}</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>
<script lang="ts" src="./external-system-upload-file.component.ts"></script>
