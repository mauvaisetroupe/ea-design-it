<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div>
        <h2>Interfaces for selected applications</h2>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span> Back</span>
        </button>
      </div>

      <div>
        <div v-html="plantUMLImage"></div>
        <div class="col-12">
          <button
            class="btn btn-warning"
            v-on:click="exportPlantUML()"
            style="font-size: 0.7em; padding: 3px; margin: 3px"
            v-if="plantUMLImage"
          >
            <span>Export plantuml</span>
          </button>
        </div>
      </div>

      <div class="table-responsive" v-if="interfaces && interfaces.length > 0">
        <table class="table table-striped">
          <thead>
            <tr>
              <th scope="row"><span>Interface</span></th>
              <th scope="row"><span>Source</span></th>
              <th scope="row"><span>Target</span></th>
              <th scope="row"><span>Protocol</span></th>
              <th scope="row"></th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="inter in interfaces" v-bind:key="inter.id">
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
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./applications-diagram.component.ts"></script>
