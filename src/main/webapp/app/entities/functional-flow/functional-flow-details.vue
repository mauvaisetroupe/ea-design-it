<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="functionalFlow">
        <h2 class="jh-entity-heading" data-cy="functionalFlowDetailsHeading"><span>Functional Flow</span> - {{ functionalFlow.alias }}</h2>
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
            <span>Documentation URL</span>
          </dt>
          <dd>
            <span
              ><a v-bind:href="functionalFlow.documentationURL">{{ functionalFlow.documentationURL }}</a></span
            >
          </dd>
          <dt>
            <span>Documentation URL 2</span>
          </dt>
          <dd>
            <span
              ><a v-bind:href="functionalFlow.documentationURL2">{{ functionalFlow.documentationURL2 }}</a></span
            >
          </dd>
          <dt>
            <span>Start Date</span>
          </dt>
          <dd>
            <span>{{ functionalFlow.startDate }}</span>
          </dd>
          <dt>
            <span>End Date</span>
          </dt>
          <dd>
            <span>{{ functionalFlow.endDate }}</span>
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
          <button @click="navigate" class="btn btn-primary" v-if="accountService().writeAuthorities">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span> Edit Information</span>
          </button>
        </router-link>
      </div>

      <br />

      <h3>Flow {{ functionalFlow.alias }} Interfaces</h3>
      <div v-html="plantUMLImage"></div>
      <br />

      <div class="table-responsive" v-if="functionalFlow.interfaces && functionalFlow.interfaces.length > 0">
        <table class="table table-striped">
          <thead>
            <tr>
              <th scope="row"><span>Interface</span></th>
              <th scope="row"><span>Source</span></th>
              <th scope="row"><span>Target</span></th>
              <th scope="row"><span>Protocol</span></th>
              <th scope="row"><span>Data Flows</span></th>
              <th scope="row"></th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="inter in functionalFlow.interfaces" v-bind:key="inter.id">
              <td>
                <router-link :to="{ name: 'FlowInterfaceView', params: { flowInterfaceId: inter.id } }">{{ inter.alias }}</router-link>
              </td>
              <td>
                <router-link :to="{ name: 'ApplicationView', params: { applicationId: inter.source.id } }">
                  {{ inter.source.name }}
                </router-link>
              </td>
              <td>
                <router-link :to="{ name: 'ApplicationView', params: { applicationId: inter.target.id } }">
                  {{ inter.target.name }}
                </router-link>
              </td>
              <td>
                <router-link v-if="inter.protocol" :to="{ name: 'ProtocolView', params: { protocolId: inter.protocol.id } }">
                  {{ inter.protocol.name }}
                </router-link>
              </td>
              <td>
                <span v-for="dataflow in inter.dataFlows" :key="dataflow.id">
                  <router-link :to="{ name: 'DataFlowView', params: { dataFlowId: dataflow.id } }">
                    {{ dataflow.id }}
                  </router-link>
                </span>
              </td>
              <td class="text-right">
                <div class="btn-group">
                  <router-link :to="{ name: 'FlowInterfaceView', params: { flowInterfaceId: inter.id } }" custom v-slot="{ navigate }">
                    <button
                      @click="navigate"
                      class="btn btn-info btn-sm details"
                      data-cy="entityDetailsButton"
                      v-if="!accountService().writeAuthorities"
                    >
                      <font-awesome-icon icon="eye"></font-awesome-icon>
                      <span class="d-none d-md-inline">View</span>
                    </button>
                    <button
                      @click="navigate"
                      class="btn btn-primary btn-sm edit"
                      data-cy="entityEditButton"
                      v-if="accountService().writeAuthorities"
                    >
                      <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                      <span class="d-none d-md-inline">Edit</span>
                    </button>
                  </router-link>
                  <b-button
                    v-if="accountService().writeAuthorities"
                    v-on:click="prepareRemove(inter)"
                    variant="warning"
                    class="btn btn-sm"
                    data-cy="entityDeleteButton"
                    v-b-modal.removeEntity
                  >
                    <font-awesome-icon icon="times"></font-awesome-icon>
                    <span class="d-none d-md-inline">Detach</span>
                  </b-button>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <div class="d-flex justify-content-end">
        <span>
          <button
            class="btn btn-primary jh-create-entity create-functional-flow"
            v-if="accountService().writeAuthorities"
            @click="addNew()"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>Add exisintg Interface</span>
          </button>
          <router-link :to="{ name: 'FlowInterfaceCreate', query: { functionalFlowId: functionalFlow.id } }" custom v-slot="{ navigate }">
            <button
              @click="navigate"
              id="jh-create-entity"
              data-cy="entityCreateButton"
              class="btn btn-primary jh-create-entity create-flow-interface"
              v-if="accountService().writeAuthorities"
            >
              <font-awesome-icon icon="plus"></font-awesome-icon>
              <span> Create a new Flow Interface </span>
            </button>
          </router-link>
        </span>
      </div>

      <div class="table-responsive" v-if="functionalFlow.landscapes && functionalFlow.landscapes.length > 0">
        <br />
        <br />
        <h3>Landscapes using Functional Flow {{ functionalFlow.alias }}</h3>

        <table class="table table-striped">
          <thead>
            <tr>
              <th scope="row"><span>Id</span></th>
              <th scope="row"><span>Diagram Name</span></th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="landscape in functionalFlow.landscapes" v-bind:key="landscape.id">
              <td>
                <router-link :to="{ name: 'LandscapeViewView', params: { landscapeViewId: landscape.id } }">{{ landscape.id }}</router-link>
              </td>
              <td>
                <router-link :to="{ name: 'LandscapeViewView', params: { landscapeViewId: landscape.id } }">{{
                  landscape.diagramName
                }}</router-link>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./functional-flow-details.component.ts"></script>
