<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="dataFlow">
        <h2 class="jh-entity-heading" data-cy="dataFlowDetailsHeading"><span>DataFlow</span> {{ dataFlow.id }}</h2>
        <dl class="row jh-entity-details">
          <dt>
            <span>Frequency</span>
          </dt>
          <dd>
            <span>{{ dataFlow.frequency }}</span>
          </dd>
          <dt>
            <span>Format</span>
          </dt>
          <dd>
            <span>{{ dataFlow.format }}</span>
          </dd>
          <dt>
            <span>Type</span>
          </dt>
          <dd>
            <span>{{ dataFlow.type }}</span>
          </dd>
          <dt>
            <span>Description</span>
          </dt>
          <dd>
            <span>{{ dataFlow.description }}</span>
          </dd>
          <dt>
            <span>Resource Name</span>
          </dt>
          <dd>
            <span>{{ dataFlow.resourceName }}</span>
          </dd>
          <dt>
            <span>Contract URL</span>
          </dt>
          <dd>
            <span>{{ dataFlow.contractURL }}</span>
          </dd>
          <dt>
            <span>Documentation URL</span>
          </dt>
          <dd>
            <span>{{ dataFlow.documentationURL }}</span>
          </dd>
          <dt>
            <span>Start Date</span>
          </dt>
          <dd>
            <span>{{ dataFlow.startDate }}</span>
          </dd>
          <dt>
            <span>End Date</span>
          </dt>
          <dd>
            <span>{{ dataFlow.endDate }}</span>
          </dd>
          <dt>
            <span>Functional Flows</span>
          </dt>
          <dd>
            <span v-for="(functionalFlows, i) in dataFlow.functionalFlows" :key="functionalFlows.id"
              >{{ i > 0 ? ', ' : '' }}
              <router-link :to="{ name: 'FunctionalFlowView', params: { functionalFlowId: functionalFlows.id } }">{{
                functionalFlows.alias
              }}</router-link>
            </span>
          </dd>
          <dt>
            <span>Flow Interface</span>
          </dt>
          <dd>
            <div v-if="dataFlow.flowInterface">
              <router-link :to="{ name: 'FlowInterfaceView', params: { flowInterfaceId: dataFlow.flowInterface.id } }">{{
                dataFlow.flowInterface.alias
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span> Back</span>
        </button>
        <router-link v-if="dataFlow.id" :to="{ name: 'DataFlowEdit', params: { dataFlowId: dataFlow.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./data-flow-details.component.ts"></script>
