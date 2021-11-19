<template>
  <div>
    <h2 id="page-heading" data-cy="EventDataHeading">
      <span id="event-data-heading">Event Data</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refresh List</span>
        </button>
        <router-link :to="{ name: 'EventDataCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-event-data"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span> Create a new Event Data </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && eventData && eventData.length === 0">
      <span>No eventData found</span>
    </div>
    <div class="table-responsive" v-if="eventData && eventData.length > 0">
      <table class="table table-striped" aria-describedby="eventData">
        <thead>
          <tr>
            <th scope="row"><span>ID</span></th>
            <th scope="row"><span>Name</span></th>
            <th scope="row"><span>Contract URL</span></th>
            <th scope="row"><span>Documentation URL</span></th>
            <th scope="row"><span>Start Date</span></th>
            <th scope="row"><span>End Date</span></th>
            <th scope="row"><span>Data Flow</span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="eventData in eventData" :key="eventData.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'EventDataView', params: { eventDataId: eventData.id } }">{{ eventData.id }}</router-link>
            </td>
            <td>{{ eventData.name }}</td>
            <td>{{ eventData.contractURL }}</td>
            <td>{{ eventData.documentationURL }}</td>
            <td>{{ eventData.startDate }}</td>
            <td>{{ eventData.endDate }}</td>
            <td>
              <div v-if="eventData.dataFlow">
                <router-link :to="{ name: 'DataFlowView', params: { dataFlowId: eventData.dataFlow.id } }">{{
                  eventData.dataFlow.id
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'EventDataView', params: { eventDataId: eventData.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'EventDataEdit', params: { eventDataId: eventData.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(eventData)"
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
        ><span id="eaDesignItApp.eventData.delete.question" data-cy="eventDataDeleteDialogHeading">Confirm delete operation</span></span
      >
      <div class="modal-body">
        <p id="jhi-delete-eventData-heading">Are you sure you want to delete this Event Data?</p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-eventData"
          data-cy="entityConfirmDeleteButton"
          v-on:click="removeEventData()"
        >
          Delete
        </button>
      </div>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./event-data.component.ts"></script>
