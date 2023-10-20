<template>
  <div>
    <h2 id="page-heading" data-cy="OwnerHeading">
      <span id="owner-heading">Owners</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refresh list</span>
        </button>
        <router-link :to="{ name: 'OwnerCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-owner"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>Create a new Owner</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && owners && owners.length === 0">
      <span>No Owners found</span>
    </div>
    <div class="table-responsive" v-if="owners && owners.length > 0">
      <table class="table table-striped" aria-describedby="owners">
        <thead>
          <tr>
            <th scope="row"><span>ID</span></th>
            <th scope="row"><span>Name</span></th>
            <th scope="row"><span>Firstname</span></th>
            <th scope="row"><span>Lastname</span></th>
            <th scope="row"><span>Email</span></th>
            <th scope="row"><span>Users</span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="owner in owners" :key="owner.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'OwnerView', params: { ownerId: owner.id } }">{{ owner.id }}</router-link>
            </td>
            <td>{{ owner.name }}</td>
            <td>{{ owner.firstname }}</td>
            <td>{{ owner.lastname }}</td>
            <td>{{ owner.email }}</td>
            <td>
              <span v-for="(users, i) in owner.users" :key="users.id"
                >{{ i > 0 ? ', ' : '' }}
                {{ users.login }}
              </span>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'OwnerView', params: { ownerId: owner.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'OwnerEdit', params: { ownerId: owner.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(owner)"
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
        <span id="eaDesignItApp.owner.delete.question" data-cy="ownerDeleteDialogHeading">Confirm delete operation</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-owner-heading">Are you sure you want to delete Owner {{ removeId }}?</p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-on:click="closeDialog()">Cancel</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-owner"
            data-cy="entityConfirmDeleteButton"
            v-on:click="removeOwner()"
          >
            Delete
          </button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./owner.component.ts"></script>
