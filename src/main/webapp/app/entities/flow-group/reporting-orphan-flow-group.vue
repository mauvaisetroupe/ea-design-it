<template>
  <div>
    <h2 id="page-heading" data-cy="FlowGroupHeading">
      <span id="flow-group-heading">Flow Groups</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refresh List</span>
        </button>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && flowGroups && flowGroups.length === 0">
      <span>No flowGroups found</span>
    </div>
    <div class="table-responsive" v-if="flowGroups && flowGroups.length > 0">
      <table class="table table-striped" aria-describedby="flowGroups">
        <thead>
          <tr>
            <th scope="row"><span>ID</span></th>
            <th scope="row"><span>Used in Functional Flow</span></th>
            <th scope="row"><span>Used in Landscapes</span></th>
            <th scope="row"><span>Title</span></th>
            <th scope="row"><span>Url</span></th>
            <th scope="row"><span>Description</span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="flowGroup in flowGroups" :key="flowGroup.id" data-cy="entityTable">
            <td>{{ flowGroup.id }}</td>
            <td>
              <router-link :to="{ name: 'FunctionalFlowView', params: { functionalFlowId: flowGroup.steps[0].flow.id } }">
                {{ flowGroup.steps[0].flow.alias }} {{ flowGroup.steps[0].flow.description }}
              </router-link>
            </td>
            <td>
              <span v-for="landscape in flowGroup.steps[0].flow.landscapes">
                {{ landscape.diagramName }}
              </span>
            </td>
            <td>{{ flowGroup.title }}</td>
            <td>{{ flowGroup.url }}</td>
            <td>{{ flowGroup.description }}</td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'FlowGroupEdit', params: { flowGroupId: flowGroup.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">Edit</span>
                  </button>
                </router-link>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script lang="ts" src="./reporting-orphan-flow-group.components"></script>
