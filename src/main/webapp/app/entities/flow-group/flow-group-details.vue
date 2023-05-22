<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="flowGroup">
        <h2 class="jh-entity-heading" data-cy="flowGroupDetailsHeading"><span>FlowGroup</span> {{ flowGroup.id }}</h2>
        <dl class="row jh-entity-details">
          <dt>
            <span>Title</span>
          </dt>
          <dd>
            <span>{{ flowGroup.title }}</span>
          </dd>
          <dt>
            <span>Url</span>
          </dt>
          <dd>
            <span>{{ flowGroup.url }}</span>
          </dd>
          <dt>
            <span>Description</span>
          </dt>
          <dd>
            <span>{{ flowGroup.description }}</span>
          </dd>
          <dt>
            <span>Flow</span>
          </dt>
          <dd>
            <div v-if="flowGroup.flow">
              <router-link :to="{ name: 'FunctionalFlowView', params: { functionalFlowId: flowGroup.flow.id } }">{{
                flowGroup.flow.alias
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span> Back</span>
        </button>
        <router-link
          v-if="flowGroup.id"
          :to="{ name: 'FlowGroupEdit', params: { flowGroupId: flowGroup.id } }"
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

<script lang="ts" src="./flow-group-details.component.ts"></script>
