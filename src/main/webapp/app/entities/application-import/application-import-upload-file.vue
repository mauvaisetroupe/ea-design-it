<template>
  <div>
    <h2 id="page-heading" data-cy="ApplicationImportHeading">
      <span id="application-import-heading">Application Imports</span>
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

      <div class="alert alert-warning">
        Application name and alias are foreign keys for many others assets. You cannot change application name for a given alias, so please
        edit database and Excel file to be synchronized for such a change. If an error occurs, the entire import will be rejected.
      </div>

      <div class="form-group" v-if="excelFile">
        <button type="submit" class="btn btn-primary mb-2" v-on:click="submitFile()">Submit File</button>
      </div>
    </div>

    <br />
    <div class="alert alert-warning" v-if="rowsLoaded && applicationImports && applicationImports.length === 0">
      <span>No applicationImports found</span>
    </div>

    <div class="table-responsive" v-if="applicationImports && applicationImports.length > 0">
      <table class="table table-striped" aria-describedby="applicationImports">
        <thead>
          <tr>
            <th scope="row"><span>ID</span></th>
            <th scope="row"><span>Import Id</span></th>
            <th scope="row"><span>Excel File Name</span></th>
            <th scope="row"><span>Id From Excel</span></th>
            <th scope="row"><span>Name</span></th>
            <th scope="row"><span>Description</span></th>
            <th scope="row"><span>Type</span></th>
            <th scope="row"><span>Software Type</span></th>
            <th scope="row"><span>Categories</span></th>
            <th scope="row"><span>Technologies</span></th>
            <th scope="row"><span>Documentation</span></th>
            <th scope="row"><span>Comment</span></th>
            <th scope="row"><span>Owner</span></th>
            <th scope="row"><span>Import Status</span></th>
            <th scope="row"><span>Import Status Message</span></th>
            <th scope="row"><span>Existing Application ID</span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="applicationImport in applicationImports" :key="applicationImport.id" data-cy="entityTable">
            <td>{{ applicationImport.id }}</td>
            <td>{{ applicationImport.importId }}</td>
            <td>{{ applicationImport.excelFileName }}</td>
            <td>{{ applicationImport.idFromExcel }}</td>
            <td>{{ applicationImport.name }}</td>
            <td>{{ applicationImport.description }}</td>
            <td>{{ applicationImport.type }}</td>
            <td>{{ applicationImport.softwareType }}</td>
            <td>{{ applicationImport.categories }}</td>
            <td>{{ applicationImport.technologies }}</td>
            <td>{{ applicationImport.documentation }}</td>
            <td>{{ applicationImport.comment }}</td>
            <td>{{ applicationImport.owner }}</td>
            <td>{{ applicationImport.importStatus }}</td>
            <td>{{ applicationImport.importStatusMessage }}</td>
            <td>{{ applicationImport.existingApplicationID }}</td>
          </tr>
        </tbody>
      </table>
    </div>
    <b-modal ref="removeEntity" id="removeEntity">
      <span slot="modal-title"
        ><span id="eaDesignItApp.applicationImport.delete.question" data-cy="applicationImportDeleteDialogHeading"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-applicationImport-heading">Are you sure you want to delete this Application Import?</p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-applicationImport"
          data-cy="entityConfirmDeleteButton"
          v-on:click="removeApplicationImport()"
        >
          Delete
        </button>
      </div>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./application-import-upload-file.component.ts"></script>
