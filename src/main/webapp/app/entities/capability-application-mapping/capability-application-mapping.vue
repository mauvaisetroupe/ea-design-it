<template>
  <div>
    <h2 id="page-heading" data-cy="CapabilityApplicationMappingHeading">
      <span id="capability-application-mapping-heading">Capability Application Mappings</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refresh list</span>
        </button>
        <router-link :to="{ name: 'CapabilityApplicationMappingCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-capability-application-mapping"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>Create a new Capability Application Mapping</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && capabilityApplicationMappings && capabilityApplicationMappings.length === 0">
      <span>No Capability Application Mappings found</span>
    </div>
    <div class="table-responsive" v-if="capabilityApplicationMappings && capabilityApplicationMappings.length > 0">
      <table class="table table-striped" aria-describedby="capabilityApplicationMappings">
        <thead>
          <tr>
            <th scope="row"><span>ID</span></th>
            <th scope="row"><span>Capability</span></th>
            <th scope="row"><span>Application</span></th>
            <th scope="row"><span>Landscape</span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr
            v-for="capabilityApplicationMapping in capabilityApplicationMappings"
            :key="capabilityApplicationMapping.id"
            data-cy="entityTable"
          >
            <td>
              <router-link
                :to="{
                  name: 'CapabilityApplicationMappingView',
                  params: { capabilityApplicationMappingId: capabilityApplicationMapping.id },
                }"
                >{{ capabilityApplicationMapping.id }}</router-link
              >
            </td>
            <td>
              <div v-if="capabilityApplicationMapping.capability">
                <router-link :to="{ name: 'CapabilityView', params: { capabilityId: capabilityApplicationMapping.capability.id } }">{{
                  capabilityApplicationMapping.capability.id
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="capabilityApplicationMapping.application">
                <router-link :to="{ name: 'ApplicationView', params: { applicationId: capabilityApplicationMapping.application.id } }">{{
                  capabilityApplicationMapping.application.id
                }}</router-link>
              </div>
            </td>
            <td>
              <span v-for="(landscape, i) in capabilityApplicationMapping.landscapes" :key="landscape.id"
                >{{ i > 0 ? ', ' : '' }}
                <router-link class="form-control-static" :to="{ name: 'LandscapeViewView', params: { landscapeViewId: landscape.id } }">{{
                  landscape.diagramName
                }}</router-link>
              </span>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{
                    name: 'CapabilityApplicationMappingView',
                    params: { capabilityApplicationMappingId: capabilityApplicationMapping.id },
                  }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">View</span>
                  </button>
                </router-link>
                <router-link
                  :to="{
                    name: 'CapabilityApplicationMappingEdit',
                    params: { capabilityApplicationMappingId: capabilityApplicationMapping.id },
                  }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(capabilityApplicationMapping)"
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
        <span id="eaDesignItApp.capabilityApplicationMapping.delete.question" data-cy="capabilityApplicationMappingDeleteDialogHeading"
          >Confirm delete operation</span
        >
      </template>
      <div class="modal-body">
        <p id="jhi-delete-capabilityApplicationMapping-heading">
          Are you sure you want to delete Capability Application Mapping {{ removeId }}?
        </p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-on:click="closeDialog()">Cancel</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-capabilityApplicationMapping"
            data-cy="entityConfirmDeleteButton"
            v-on:click="removeCapabilityApplicationMapping()"
          >
            Delete
          </button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./capability-application-mapping.component.ts"></script>
