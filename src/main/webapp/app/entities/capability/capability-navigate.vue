<template>
  <div>
    <h2 class="jh-entity-heading" data-cy="capabilitiesNavigateHeading"><span>Capability Map</span></h2>
    <br />
    <CapabilityComponent :capability="capability" @retrieveCapability="retrieveCapability" :path="path" :defaultShowApplications="true" />
  </div>
</template>

<script lang="ts" src="./capability-navigate.component.ts"></script>
