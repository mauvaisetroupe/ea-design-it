<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="dataObject">
        <h2 class="jh-entity-heading" data-cy="dataObjectDetailsHeading"><span>Data Object</span> {{ dataObject.id }}</h2>
        <dl class="row jh-entity-details">
          <dt>
            <span>Name</span>
          </dt>
          <dd>
            <span>{{ dataObject.name }}</span>
          </dd>
          <dt>
            <span>Type</span>
          </dt>
          <dd>
            <span>{{ dataObject.type }}</span>
          </dd>
          <dt>
            <span>Application</span>
          </dt>
          <dd>
            <div v-if="dataObject.application">
              <router-link :to="{ name: 'ApplicationView', params: { applicationId: dataObject.application.id } }">{{
                dataObject.application.name
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span>Owner</span>
          </dt>
          <dd>
            <div v-if="dataObject.owner">
              <router-link :to="{ name: 'OwnerView', params: { ownerId: dataObject.owner.id } }">{{ dataObject.owner.name }}</router-link>
            </div>
          </dd>
          <dt>
            <span>Technologies</span>
          </dt>
          <dd>
            <span v-for="(technologies, i) in dataObject.technologies" :key="technologies.id"
              >{{ i > 0 ? ', ' : '' }}
              <router-link :to="{ name: 'TechnologyView', params: { technologyId: technologies.id } }">{{ technologies.name }}</router-link>
            </span>
          </dd>
          <dt>
            <span>Landscapes</span>
          </dt>
          <dd>
            <span v-for="(landscapes, i) in dataObject.landscapes" :key="landscapes.id"
              >{{ i > 0 ? ', ' : '' }}
              <router-link :to="{ name: 'LandscapeViewView', params: { landscapeViewId: landscapes.id } }">{{
                landscapes.diagramName
              }}</router-link>
            </span>
          </dd>
          <dt>
            <span>Parent</span>
          </dt>
          <dd>
            <div v-if="dataObject.parent">
              <a v-on:click="retrieveDataObject(dataObject.parent.id)" class="text-primary">{{ dataObject.parent.name }}</a>
            </div>
          </dd>
          <dt>
            <span>Business Object</span>
          </dt>
          <dd>
            <div v-if="dataObject.businessObject">
              <router-link :to="{ name: 'BusinessObjectView', params: { businessObjectId: dataObject.businessObject.id } }">{{
                dataObject.businessObject.name
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>Back</span>
        </button>
        <router-link
          v-if="dataObject.id"
          :to="{ name: 'DataObjectEdit', params: { dataObjectId: dataObject.id } }"
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

<script lang="ts" src="./data-object-details.component.ts"></script>
