<template>
  <div>
    <h2 id="page-heading" data-cy="FunctionalFlowStepHeading">
      <span id="functional-flow-step-heading">Functional Flow Steps</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refresh list</span>
        </button>
        <router-link :to="{ name: 'FunctionalFlowStepCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-functional-flow-step"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>Create a new Functional Flow Step</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && functionalFlowSteps && functionalFlowSteps.length === 0">
      <span>No Functional Flow Steps found</span>
    </div>
    <div class="table-responsive" v-if="functionalFlowSteps && functionalFlowSteps.length > 0">
      <table class="table table-striped" aria-describedby="functionalFlowSteps">
        <thead>
          <tr>
            <th scope="row"><span>ID</span></th>
            <th scope="row"><span>Description</span></th>
            <th scope="row"><span>Step Order</span></th>
            <th scope="row"><span>Flow Interface</span></th>
            <th scope="row"><span>Group</span></th>
            <th scope="row"><span>Flow</span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="functionalFlowStep in functionalFlowSteps" :key="functionalFlowStep.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'FunctionalFlowStepView', params: { functionalFlowStepId: functionalFlowStep.id } }">{{
                functionalFlowStep.id
              }}</router-link>
            </td>
            <td>{{ functionalFlowStep.description }}</td>
            <td>{{ functionalFlowStep.stepOrder }}</td>
            <td>
              <div v-if="functionalFlowStep.flowInterface">
                <router-link :to="{ name: 'FlowInterfaceView', params: { flowInterfaceId: functionalFlowStep.flowInterface.id } }">{{
                  functionalFlowStep.flowInterface.alias
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="functionalFlowStep.group">
                <router-link :to="{ name: 'FlowGroupView', params: { flowGroupId: functionalFlowStep.group.id } }">{{
                  functionalFlowStep.group.alias
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="functionalFlowStep.flow">
                <router-link :to="{ name: 'FunctionalFlowView', params: { functionalFlowId: functionalFlowStep.flow.id } }">{{
                  functionalFlowStep.flow.alias
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'FunctionalFlowStepView', params: { functionalFlowStepId: functionalFlowStep.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">View</span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'FunctionalFlowStepEdit', params: { functionalFlowStepId: functionalFlowStep.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(functionalFlowStep)"
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
        <span id="eaDesignItApp.functionalFlowStep.delete.question" data-cy="functionalFlowStepDeleteDialogHeading"
          >Confirm delete operation</span
        >
      </template>
      <div class="modal-body">
        <p id="jhi-delete-functionalFlowStep-heading">Are you sure you want to delete Functional Flow Step {{ removeId }}?</p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-on:click="closeDialog()">Cancel</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-functionalFlowStep"
            data-cy="entityConfirmDeleteButton"
            v-on:click="removeFunctionalFlowStep()"
          >
            Delete
          </button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./functional-flow-step.component.ts"></script>
