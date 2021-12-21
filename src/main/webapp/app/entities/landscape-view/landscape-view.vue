<template>
  <div>
    <h2 id="page-heading" data-cy="LandscapeViewHeading">
      <span id="landscape-view-heading">Landscape Views</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refresh List</span>
        </button>
        <router-link :to="{ name: 'LandscapeViewCreate' }" custom v-slot="{ navigate }" v-if="accountService().writeAuthorities">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-landscape-view"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span> Create a new Landscape View </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && landscapeViews && landscapeViews.length === 0">
      <span>No landscapeViews found</span>
    </div>
    <div class="table-responsive" v-if="landscapeViews && landscapeViews.length > 0">
      <table class="table table-striped" aria-describedby="landscapeViews">
        <thead>
          <tr>
            <th scope="row"><span>ID</span></th>
            <th scope="row"><span>Diagram Name</span></th>
            <th scope="row"><span>Owner</span></th>
            <th scope="row"><span>Flows</span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="landscapeView in landscapeViews" :key="landscapeView.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'LandscapeViewView', params: { landscapeViewId: landscapeView.id } }">{{
                landscapeView.id
              }}</router-link>
            </td>
            <td>{{ landscapeView.diagramName }}</td>
            <td>
              <div v-if="landscapeView.owner">
                <router-link :to="{ name: 'OwnerView', params: { ownerId: landscapeView.owner.id } }">{{
                  landscapeView.owner.name
                }}</router-link>
              </div>
            </td>
            <td>
              <span v-for="(flows, i) in landscapeView.flows" :key="flows.id"
                >{{ i > 0 ? ', ' : '' }}
                <router-link
                  :title="flows.description"
                  class="form-control-static"
                  :to="{ name: 'FunctionalFlowView', params: { functionalFlowId: flows.id } }"
                  >{{ flows.alias }}</router-link
                >
              </span>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'LandscapeViewView', params: { landscapeViewId: landscapeView.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">View</span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'LandscapeViewEdit', params: { landscapeViewId: landscapeView.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button
                    @click="navigate"
                    class="btn btn-primary btn-sm edit"
                    data-cy="entityEditButton"
                    v-if="accountService().writeAuthorities"
                  >
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-if="accountService().writeAuthorities"
                  v-on:click="prepareRemove(landscapeView)"
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
        ><span id="eaDesignItApp.landscapeView.delete.question" data-cy="landscapeViewDeleteDialogHeading"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-landscapeView-heading">Are you sure you want to delete this Landscape View?</p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-landscapeView"
          data-cy="entityConfirmDeleteButton"
          v-on:click="removeLandscapeView()"
        >
          Delete
        </button>
      </div>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./landscape-view.component.ts"></script>
