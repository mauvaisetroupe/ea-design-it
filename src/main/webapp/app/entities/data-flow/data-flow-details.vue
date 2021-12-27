<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="dataFlow">
        <h2 class="jh-entity-heading" data-cy="dataFlowDetailsHeading">
          <font-awesome-icon icon="folder" style="color: Tomato; font-size: 0.9em"></font-awesome-icon> <span>DataFlow</span>
          {{ dataFlow.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span>Resource Name</span>
          </dt>
          <dd>
            <span>{{ dataFlow.resourceName }}</span>
          </dd>
          <dt>
            <span>Resource Type</span>
          </dt>
          <dd>
            <span>{{ dataFlow.resourceType }}</span>
          </dd>
          <dt>
            <span>Description</span>
          </dt>
          <dd>
            <span>{{ dataFlow.description }}</span>
          </dd>
          <dt>
            <span>Frequency</span>
          </dt>
          <dd>
            <span>{{ dataFlow.frequency }}</span>
          </dd>
          <dt>
            <span>Contract URL</span>
          </dt>
          <dd>
            <span>
              <a v-bind:href="dataFlow.contractURL">{{ dataFlow.contractURL }}</a>
            </span>
          </dd>
          <dt>
            <span>Documentation URL</span>
          </dt>
          <dd>
            <span
              ><a v-bind:href="dataFlow.documentationURL">{{ dataFlow.documentationURL }}</a></span
            >
          </dd>
          <dt>
            <span>Start Date</span>
          </dt>
          <dd>
            <span>{{ dataFlow.startDate }}</span>
          </dd>
          <dt>
            <span>End Date</span>
          </dt>
          <dd>
            <span>{{ dataFlow.endDate }}</span>
          </dd>
          <dt>
            <span>Format</span>
          </dt>
          <dd>
            <div v-if="dataFlow.format">
              <router-link :to="{ name: 'DataFormatView', params: { dataFormatId: dataFlow.format.id } }">{{
                dataFlow.format.name
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span>Functional Flows</span>
          </dt>
          <dd>
            <span v-for="(functionalFlows, i) in dataFlow.functionalFlows" :key="functionalFlows.id"
              >{{ i > 0 ? ', ' : '' }}
              <router-link :to="{ name: 'FunctionalFlowView', params: { functionalFlowId: functionalFlows.id } }">{{
                functionalFlows.alias
              }}</router-link>
            </span>
          </dd>
          <dt>
            <span>Flow Interface</span>
          </dt>
          <dd>
            <div v-if="dataFlow.flowInterface">
              <router-link :to="{ name: 'FlowInterfaceView', params: { flowInterfaceId: dataFlow.flowInterface.id } }">
                {{ dataFlow.flowInterface.alias }}</router-link
              >
              ({{ dataFlow.flowInterface.protocol ? dataFlow.flowInterface.protocol.type : 'Unknown' }})
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span> Back</span>
        </button>
        <router-link v-if="dataFlow.id" :to="{ name: 'DataFlowEdit', params: { dataFlowId: dataFlow.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary" v-if="accountService().writeAuthorities">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span> Edit</span>
          </button>
        </router-link>
      </div>

      <div class="table-responsive" v-if="dataFlow.items && dataFlow.items.length > 0">
        <br />
        <br />
        <h2><font-awesome-icon icon="clone" style="color: Tomato; font-size: 0.7em"></font-awesome-icon> DataFlow Items :</h2>
        <table class="table table-striped" aria-describedby="dataFlowItems">
          <thead>
            <tr>
              <th scope="row"><span>ID</span></th>
              <th scope="row"><span>Resource Name</span></th>
              <th scope="row"><span>Resource Type</span></th>
              <th scope="row"><span>Description</span></th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="dataFlowItem in dataFlow.items" :key="dataFlowItem.id" data-cy="entityTable">
              <td>
                <router-link :to="{ name: 'DataFlowItemView', params: { dataFlowItemId: dataFlowItem.id } }">{{
                  dataFlowItem.id
                }}</router-link>
              </td>
              <td>{{ dataFlowItem.resourceName }}</td>
              <td>{{ dataFlowItem.resourceType }}</td>
              <td>{{ dataFlowItem.description }}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./data-flow-details.component.ts"></script>
