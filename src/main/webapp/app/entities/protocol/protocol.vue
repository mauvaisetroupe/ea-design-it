<template>
  <div>
    <h2 id="page-heading" data-cy="ProtocolHeading">
      <span id="protocol-heading">Protocols</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refresh List</span>
        </button>
        <router-link :to="{ name: 'ProtocolCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-protocol"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span> Create a new Protocol </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && protocols && protocols.length === 0">
      <span>No protocols found</span>
    </div>
    <div class="table-responsive" v-if="protocols && protocols.length > 0">
      <table class="table table-striped" aria-describedby="protocols">
        <thead>
          <tr>
            <th scope="row"><span>ID</span></th>
            <th scope="row"><span>Name</span></th>
            <th scope="row"><span>Type</span></th>
            <th scope="row"><span>Description</span></th>
            <th scope="row"><span>Scope</span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="protocol in protocols" :key="protocol.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'ProtocolView', params: { protocolId: protocol.id } }">{{ protocol.id }}</router-link>
            </td>
            <td>{{ protocol.name }}</td>
            <td>{{ protocol.type }}</td>
            <td>{{ protocol.description }}</td>
            <td>{{ protocol.scope }}</td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'ProtocolView', params: { protocolId: protocol.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'ProtocolEdit', params: { protocolId: protocol.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(protocol)"
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
        ><span id="eaDesignItApp.protocol.delete.question" data-cy="protocolDeleteDialogHeading">Confirm delete operation</span></span
      >
      <div class="modal-body">
        <p id="jhi-delete-protocol-heading">Are you sure you want to delete this Protocol?</p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-protocol"
          data-cy="entityConfirmDeleteButton"
          v-on:click="removeProtocol()"
        >
          Delete
        </button>
      </div>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./protocol.component.ts"></script>
