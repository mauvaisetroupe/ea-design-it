<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="externalReference">
        <h2 class="jh-entity-heading" data-cy="externalReferenceDetailsHeading">
          <span>External Reference</span> {{ externalReference.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span>External ID</span>
          </dt>
          <dd>
            <span>{{ externalReference.externalID }}</span>
          </dd>
          <dt>
            <span>External System</span>
          </dt>
          <dd>
            <div v-if="externalReference.externalSystem">
              <router-link :to="{ name: 'ExternalSystemView', params: { externalSystemId: externalReference.externalSystem.id } }">{{
                externalReference.externalSystem.externalSystemID
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>Back</span>
        </button>
        <router-link
          v-if="externalReference.id"
          :to="{ name: 'ExternalReferenceEdit', params: { externalReferenceId: externalReference.id } }"
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

<script lang="ts" src="./external-reference-details.component.ts"></script>
