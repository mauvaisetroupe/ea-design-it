<template>
  <div>
    <h2 id="page-heading" data-cy="DataFlowHeading">
      <span id="data-flow-heading"
        ><font-awesome-icon icon="folder" style="color: Tomato; font-size: 0.9em"></font-awesome-icon> Data Flows</span
      >
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refresh list</span>
        </button>
        <router-link :to="{ name: 'DataFlowCreate' }" custom v-slot="{ navigate }" v-if="accountService().writeAuthorities">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-data-flow"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>Create a new Data Flow</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && dataFlows && dataFlows.length === 0">
      <span>No Data Flows found</span>
    </div>

    <div>
      <input type="text" placeholder="Filter by text" v-model="filter" />
    </div>

    <div class="table-responsive" v-if="dataFlows && dataFlows.length > 0">
      <table class="table table-striped" aria-describedby="dataFlows">
        <thead>
          <tr>
            <th scope="row"><span>ID</span></th>
            <th scope="row"><span>Resource Name</span></th>
            <th scope="row"><span>Resource Type</span></th>
            <th scope="row"><span>Description</span></th>
            <th scope="row"><span>Frequency</span></th>
            <th scope="row"><span>Contract URL</span></th>
            <th scope="row"><span>Documentation URL</span></th>
            <th scope="row"><span>Start Date</span></th>
            <th scope="row"><span>End Date</span></th>
            <th scope="row"><span>Format</span></th>
            <th scope="row"><span>Data Items</span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="dataFlow in filteredRows" :key="dataFlow.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'DataFlowView', params: { dataFlowId: dataFlow.id } }">{{ dataFlow.id }}</router-link>
            </td>
            <td>{{ dataFlow.resourceName }}</td>
            <td>{{ dataFlow.resourceType }}</td>
            <td>{{ dataFlow.description }}</td>
            <td>{{ dataFlow.frequency }}</td>
            <td>
              <a v-if="dataFlow.contractURL" v-bind:href="dataFlow.contractURL">{{ dataFlow.contractURL.substring(0, 20) }}...</a>
            </td>
            <td>
              <a v-if="dataFlow.documentationURL" v-bind:href="dataFlow.documentationURL"
                >{{ dataFlow.documentationURL.substring(0, 20) }}...</a
              >
            </td>
            <td>{{ dataFlow.startDate }}</td>
            <td>{{ dataFlow.endDate }}</td>
            <td>
              <div v-if="dataFlow.format">
                <router-link :to="{ name: 'DataFormatView', params: { dataFormatId: dataFlow.format.id } }">{{
                  dataFlow.format.name
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
                <router-link
                  :to="{ name: 'DataFlowEdit', params: { dataFlowId: dataFlow.id } }"
                  custom
                  v-slot="{ navigate }"
                  v-if="accountService().writeAuthorities"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-if="accountService().deleteAuthorities"
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
      <template #modal-title>
        <span id="eaDesignItApp.dataFlow.delete.question" data-cy="dataFlowDeleteDialogHeading">Confirm delete operation</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-dataFlow-heading">Are you sure you want to delete Data Flow {{ removeId }}?</p>
      </div>
      <template #modal-footer>
        <div>
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
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./data-flow.component.ts"></script>
