<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="externalSystem">
        <h2 class="jh-entity-heading" data-cy="externalSystemDetailsHeading"><span>ExternalSystem</span> {{ externalSystem.id }}</h2>
        <dl class="row jh-entity-details">
          <dt>
            <span>External System ID</span>
          </dt>
          <dd>
            <span>{{ externalSystem.externalSystemID }}</span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span> Back</span>
        </button>
        <router-link
          v-if="externalSystem.id"
          :to="{ name: 'ExternalSystemEdit', params: { externalSystemId: externalSystem.id } }"
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

<script lang="ts" src="./external-system-details.component.ts"></script>
