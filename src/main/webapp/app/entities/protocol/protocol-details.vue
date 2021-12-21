<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="protocol">
        <h2 class="jh-entity-heading" data-cy="protocolDetailsHeading"><span>Protocol</span> {{ protocol.id }}</h2>
        <dl class="row jh-entity-details">
          <dt>
            <span>Name</span>
          </dt>
          <dd>
            <span>{{ protocol.name }}</span>
          </dd>
          <dt>
            <span>Type</span>
          </dt>
          <dd>
            <span>{{ protocol.type }}</span>
          </dd>
          <dt>
            <span>Description</span>
          </dt>
          <dd>
            <span>{{ protocol.description }}</span>
          </dd>
          <dt>
            <span>Scope</span>
          </dt>
          <dd>
            <span>{{ protocol.scope }}</span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span> Back</span>
        </button>
        <router-link v-if="protocol.id" :to="{ name: 'ProtocolEdit', params: { protocolId: protocol.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary" v-if="accountService().writeAuthorities">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./protocol-details.component.ts"></script>
