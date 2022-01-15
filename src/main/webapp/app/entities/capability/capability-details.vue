<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="capability">
        <h2 class="jh-entity-heading" data-cy="capabilityDetailsHeading"><span>Capability</span> {{ capability.name }}</h2>
        <dl class="row jh-entity-details">
          <dt>
            <span>Id</span>
          </dt>
          <dd>
            <span>{{ capability.id }}</span>
          </dd>
          <dt>
            <span>Description</span>
          </dt>
          <dd>
            <span>{{ capability.description }}</span>
          </dd>
          <dt>
            <span>Comment</span>
          </dt>
          <dd>
            <span>{{ capability.comment }}</span>
          </dd>
          <dt>
            <span>Level</span>
          </dt>
          <dd>
            <span>{{ capability.level }}</span>
          </dd>
          <dt>
            <span>Parent</span>
          </dt>
          <dd>
            <div v-if="capability.parent">
              <a @click="retrieveCapability(capability.parent.id)">{{ capability.parent.name }}</a>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span> Back</span>
        </button>
        <router-link
          v-if="capability.id"
          :to="{ name: 'CapabilityEdit', params: { capabilityId: capability.id } }"
          custom
          v-slot="{ navigate }"
        >
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
    <div class="col-12">
      <h2>Capabilities</h2>
      <div v-html="capabilitiesPlantUMLImage"></div>
    </div>
  </div>
</template>

<script lang="ts" src="./capability-details.component.ts"></script>
