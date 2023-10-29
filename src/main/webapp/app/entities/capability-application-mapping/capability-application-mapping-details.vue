<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="capabilityApplicationMapping">
        <h2 class="jh-entity-heading" data-cy="capabilityApplicationMappingDetailsHeading">
          <span>Capability Application Mapping</span> {{ capabilityApplicationMapping.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span>Capability</span>
          </dt>
          <dd>
            <div v-if="capabilityApplicationMapping.capability">
              <router-link :to="{ name: 'CapabilityView', params: { capabilityId: capabilityApplicationMapping.capability.id } }">{{
                capabilityApplicationMapping.capability.id
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span>Application</span>
          </dt>
          <dd>
            <div v-if="capabilityApplicationMapping.application">
              <router-link :to="{ name: 'ApplicationView', params: { applicationId: capabilityApplicationMapping.application.id } }">{{
                capabilityApplicationMapping.application.id
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span>Landscape</span>
          </dt>
          <dd>
            <span v-for="(landscape, i) in capabilityApplicationMapping.landscapes" :key="landscape.id"
              >{{ i > 0 ? ', ' : '' }}
              <router-link :to="{ name: 'LandscapeViewView', params: { landscapeViewId: landscape.id } }">{{
                landscape.diagramName
              }}</router-link>
            </span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>Back</span>
        </button>
        <router-link
          v-if="capabilityApplicationMapping.id"
          :to="{ name: 'CapabilityApplicationMappingEdit', params: { capabilityApplicationMappingId: capabilityApplicationMapping.id } }"
          custom
          v-slot="{ navigate }"
        >
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span>Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./capability-application-mapping-details.component.ts"></script>
