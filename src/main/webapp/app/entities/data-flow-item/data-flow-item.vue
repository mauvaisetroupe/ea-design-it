<template>
  <div>
    <h2 id="page-heading" data-cy="DataFlowItemHeading">
      <font-awesome-icon icon="clone" style="color: Tomato; font-size: 0.7em"></font-awesome-icon>
      <span id="data-flow-item-heading">Data Flow Items</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refresh List</span>
        </button>
        <router-link :to="{ name: 'DataFlowItemCreate' }" custom v-slot="{ navigate }" v-if="accountService().writeAuthorities">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-data-flow-item"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span> Create a new Data Flow Item </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && dataFlowItems && dataFlowItems.length === 0">
      <span>No dataFlowItems found</span>
    </div>
    <div class="table-responsive" v-if="dataFlowItems && dataFlowItems.length > 0">
      <table class="table table-striped" aria-describedby="dataFlowItems">
        <thead>
          <tr>
            <th scope="row"><span>ID</span></th>
            <th scope="row"><span>Resource Name</span></th>
            <th scope="row"><span>Resource Type</span></th>
            <th scope="row"><span>Description</span></th>
            <th scope="row"><span>Contract URL</span></th>
            <th scope="row"><span>Documentation URL</span></th>
            <th scope="row"><span>Start Date</span></th>
            <th scope="row"><span>End Date</span></th>
            <th scope="row"><span>Data Flow</span></th>
            <th scope="row"><span>Flow Interface</span></th>
            <th scope="row"><span>Interface Protocol</span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="dataFlowItem in dataFlowItems" :key="dataFlowItem.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'DataFlowItemView', params: { dataFlowItemId: dataFlowItem.id } }">{{
                dataFlowItem.id
              }}</router-link>
            </td>
            <td>{{ dataFlowItem.resourceName }}</td>
            <td>{{ dataFlowItem.resourceType }}</td>
            <td>{{ dataFlowItem.description }}</td>
            <td>
              <a v-bind:href="dataFlowItem.contractURL">{{ dataFlowItem.contractURL ? dataFlowItem.contractURL.substring(0, 20) : '' }}</a>
            </td>
            <td>
              <a v-bind:href="dataFlowItem.documentationURL">{{
                dataFlowItem.documentationURL ? dataFlowItem.documentationURL.substring(0, 20) : ''
              }}</a>
            </td>
            <td>{{ dataFlowItem.startDate }}</td>
            <td>{{ dataFlowItem.endDate }}</td>
            <td>
              <div v-if="dataFlowItem.dataFlow">
                <router-link :to="{ name: 'DataFlowView', params: { dataFlowId: dataFlowItem.dataFlow.id } }">{{
                  dataFlowItem.dataFlow.id
                }}</router-link>
              </div>
            </td>

            <td>
              <div v-if="dataFlowItem.dataFlow && dataFlowItem.dataFlow.flowInterface">
                <router-link
                  :title="dataFlowItem.dataFlow.flowInterface.protocol ? dataFlowItem.dataFlow.flowInterface.protocol.name : ''"
                  :to="{ name: 'FlowInterfaceView', params: { flowInterfaceId: dataFlowItem.dataFlow.flowInterface.id } }"
                  >{{ dataFlowItem.dataFlow.flowInterface.alias }}</router-link
                >
              </div>
            </td>
            <td>
              <div v-if="dataFlowItem.dataFlow && dataFlowItem.dataFlow.flowInterface">
                {{ dataFlowItem.dataFlow.flowInterface.protocol ? dataFlowItem.dataFlow.flowInterface.protocol.name : '' }}
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'DataFlowItemView', params: { dataFlowItemId: dataFlowItem.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">View</span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'DataFlowItemEdit', params: { dataFlowItemId: dataFlowItem.id } }"
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
                  v-on:click="prepareRemove(dataFlowItem)"
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
        ><span id="eaDesignItApp.dataFlowItem.delete.question" data-cy="dataFlowItemDeleteDialogHeading"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-dataFlowItem-heading">Are you sure you want to delete this Data Flow Item?</p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-dataFlowItem"
          data-cy="entityConfirmDeleteButton"
          v-on:click="removeDataFlowItem()"
        >
          Delete
        </button>
      </div>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./data-flow-item.component.ts"></script>
