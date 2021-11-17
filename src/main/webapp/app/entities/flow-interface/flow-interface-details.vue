<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="flowInterface">
        <h2 class="jh-entity-heading" data-cy="flowInterfaceDetailsHeading"><span>FlowInterface</span> {{ flowInterface.id }}</h2>
        <dl class="row jh-entity-details">
          <dt>
            <span>Alias</span>
          </dt>
          <dd>
            <span>{{ flowInterface.alias }}</span>
          </dd>
          <dt>
            <span>Protocol</span>
          </dt>
          <dd>
            <span>{{ flowInterface.protocol }}</span>
          </dd>
          <dt>
            <span>Status</span>
          </dt>
          <dd>
            <span>{{ flowInterface.status }}</span>
          </dd>
          <dt>
            <span>Documentation URL</span>
          </dt>
          <dd>
            <span>{{ flowInterface.documentationURL }}</span>
          </dd>
          <dt>
            <span>Source</span>
          </dt>
          <dd>
            <div v-if="flowInterface.source">
              <router-link :to="{ name: 'ApplicationView', params: { applicationId: flowInterface.source.id } }">{{
                flowInterface.source.name
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span>Target</span>
          </dt>
          <dd>
            <div v-if="flowInterface.target">
              <router-link :to="{ name: 'ApplicationView', params: { applicationId: flowInterface.target.id } }">{{
                flowInterface.target.name
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span>Source Component</span>
          </dt>
          <dd>
            <div v-if="flowInterface.sourceComponent">
              <router-link
                :to="{ name: 'ApplicationComponentView', params: { applicationComponentId: flowInterface.sourceComponent.id } }"
                >{{ flowInterface.sourceComponent.name }}</router-link
              >
            </div>
          </dd>
          <dt>
            <span>Target Component</span>
          </dt>
          <dd>
            <div v-if="flowInterface.targetComponent">
              <router-link
                :to="{ name: 'ApplicationComponentView', params: { applicationComponentId: flowInterface.targetComponent.id } }"
                >{{ flowInterface.targetComponent.name }}</router-link
              >
            </div>
          </dd>
          <dt>
            <span>Owner</span>
          </dt>
          <dd>
            <div v-if="flowInterface.owner">
              <router-link :to="{ name: 'OwnerView', params: { ownerId: flowInterface.owner.id } }">{{
                flowInterface.owner.name
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span> Back</span>
        </button>
        <router-link
          v-if="flowInterface.id"
          :to="{ name: 'FlowInterfaceEdit', params: { flowInterfaceId: flowInterface.id } }"
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

<script lang="ts" src="./flow-interface-details.component.ts"></script>
