<template>
  <div>
    <h2 id="page-heading" data-cy="ApplicationHeading">
      <span id="application-heading">Applications and Actors</span>
      <div class="d-flex justify-content-end">
        <router-link :to="{ name: 'ApplicationsDiagram', query: { id: selectedApplicationIds } }" v-if="selectedApplicationIds.length >= 2">
          <button class="btn btn-warning mr-2" title="Select applications to generate diagram">
            <font-awesome-icon icon="plus"></font-awesome-icon><span> Generate Diagram</span>
          </button>
        </router-link>

        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching" data-cy="entityListRefreshButton">
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

    <div class="border p-2 m-1" v-if="applications && applications.length > 0">
      <input type="text" placeholder="Filter by text" v-model="filter" />
      <br />
      <a @click="showAdvanced = !showAdvanced" href="javascript:" class="text-decoration-none"
        ><span class="small">Advance Filters <font-awesome-icon icon="angle-down"></font-awesome-icon></span
      ></a>
      <div v-show="showAdvanced" class="p-2 m-1">
        <div class="row p-2">
          <div class="col-2">Text filter on :</div>
          <div>
            <div class="form-check form-check-inline">
              <input class="form-check-input" type="checkbox" id="alias" value="alias" v-model="filterOn" />
              <label class="form-check-label" for="alias">alias</label>
            </div>

            <div class="form-check form-check-inline">
              <input class="form-check-input" type="checkbox" id="name" value="name" v-model="filterOn" />
              <label class="form-check-label" for="name">name</label>
            </div>

            <div class="form-check form-check-inline">
              <input class="form-check-input" type="checkbox" id="description" value="description" v-model="filterOn" />
              <label class="form-check-label" for="description">description</label>
            </div>

            <div class="form-check form-check-inline">
              <input class="form-check-input" type="checkbox" id="categories" value="categories" v-model="filterOn" />
              <label class="form-check-label" for="categories">categories</label>
            </div>

            <div class="form-check form-check-inline">
              <input class="form-check-input" type="checkbox" id="technologies" value="technologies" v-model="filterOn" />
              <label class="form-check-label" for="technologies">technologies</label>
            </div>

            <div class="form-check form-check-inline">
              <input class="form-check-input" type="checkbox" id="owner" value="owner" v-model="filterOn" />
              <label class="form-check-label" for="owner">owner</label>
            </div>

            <div class="form-check form-check-inline">
              <input class="form-check-input" type="checkbox" id="itOwner" value="itOwner" v-model="filterOn" />
              <label class="form-check-label" for="itOwner">IT Owner</label>
            </div>

            <div class="form-check form-check-inline">
              <input class="form-check-input" type="checkbox" id="businessOwner" value="businessOwner" v-model="filterOn" />
              <label class="form-check-label" for="businessOwner">Business Owner</label>
            </div>
          </div>
        </div>
        <div class="row p-2">
          <div class="col-2">Application type:</div>
          <div class="col-3">
            <select class="form-control form-control-sm" v-model="applicationTypeSelected">
              <option :value="undefined">--</option>
              <option :value="option" v-for="option in applicationTypeValues" :key="option">{{ option }}</option>
            </select>
          </div>
        </div>
        <div class="row p-2">
          <div class="col-2">Software type:</div>
          <div class="col-3">
            <select class="form-control form-control-sm" v-model="softwareTypeSelected">
              <option :value="undefined">--</option>
              <option :value="option" v-for="option in softwareTypeValues" :key="option">{{ option }}</option>
            </select>
          </div>
        </div>
      </div>
    </div>

    <br />

    <b-table
      striped
      v-if="applications && applications.length > 0"
      :items="filteredApplications"
      :fields="fields"
      :sort-by.sync="sortBy"
      :sort-desc.sync="sortDesc"
      sort-icon-left
      responsive
      :filter="filter"
      :filter-included-fields="filterOn"
      data-cy="entityTable"
    >
      <!-- <template #thead-top="data">
        <TR>
          <td colspan="5">
            <div>
              <input type="text" placeholder="Filter by text" v-model="filter" />
              <input type="checkbox" id="alias" value="alias" v-model="filterOn">
              <label for="alias">alias</label>
              <input type="checkbox" id="name" value="name" v-model="filterOn">
              <label for="name">name</label>
              <input type="checkbox" id="description" value="description" v-model="filterOn">
              <label for="description">description</label>
            </div>
          </td>
          <td>
            <select class="form-control form-control-sm" v-model="applicationTypeSelected" >
              <option :value="undefined">--</option>
              <option :value="option" v-for="option in applicationTypeValues" :key="option">{{ option }}</option> 
            </select>
          </td>
          <td>
            <select class="form-control form-control-sm" v-model="softwareTypeSelected" >
              <option :value="undefined">--</option>
              <option :value="option" v-for="option in softwareTypeValues" :key="option">{{ option }}</option> 
            </select>
          </td>
          <td></td>
          <td></td>
          <td></td>
          <td></td>
          <td></td>
          <td></td>
          <td></td>
        </TR>
      </template> -->

      <template #cell(CHECKBOX)="row">
        <input type="checkbox" name="selectedAppliation" :id="row.item.id" :value="row.item.id" v-model="selectedApplicationIds" />
      </template>

      <template #cell(id)="PP">
        <router-link :to="{ name: 'ApplicationView', params: { applicationId: PP.item.id } }">{{ PP.item.id }}</router-link>
      </template>

      <template #cell(alias)="PP">
        <router-link :to="{ name: 'ApplicationView', params: { applicationId: PP.item.id } }">{{ PP.item.alias }}</router-link>
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
    </b-table>
  </div>
</template>

<script lang="ts" src="./application.component.ts"></script>
