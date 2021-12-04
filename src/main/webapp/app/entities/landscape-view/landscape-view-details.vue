<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="landscapeView">
        <h2 class="jh-entity-heading" data-cy="landscapeViewDetailsHeading"><span>LandscapeView</span> {{ landscapeView.id }}</h2>
        <dl class="row jh-entity-details">
          <dt>
            <span>Viewpoint</span>
          </dt>
          <dd>
            <span>{{ landscapeView.viewpoint }}</span>
          </dd>
          <dt>
            <span>Diagram Name</span>
          </dt>
          <dd>
            <span>{{ landscapeView.diagramName }}</span>
          </dd>
          <dt>
            <span>Compressed Draw XML</span>
          </dt>
          <dd>
            <span>{{ landscapeView.compressedDrawXML }}</span>
          </dd>
          <dt>
            <span>Compressed Draw SVG</span>
          </dt>
          <dd>
            <span>{{ landscapeView.compressedDrawSVG }}</span>
          </dd>
          <dt>
            <span>Owner</span>
          </dt>
          <dd>
            <div v-if="landscapeView.owner">
              <router-link :to="{ name: 'OwnerView', params: { ownerId: landscapeView.owner.id } }">{{
                landscapeView.owner.name
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span>Flows</span>
          </dt>
          <dd>
            <span v-for="(flows, i) in landscapeView.flows" :key="flows.id"
              >{{ i > 0 ? ', ' : '' }}
              <router-link :to="{ name: 'FunctionalFlowView', params: { functionalFlowId: flows.id } }">{{ flows.alias }}</router-link>
            </span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span> Back</span>
        </button>
        <router-link
          v-if="landscapeView.id"
          :to="{ name: 'LandscapeViewEdit', params: { landscapeViewId: landscapeView.id } }"
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

<script lang="ts" src="./landscape-view-details.component.ts"></script>
