<template>
  <div>
    <h2 id="page-heading" data-cy="ApplicationCategoryHeading">
      <span id="application-category-heading">Application Categories</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refresh list</span>
        </button>
        <router-link :to="{ name: 'ApplicationCategoryCreate' }" custom v-slot="{ navigate }" v-if="accountService().writeAuthorities">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-application-category"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>Create a new Application Category</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && applicationCategories && applicationCategories.length === 0">
      <span>No Application Categories found</span>
    </div>
    <div class="table-responsive" v-if="applicationCategories && applicationCategories.length > 0">
      <table class="table table-striped" aria-describedby="applicationCategories">
        <thead>
          <tr>
            <th scope="row"><span>ID</span></th>
            <th scope="row"><span>Name</span></th>
            <th scope="row"><span>Type</span></th>
            <th scope="row"><span>Description</span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="applicationCategory in applicationCategories" :key="applicationCategory.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'ApplicationCategoryView', params: { applicationCategoryId: applicationCategory.id } }">{{
                applicationCategory.id
              }}</router-link>
            </td>
            <td>{{ applicationCategory.name }}</td>
            <td>{{ applicationCategory.type }}</td>
            <td>{{ applicationCategory.description }}</td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'ApplicationCategoryView', params: { applicationCategoryId: applicationCategory.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">View</span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'ApplicationCategoryEdit', params: { applicationCategoryId: applicationCategory.id } }"
                  custom
                  v-slot="{ navigate }"
                >
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
                  v-on:click="prepareRemove(applicationCategory)"
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
        <span id="eaDesignItApp.applicationCategory.delete.question" data-cy="applicationCategoryDeleteDialogHeading"
          >Confirm delete operation</span
        >
      </template>
      <div class="modal-body">
        <p id="jhi-delete-applicationCategory-heading">Are you sure you want to delete Application Category {{ removeId }}?</p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-on:click="closeDialog()">Cancel</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-applicationCategory"
            data-cy="entityConfirmDeleteButton"
            v-on:click="removeApplicationCategory()"
          >
            Delete
          </button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./application-category.component.ts"></script>
