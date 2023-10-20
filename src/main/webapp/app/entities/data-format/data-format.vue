<template>
  <div>
    <h2 id="page-heading" data-cy="DataFormatHeading">
      <span id="data-format-heading">Data Formats</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refresh list</span>
        </button>
        <router-link :to="{ name: 'DataFormatCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-data-format"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>Create a new Data Format</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && dataFormats && dataFormats.length === 0">
      <span>No Data Formats found</span>
    </div>
    <div class="table-responsive" v-if="dataFormats && dataFormats.length > 0">
      <table class="table table-striped" aria-describedby="dataFormats">
        <thead>
          <tr>
            <th scope="row"><span>ID</span></th>
            <th scope="row"><span>Name</span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="dataFormat in dataFormats" :key="dataFormat.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'DataFormatView', params: { dataFormatId: dataFormat.id } }">{{ dataFormat.id }}</router-link>
            </td>
            <td>{{ dataFormat.name }}</td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'DataFormatView', params: { dataFormatId: dataFormat.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'DataFormatEdit', params: { dataFormatId: dataFormat.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(dataFormat)"
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
      <template #modal-title>
        <span id="eaDesignItApp.dataFormat.delete.question" data-cy="dataFormatDeleteDialogHeading">Confirm delete operation</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-dataFormat-heading">Are you sure you want to delete Data Format {{ removeId }}?</p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-on:click="closeDialog()">Cancel</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-dataFormat"
            data-cy="entityConfirmDeleteButton"
            v-on:click="removeDataFormat()"
          >
            Delete
          </button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./data-format.component.ts"></script>
