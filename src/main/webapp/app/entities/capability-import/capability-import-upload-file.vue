<template>
  <div>
    <h2 id="page-heading" data-cy="capabilityImportHeading">
      <span id="capability-import-heading">Capabilities Imports</span>
      <div class="d-flex justify-content-end" v-if="rowsLoaded || isFetching">
        <button class="btn btn-info mr-2" v-on:click="filterErrors" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Filter Errors</span>
        </button>
      </div>
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
    <div class="alert alert-warning" v-if="!isFetching && capabilitiesImports && capabilitiesImports.length === 0">
      <span>No capabilitiesImports found</span>
    </div>

    <div class="table-responsive" v-if="capabilitiesImports && capabilitiesImports.length > 0">
      <table class="table table-striped" aria-describedby="capabilitiesImports">
        <thead>
          <tr>
            <th scope="row"><span>Status</span></th>

            <th scope="row"><span>Error</span></th>

            <th scope="row"><span>Domain</span></th>

            <th scope="row"><span>L0 Level</span></th>
            <th scope="row"><span>L0 Name</span></th>
            <th scope="row"><span>L0 Description</span></th>

            <th scope="row"><span>L1 Level</span></th>
            <th scope="row"><span>L1 Name</span></th>
            <th scope="row"><span>L1 Description</span></th>

            <th scope="row"><span>L2 Level</span></th>
            <th scope="row"><span>L2 Name</span></th>
            <th scope="row"><span>L2 Description</span></th>

            <th scope="row"><span>L3 Level</span></th>
            <th scope="row"><span>L3 Name</span></th>
            <th scope="row"><span>L3 Description</span></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(dto, i) in filteredCapabilitiesImports" :key="i">
            <td>
              <span v-bind:class="[dto.status === 'ERROR' ? 'rederror' : '']">{{ dto.status }} </span>
            </td>
            <td>{{ dto.error }}</td>
            <td>
              <span v-if="dto.domain">{{ dto.domain.name }}</span>
            </td>
            <td>
              <span v-if="dto.l0">{{ dto.l0.level }}</span>
            </td>
            <td>
              <span v-if="dto.l0">{{ dto.l0.name }}</span>
            </td>
            <td>
              <span v-if="dto.l0">{{ dto.l0.description }}</span>
            </td>

            <td>
              <span v-if="dto.l1">{{ dto.l1.level }}</span>
            </td>
            <td>
              <span v-if="dto.l1">{{ dto.l1.name }}</span>
            </td>
            <td>
              <span v-if="dto.l1">{{ dto.l1.description }}</span>
            </td>

            <td>
              <span v-if="dto.l2">{{ dto.l2.level }}</span>
            </td>
            <td>
              <span v-if="dto.l2">{{ dto.l2.name }}</span>
            </td>
            <td>
              <span v-if="dto.l2">{{ dto.l2.description }}</span>
            </td>

            <td>
              <span v-if="dto.l3">{{ dto.l3.level }}</span>
            </td>
            <td>
              <span v-if="dto.l3">{{ dto.l3.name }}</span>
            </td>
            <td>
              <span v-if="dto.l3">{{ dto.l3.description }}</span>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script lang="ts" src="./capability-import-upload-file.component.ts"></script>
<style>
.rederror {
  background-color: red;
  color: white;
}
</style>
