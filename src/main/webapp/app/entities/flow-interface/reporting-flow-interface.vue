<template>
  <div>
    <h2 id="page-heading" data-cy="FlowInterfaceHeading">
      <span id="flow-interface-heading">Flow Interfaces - Possible duplicates</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refresh List</span>
        </button>
        <router-link :to="{ name: 'FlowInterfaceCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-flow-interface"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span> Create a new Flow Interface </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && flowInterfaces && flowInterfaces.length === 0">
      <span>No flowInterfaces found</span>
    </div>

    <div>
      <input type="text" placeholder="Filter by text" v-model="filter" />
    </div>

    <div class="table-responsive" v-if="flowInterfaces && flowInterfaces.length > 0">
      <table class="table" aria-describedby="flowInterfaces">
        <thead>
          <tr>
            <th scope="row"><span>ID</span></th>
            <th scope="row"><span>Alias</span></th>
            <th scope="row"><span>Status</span></th>
            <th scope="row"><span>Documentation URL</span></th>
            <th scope="row"><span>Documentation URL 2</span></th>
            <th scope="row"><span>Start Date</span></th>
            <th scope="row"><span>End Date</span></th>
            <th scope="row"><span>Source</span></th>
            <th scope="row"><span>Target</span></th>
            <th scope="row"><span>Source Component</span></th>
            <th scope="row"><span>Target Component</span></th>
            <th scope="row"><span>Protocol</span></th>
            <th scope="row"><span>Owner</span></th>
            <th scope="row"><span>Data Flow</span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="flowInterface in filteredRows" :key="flowInterface.id" data-cy="entityTable" :class="flowInterface.colored">
            <td>
              <router-link :to="{ name: 'FlowInterfaceView', params: { flowInterfaceId: flowInterface.id } }">{{
                flowInterface.id
              }}</router-link>
            </td>
            <td>{{ flowInterface.alias }}</td>
            <td>{{ flowInterface.status }} {{ flowInterface.toto }}</td>
            <td>
              <a v-bind:href="flowInterface.documentationURL">{{ flowInterface.documentationURL }}</a>
            </td>
            <td>
              <a v-bind:href="flowInterface.documentationURL2">{{ flowInterface.documentationURL2 }}</a>
            </td>
            <td>{{ flowInterface.startDate }}</td>
            <td>{{ flowInterface.endDate }}</td>
            <td>
              <div v-if="flowInterface.source">
                <router-link :to="{ name: 'ApplicationView', params: { applicationId: flowInterface.source.id } }">{{
                  flowInterface.source.name
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="flowInterface.target">
                <router-link :to="{ name: 'ApplicationView', params: { applicationId: flowInterface.target.id } }">{{
                  flowInterface.target.name
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="flowInterface.sourceComponent">
                <router-link
                  :to="{ name: 'ApplicationComponentView', params: { applicationComponentId: flowInterface.sourceComponent.id } }"
                  >{{ flowInterface.sourceComponent.name }}</router-link
                >
              </div>
            </td>
            <td>
              <div v-if="flowInterface.targetComponent">
                <router-link
                  :to="{ name: 'ApplicationComponentView', params: { applicationComponentId: flowInterface.targetComponent.id } }"
                  >{{ flowInterface.targetComponent.name }}</router-link
                >
              </div>
            </td>
            <td>
              <div v-if="flowInterface.protocol">
                <router-link :to="{ name: 'ProtocolView', params: { protocolId: flowInterface.protocol.id } }">{{
                  flowInterface.protocol.name
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="flowInterface.owner">
                <router-link :to="{ name: 'OwnerView', params: { ownerId: flowInterface.owner.id } }">{{
                  flowInterface.owner.name
                }}</router-link>
              </div>
            </td>
            <td>
              <span v-for="(dataFlow, i) in flowInterface.dataFlows" :key="dataFlow.id"
                >{{ i > 0 ? ', ' : '' }}
                <router-link class="form-control-static" :to="{ name: 'DataFlowView', params: { dataFlowId: dataFlow.id } }">{{
                  dataFlow.id
                }}</router-link>
              </span>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <b-button
                  v-on:click="prepareMerge(flowInterface)"
                  variant="warning"
                  class="btn btn-sm"
                  data-cy="entityDeleteButton"
                  v-b-modal.mergeEntity
                >
                  <font-awesome-icon icon="asterisk"></font-awesome-icon>
                  <span class="d-none d-md-inline">Merge</span>
                </b-button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <b-modal ref="mergeEntity" id="mergeEntity" class="mymodalclass">
      <span slot="modal-title"
        ><span id="eaDesignItApp.flowInterface.delete.question" data-cy="flowInterfaceDeleteDialogHeading"
          >Confirm merge operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-flowInterface-heading" v-if="interfaceToKeep">
          Are you sure you want to merge flows Interface {{ checkToMerge }} between <strong>{{ interfaceToKeep.source.name }}</strong> and
          <strong>{{ interfaceToKeep.target.name }}</strong> ?
        </p>
        <strong>Data Flow Comparison</strong>
        <div class="table-responsive">
          <table class="table">
            <thead>
              <tr>
                <th></th>
                <th scope="row"><span>Inter.</span></th>
                <th scope="row"><span>ID</span></th>
                <th scope="row"><span>Resource Name</span></th>
                <th scope="row"><span>Frequency</span></th>
                <th scope="row"><span>Format</span></th>
              </tr>
            </thead>
            <tr v-for="dataFlowToMerge in dataFlowsToMerge" :key="dataFlowToMerge.id">
              <td>
                <input
                  v-if="dataFlowToMerge.flowInterface.alias != interfaceToKeep.alias"
                  type="checkbox"
                  :id="dataFlowToMerge.flowInterface.alias"
                  :value="dataFlowToMerge.flowInterface.alias"
                  v-model="checkToMerge"
                />
              </td>
              <td>{{ dataFlowToMerge.flowInterface.alias }}</td>
              <td>{{ dataFlowToMerge.id }}</td>
              <td>{{ dataFlowToMerge.resourceName }}</td>
              <td>{{ dataFlowToMerge.frequency }}</td>
              <td>{{ dataFlowToMerge.format ? dataFlowToMerge.format.name : '' }}</td>
              <td>{{ dataFlowToMerge.resourceType }}</td>
              <td>{{ dataFlowToMerge.contractURL | truncate 50 }}</td>
              <td>{{ dataFlowToMerge.documentationURL | truncate 50 }}</td>
              <td>{{ dataFlowToMerge.documentationURL2 | truncate 50 }}</td>
              <td>{{ dataFlowToMerge.startDate }}</td>
              <td>{{ dataFlowToMerge.endDate }}</td>
              <td>{{ dataFlowToMerge.description }}</td>
              <td>Done</td>
            </tr>
          </table>
        </div>

        <p id="jhi-delete-flowInterface-heading" v-if="interfaceToKeep">
          Interface <strong>{{ interfaceToKeep.alias }}</strong> will replace all others?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-on:click="closeMergeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-flowInterface"
          data-cy="entityConfirmDeleteButton"
          v-on:click="mergeFlowInterface()"
        >
          Merge
        </button>
      </div>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./reporting-flow-interface.components.ts"></script>

<style>
.mycolor {
  background-color: rgba(0, 0, 0, 0.1);
}
.mymodalclass {
  max-width: 1000px;
}
.modal-dialog {
  max-width: 80%;
}
</style>
