<template>
  <div class="row justify-content-center">
    <div class="col-12">
      <div v-if="flowInterface">
        <h2 class="jh-entity-heading" data-cy="flowInterfaceDetailsHeading">
          <font-awesome-icon icon="grip-lines" style="color: Tomato; font-size: 0.9em"></font-awesome-icon> <span>Interface</span> -
          {{ flowInterface.alias }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span>Alias</span>
          </dt>
          <dd>
            <span>{{ flowInterface.alias }}</span>
          </dd>
          <dt>
            <span>Status</span>
          </dt>
          <dd>
            <span>{{ flowInterface.status }}</span>
          </dd>
          <dt>
            <span>Documentation URL</span>
          </dt>
          <dd>
            <span
              ><a v-bind:href="flowInterface.documentationURL">{{ flowInterface.documentationURL }}</a></span
            >
          </dd>
          <dt>
            <span>Documentation URL 2</span>
          </dt>
          <dd>
            <span
              ><a v-bind:href="flowInterface.documentationURL2">{{ flowInterface.documentationURL2 }}</a></span
            >
          </dd>
          <dt>
            <span>Description</span>
          </dt>
          <dd>
            <span>{{ flowInterface.description }}</span>
          </dd>
          <dt>
            <span>Start Date</span>
          </dt>
          <dd>
            <span>{{ flowInterface.startDate }}</span>
          </dd>
          <dt>
            <span>End Date</span>
          </dt>
          <dd>
            <span>{{ flowInterface.endDate }}</span>
          </dd>
          <dt>
            <span>Source</span>
          </dt>
          <dd>
            <div v-if="flowInterface.source">
              <router-link :to="{ name: 'ApplicationView', params: { applicationId: flowInterface.source.id } }">{{
                flowInterface.source.name
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span>Source Component</span>
          </dt>
          <dd>
            <div v-if="flowInterface.sourceComponent">
              <router-link
                :to="{ name: 'ApplicationComponentView', params: { applicationComponentId: flowInterface.sourceComponent.id } }"
                >{{ flowInterface.sourceComponent.name }}</router-link
              >
            </div>
          </dd>
          <dt>
            <span>Target</span>
          </dt>
          <dd>
            <div v-if="flowInterface.target">
              <router-link :to="{ name: 'ApplicationView', params: { applicationId: flowInterface.target.id } }">{{
                flowInterface.target.name
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span>Target Component</span>
          </dt>
          <dd>
            <div v-if="flowInterface.targetComponent">
              <router-link
                :to="{ name: 'ApplicationComponentView', params: { applicationComponentId: flowInterface.targetComponent.id } }"
                >{{ flowInterface.targetComponent.name }}</router-link
              >
            </div>
          </dd>
          <dt>
            <span>Protocol</span>
          </dt>
          <dd>
            <div v-if="flowInterface.protocol">
              <router-link :to="{ name: 'ProtocolView', params: { protocolId: flowInterface.protocol.id } }">{{
                flowInterface.protocol.name
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span>Owner</span>
          </dt>
          <dd>
            <div v-if="flowInterface.owner">
              <router-link :to="{ name: 'OwnerView', params: { ownerId: flowInterface.owner.id } }">{{
                flowInterface.owner.name
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>Back</span>
        </button>
        <router-link
          v-if="flowInterface.id"
          :to="{ name: 'FlowInterfaceEdit', params: { flowInterfaceId: flowInterface.id } }"
          custom
          v-slot="{ navigate }"
        >
          <button @click="navigate" class="btn btn-primary" v-if="accountService.writeOrContributor" :disabled="!isOwner(flowInterface)">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span> Edit</span>
          </button>
        </router-link>
        <br />
        <br />

        <!-- 
        <h3><font-awesome-icon icon="folder" style="color: Tomato; font-size: 0.9em"></font-awesome-icon> Data Flows</h3>
        <div class="table-responsive" v-if="flowInterface.dataFlows && flowInterface.dataFlows.length > 0">
          <br />
          <br />
          <table class="table table-striped" aria-describedby="dataFlows">
            <thead>
              <tr>
                <th scope="row"><span>ID</span></th>
                <th scope="row"><span>Resource Name</span></th>
                <th scope="row"><span>Resource Type</span></th>
                <th scope="row"><span>Description</span></th>
                <th scope="row"><span>Data Items</span></th>
                <th scope="row"></th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="dataFlow in flowInterface.dataFlows" :key="dataFlow.id" data-cy="entityTable">
                <td>
                  <router-link :to="{ name: 'DataFlowView', params: { dataFlowId: dataFlow.id } }">{{ dataFlow.id }}</router-link>
                </td>
                <td>{{ dataFlow.resourceName }}</td>
                <td>{{ dataFlow.resourceType }}</td>
                <td>{{ dataFlow.description }}</td>
                <td>
                  <span v-for="item in dataFlow.items" :key="item.id">
                    <router-link :to="{ name: 'DataFlowItemView', params: { dataFlowItemId: item.id } }" :title="item.resourceName">
                      {{ item.id }}
                    </router-link>
                  </span>
                </td>
                <td class="text-right">
                  <div class="btn-group">
                    <router-link :to="{ name: 'DataFlowView', params: { dataFlowId: dataFlow.id } }" custom v-slot="{ navigate }">
                      <button
                        @click="navigate"
                        class="btn btn-info btn-sm details"
                        data-cy="entityDetailsButton"
                        v-if="!accountService.writeAuthorities"
                      >
                        <font-awesome-icon icon="eye"></font-awesome-icon>
                        <span class="d-none d-md-inline">View</span>
                      </button>
                      <button
                        @click="navigate"
                        class="btn btn-primary btn-sm edit"
                        data-cy="entityEditButton"
                        v-if="accountService.writeAuthorities"
                      >
                        <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                        <span class="d-none d-md-inline">Edit</span>
                      </button>
                    </router-link>
                    <b-button
                      v-if="accountService.writeAuthorities"
                      v-on:click="prepareToDetach(dataFlow)"
                      variant="warning"
                      class="btn btn-sm"
                      data-cy="entityDeleteButton"
                      v-b-modal.removeEntity
                    >
                      <font-awesome-icon icon="times"></font-awesome-icon>
                      <span class="d-none d-md-inline">Detach</span>
                    </b-button>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
        <div class="d-flex justify-content-end">
          <span>
            <button
            class="btn btn-primary jh-create-entity create-functional-flow"
            v-if="accountService.writeAuthorities"
            @click="addNew()"
            >
              <font-awesome-icon icon="plus"></font-awesome-icon>
              <span>Add exisintg Data Flow</span>
            </button>
            <router-link :to="{ name: 'DataFlowCreate' }" custom v-slot="{ navigate }" v-if="accountService.writeAuthorities">
              <button
              @click="navigate"
              id="jh-create-entity"
              data-cy="entityCreateButton"
              class="btn btn-primary jh-create-entity create-flow-interface"
              v-if="accountService.writeAuthorities"
              >
              <font-awesome-icon icon="plus"></font-awesome-icon>
              <span> Create a new Data Flow </span>
            </button>
          </router-link>
        </span>
      </div>
    -->

        <div class="table-responsive" v-if="flowInterface.steps && flowInterface.steps.length > 0">
          <br />
          <br />
          <h3>
            <font-awesome-icon icon="project-diagram" style="color: Tomato; font-size: 0.7em"></font-awesome-icon> Functional Flows using
            interface {{ flowInterface.alias }}
          </h3>
          <table class="table table-striped" aria-describedby="functionalFlows">
            <thead>
              <tr>
                <th scope="row"><span>Functional Flow ID</span></th>
                <th scope="row"><span>Alias</span></th>
                <th scope="row"><span>Description</span></th>
                <th scope="row"><span>Landscape(s)</span></th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="step in flowInterface.steps" :key="step.id" data-cy="entityTable" :set="(functionalFlow = step.flow)">
                <td>
                  <router-link :to="{ name: 'FunctionalFlowView', params: { functionalFlowId: functionalFlow.id } }">{{
                    functionalFlow.id
                  }}</router-link>
                </td>
                <td>{{ functionalFlow.alias }}</td>
                <td>{{ functionalFlow.description }}</td>
                <td>
                  <span v-for="(landscape, i) in functionalFlow.landscapes" :key="landscape.id">
                    {{ i > 0 ? ', ' : '' }}
                    <router-link :to="{ name: 'LandscapeViewView', params: { landscapeViewId: landscape.id } }">{{
                      landscape.diagramName
                    }}</router-link>
                  </span>
                </td>
                <td></td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
    <!-- <b-modal ref="detachDataEntity" id="detachDataEntity">
      <span slot="modal-title"
        ><span id="eaDesignItApp.landscapeView.delete.question" data-cy="landscapeViewDeleteDialogHeading"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-landscapeView-heading">Are you sure you want to detach this Data Flow?</p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-on:click="closeDetachDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-landscapeView"
          data-cy="entityConfirmDeleteButton"
          v-on:click="detachDataFlow()"
        >
          Delete
        </button>
      </div>
    </b-modal> -->
  </div>
</template>

<script lang="ts" src="./flow-interface-details.component.ts"></script>
