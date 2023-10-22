<template>
  <div>
    <h2 id="page-heading" data-cy="LandscapeViewHeading">
      <span id="landscape-view-heading"
        ><font-awesome-icon icon="map" style="color: Tomato; font-size: 0.7em"></font-awesome-icon> Landscape Views</span
      >
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refresh list</span>
        </button>
        <router-link :to="{ name: 'LandscapeViewCreate' }" custom v-slot="{ navigate }" v-if="accountService().writeAuthorities">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-landscape-view"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>Create a new Landscape View</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && landscapeViews && landscapeViews.length === 0">
      <span>No Landscape Views found</span>
    </div>

    <div v-if="landscapeViews">
      <div>
        <b-table
          striped
          borderless
          :items="filteredRows"
          :fields="['id', 'diagramName', 'owner', { key: 'actions', label: '', tdClass: 'text-right' }]"
          class="col-12"
        >
          <!-- <template #thead-top="data">
            <b-tr>
              <b-th :colspan="data.columns - 3"></b-th>
              <b-th>
                <input type="text" placeholder="Filter Diagram Name" v-model="filterDescription" style="width: 100%" class="m-0" />
              </b-th>
              <b-th>
                <input type="text" placeholder="Filter Owner" v-model="filterDescription" style="width: 100%" class="m-0" />
              </b-th>
              <b-th class="float-right">
                <b-pagination
                  v-model="currentPage"
                  :total-rows="filteredRows.length"
                  :per-page="perPage"
                  aria-controls="my-table"
                  class="m-0"
                ></b-pagination>
              </b-th>
            </b-tr>
          </template> -->
          <template #cell(id)="data">
            <router-link :to="{ name: 'LandscapeViewView', params: { landscapeViewId: data.item.id } }">{{ data.item.id }}</router-link>
          </template>
          <template #cell(diagramName)="data">
            <router-link :to="{ name: 'LandscapeViewView', params: { landscapeViewId: data.item.id } }">{{
              data.item.diagramName
            }}</router-link>
          </template>
          <template #cell(owner)="data">
            <router-link :to="{ name: 'OwnerView', params: { ownerId: data.item.owner.id } }" v-if="data.item.owner">{{
              data.item.owner.name
            }}</router-link>
          </template>
          <template #cell(actions)="data">
            <div class="btn-group">
              <router-link :to="{ name: 'LandscapeViewView', params: { landscapeViewId: data.item.id } }" custom v-slot="{ navigate }">
                <button
                  @click="navigate"
                  class="btn btn-info btn-sm details"
                  :data-cy="'entityDetailsButton-' + data.item.diagramName"
                  data-cy2="entityDetailsButton"
                >
                  <font-awesome-icon icon="eye"></font-awesome-icon>
                  <span class="d-none d-md-inline">View</span>
                </button>
              </router-link>
              <router-link :to="{ name: 'LandscapeViewEdit', params: { landscapeViewId: data.item.id } }" custom v-slot="{ navigate }">
                <button
                  @click="navigate"
                  class="btn btn-primary btn-sm edit"
                  data-cy="entityEditButton"
                  v-if="accountService().writeAuthorities"
                >
                  <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                  <span class="d-none d-md-inline">Edit</span>
                </button>
              </router-link>
              <b-button
                v-if="accountService().deleteAuthorities"
                v-on:click="prepareRemove(data.item)"
                variant="danger"
                class="btn btn-sm"
                data-cy="entityDeleteButton"
                v-b-modal.removeEntity
              >
                <font-awesome-icon icon="times"></font-awesome-icon>
                <span class="d-none d-md-inline">Delete</span>
              </b-button>
            </div>
          </template>
        </b-table>
      </div>
    </div>
    <b-modal ref="removeEntity" id="removeEntity">
      <template #modal-title>
        <span id="eaDesignItApp.landscapeView.delete.question" data-cy="landscapeViewDeleteDialogHeading">Confirm delete operation</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-landscapeView-heading">Are you sure you want to delete this Landscape View?</p>
        <p><input type="checkbox" v-model="deleteFunctionalFlows" @change="deleteCoherence()" /> Delete unused Functional Flow</p>
        <p>
          <input type="checkbox" v-model="deleteInterfaces" @change="deleteCoherence()" :disabled="!deleteFunctionalFlows" /> Delete unused
          Interfaces
        </p>
        <p>
          <input type="checkbox" v-model="deleteDatas" @change="deleteCoherence()" :disabled="!deleteInterfaces" /> Delete unused Data Flows
          &amp; Data Flow Items
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-landscapeView"
          data-cy="entityConfirmDeleteButton"
          v-on:click="removeLandscapeView()"
        >
          Delete
        </button>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-on:click="closeDialog()">Cancel</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-landscapeView"
            data-cy="entityConfirmDeleteButton"
            v-on:click="removeLandscapeView()"
          >
            Delete
          </button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./landscape-view.component.ts"></script>
