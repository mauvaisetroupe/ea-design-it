<template>
  <div>
    <h2 id="page-heading" data-cy="ApplicationImportHeading">
      <span id="application-import-heading">Application Imports</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refresh List</span>
        </button>
        <router-link :to="{ name: 'ApplicationImportCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-application-import"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span> Create a new Application Import </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && applicationImports && applicationImports.length === 0">
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
            <th scope="row"><span>Category 1</span></th>
            <th scope="row"><span>Category 2</span></th>
            <th scope="row"><span>Category 3</span></th>
            <th scope="row"><span>Technology</span></th>
            <th scope="row"><span>Documentation</span></th>
            <th scope="row"><span>Comment</span></th>
            <th scope="row"><span>Import Status</span></th>
            <th scope="row"><span>Import Status Message</span></th>
            <th scope="row"><span>Existing Application ID</span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="applicationImport in applicationImports" :key="applicationImport.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'ApplicationImportView', params: { applicationImportId: applicationImport.id } }">{{
                applicationImport.id
              }}</router-link>
            </td>
            <td>{{ applicationImport.importId }}</td>
            <td>{{ applicationImport.excelFileName }}</td>
            <td>{{ applicationImport.idFromExcel }}</td>
            <td>{{ applicationImport.name }}</td>
            <td>{{ applicationImport.description }}</td>
            <td>{{ applicationImport.type }}</td>
            <td>{{ applicationImport.softwareType }}</td>
            <td>{{ applicationImport.category1 }}</td>
            <td>{{ applicationImport.category2 }}</td>
            <td>{{ applicationImport.category3 }}</td>
            <td>{{ applicationImport.technology }}</td>
            <td>{{ applicationImport.documentation }}</td>
            <td>{{ applicationImport.comment }}</td>
            <td>{{ applicationImport.importStatus }}</td>
            <td>{{ applicationImport.importStatusMessage }}</td>
            <td>{{ applicationImport.existingApplicationID }}</td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'ApplicationImportView', params: { applicationImportId: applicationImport.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">View</span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'ApplicationImportEdit', params: { applicationImportId: applicationImport.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(applicationImport)"
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

<script lang="ts" src="./application-import.component.ts"></script>
