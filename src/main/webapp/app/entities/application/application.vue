<template>
  <div>
    <h2 id="page-heading" data-cy="ApplicationHeading">
      <span id="application-heading">Applications</span>
      <div class="d-flex justify-content-end">
        <router-link :to="{ name: 'ApplicationsDiagram', query: { id: selectedApplicationIds } }" v-if="selectedApplicationIds.length >= 2">
          <button class="btn btn-warning mr-2" title="Select applications to generate diagram">
            <font-awesome-icon icon="plus"></font-awesome-icon><span> Generate Diagram</span>
          </button>
        </router-link>

        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refresh List</span>
        </button>
        <router-link :to="{ name: 'ApplicationCreate' }" custom v-slot="{ navigate }">
          <button
            v-if="accountService().writeAuthorities"
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-application"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span> Create a new Application </span>
          </button>
        </router-link>
        &nbsp;
        <button class="btn btn-success mr-2" v-on:click="exportExcel()" :disabled="isFetching">
          <font-awesome-icon icon="file-excel"></font-awesome-icon> <span>&nbsp;Export Excel</span>
        </button>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && applications && applications.length === 0">
      <span>No applications found</span>
    </div>

    <div>
      <input type="text" placeholder="Filter by text" v-model="filter" />
    </div>

    <div class="table-responsive" v-if="applications && applications.length > 0">
      <table class="table table-striped" aria-describedby="applications">
        <thead>
          <tr>
            <th></th>
            <th scope="row"><span>ID</span></th>
            <th scope="row"><span>Alias</span></th>
            <th scope="row"><span>Name</span></th>
            <th scope="row"><span>Description</span></th>
            <th scope="row"><span>Comment</span></th>
            <th scope="row"><span>Documentation URL</span></th>
            <th scope="row"><span>Start Date</span></th>
            <th scope="row"><span>End Date</span></th>
            <th scope="row"><span>Application Type</span></th>
            <th scope="row"><span>Software Type</span></th>
            <th scope="row"><span>Owner</span></th>
            <th scope="row"><span>It Owner</span></th>
            <th scope="row"><span>Business Owner</span></th>
            <th scope="row"><span>Categories</span></th>
            <th scope="row"><span>Technologies</span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="application in filteredRows" :key="application.id" data-cy="entityTable">
            <td>
              <input
                type="checkbox"
                name="selectedAppliation"
                :id="application.id"
                :value="application.id"
                v-model="selectedApplicationIds"
              />
            </td>
            <td>
              <router-link :to="{ name: 'ApplicationView', params: { applicationId: application.id } }">{{ application.id }}</router-link>
            </td>
            <td>{{ application.alias }}</td>
            <td>{{ application.name }}</td>
            <td>{{ application.description }}</td>
            <td>{{ application.comment }}</td>
            <td>
              <a v-bind:href="application.documentationURL"> {{ application.documentationURL }}</a>
            </td>
            <td>{{ application.startDate }}</td>
            <td>{{ application.endDate }}</td>
            <td>{{ application.applicationType }}</td>
            <td>{{ application.softwareType }}</td>
            <td>
              <div v-if="application.owner">
                <router-link :to="{ name: 'OwnerView', params: { ownerId: application.owner.id } }">{{
                  application.owner.name
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="application.itOwner">
                <router-link :to="{ name: 'OwnerView', params: { ownerId: application.itOwner.id } }">{{
                  application.itOwner.name
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="application.businessOwner">
                <router-link :to="{ name: 'OwnerView', params: { ownerId: application.businessOwner.id } }">{{
                  application.businessOwner.name
                }}</router-link>
              </div>
            </td>
            <td>
              <span v-for="(categories, i) in application.categories" :key="categories.id"
                >{{ i > 0 ? ', ' : '' }}
                <router-link
                  class="form-control-static"
                  :to="{ name: 'ApplicationCategoryView', params: { applicationCategoryId: categories.id } }"
                  >{{ categories.name }}</router-link
                >
              </span>
            </td>
            <td>
              <span v-for="(technologies, i) in application.technologies" :key="technologies.id"
                >{{ i > 0 ? ', ' : '' }}
                <router-link class="form-control-static" :to="{ name: 'TechnologyView', params: { technologyId: technologies.id } }">{{
                  technologies.name
                }}</router-link>
              </span>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'ApplicationView', params: { applicationId: application.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">View</span>
                  </button>
                </router-link>
                <router-link
                  v-if="accountService().writeOrContributor"
                  :to="{ name: 'ApplicationEdit', params: { applicationId: application.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button
                    @click="navigate"
                    class="btn btn-primary btn-sm edit"
                    data-cy="entityEditButton"
                    :disabled="!isOwner(application)"
                  >
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-if="accountService().deleteAuthorities"
                  v-on:click="prepareRemove(application)"
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
        ><span id="eaDesignItApp.application.delete.question" data-cy="applicationDeleteDialogHeading">Confirm delete operation</span></span
      >
      <div class="modal-body">
        <p id="jhi-delete-application-heading">Are you sure you want to delete this Application?</p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-application"
          data-cy="entityConfirmDeleteButton"
          v-on:click="removeApplication()"
        >
          Delete
        </button>
      </div>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./application.component.ts"></script>
