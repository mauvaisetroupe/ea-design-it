<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="functionalFlow">
        <h2 class="jh-entity-heading" data-cy="functionalFlowDetailsHeading"><span>Functional Flow</span> {{ functionalFlow.alias }}</h2>
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
              ><a v-bind:href="functionalFlow.documentationURL">{{ functionalFlow.documentationURL }}</a></span
            >
          </dd>
          <dt>
            <span>Documentation URL 2</span>
          </dt>
          <dd>
            <span
              ><a v-bind:href="functionalFlow.documentationURL2">{{ functionalFlow.documentationURL2 }}</a></span
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
            <span>Interfaces</span>
          </dt>
          <dd>
            <span v-for="(interfaces, i) in functionalFlow.interfaces" :key="interfaces.id"
              >{{ i > 0 ? ', ' : '' }}
              <router-link :to="{ name: 'FlowInterfaceView', params: { flowInterfaceId: interfaces.id } }">{{
                interfaces.alias
              }}</router-link>
            </span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span> Back</span>
        </button>
        <router-link
          v-if="functionalFlow.id"
          :to="{ name: 'FunctionalFlowEdit', params: { functionalFlowId: functionalFlow.id } }"
          custom
          v-slot="{ navigate }"
        >
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span> Edit</span>
          </button>
        </router-link>
      </div>

      <br />

      <h2>PlantUML preview</h2>
      <div v-html="plantUMLImage"></div>
      <br />
      <table class="table">
        <thead>
          <tr>
            <th scope="row"><span>Interface</span></th>
            <th scope="row"><span>Source</span></th>
            <th scope="row"><span>Target</span></th>
            <th scope="row"><span>Protocol</span></th>
            <th scope="row"><span>Data Flows</span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="caption in captions" v-bind:key="caption.id">
            <td>
              <router-link :to="{ name: 'FlowInterfaceView', params: { flowInterfaceId: caption.interfaceID } }">{{
                caption.interfaceAlias
              }}</router-link>
            </td>
            <td>
              <router-link :to="{ name: 'ApplicationView', params: { applicationId: caption.source.id } }">
                {{ caption.source.name }}
              </router-link>
            </td>
            <td>
              <router-link :to="{ name: 'ApplicationView', params: { applicationId: caption.target.id } }">
                {{ caption.target.name }}
              </router-link>
            </td>
            <td>
              <router-link v-if="caption.protocol" :to="{ name: 'ProtocolView', params: { protocolId: caption.protocol.id } }">
                {{ caption.protocol.name }}
              </router-link>
            </td>
            <td>
              <span v-for="dataflow in caption.dataFlows" :key="dataflow.id">
                <router-link :to="{ name: 'DataFlowView', params: { dataFlowId: dataflow.id } }">
                  {{ dataflow.id }}
                </router-link>
              </span>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script lang="ts" src="./functional-flow-details.component.ts"></script>
