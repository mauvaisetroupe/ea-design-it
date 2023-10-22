<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="functionalFlowStep">
        <h2 class="jh-entity-heading" data-cy="functionalFlowStepDetailsHeading">
          <span>Functional Flow Step</span> {{ functionalFlowStep.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span>Description</span>
          </dt>
          <dd>
            <span>{{ functionalFlowStep.description }}</span>
          </dd>
          <dt>
            <span>Step Order</span>
          </dt>
          <dd>
            <span>{{ functionalFlowStep.stepOrder }}</span>
          </dd>
          <dt>
            <span>Flow Interface</span>
          </dt>
          <dd>
            <div v-if="functionalFlowStep.flowInterface">
              <router-link :to="{ name: 'FlowInterfaceView', params: { flowInterfaceId: functionalFlowStep.flowInterface.id } }">{{
                functionalFlowStep.flowInterface.alias
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span>Group</span>
          </dt>
          <dd>
            <div v-if="functionalFlowStep.group">
              <router-link :to="{ name: 'FlowGroupView', params: { flowGroupId: functionalFlowStep.group.id } }">{{
                functionalFlowStep.group.alias
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span>Flow</span>
          </dt>
          <dd>
            <div v-if="functionalFlowStep.flow">
              <router-link :to="{ name: 'FunctionalFlowView', params: { functionalFlowId: functionalFlowStep.flow.id } }">{{
                functionalFlowStep.flow.alias
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>Back</span>
        </button>
        <router-link
          v-if="functionalFlowStep.id"
          :to="{ name: 'FunctionalFlowStepEdit', params: { functionalFlowStepId: functionalFlowStep.id } }"
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

<script lang="ts" src="./functional-flow-step-details.component.ts"></script>
