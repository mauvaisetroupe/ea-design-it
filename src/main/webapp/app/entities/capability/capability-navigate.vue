<template>
  <div>
    <CapabilityComponent :capability="capability" @retrieveCapability="retrieveCapability" :path="path"></CapabilityComponent>
  </div>
</template>

<script lang="ts" src="./capability-navigate.component.ts"></script>
