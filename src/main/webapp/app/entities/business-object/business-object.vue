<template>
  <div>
    <h2 id="page-heading" data-cy="BusinessObjectHeading">
      <span id="business-object-heading">Business Objects</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refresh list</span>
        </button>
        <router-link :to="{ name: 'BusinessObjectCreate' }" custom v-slot="{ navigate }" v-if="accountService.writeAuthorities">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-business-object"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>Create a new Business Object</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && businessObjects && businessObjects.length === 0">
      <span>No Business Objects found</span>
    </div>
    <div class="table-responsive" v-if="businessObjects && businessObjects.length > 0">
      <table class="table table-striped" aria-describedby="businessObjects">
        <thead>
          <tr>
            <th scope="row"><span>Generalization</span></th>
            <th scope="row"><span>Business Object</span></th>
            <th scope="row"><span>Golden Sources</span></th>
            <th scope="row"><span>Replicas </span></th>
            <th scope="row"><span>Abstract</span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="businessObject in businessObjects" :key="businessObject.id" data-cy="entityTable">
            <td>
              <div v-if="businessObject.generalization">
                <router-link :to="{ name: 'BusinessObjectView', params: { businessObjectId: businessObject.generalization.id } }">{{
                  businessObject.generalization.name
                }}</router-link>
              </div>
            </td>
            <td>
              <BusinessAndDataObjectFullpath
                :objectWithParent="businessObject"
                routerView="BusinessObjectView"
                routerParamName="businessObjectId"
              />
            </td>
            <td>
              <span v-for="dataObj in businessObject.dataObjects" :key="dataObj.id">
                <div v-if="dataObj.type === 'GOLDEN_SOURCE'" class="font-weight-bold text-primary">
                  <router-link :to="{ name: 'DataObjectView', params: { dataObjectId: dataObj.id } }"
                    >{{ dataObj.name }}@{{ dataObj.application?.name }}</router-link
                  >
                </div>
              </span>
            </td>
            <td>
              <span v-for="dataObj in businessObject.dataObjects" :key="dataObj.id">
                <div v-if="dataObj.type !== 'GOLDEN_SOURCE'" class="font-weight-bold text-primary">
                  <router-link :to="{ name: 'DataObjectView', params: { dataObjectId: dataObj.id } }"
                    >{{ dataObj.name }}@{{ dataObj.application?.name }}</router-link
                  >
                </div>
              </span>
            </td>
            <td><input type="checkbox" v-model="businessObject.abstractBusinessObject" disabled="true" /></td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'BusinessObjectView', params: { businessObjectId: businessObject.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">View</span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'BusinessObjectEdit', params: { businessObjectId: businessObject.id } }"
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
                  v-on:click="prepareRemove(businessObject)"
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
        <span id="eaDesignItApp.businessObject.delete.question" data-cy="businessObjectDeleteDialogHeading">Confirm delete operation</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-businessObject-heading">Are you sure you want to delete Business Object {{ removeId }}?</p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-on:click="closeDialog()">Cancel</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-businessObject"
            data-cy="entityConfirmDeleteButton"
            v-on:click="removeBusinessObject()"
          >
            Delete
          </button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./business-object.component.ts"></script>
