<template>
  <div>
    <h2 id="page-heading" data-cy="FlowImportHeading">Import Flow from Plantuml Sequence Diagram</h2>

    <div>
      <textarea style="width: 100%; min-width: 600px" rows="30" v-model="plantuml"></textarea>
    </div>
    <div>
      <button type="Submit" class="btn btn-primary" data-cy="submit" @click="getPlantUML">Preview</button>
      <button type="submit" class="btn btn-primary" data-cy="submit" @click="importPlantUML">Import</button>
    </div>
    <div v-html="plantUMLImage" class="table-responsive"></div>
    <div class="table-responsive" v-if="functionalFlow.steps && functionalFlow.steps.length > 0">
      <table class="table table-striped">
        <thead>
          <tr>
            <th scope="row" v-if="!notPersisted"><span>Step</span></th>
            <th scope="row" v-if="reorderAlias"></th>
            <th scope="row" v-if="reorderAlias"></th>
            <th scope="row"><span>Interface</span></th>
            <th scope="row"><span>Source</span></th>
            <th scope="row"><span>Target</span></th>
            <th scope="row"><span>Protocol</span></th>
            <th scope="row"><span>Data Flows</span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(step, i) in functionalFlow.steps" v-bind:key="step.id" :set="(inter = step.flowInterface)">
            <td v-if="!notPersisted">
              {{ step.stepOrder }}.
              <span v-if="!reorderAlias">{{ step.description }}</span>
              <span v-else>
                <textarea
                  style="width: 100%; min-width: 600px"
                  rows="1"
                  v-model="step.description"
                  @change="changeStepDescription(functionalFlow, step)"
                ></textarea>
              </span>
            </td>
            <td v-if="reorderAlias">
              <font-awesome-icon icon="chevron-up" class="btn-success" v-if="i != 0" @click="swap(i, i - 1)"></font-awesome-icon>
            </td>
            <td v-if="reorderAlias">
              <font-awesome-icon
                icon="chevron-down"
                class="btn-success"
                v-if="i != functionalFlow.steps.length - 1"
                @click="swap(i, i + 1)"
              ></font-awesome-icon>
            </td>
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
                <router-link
                  :to="{ name: 'DataFlowView', params: { dataFlowId: dataflow.id } }"
                  :title="dataflow.resourceName + (dataflow.items.length > 0 ? ' / ' + dataflow.items.length + ' items ' : ' / no items')"
                  >{{ dataflow.id }}</router-link
                ><sup v-if="dataflow.items && dataflow.items.length > 0">({{ dataflow.items.length }})</sup>&nbsp;
              </span>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script lang="ts" src="./import.component.ts"></script>
