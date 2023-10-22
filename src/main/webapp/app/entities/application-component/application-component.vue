<template>
  <div>
    <h2 id="page-heading" data-cy="ApplicationComponentHeading">
      <span id="application-component-heading">Application Components</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refresh list</span>
        </button>
        <router-link :to="{ name: 'ApplicationComponentCreate' }" custom v-slot="{ navigate }" v-if="accountService().writeAuthorities">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-application-component"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>Create a new Application Component</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && applicationComponents && applicationComponents.length === 0">
      <span>No Application Components found</span>
    </div>

    <b-table
      striped
      borderless
      v-if="applicationComponents"
      :items="applicationComponents"
      :fields="fields"
      sort-icon-left
      responsive
      data-cy="entityTable"
      :perPage="perPage"
      :current-page="currentPage"
      :filter="filter"
      :filter-included-fields="['alias', 'name', 'description', 'application']"
    >
      <template #thead-top="data">
        <b-tr>
          <b-th colspan="2">
            <input type="text" placeholder="Filter text" v-model="filter" style="width: 100%" class="m-0" />
          </b-th>
          <b-th :colspan="data.columns - 3"></b-th>
          <b-th class="float-right">
            <b-pagination
              v-model="currentPage"
              :total-rows="applicationComponents.length"
              :per-page="perPage"
              aria-controls="my-table"
              class="m-0"
            ></b-pagination>
          </b-th>
        </b-tr>
      </template>

      <template #cell(id)="PP">
        <router-link :to="{ name: 'ApplicationComponentView', params: { applicationComponentId: PP.item.id } }">{{
          PP.item.id
        }}</router-link>
      </template>

      <template #cell(alias)="PP">
        <router-link :to="{ name: 'ApplicationComponentView', params: { applicationComponentId: PP.item.id } }">{{
          PP.item.alias
        }}</router-link>
      </template>

      <template #cell(application)="PP">
        <router-link :to="{ name: 'ApplicationView', params: { applicationId: PP.item.application.id } }">{{
          PP.item.application.name
        }}</router-link>
      </template>

      <template #cell(technologies)="PP">
        <span v-for="(technologies, i) in PP.item.technologies" :key="technologies.id"
          >{{ i > 0 ? ', ' : '' }}
          <router-link class="form-control-static" :to="{ name: 'TechnologyView', params: { technologyId: technologies.id } }">{{
            technologies.name
          }}</router-link>
        </span>
      </template>

      <template #cell(categories)="PP">
        <span v-for="(categories, i) in PP.item.categories" :key="categories.id"
          >{{ i > 0 ? ', ' : '' }}
          <router-link
            class="form-control-static"
            :to="{ name: 'ApplicationCategoryView', params: { applicationCategoryId: categories.id } }"
            >{{ categories.name }}</router-link
          >
        </span>
      </template>

      <template #cell(actions)="data">
        <div class="btn-group">
          <router-link
            :to="{ name: 'ApplicationComponentView', params: { applicationComponentId: data.item.id } }"
            custom
            v-slot="{ navigate }"
          >
            <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
              <font-awesome-icon icon="eye"></font-awesome-icon>
              <span class="d-none d-md-inline">View</span>
            </button>
          </router-link>
          <router-link
            :to="{ name: 'ApplicationComponentEdit', params: { applicationComponentId: data.item.id } }"
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
    <b-modal ref="removeEntity" id="removeEntity">
      <template #modal-title>
        <span id="eaDesignItApp.applicationComponent.delete.question" data-cy="applicationComponentDeleteDialogHeading"
          >Confirm delete operation</span
        >
      </template>
      <div class="modal-body">
        <p id="jhi-delete-applicationComponent-heading">Are you sure you want to delete Application Component {{ removeId }}?</p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-on:click="closeDialog()">Cancel</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-applicationComponent"
            data-cy="entityConfirmDeleteButton"
            v-on:click="removeApplicationComponent()"
          >
            Delete
          </button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./application-component.component.ts"></script>
