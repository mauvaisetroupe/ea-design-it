<template>
  <div>
    <h2 class="jh-entity-heading" data-cy="capabilitiesNavigateHeading"><span>Capability Map</span></h2>
    <b-tabs content-class="mt-3" active-nav-item-class="bg-info" card pills>
      <b-tab title="Graphical View">
        <div class="row" v-if="!isFetching">
          <CapabilityComponent :capability="capability" @retrieveCapability="retrieveCapability" :defaultShowApplications="false" />
        </div>
      </b-tab>
      <b-tab title="Text View">
        <input type="text" placeholder="Filter by text" v-model="filter" style="width: 800px" class="mb-4" />
        <div class="overflow-auto" style="height: 600px">
          <div v-for="(capability, i) in filteredCapabilities" :key="i">{{ capability }}</div>
        </div>
      </b-tab>
    </b-tabs>
  </div>
</template>

<script lang="ts" src="./capability-navigate.component.ts"></script>
