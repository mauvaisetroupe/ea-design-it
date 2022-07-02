<template>
  <div>
    <h2 id="page-heading" data-cy="capabilityImportHeading">
      <span id="capability-import-heading">Application/Capabilities Imports</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-if="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refreshing</span>
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

      <div class="form-group" v-if="excelFile && sheetnames.length == 0">
        <button type="submit" class="btn btn-primary mb-2" v-on:click="getSheetnames()">Submit File</button>
      </div>

      <div v-if="sheetnames.length > 0" class="row">
        <div class="col-md-12">
          <span class="h3">Choose sheets to import</span>
          [<a @click="selectAll">Select All</a>] [<a @click="selectNone">Select None</a>]
        </div>

        <div v-for="sheet in sheetnames" class="col-md-2" :key="sheet">
          <input type="checkbox" v-model="checkedNames" :value="sheet" :id="sheet" />
          <label :for="sheet">{{ sheet }}</label>
        </div>
        <div class="form-group col-md-12" v-if="excelFile">
          <button type="submit" class="btn btn-primary mb-2" v-on:click="submitFile()">Submit File</button>
        </div>
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
            <th scope="row"><span>Sheet</span></th>
            <th scope="row"><span>Status</span></th>

            <th scope="row"><span>L0 Name</span></th>
            <th scope="row"><span>L0 Description</span></th>
            <th scope="row"><span>L0 Level</span></th>

            <th scope="row"><span>L1 Name</span></th>
            <th scope="row"><span>L1 Description</span></th>
            <th scope="row"><span>L1 Level</span></th>

            <th scope="row"><span>L2 Name</span></th>
            <th scope="row"><span>L2 Description</span></th>
            <th scope="row"><span>L2 Level</span></th>

            <th scope="row"><span>L3 Name</span></th>
            <th scope="row"><span>L3 Description</span></th>
            <th scope="row"><span>L3 Level</span></th>

            <th scope="row"><span>Application</span></th>
            <th scope="row"><span>Application</span></th>
            <th scope="row"><span>Application</span></th>
            <th scope="row"><span>Application</span></th>
            <th scope="row"><span>Application</span></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="dto in capabilitiesImports" :key="dto">
            <td>
              <span :class="[dto.importStatus === 'ERROR' ? 'rederror' : '']">{{ dto.sheetname }}</span>
            </td>
            <td>
              <span :class="[dto.importStatus === 'ERROR' ? 'rederror' : '']">{{ dto.importStatus }}</span>
            </td>
            <td>
              <span v-if="dto.capabilityImportDTO.l0" :class="[dto.importStatus === 'ERROR' ? 'rederror' : '']">{{
                dto.capabilityImportDTO.l0.name
              }}</span>
            </td>
            <td>
              <span v-if="dto.capabilityImportDTO.l0" :class="[dto.importStatus === 'ERROR' ? 'rederror' : '']">{{
                dto.capabilityImportDTO.l0.description
              }}</span>
            </td>
            <td>
              <span v-if="dto.capabilityImportDTO.l0" :class="[dto.importStatus === 'ERROR' ? 'rederror' : '']">{{
                dto.capabilityImportDTO.l0.level
              }}</span>
            </td>

            <td>
              <span v-if="dto.capabilityImportDTO.l1" :class="[dto.importStatus === 'ERROR' ? 'rederror' : '']">{{
                dto.capabilityImportDTO.l1.name
              }}</span>
            </td>
            <td>
              <span v-if="dto.capabilityImportDTO.l1" :class="[dto.importStatus === 'ERROR' ? 'rederror' : '']">{{
                dto.capabilityImportDTO.l1.description
              }}</span>
            </td>
            <td>
              <span v-if="dto.capabilityImportDTO.l1" :class="[dto.importStatus === 'ERROR' ? 'rederror' : '']">{{
                dto.capabilityImportDTO.l1.level
              }}</span>
            </td>

            <td>
              <span v-if="dto.capabilityImportDTO.l2" :class="[dto.importStatus === 'ERROR' ? 'rederror' : '']">{{
                dto.capabilityImportDTO.l2.name
              }}</span>
            </td>
            <td>
              <span v-if="dto.capabilityImportDTO.l2" :class="[dto.importStatus === 'ERROR' ? 'rederror' : '']">{{
                dto.capabilityImportDTO.l2.description
              }}</span>
            </td>
            <td>
              <span v-if="dto.capabilityImportDTO.l2" :class="[dto.importStatus === 'ERROR' ? 'rederror' : '']">{{
                dto.capabilityImportDTO.l2.level
              }}</span>
            </td>

            <td>
              <span v-if="dto.capabilityImportDTO.l3" :class="[dto.importStatus === 'ERROR' ? 'rederror' : '']">{{
                dto.capabilityImportDTO.l3.name
              }}</span>
            </td>
            <td>
              <span v-if="dto.capabilityImportDTO.l3" :class="[dto.importStatus === 'ERROR' ? 'rederror' : '']">{{
                dto.capabilityImportDTO.l3.description
              }}</span>
            </td>
            <td>
              <span v-if="dto.capabilityImportDTO.l3" :class="[dto.importStatus === 'ERROR' ? 'rederror' : '']">{{
                dto.capabilityImportDTO.l3.level
              }}</span>
            </td>
            <td v-for="application in dto.applicationNames" :key="application">
              <span :class="[dto.importStatus === 'ERROR' ? 'rederror' : '']">{{ application }}</span>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<style>
.rederror {
  background-color: red;
  color: white;
}
</style>

<script lang="ts" src="./application-capability-import-upload-file.component.ts"></script>
