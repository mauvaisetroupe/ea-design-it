<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="businessObject">
        <h2 class="jh-entity-heading" data-cy="businessObjectDetailsHeading"><span>Business Object</span> {{ businessObject.name }}</h2>
        <dl class="row jh-entity-details">
          <dt>
            <span>Abstract Business Object</span>
          </dt>
          <dd>
            <span>{{ businessObject.abstractBusinessObject }}</span>
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
              <a v-on:click="retrieveBusinessObject(businessObject.generalization.id)" class="text-primary">{{
                businessObject.generalization.name
              }}</a>
            </div>
          </dd>
          <dt>
            <span>Parent</span>
          </dt>
          <dd>
            <div v-if="businessObject.parent">
              <a v-on:click="retrieveBusinessObject(businessObject.parent.id)" class="text-primary">{{ businessObject.parent.name }}</a>
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
      <div v-html="plantUMLImage" class="table-responsive my-5" v-if="plantUMLImage"></div>
    </div>
  </div>
</template>

<script lang="ts" src="./business-object-details.component.ts"></script>
