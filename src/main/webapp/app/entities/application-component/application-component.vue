<template>
  <div>
    <h2 id="page-heading" data-cy="ApplicationComponentHeading">
      <span id="application-component-heading">Application Components</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refresh List</span>
        </button>
        <router-link :to="{ name: 'ApplicationComponentCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-application-component"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span> Create a new Application Component </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && applicationComponents && applicationComponents.length === 0">
      <span>No applicationComponents found</span>
    </div>
    <div class="table-responsive" v-if="applicationComponents && applicationComponents.length > 0">
      <table class="table table-striped" aria-describedby="applicationComponents">
        <thead>
          <tr>
            <th scope="row"><span>ID</span></th>
            <th scope="row"><span>Name</span></th>
            <th scope="row"><span>Description</span></th>
            <th scope="row"><span>Type</span></th>
            <th scope="row"><span>Technology</span></th>
            <th scope="row"><span>Comment</span></th>
            <th scope="row"><span>Application</span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="applicationComponent in applicationComponents" :key="applicationComponent.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'ApplicationComponentView', params: { applicationComponentId: applicationComponent.id } }">{{
                applicationComponent.id
              }}</router-link>
            </td>
            <td>{{ applicationComponent.name }}</td>
            <td>{{ applicationComponent.description }}</td>
            <td>{{ applicationComponent.type }}</td>
            <td>{{ applicationComponent.technology }}</td>
            <td>{{ applicationComponent.comment }}</td>
            <td>
              <div v-if="applicationComponent.application">
                <router-link :to="{ name: 'ApplicationView', params: { applicationId: applicationComponent.application.id } }">{{
                  applicationComponent.application.name
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'ApplicationComponentView', params: { applicationComponentId: applicationComponent.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">View</span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'ApplicationComponentEdit', params: { applicationComponentId: applicationComponent.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(applicationComponent)"
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
        ><span id="eaDesignItApp.applicationComponent.delete.question" data-cy="applicationComponentDeleteDialogHeading"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-applicationComponent-heading">Are you sure you want to delete this Application Component?</p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-applicationComponent"
          data-cy="entityConfirmDeleteButton"
          v-on:click="removeApplicationComponent()"
        >
          Delete
        </button>
      </div>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./application-component.component.ts"></script>