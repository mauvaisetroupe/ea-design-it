<template>
  <div>
    <h2 id="page-heading" data-cy="FunctionalFlowHeading">
      <span id="functional-flow-heading"
        ><font-awesome-icon icon="project-diagram" style="color: Tomato; font-size: 0.7em"></font-awesome-icon> Functional Flows</span
      >
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refresh List</span>
        </button>
        <router-link :to="{ name: 'FunctionalFlowCreate' }" custom v-slot="{ navigate }" v-if="accountService().writeAuthorities">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-functional-flow"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span> Create a new Functional Flow </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && functionalFlows && functionalFlows.length === 0">
      <span>No functionalFlows found</span>
    </div>

    <div>
      <input type="text" placeholder="Filter by text" v-model="filter" />
    </div>

    <div class="table-responsive" v-if="functionalFlows && functionalFlows.length > 0">
      <table class="table table-striped" aria-describedby="functionalFlows">
        <thead>
          <tr>
            <th scope="row"><span>ID</span></th>
            <th scope="row"><span>Alias</span></th>
            <th scope="row"><span>Description</span></th>
            <th scope="row"><span>Comment</span></th>
            <th scope="row"><span>Status</span></th>
            <th scope="row"><span>Documentation URL</span></th>
            <th scope="row"><span>Documentation URL 2</span></th>
            <th scope="row"><span>Start Date</span></th>
            <th scope="row"><span>End Date</span></th>
            <th scope="row"><span>Interfaces</span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="functionalFlow in filteredRows" :key="functionalFlow.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'FunctionalFlowView', params: { functionalFlowId: functionalFlow.id } }">{{
                functionalFlow.id
              }}</router-link>
            </td>
            <td>{{ functionalFlow.alias }}</td>
            <td>{{ functionalFlow.description }}</td>
            <td>{{ functionalFlow.comment ? functionalFlow.comment.substring(0, 30) : '' }}</td>
            <td>{{ functionalFlow.status }}</td>
            <td>
              <a :href="functionalFlow.documentationURL">{{
                functionalFlow.documentationURL ? functionalFlow.documentationURL.substring(0, 20) : ''
              }}</a>
            </td>
            <td>
              <a :href="functionalFlow.documentationURL2">{{
                functionalFlow.documentationURL2 ? functionalFlow.documentationURL2.substring(0, 20) : ''
              }}</a>
            </td>
            <td>{{ functionalFlow.startDate }}</td>
            <td>{{ functionalFlow.endDate }}</td>
            <td>
              <span v-for="(interfaces, i) in functionalFlow.interfaces" :key="interfaces.id"
                >{{ i > 0 ? ', ' : '' }}
                <router-link
                  :title="
                    '[ ' +
                    interfaces.source.name +
                    ' / ' +
                    interfaces.target.name +
                    ' ]' +
                    (interfaces.protocol ? ' (' + interfaces.protocol.type + ') ' : '')
                  "
                  class="form-control-static"
                  :to="{ name: 'FlowInterfaceView', params: { flowInterfaceId: interfaces.id } }"
                  >{{ interfaces.alias }}</router-link
                >
              </span>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'FunctionalFlowView', params: { functionalFlowId: functionalFlow.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">View</span>
                  </button>
                </router-link>
                <router-link
                  v-if="accountService().writeAuthorities"
                  :to="{ name: 'FunctionalFlowEdit', params: { functionalFlowId: functionalFlow.id } }"
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
                  v-on:click="prepareRemove(functionalFlow)"
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
        ><span id="eaDesignItApp.functionalFlow.delete.question" data-cy="functionalFlowDeleteDialogHeading"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-functionalFlow-heading">Are you sure you want to delete this Functional Flow?</p>
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
    </b-modal>
  </div>
</template>

<script lang="ts" src="./functional-flow.component.ts"></script>
