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
    <div class="table-responsive" v-if="flowInterfaces && flowInterfaces.length > 0">
      <table class="table" aria-describedby="flowInterfaces">
        <thead>
          <tr>
            <th scope="row"><span>Alias</span></th>
            <th scope="row"><span>Flows</span></th>
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
          <tr v-for="flowInterface in flowInterfaces" :key="flowInterface.id" data-cy="entityTable" :class="flowInterface.colored">
            <td>
              <router-link :to="{ name: 'FlowInterfaceView', params: { flowInterfaceId: flowInterface.id } }">{{
                flowInterface.alias
              }}</router-link>
            </td>
            <td>
              <span v-for="(functionalFlow, i) in flowInterface.functionalFlows" :key="functionalFlow.id">
                {{ i > 0 ? ', ' : '' }}
                <router-link :to="{ name: 'FunctionalFlowView', params: { functionalFlowId: functionalFlow.id } }">{{
                  functionalFlow.alias
                }}</router-link>
              </span>
            </td>
            <td>{{ flowInterface.status }}</td>
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
                <router-link class="form-control-static" :to="{ name: 'DataFlowView', params: { dataFlowId: dataFlow.id } }"
                  >{{ dataFlow.id }}<sup v-if="dataFlow.items && dataFlow.items.length > 0">({{ dataFlow.items.length }})</sup></router-link
                >
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
          Are you sure you want to merge flows Interface between <strong>{{ interfaceToKeep.source.name }}</strong> and
          <strong>{{ interfaceToKeep.target.name }}</strong> ?
        </p>
        <p id="jhi-delete-flowInterface-heading" v-if="interfaceToKeep">
          Interface <strong>{{ interfaceToKeep.alias }}</strong> will replace <strong>{{ checkToMerge }}</strong> ?
        </p>
        <strong>Data Flow Comparison</strong>
        <div class="table-responsive">
          <table class="table">
            <thead>
              <tr>
                <th scope="row">keep</th>
                <th scope="row">merge</th>
                <th scope="row" colspan="3"><span>Interface</span></th>
                <th scope="row"><span>Flow</span></th>
                <th scope="row"><span>Data Id</span></th>
                <th scope="row"><span>Data Resource Name</span></th>
                <th scope="row"><span>Data Frequency</span></th>
                <th scope="row"><span>Data Format</span></th>
              </tr>
            </thead>
            <template v-for="(inter, i) in interfacesToMerge">
              <template v-for="(dataFlowToMerge, j) in inter.dataFlows">
                <tr :key="dataFlowToMerge.id" :class="i % 2 == 0 ? 'mycolor' : ''">
                  <td><input v-if="j == 0" type="radio" :value="inter" v-model="interfaceToKeep" @change="prepareMerge(inter)" /></td>
                  <td>
                    <input
                      v-if="j == 0"
                      :disabled="inter.alias == interfaceToKeep.alias"
                      type="checkbox"
                      :id="inter.alias"
                      :value="inter.alias"
                      v-model="checkToMerge"
                    />
                  </td>
                  <td>{{ inter.alias }}</td>
                  <td>
                    <span v-if="inter.documentationURL">
                      <a :href="inter.documentationURL" :title="inter.alias" target="_blank">{{ inter.documentationURL }}</a>
                    </span>
                  </td>
                  <td>
                    <span v-if="inter.documentationURL2">
                      <a :href="inter.documentationURL2" :title="inter.alias" target="_blank">{{ inter.documentationURL2 }}</a>
                    </span>
                  </td>
                  <td>
                    <span v-for="(flow, k) in inter.functionalFlows" :key="flow.id" :title="flow.description">
                      {{ k > 0 ? ', ' : '' }}
                      {{ flow.alias }}
                    </span>
                  </td>
                  <td>{{ dataFlowToMerge.id }}</td>
                  <td>{{ dataFlowToMerge.resourceName }}</td>
                  <td>{{ dataFlowToMerge.frequency }}</td>
                  <td>{{ dataFlowToMerge.format ? dataFlowToMerge.format.name : '' }}</td>
                  <td>{{ dataFlowToMerge.resourceType }}</td>
                  <td>
                    <span v-if="dataFlowToMerge.contractURL">
                      <a
                        :href="dataFlowToMerge.contractURL"
                        :title="'dataFlow ' + dataFlowToMerge.id + ' : ' + dataFlowToMerge.resourceName"
                        target="_blank"
                        >{{ dataFlowToMerge.contractURL }}</a
                      >
                    </span>
                  </td>
                  <td>
                    <span v-if="dataFlowToMerge.documentationURL">
                      <a :href="dataFlowToMerge.documentationURL" :title="dataFlowToMerge.resourceName" target="_blank">{{
                        dataFlowToMerge.documentationURL
                      }}</a>
                    </span>
                  </td>
                  <td>{{ dataFlowToMerge.startDate }}</td>
                  <td>{{ dataFlowToMerge.endDate }}</td>
                  <td title="dataFlow description">{{ dataFlowToMerge.description }}</td>
                </tr>
              </template>
            </template>
          </table>
        </div>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-on:click="closeMergeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="mergeflowInterfaceButtonID"
          ref="mergeflowInterfaceButtonRef"
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
