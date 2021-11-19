<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="eventData">
        <h2 class="jh-entity-heading" data-cy="eventDataDetailsHeading"><span>EventData</span> {{ eventData.id }}</h2>
        <dl class="row jh-entity-details">
          <dt>
            <span>Name</span>
          </dt>
          <dd>
            <span>{{ eventData.name }}</span>
          </dd>
          <dt>
            <span>Contract URL</span>
          </dt>
          <dd>
            <span>{{ eventData.contractURL }}</span>
          </dd>
          <dt>
            <span>Documentation URL</span>
          </dt>
          <dd>
            <span>{{ eventData.documentationURL }}</span>
          </dd>
          <dt>
            <span>Start Date</span>
          </dt>
          <dd>
            <span>{{ eventData.startDate }}</span>
          </dd>
          <dt>
            <span>End Date</span>
          </dt>
          <dd>
            <span>{{ eventData.endDate }}</span>
          </dd>
          <dt>
            <span>Data Flow</span>
          </dt>
          <dd>
            <div v-if="eventData.dataFlow">
              <router-link :to="{ name: 'DataFlowView', params: { dataFlowId: eventData.dataFlow.id } }">{{
                eventData.dataFlow.id
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span> Back</span>
        </button>
        <router-link
          v-if="eventData.id"
          :to="{ name: 'EventDataEdit', params: { eventDataId: eventData.id } }"
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

<script lang="ts" src="./event-data-details.component.ts"></script>
