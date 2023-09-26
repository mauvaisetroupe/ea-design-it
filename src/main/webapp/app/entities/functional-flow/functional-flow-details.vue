<template>
  <div>
    <div class="row">
      <div v-if="functionalFlow" class="col-10">
        <h2 class="jh-entity-heading" data-cy="functionalFlowDetailsHeading">
          <font-awesome-icon icon="project-diagram" style="color: Tomato; font-size: 0.7em"></font-awesome-icon>
          <span>Functional Flow</span> - {{ functionalFlow.alias }} - {{ functionalFlow.description }}
        </h2>
      </div>
    </div>
    <div class="col-12">
      <b-tabs content-class="mt-3" active-nav-item-class="bg-info" card pills>
        <b-tab title="Information" active>
          <div class="row">
            <dl class="row jh-entity-details">
              <dt>
                <span>Alias</span>
              </dt>
              <dd>
                <span>{{ functionalFlow.alias }}</span>
              </dd>
              <dt>
                <span>Description</span>
              </dt>
              <dd>
                <span>{{ functionalFlow.description }}</span>
              </dd>
              <dt>
                <span>Comment</span>
              </dt>
              <dd>
                <span>{{ functionalFlow.comment }}</span>
              </dd>
              <dt>
                <span>Status</span>
              </dt>
              <dd>
                <span>{{ functionalFlow.status }}</span>
              </dd>
              <dt>
                <span>Documentation URL</span>
              </dt>
              <dd>
                <span
                  ><a v-bind:href="functionalFlow.documentationURL">{{
                    functionalFlow.documentationURL ? functionalFlow.documentationURL.substring(0, 200) : ''
                  }}</a></span
                >
              </dd>
              <dt>
                <span>Documentation URL 2</span>
              </dt>
              <dd>
                <span
                  ><a v-bind:href="functionalFlow.documentationURL2">{{
                    functionalFlow.documentationURL2 ? functionalFlow.documentationURL2.substring(0, 200) : ''
                  }}</a></span
                >
              </dd>
              <dt>
                <span>Start Date</span>
              </dt>
              <dd>
                <span>{{ functionalFlow.startDate }}</span>
              </dd>
              <dt>
                <span>End Date</span>
              </dt>
              <dd>
                <span>{{ functionalFlow.endDate }}</span>
              </dd>
              <dt>
                <span>Owner</span>
              </dt>
              <dd>
                <div v-if="functionalFlow.owner">
                  <router-link :to="{ name: 'OwnerView', params: { ownerId: functionalFlow.owner.id } }">{{
                    functionalFlow.owner.name
                  }}</router-link>
                </div>
              </dd>
            </dl>
          </div>
        </b-tab>
        <b-tab title="Schema" active>
          <div class="row my-5">
            <div v-html="plantUMLImage"></div>
            <div class="col-12">
              <button
                class="btn btn-warning"
                v-on:click="exportPlantUML()"
                style="font-size: 0.7em; padding: 3px; margin: 3px"
                v-if="plantUMLImage"
                :disabled="isFetching"
              >
                <span>Export plantuml</span>
              </button>
              <button
                class="btn btn-secondary"
                v-on:click="changeDiagram()"
                style="font-size: 0.7em; padding: 3px; margin: 3px"
                :disabled="isFetching"
              >
                <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
                <span v-text="sequenceDiagram ? 'component diagram' : 'sequence diagram'" />
              </button>
            </div>
          </div>
        </b-tab>
        <b-tab title="Interfaces">
          <div class="row">
            <div class="table-responsive" v-if="functionalFlow.steps && functionalFlow.steps.length > 0">
              <table class="table table-striped">
                <thead>
                  <tr>
                    <th scope="row"><span>#</span></th>
                    <th scope="row"><span>Step</span></th>
                    <th scope="row" v-if="reorderAlias"></th>
                    <th scope="row" v-if="reorderAlias"></th>
                    <th scope="row"><span>Interface</span></th>
                    <th scope="row"><span>Source</span></th>
                    <th scope="row"><span>Target</span></th>
                    <th scope="row"><span>Protocol</span></th>
                    <th scope="row"><span>Data Flows</span></th>
                    <th scope="row"></th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="(step, i) in functionalFlow.steps" v-bind:key="step.id" :set="(inter = step.flowInterface)">
                    <td>
                      {{ step.stepOrder }}
                    </td>
                    <td>
                      <span v-if="!reorderAlias">{{ step.description }}</span>
                      <span v-else>
                        <textarea
                          style="width: 100%; min-width: 600px"
                          rows="1"
                          v-model="step.description"
                          @change="changeStepDescription(functionalFlow, step)"
                        ></textarea>
                      </span>
                    </td>
                    <td v-if="reorderAlias">
                      <font-awesome-icon icon="chevron-up" class="btn-success" v-if="i != 0" @click="swap(i, i - 1)"></font-awesome-icon>
                    </td>
                    <td v-if="reorderAlias">
                      <font-awesome-icon
                        icon="chevron-down"
                        class="btn-success"
                        v-if="i != functionalFlow.steps.length - 1"
                        @click="swap(i, i + 1)"
                      ></font-awesome-icon>
                    </td>
                    <td>
                      <router-link :to="{ name: 'FlowInterfaceView', params: { flowInterfaceId: inter.id } }">{{
                        inter.alias
                      }}</router-link>
                    </td>
                    <td>
                      <router-link :to="{ name: 'ApplicationView', params: { applicationId: inter.source.id } }">
                        {{ inter.source.name }}
                      </router-link>
                      <span v-if="step.flowInterface.id && step.flowInterface.sourceComponent">
                        /
                        <router-link
                          :to="{
                            name: 'ApplicationComponentView',
                            params: { applicationComponentId: step.flowInterface.sourceComponent.id },
                          }"
                          >{{ step.flowInterface.sourceComponent.name }}</router-link
                        >
                      </span>
                    </td>
                    <td>
                      <router-link :to="{ name: 'ApplicationView', params: { applicationId: inter.target.id } }">
                        {{ inter.target.name }}
                      </router-link>
                      <span v-if="step.flowInterface.id && step.flowInterface.targetComponent">
                        /
                        <router-link
                          :to="{
                            name: 'ApplicationComponentView',
                            params: { applicationComponentId: step.flowInterface.targetComponent.id },
                          }"
                          >{{ step.flowInterface.targetComponent.name }}</router-link
                        >
                      </span>
                    </td>
                    <td>
                      <router-link v-if="inter.protocol" :to="{ name: 'ProtocolView', params: { protocolId: inter.protocol.id } }">
                        {{ inter.protocol.name }}
                      </router-link>
                    </td>
                    <td>
                      <span v-for="dataflow in inter.dataFlows" :key="dataflow.id">
                        <router-link
                          :to="{ name: 'DataFlowView', params: { dataFlowId: dataflow.id } }"
                          :title="
                            dataflow.resourceName + (dataflow.items.length > 0 ? ' / ' + dataflow.items.length + ' items ' : ' / no items')
                          "
                          >{{ dataflow.id }}</router-link
                        ><sup v-if="dataflow.items && dataflow.items.length > 0">({{ dataflow.items.length }})</sup>&nbsp;
                      </span>
                    </td>
                    <td class="text-right">
                      <div class="btn-group">
                        <router-link
                          :to="{ name: 'FlowInterfaceView', params: { flowInterfaceId: inter.id } }"
                          custom
                          v-slot="{ navigate }"
                        >
                          <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                            <font-awesome-icon icon="eye"></font-awesome-icon>
                            <span class="d-none d-md-inline">View Interface</span>
                          </button>
                        </router-link>
                      </div>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </b-tab>
        <b-tab title="References">
          <div class="row">
            <div class="table-responsive" v-if="functionalFlow.landscapes && functionalFlow.landscapes.length > 0">
              <h3>
                <font-awesome-icon icon="map" style="color: Tomato; font-size: 0.7em"></font-awesome-icon> Landscapes using Functional Flow
                {{ functionalFlow.alias }}
              </h3>
              <table class="table table-striped">
                <thead>
                  <tr>
                    <th scope="row"><span>Landscape Id</span></th>
                    <th scope="row"><span>Diagram Name</span></th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="landscape in functionalFlow.landscapes" v-bind:key="landscape.id">
                    <td>
                      <router-link :to="{ name: 'LandscapeViewView', params: { landscapeViewId: landscape.id } }">{{
                        landscape.id
                      }}</router-link>
                    </td>
                    <td>
                      <router-link :to="{ name: 'LandscapeViewView', params: { landscapeViewId: landscape.id } }">{{
                        landscape.diagramName
                      }}</router-link>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </b-tab>
      </b-tabs>
    </div>
    <div class="md-12">
      <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
        <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span> Back</span>
      </button>
      <router-link
        v-if="functionalFlow.id"
        :to="{ name: 'FunctionalFlowEdit', params: { functionalFlowId: functionalFlow.id } }"
        custom
        v-slot="{ navigate }"
      >
        <button @click="navigate" class="btn btn-primary" v-if="accountService().writeAuthorities">
          <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span> Edit </span>
        </button>
      </router-link>
    </div>
  </div>
</template>

<script lang="ts" src="./functional-flow-details.component.ts"></script>

<style>
.modal-dialog {
  max-width: 80%;
}
.fa-project-diagram g g path {
  stroke: red;
  stroke-width: 10;
}
</style>
