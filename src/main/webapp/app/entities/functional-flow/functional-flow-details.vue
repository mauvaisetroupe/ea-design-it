<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="functionalFlow">
        <h2 class="jh-entity-heading" data-cy="functionalFlowDetailsHeading"><span>FunctionalFlow</span> {{ functionalFlow.id }}</h2>
        <dl class="row jh-entity-details">
          <dt>
            <span>Alias</span>
          </dt>
          <dd>
            <span>{{ functionalFlow.alias }}</span>
          </dd>
          <dt>
            <span>Description</span>
          </dt>
          <dd>
            <span>{{ functionalFlow.description }}</span>
          </dd>
          <dt>
            <span>Comment</span>
          </dt>
          <dd>
            <span>{{ functionalFlow.comment }}</span>
          </dd>
          <dt>
            <span>Status</span>
          </dt>
          <dd>
            <span>{{ functionalFlow.status }}</span>
          </dd>
          <dt>
            <span>Documentation URL</span>
          </dt>
          <dd>
            <span>{{ functionalFlow.documentationURL }}</span>
          </dd>
          <dt>
            <span>Documentation URL 2</span>
          </dt>
          <dd>
            <span>{{ functionalFlow.documentationURL2 }}</span>
          </dd>
          <dt>
            <span>Start Date</span>
          </dt>
          <dd>
            <span>{{ functionalFlow.startDate }}</span>
          </dd>
          <dt>
            <span>End Date</span>
          </dt>
          <dd>
            <span>{{ functionalFlow.endDate }}</span>
          </dd>
          <dt>
            <span>Owner</span>
          </dt>
          <dd>
            <div v-if="functionalFlow.owner">
              <router-link :to="{ name: 'OwnerView', params: { ownerId: functionalFlow.owner.id } }">{{
                functionalFlow.owner.name
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span>Interfaces</span>
          </dt>
          <dd>
            <span v-for="(interfaces, i) in functionalFlow.interfaces" :key="interfaces.id"
              >{{ i > 0 ? ', ' : '' }}
              <router-link :to="{ name: 'FlowInterfaceView', params: { flowInterfaceId: interfaces.id } }">{{
                interfaces.alias
              }}</router-link>
            </span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span> Back</span>
        </button>
        <router-link
          v-if="functionalFlow.id"
          :to="{ name: 'FunctionalFlowEdit', params: { functionalFlowId: functionalFlow.id } }"
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

<script lang="ts" src="./functional-flow-details.component.ts"></script>
