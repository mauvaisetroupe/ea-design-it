<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="businessObject">
        <h2 class="jh-entity-heading" data-cy="businessObjectDetailsHeading"><span>Business Object</span> {{ businessObject.id }}</h2>
        <dl class="row jh-entity-details">
          <dt>
            <span>Name</span>
          </dt>
          <dd>
            <span>{{ businessObject.name }}</span>
          </dd>
          <dt>
            <span>Implementable</span>
          </dt>
          <dd>
            <span>{{ businessObject.implementable }}</span>
          </dd>
          <dt>
            <span>Owner</span>
          </dt>
          <dd>
            <div v-if="businessObject.owner">
              <router-link :to="{ name: 'OwnerView', params: { ownerId: businessObject.owner.id } }">{{
                businessObject.owner.name
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span>Generalization</span>
          </dt>
          <dd>
            <div v-if="businessObject.generalization">
              <router-link :to="{ name: 'BusinessObjectView', params: { businessObjectId: businessObject.generalization.id } }">{{
                businessObject.generalization.name
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span>Container</span>
          </dt>
          <dd>
            <div v-if="businessObject.container">
              <router-link :to="{ name: 'BusinessObjectView', params: { businessObjectId: businessObject.container.id } }">{{
                businessObject.container.name
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>Back</span>
        </button>
        <router-link
          v-if="businessObject.id"
          :to="{ name: 'BusinessObjectEdit', params: { businessObjectId: businessObject.id } }"
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

<script lang="ts" src="./business-object-details.component.ts"></script>
