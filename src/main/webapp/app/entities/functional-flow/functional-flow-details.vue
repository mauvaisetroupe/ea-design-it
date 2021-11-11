<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="functionalFlow">
        <h2 class="jh-entity-heading" data-cy="functionalFlowDetailsHeading"><span>FunctionalFlow</span> {{ functionalFlow.id }}</h2>
        <dl class="row jh-entity-details">
          <dt>
            <span>Alias</span>
          </dt>
          <dd>
            <span>{{ functionalFlow.alias }}</span>
          </dd>
          <dt>
            <span>Description</span>
          </dt>
          <dd>
            <span>{{ functionalFlow.description }}</span>
          </dd>
          <dt>
            <span>Comment</span>
          </dt>
          <dd>
            <span>{{ functionalFlow.comment }}</span>
          </dd>
          <dt>
            <span>Status</span>
          </dt>
          <dd>
            <span>{{ functionalFlow.status }}</span>
          </dd>
          <dt>
            <span>Interfaces</span>
          </dt>
          <dd>
            <span v-for="(interfaces, i) in functionalFlow.interfaces" :key="interfaces.id"
              >{{ i > 0 ? ', ' : '' }}
              <router-link :to="{ name: 'FlowInterfaceView', params: { flowInterfaceId: interfaces.id } }">{{
                interfaces.alias
              }}</router-link>
            </span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span> Back</span>
        </button>
        <router-link
          v-if="functionalFlow.id"
          :to="{ name: 'FunctionalFlowEdit', params: { functionalFlowId: functionalFlow.id } }"
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
      <table class="table">
        <thead>
          <tr>
            <th scope="row"><span>Flow</span></th>
            <th scope="row"><span>Interface</span></th>
            <th scope="row"><span>Source</span></th>
            <th scope="row"><span>Target</span></th>
            <th scope="row"><span>Protocol</span></th>
            <th scope="row"><span>Description</span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="caption in captions" v-bind:key="caption.id">
            <td>{{ caption.flowAlias }}</td>
            <td>{{ caption.idFlowFromExcel }}</td>
            <td>{{ caption.sourceElement }}</td>
            <td>{{ caption.targetElement }}</td>
            <td>{{ caption.integrationPattern }}</td>
            <td>{{ caption.description }}</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script lang="ts" src="./functional-flow-details.component.ts"></script>
