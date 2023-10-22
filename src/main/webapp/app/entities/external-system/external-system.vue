<template>
  <div>
    <h2 id="page-heading" data-cy="ExternalSystemHeading">
      <span id="external-system-heading">External Systems</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refresh list</span>
        </button>
        <router-link :to="{ name: 'ExternalSystemCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-external-system"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>Create a new External System</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && externalSystems && externalSystems.length === 0">
      <span>No External Systems found</span>
    </div>
    <div class="table-responsive" v-if="externalSystems && externalSystems.length > 0">
      <table class="table table-striped" aria-describedby="externalSystems">
        <thead>
          <tr>
            <th scope="row"><span>ID</span></th>
            <th scope="row"><span>External System ID</span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="externalSystem in externalSystems" :key="externalSystem.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'ExternalSystemView', params: { externalSystemId: externalSystem.id } }">{{
                externalSystem.id
              }}</router-link>
            </td>
            <td>{{ externalSystem.externalSystemID }}</td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'ExternalSystemView', params: { externalSystemId: externalSystem.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">View</span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'ExternalSystemEdit', params: { externalSystemId: externalSystem.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(externalSystem)"
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
        <span id="eaDesignItApp.externalSystem.delete.question" data-cy="externalSystemDeleteDialogHeading">Confirm delete operation</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-externalSystem-heading">Are you sure you want to delete External System {{ removeId }}?</p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-on:click="closeDialog()">Cancel</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-externalSystem"
            data-cy="entityConfirmDeleteButton"
            v-on:click="removeExternalSystem()"
          >
            Delete
          </button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./external-system.component.ts"></script>
