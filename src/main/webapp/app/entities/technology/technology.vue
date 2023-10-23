<template>
  <div>
    <h2 id="page-heading" data-cy="TechnologyHeading">
      <span id="technology-heading">Technologies</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refresh list</span>
        </button>
        <router-link :to="{ name: 'TechnologyCreate' }" custom v-slot="{ navigate }" v-if="accountService.writeAuthorities">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-technology"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>Create a new Technology</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && technologies && technologies.length === 0">
      <span>No Technologies found</span>
    </div>
    <div class="table-responsive" v-if="technologies && technologies.length > 0">
      <table class="table table-striped" aria-describedby="technologies">
        <thead>
          <tr>
            <th scope="row"><span>ID</span></th>
            <th scope="row"><span>Name</span></th>
            <th scope="row"><span>Type</span></th>
            <th scope="row"><span>Description</span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="technology in technologies" :key="technology.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'TechnologyView', params: { technologyId: technology.id } }">{{ technology.id }}</router-link>
            </td>
            <td>{{ technology.name }}</td>
            <td>{{ technology.type }}</td>
            <td>{{ technology.description }}</td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'TechnologyView', params: { technologyId: technology.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">View</span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'TechnologyEdit', params: { technologyId: technology.id } }"
                  custom
                  v-slot="{ navigate }"
                  v-if="accountService.writeAuthorities"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-if="accountService.writeAuthorities"
                  v-on:click="prepareRemove(technology)"
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
        <span id="eaDesignItApp.technology.delete.question" data-cy="technologyDeleteDialogHeading">Confirm delete operation</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-technology-heading">Are you sure you want to delete Technology {{ removeId }}?</p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-on:click="closeDialog()">Cancel</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-technology"
            data-cy="entityConfirmDeleteButton"
            v-on:click="removeTechnology()"
          >
            Delete
          </button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./technology.component.ts"></script>
