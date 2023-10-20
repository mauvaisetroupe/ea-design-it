<template>
  <div>
    <h2 id="page-heading" data-cy="ExternalReferenceHeading">
      <span id="external-reference-heading">External References</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refresh list</span>
        </button>
        <router-link :to="{ name: 'ExternalReferenceCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-external-reference"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>Create a new External Reference</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && externalReferences && externalReferences.length === 0">
      <span>No External References found</span>
    </div>
    <div class="table-responsive" v-if="externalReferences && externalReferences.length > 0">
      <table class="table table-striped" aria-describedby="externalReferences">
        <thead>
          <tr>
            <th scope="row"><span>ID</span></th>
            <th scope="row"><span>External ID</span></th>
            <th scope="row"><span>External System</span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="externalReference in externalReferences" :key="externalReference.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'ExternalReferenceView', params: { externalReferenceId: externalReference.id } }">{{
                externalReference.id
              }}</router-link>
            </td>
            <td>{{ externalReference.externalID }}</td>
            <td>
              <div v-if="externalReference.externalSystem">
                <router-link :to="{ name: 'ExternalSystemView', params: { externalSystemId: externalReference.externalSystem.id } }">{{
                  externalReference.externalSystem.externalSystemID
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'ExternalReferenceView', params: { externalReferenceId: externalReference.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">View</span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'ExternalReferenceEdit', params: { externalReferenceId: externalReference.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(externalReference)"
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
        <span id="eaDesignItApp.externalReference.delete.question" data-cy="externalReferenceDeleteDialogHeading"
          >Confirm delete operation</span
        >
      </template>
      <div class="modal-body">
        <p id="jhi-delete-externalReference-heading">Are you sure you want to delete External Reference {{ removeId }}?</p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-on:click="closeDialog()">Cancel</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-externalReference"
            data-cy="entityConfirmDeleteButton"
            v-on:click="removeExternalReference()"
          >
            Delete
          </button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./external-reference.component.ts"></script>
