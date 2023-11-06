<template>
  <div>
    <h2 id="page-heading" data-cy="DataObjectHeading">
      <span id="data-object-heading">Data Objects</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refresh list</span>
        </button>
        <router-link :to="{ name: 'DataObjectCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-data-object"
            v-if="accountService.writeAuthorities"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>Create a new Data Object</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && dataObjects && dataObjects.length === 0">
      <span>No Data Objects found</span>
    </div>
    <div class="table-responsive" v-if="dataObjects && dataObjects.length > 0">
      <table class="table table-striped" aria-describedby="dataObjects">
        <thead>
          <tr>
            <th scope="row"><span>ID</span></th>
            <th scope="row"><span>Name</span></th>
            <th scope="row"><span>Type</span></th>
            <th scope="row"><span>Application</span></th>
            <th scope="row"><span>Owner</span></th>
            <th scope="row"><span>Technologies</span></th>
            <th scope="row"><span>Landscapes</span></th>
            <th scope="row"><span>Parent</span></th>
            <th scope="row"><span>Business Object</span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="dataObject in dataObjects" :key="dataObject.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'DataObjectView', params: { dataObjectId: dataObject.id } }">{{ dataObject.id }}</router-link>
            </td>
            <td>{{ dataObject.name }}</td>
            <td>{{ dataObject.type }}</td>
            <td>
              <div v-if="dataObject.application">
                <router-link :to="{ name: 'ApplicationView', params: { applicationId: dataObject.application.id } }">{{
                  dataObject.application.name
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="dataObject.owner">
                <router-link :to="{ name: 'OwnerView', params: { ownerId: dataObject.owner.id } }">{{ dataObject.owner.name }}</router-link>
              </div>
            </td>
            <td>
              <span v-for="(technologies, i) in dataObject.technologies" :key="technologies.id"
                >{{ i > 0 ? ', ' : '' }}
                <router-link class="form-control-static" :to="{ name: 'TechnologyView', params: { technologyId: technologies.id } }">{{
                  technologies.name
                }}</router-link>
              </span>
            </td>
            <td>
              <span v-for="(landscapes, i) in dataObject.landscapes" :key="landscapes.id"
                >{{ i > 0 ? ', ' : '' }}
                <router-link class="form-control-static" :to="{ name: 'LandscapeViewView', params: { landscapeViewId: landscapes.id } }">{{
                  landscapes.diagramName
                }}</router-link>
              </span>
            </td>
            <td>
              <div v-if="dataObject.parent">
                <router-link :to="{ name: 'DataObjectView', params: { dataObjectId: dataObject.parent.id } }">{{
                  dataObject.parent.name
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="dataObject.businessObject">
                <router-link :to="{ name: 'BusinessObjectView', params: { businessObjectId: dataObject.businessObject.id } }">{{
                  dataObject.businessObject.name
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'DataObjectView', params: { dataObjectId: dataObject.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">View</span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'DataObjectEdit', params: { dataObjectId: dataObject.id } }"
                  custom
                  v-slot="{ navigate }"
                  v-if="accountService.writeAuthorities"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(dataObject)"
                  variant="danger"
                  class="btn btn-sm"
                  data-cy="entityDeleteButton"
                  v-b-modal.removeEntity
                  v-if="accountService.writeAuthorities"
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
        <span id="eaDesignItApp.dataObject.delete.question" data-cy="dataObjectDeleteDialogHeading">Confirm delete operation</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-dataObject-heading">Are you sure you want to delete Data Object {{ removeId }}?</p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-on:click="closeDialog()">Cancel</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-dataObject"
            data-cy="entityConfirmDeleteButton"
            v-on:click="removeDataObject()"
          >
            Delete
          </button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./data-object.component.ts"></script>
