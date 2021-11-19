<template>
  <div>
    <h2 id="page-heading" data-cy="FlowInterfaceHeading">
      <span id="flow-interface-heading">Flow Interfaces</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refresh List</span>
        </button>
        <router-link :to="{ name: 'FlowInterfaceCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-flow-interface"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span> Create a new Flow Interface </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && flowInterfaces && flowInterfaces.length === 0">
      <span>No flowInterfaces found</span>
    </div>
    <div class="table-responsive" v-if="flowInterfaces && flowInterfaces.length > 0">
      <table class="table table-striped" aria-describedby="flowInterfaces">
        <thead>
          <tr>
            <th scope="row"><span>ID</span></th>
            <th scope="row"><span>Alias</span></th>
            <th scope="row"><span>Protocol</span></th>
            <th scope="row"><span>Status</span></th>
            <th scope="row"><span>Documentation URL</span></th>
            <th scope="row"><span>Documentation URL 2</span></th>
            <th scope="row"><span>Start Date</span></th>
            <th scope="row"><span>End Date</span></th>
            <th scope="row"><span>Source</span></th>
            <th scope="row"><span>Target</span></th>
            <th scope="row"><span>Source Component</span></th>
            <th scope="row"><span>Target Component</span></th>
            <th scope="row"><span>Owner</span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="flowInterface in flowInterfaces" :key="flowInterface.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'FlowInterfaceView', params: { flowInterfaceId: flowInterface.id } }">{{
                flowInterface.id
              }}</router-link>
            </td>
            <td>{{ flowInterface.alias }}</td>
            <td>{{ flowInterface.protocol }}</td>
            <td>{{ flowInterface.status }}</td>
            <td>{{ flowInterface.documentationURL }}</td>
            <td>{{ flowInterface.documentationURL2 }}</td>
            <td>{{ flowInterface.startDate }}</td>
            <td>{{ flowInterface.endDate }}</td>
            <td>
              <div v-if="flowInterface.source">
                <router-link :to="{ name: 'ApplicationView', params: { applicationId: flowInterface.source.id } }">{{
                  flowInterface.source.name
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="flowInterface.target">
                <router-link :to="{ name: 'ApplicationView', params: { applicationId: flowInterface.target.id } }">{{
                  flowInterface.target.name
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="flowInterface.sourceComponent">
                <router-link
                  :to="{ name: 'ApplicationComponentView', params: { applicationComponentId: flowInterface.sourceComponent.id } }"
                  >{{ flowInterface.sourceComponent.name }}</router-link
                >
              </div>
            </td>
            <td>
              <div v-if="flowInterface.targetComponent">
                <router-link
                  :to="{ name: 'ApplicationComponentView', params: { applicationComponentId: flowInterface.targetComponent.id } }"
                  >{{ flowInterface.targetComponent.name }}</router-link
                >
              </div>
            </td>
            <td>
              <div v-if="flowInterface.owner">
                <router-link :to="{ name: 'OwnerView', params: { ownerId: flowInterface.owner.id } }">{{
                  flowInterface.owner.name
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'FlowInterfaceView', params: { flowInterfaceId: flowInterface.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">View</span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'FlowInterfaceEdit', params: { flowInterfaceId: flowInterface.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(flowInterface)"
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
        ><span id="eaDesignItApp.flowInterface.delete.question" data-cy="flowInterfaceDeleteDialogHeading"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-flowInterface-heading">Are you sure you want to delete this Flow Interface?</p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-flowInterface"
          data-cy="entityConfirmDeleteButton"
          v-on:click="removeFlowInterface()"
        >
          Delete
        </button>
      </div>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./flow-interface.component.ts"></script>
