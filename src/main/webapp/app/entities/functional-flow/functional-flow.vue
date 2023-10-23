<template>
  <div>
    <h2 id="page-heading" data-cy="FunctionalFlowHeading">
      <span id="functional-flow-heading"
        ><font-awesome-icon icon="project-diagram" style="color: Tomato; font-size: 0.7em"></font-awesome-icon> Functional Flows</span
      >
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refresh list</span>
        </button>
        <router-link :to="{ name: 'FunctionalFlowCreate' }" custom v-slot="{ navigate }" v-if="accountService.writeAuthorities">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-functional-flow"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>Create a new Functional Flow</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && functionalFlows && functionalFlows.length === 0">
      <span>No Functional Flows found</span>
    </div>

    <div v-if="functionalFlows && functionalFlows.length > 0">
      <div>
        <b-table
          striped
          borderless
          :items="filteredRows"
          :fields="[
            'id',
            'alias',
            'description',
            'comment',
            'status',
            'documentationURL',
            'documentationURL2',
            'startDate',
            'endDate',
            { key: 'actions', label: '', tdClass: 'text-right' },
          ]"
          :perPage="perPage"
          :current-page="currentPage"
          class="col-12"
        >
          <template #thead-top="data">
            <b-tr>
              <b-th></b-th>
              <b-th>
                <input type="text" placeholder="Filter Alias" v-model="filterAlias" style="width: 100%" class="m-0" />
              </b-th>
              <b-th>
                <input type="text" placeholder="Filter Description" v-model="filterDescription" style="width: 100%" class="m-0" />
              </b-th>
              <b-th :colspan="data.columns - 4"></b-th>
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
          </template>

          <template #cell(id)="data">
            <router-link :to="{ name: 'FunctionalFlowView', params: { functionalFlowId: data.item.id } }">{{ data.item.id }}</router-link>
          </template>

          <template #cell(alias)="data">
            <router-link :to="{ name: 'FunctionalFlowView', params: { functionalFlowId: data.item.id } }">{{
              data.item.alias
            }}</router-link>
          </template>

          <template #cell(documentationURL)="data">
            <a :href="data.item.documentationURL">{{ data.item.documentationURL ? data.item.documentationURL.substring(0, 20) : '' }}</a>
          </template>

          <template #cell(documentationURL2)="data">
            <a :href="data.item.documentationURL2">{{ data.item.documentationURL2 ? data.item.documentationURL2.substring(0, 20) : '' }}</a>
          </template>

          <template #cell(actions)="data">
            <div class="btn-group">
              <router-link :to="{ name: 'FunctionalFlowView', params: { functionalFlowId: data.item.id } }" custom v-slot="{ navigate }">
                <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                  <font-awesome-icon icon="eye"></font-awesome-icon>
                  <span class="d-none d-md-inline">View</span>
                </button>
              </router-link>
              <router-link
                v-if="accountService.writeAuthorities"
                :to="{ name: 'FunctionalFlowEdit', params: { functionalFlowId: data.item.id } }"
                custom
                v-slot="{ navigate }"
              >
                <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
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
                :disabled="data.item.landscapes && data.item.landscapes.length > 0"
                :title="
                  !data.item.landscapes || data.item.landscapes.length == 0
                    ? ''
                    : 'Cannot be deleted, please detache from all landscapes first'
                "
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
        <span id="eaDesignItApp.functionalFlow.delete.question" data-cy="functionalFlowDeleteDialogHeading">Confirm delete operation</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-functionalFlow-heading">Are you sure you want to delete this Functional Flow?</p>
        <p><input type="checkbox" v-model="deleteInterfaces" @change="deleteCoherence()" /> Delete unused Interfaces</p>
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
          id="jhi-confirm-delete-functionalFlow"
          data-cy="entityConfirmDeleteButton"
          v-on:click="removeFunctionalFlow()"
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
            id="jhi-confirm-delete-functionalFlow"
            data-cy="entityConfirmDeleteButton"
            v-on:click="removeFunctionalFlow()"
          >
            Delete
          </button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./functional-flow.component.ts"></script>
