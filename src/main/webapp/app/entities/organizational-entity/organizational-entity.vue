<template>
  <div>
    <h2 id="page-heading" data-cy="OrganizationalEntityHeading">
      <span id="organizational-entity-heading">Organizational Entities</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refresh list</span>
        </button>
        <router-link :to="{ name: 'OrganizationalEntityCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-organizational-entity"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>Create a new Organizational Entity</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && organizationalEntities && organizationalEntities.length === 0">
      <span>No Organizational Entities found</span>
    </div>
    <div class="table-responsive" v-if="organizationalEntities && organizationalEntities.length > 0">
      <table class="table table-striped" aria-describedby="organizationalEntities">
        <thead>
          <tr>
            <th scope="row"><span>ID</span></th>
            <th scope="row"><span>Name</span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="organizationalEntity in organizationalEntities" :key="organizationalEntity.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'OrganizationalEntityView', params: { organizationalEntityId: organizationalEntity.id } }">{{
                organizationalEntity.id
              }}</router-link>
            </td>
            <td>{{ organizationalEntity.name }}</td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'OrganizationalEntityView', params: { organizationalEntityId: organizationalEntity.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">View</span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'OrganizationalEntityEdit', params: { organizationalEntityId: organizationalEntity.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(organizationalEntity)"
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
        <span id="eaDesignItApp.organizationalEntity.delete.question" data-cy="organizationalEntityDeleteDialogHeading"
          >Confirm delete operation</span
        >
      </template>
      <div class="modal-body">
        <p id="jhi-delete-organizationalEntity-heading">Are you sure you want to delete Organizational Entity {{ removeId }}?</p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-on:click="closeDialog()">Cancel</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-organizationalEntity"
            data-cy="entityConfirmDeleteButton"
            v-on:click="removeOrganizationalEntity()"
          >
            Delete
          </button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./organizational-entity.component.ts"></script>
