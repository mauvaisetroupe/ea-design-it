<template>
  <div>
    <h2 id="page-heading" data-cy="FunctionalFlowHeading">
      <span id="functional-flow-heading">Functional Flows</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refresh List</span>
        </button>
        <router-link :to="{ name: 'FunctionalFlowCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-functional-flow"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span> Create a new Functional Flow </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && functionalFlows && functionalFlows.length === 0">
      <span>No functionalFlows found</span>
    </div>
    <div class="table-responsive" v-if="functionalFlows && functionalFlows.length > 0">
      <table class="table table-striped" aria-describedby="functionalFlows">
        <thead>
          <tr>
            <th scope="row"><span>ID</span></th>
            <th scope="row"><span>Alias</span></th>
            <th scope="row"><span>Description</span></th>
            <th scope="row"><span>Comment</span></th>
            <th scope="row"><span>Status</span></th>
            <th scope="row"><span>Interfaces</span></th>
            <th scope="row"><span>Landscape</span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="functionalFlow in functionalFlows" :key="functionalFlow.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'FunctionalFlowView', params: { functionalFlowId: functionalFlow.id } }">{{
                functionalFlow.id
              }}</router-link>
            </td>
            <td>{{ functionalFlow.alias }}</td>
            <td>{{ functionalFlow.description }}</td>
            <td>{{ functionalFlow.comment }}</td>
            <td>{{ functionalFlow.status }}</td>
            <td>
              <span v-for="(interfaces, i) in functionalFlow.interfaces" :key="interfaces.id"
                >{{ i > 0 ? ', ' : '' }}
                <router-link class="form-control-static" :to="{ name: 'FlowInterfaceView', params: { flowInterfaceId: interfaces.id } }">{{
                  interfaces.id
                }}</router-link>
              </span>
            </td>
            <td>
              <div v-if="functionalFlow.landscape">
                <router-link :to="{ name: 'LandscapeViewView', params: { landscapeViewId: functionalFlow.landscape.id } }">{{
                  functionalFlow.landscape.diagramName
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'FunctionalFlowView', params: { functionalFlowId: functionalFlow.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">View</span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'FunctionalFlowEdit', params: { functionalFlowId: functionalFlow.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(functionalFlow)"
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
        ><span id="eaDesignItApp.functionalFlow.delete.question" data-cy="functionalFlowDeleteDialogHeading"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-functionalFlow-heading">Are you sure you want to delete this Functional Flow?</p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-functionalFlow"
          data-cy="entityConfirmDeleteButton"
          v-on:click="removeFunctionalFlow()"
        >
          Delete
        </button>
      </div>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./functional-flow.component.ts"></script>
