<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="dataFlowItem">
        <h2 class="jh-entity-heading" data-cy="dataFlowItemDetailsHeading"><span>DataFlowItem</span> {{ dataFlowItem.id }}</h2>
        <dl class="row jh-entity-details">
          <dt>
            <span>Resource Name</span>
          </dt>
          <dd>
            <span>{{ dataFlowItem.resourceName }}</span>
          </dd>
          <dt>
            <span>Resource Type</span>
          </dt>
          <dd>
            <span>{{ dataFlowItem.resourceType }}</span>
          </dd>
          <dt>
            <span>Description</span>
          </dt>
          <dd>
            <span>{{ dataFlowItem.description }}</span>
          </dd>
          <dt>
            <span>Contract URL</span>
          </dt>
          <dd>
            <span
              ><a v-bind:href="dataFlowItem.contractURL">{{ dataFlowItem.contractURL }}</a></span
            >
          </dd>
          <dt>
            <span>Documentation URL</span>
          </dt>
          <dd>
            <span
              ><a v-bind:href="dataFlowItem.documentationURL">{{ dataFlowItem.documentationURL }}</a></span
            >
          </dd>
          <dt>
            <span>Start Date</span>
          </dt>
          <dd>
            <span>{{ dataFlowItem.startDate }}</span>
          </dd>
          <dt>
            <span>End Date</span>
          </dt>
          <dd>
            <span>{{ dataFlowItem.endDate }}</span>
          </dd>
          <dt>
            <span>Data Flow</span>
          </dt>
          <dd>
            <div v-if="dataFlowItem.dataFlow">
              <router-link :to="{ name: 'DataFlowView', params: { dataFlowId: dataFlowItem.dataFlow.id } }">
                {{ dataFlowItem.dataFlow.id }}
              </router-link>
              ({{ dataFlowItem.dataFlow.flowInterface.protocol ? dataFlowItem.dataFlow.flowInterface.protocol.type : 'Unknown' }})
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span> Back</span>
        </button>
        <router-link
          v-if="dataFlowItem.id"
          :to="{ name: 'DataFlowItemEdit', params: { dataFlowItemId: dataFlowItem.id } }"
          custom
          v-slot="{ navigate }"
        >
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./data-flow-item-details.component.ts"></script>
