<template>
  <div>
    <h2 id="page-heading" data-cy="CapabilityHeading">
      <span id="capability-heading">Capabilities</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refresh list</span>
        </button>
        <router-link :to="{ name: 'CapabilityCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-capability"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>Create a new Capability</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && capabilities && capabilities.length === 0">
      <span>No Capabilities found</span>
    </div>
    <div class="table-responsive" v-if="capabilities && capabilities.length > 0">
      <table class="table table-striped" aria-describedby="capabilities">
        <thead>
          <tr>
            <th scope="row"><span>ID</span></th>
            <th scope="row"><span>Name</span></th>
            <th scope="row"><span>Description</span></th>
            <th scope="row"><span>Comment</span></th>
            <th scope="row"><span>Level</span></th>
            <th scope="row"><span>Parent</span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="capability in capabilities" :key="capability.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'CapabilityView', params: { capabilityId: capability.id } }">{{ capability.id }}</router-link>
            </td>
            <td>{{ capability.name }}</td>
            <td>{{ capability.description }}</td>
            <td>{{ capability.comment }}</td>
            <td>{{ capability.level }}</td>
            <td>
              <div v-if="capability.parent">
                <router-link :to="{ name: 'CapabilityView', params: { capabilityId: capability.parent.id } }">{{
                  capability.parent.name
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'CapabilityView', params: { capabilityId: capability.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'CapabilityEdit', params: { capabilityId: capability.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(capability)"
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
        <span id="eaDesignItApp.capability.delete.question" data-cy="capabilityDeleteDialogHeading">Confirm delete operation</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-capability-heading">Are you sure you want to delete Capability {{ removeId }}?</p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-on:click="closeDialog()">Cancel</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-capability"
            data-cy="entityConfirmDeleteButton"
            v-on:click="removeCapability()"
          >
            Delete
          </button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./capability.component.ts"></script>
