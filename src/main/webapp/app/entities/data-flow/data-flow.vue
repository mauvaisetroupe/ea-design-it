<template>
  <div>
    <h2 id="page-heading" data-cy="DataFlowHeading">
      <span id="data-flow-heading">Data Flows</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refresh List</span>
        </button>
        <router-link :to="{ name: 'DataFlowCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-data-flow"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span> Create a new Data Flow </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && dataFlows && dataFlows.length === 0">
      <span>No dataFlows found</span>
    </div>
    <div class="table-responsive" v-if="dataFlows && dataFlows.length > 0">
      <table class="table table-striped" aria-describedby="dataFlows">
        <thead>
          <tr>
            <th scope="row"><span>ID</span></th>
            <th scope="row"><span>Frequency</span></th>
            <th scope="row"><span>Format</span></th>
            <th scope="row"><span>Type</span></th>
            <th scope="row"><span>Description</span></th>
            <th scope="row"><span>Resource Name</span></th>
            <th scope="row"><span>Contract URL</span></th>
            <th scope="row"><span>Documentation URL</span></th>
            <th scope="row"><span>Functional Flows</span></th>
            <th scope="row"><span>Flow Interface</span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="dataFlow in dataFlows" :key="dataFlow.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'DataFlowView', params: { dataFlowId: dataFlow.id } }">{{ dataFlow.id }}</router-link>
            </td>
            <td>{{ dataFlow.frequency }}</td>
            <td>{{ dataFlow.format }}</td>
            <td>{{ dataFlow.type }}</td>
            <td>{{ dataFlow.description }}</td>
            <td>{{ dataFlow.resourceName }}</td>
            <td>{{ dataFlow.contractURL }}</td>
            <td>{{ dataFlow.documentationURL }}</td>
            <td>
              <span v-for="(functionalFlows, i) in dataFlow.functionalFlows" :key="functionalFlows.id"
                >{{ i > 0 ? ', ' : '' }}
                <router-link
                  class="form-control-static"
                  :to="{ name: 'FunctionalFlowView', params: { functionalFlowId: functionalFlows.id } }"
                  >{{ functionalFlows.alias }}</router-link
                >
              </span>
            </td>
            <td>
              <div v-if="dataFlow.flowInterface">
                <router-link :to="{ name: 'FlowInterfaceView', params: { flowInterfaceId: dataFlow.flowInterface.id } }">{{
                  dataFlow.flowInterface.alias
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'DataFlowView', params: { dataFlowId: dataFlow.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'DataFlowEdit', params: { dataFlowId: dataFlow.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(dataFlow)"
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
        ><span id="eaDesignItApp.dataFlow.delete.question" data-cy="dataFlowDeleteDialogHeading">Confirm delete operation</span></span
      >
      <div class="modal-body">
        <p id="jhi-delete-dataFlow-heading">Are you sure you want to delete this Data Flow?</p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-dataFlow"
          data-cy="entityConfirmDeleteButton"
          v-on:click="removeDataFlow()"
        >
          Delete
        </button>
      </div>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./data-flow.component.ts"></script>