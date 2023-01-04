<template>
  <div>
    <h2 id="page-heading" data-cy="FlowGroupHeading">
      <span id="flow-group-heading">Flow Groups</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refresh List</span>
        </button>
        <router-link :to="{ name: 'FlowGroupCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-flow-group"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span> Create a new Flow Group </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && flowGroups && flowGroups.length === 0">
      <span>No flowGroups found</span>
    </div>
    <div class="table-responsive" v-if="flowGroups && flowGroups.length > 0">
      <table class="table table-striped" aria-describedby="flowGroups">
        <thead>
          <tr>
            <th scope="row"><span>ID</span></th>
            <th scope="row"><span>Title</span></th>
            <th scope="row"><span>Url</span></th>
            <th scope="row"><span>Flow</span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="flowGroup in flowGroups" :key="flowGroup.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'FlowGroupView', params: { flowGroupId: flowGroup.id } }">{{ flowGroup.id }}</router-link>
            </td>
            <td>{{ flowGroup.title }}</td>
            <td>{{ flowGroup.url }}</td>
            <td>
              <div v-if="flowGroup.flow">
                <router-link :to="{ name: 'FunctionalFlowView', params: { functionalFlowId: flowGroup.flow.id } }">{{
                  flowGroup.flow.alias
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'FlowGroupView', params: { flowGroupId: flowGroup.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'FlowGroupEdit', params: { flowGroupId: flowGroup.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(flowGroup)"
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
        ><span id="eaDesignItApp.flowGroup.delete.question" data-cy="flowGroupDeleteDialogHeading">Confirm delete operation</span></span
      >
      <div class="modal-body">
        <p id="jhi-delete-flowGroup-heading">Are you sure you want to delete this Flow Group?</p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-flowGroup"
          data-cy="entityConfirmDeleteButton"
          v-on:click="removeFlowGroup()"
        >
          Delete
        </button>
      </div>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./flow-group.component.ts"></script>
