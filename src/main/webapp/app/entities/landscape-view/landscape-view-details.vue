<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="landscapeView">
        <h2 class="jh-entity-heading" data-cy="landscapeViewDetailsHeading"><span>LandscapeView</span> {{ landscapeView.id }}</h2>
        <dl class="row jh-entity-details">
          <dt>
            <span>Viewpoint</span>
          </dt>
          <dd>
            <span>{{ landscapeView.viewpoint }}</span>
          </dd>
          <dt>
            <span>Diagram Name</span>
          </dt>
          <dd>
            <span>{{ landscapeView.diagramName }}</span>
          </dd>
          <dt>
            <span>Owner</span>
          </dt>
          <dd>
            <div v-if="landscapeView.owner">
              <router-link :to="{ name: 'OwnerView', params: { ownerId: landscapeView.owner.id } }">{{
                landscapeView.owner.name
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span> Back</span>
        </button>
        <router-link
          v-if="landscapeView.id"
          :to="{ name: 'LandscapeViewEdit', params: { landscapeViewId: landscapeView.id } }"
          custom
          v-slot="{ navigate }"
        >
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span> Edit</span>
          </button>
        </router-link>
      </div>
      <div>
        <br />
      </div>
      <br />
      <h2>PlantUML preview</h2>
      <div><img :src="plantUMLImage" alt="Base64 encoded image" /></div>
      <br />
      <table class="table">
        <thead>
          <tr>
            <th scope="row"><span>Alias</span></th>
            <th scope="row"><span>ID</span></th>
            <th scope="row"><span>Protocol</span></th>
            <th scope="row"><span>Description</span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="legend in legends" v-bind:key="legend.id">
            <td>{{ legend.flowAlias }}</td>
            <td>{{ legend.idFlowFromExcel }}</td>
            <td>{{ legend.integrationPattern }}</td>
            <td>{{ legend.description }}</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script lang="ts" src="./landscape-view-details.component.ts"></script>
