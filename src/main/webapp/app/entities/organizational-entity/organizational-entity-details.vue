<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="organizationalEntity">
        <h2 class="jh-entity-heading" data-cy="organizationalEntityDetailsHeading">
          <span>Organizational Entity</span> {{ organizationalEntity.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span>Name</span>
          </dt>
          <dd>
            <span>{{ organizationalEntity.name }}</span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>Back</span>
        </button>
        <router-link
          v-if="organizationalEntity.id"
          :to="{ name: 'OrganizationalEntityEdit', params: { organizationalEntityId: organizationalEntity.id } }"
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

<script lang="ts" src="./organizational-entity-details.component.ts"></script>
