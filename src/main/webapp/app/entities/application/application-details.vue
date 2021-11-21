<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="application">
        <h2 class="jh-entity-heading" data-cy="applicationDetailsHeading"><span>Application</span> {{ application.id }}</h2>
        <dl class="row jh-entity-details">
          <dt>
            <span>Alias</span>
          </dt>
          <dd>
            <span>{{ application.alias }}</span>
          </dd>
          <dt>
            <span>Name</span>
          </dt>
          <dd>
            <span>{{ application.name }}</span>
          </dd>
          <dt>
            <span>Description</span>
          </dt>
          <dd>
            <span>{{ application.description }}</span>
          </dd>
          <dt>
            <span>Type</span>
          </dt>
          <dd>
            <span>{{ application.type }}</span>
          </dd>
          <dt>
            <span>Technology</span>
          </dt>
          <dd>
            <span>{{ application.technology }}</span>
          </dd>
          <dt>
            <span>Comment</span>
          </dt>
          <dd>
            <span>{{ application.comment }}</span>
          </dd>
          <dt>
            <span>Documentation URL</span>
          </dt>
          <dd>
            <span>{{ application.documentationURL }}</span>
          </dd>
          <dt>
            <span>Start Date</span>
          </dt>
          <dd>
            <span>{{ application.startDate }}</span>
          </dd>
          <dt>
            <span>End Date</span>
          </dt>
          <dd>
            <span>{{ application.endDate }}</span>
          </dd>
          <dt>
            <span>Owner</span>
          </dt>
          <dd>
            <div v-if="application.owner">
              <router-link :to="{ name: 'OwnerView', params: { ownerId: application.owner.id } }">{{ application.owner.name }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span> Back</span>
        </button>
        <router-link
          v-if="application.id"
          :to="{ name: 'ApplicationEdit', params: { applicationId: application.id } }"
          custom
          v-slot="{ navigate }"
        >
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span> Edit</span>
          </button>
        </router-link>
      </div>
      <br />
      <h2>PlantUML preview</h2>
      <div v-html="plantUMLImage"></div>
      <br />
    </div>
  </div>
</template>

<script lang="ts" src="./application-details.component.ts"></script>
