<template>
  <div>
    <h2 id="page-heading" data-cy="FlowImportHeading">Import Flow from Plantuml Sequence Diagram</h2>

    <div>
      <textarea style="width: 100%; min-width: 600px" rows="30" v-model="plantuml"></textarea>
    </div>
    <div>
      <button type="submit" class="btn btn-primary" data-cy="submit" @click="importPlantUML">Import</button>
    </div>
    <div v-html="plantUMLImage" class="table-responsive"></div>
    <div v-if="importError" class="alert alert-danger">Error during import</div>
    <h2 v-if="functionalFlow">
      {{ functionalFlow.description }}
    </h2>
    <div class="table-responsive" v-if="functionalFlow && functionalFlow.flowImportLines && functionalFlow.flowImportLines.length > 0">
      <table class="table table-striped">
        <thead>
          <tr>
            <th scope="row"><span>Step</span></th>
            <th scope="row"><span>Interface</span></th>
            <th scope="row"><span>Source</span></th>
            <th scope="row"><span>Target</span></th>
            <th scope="row"><span>Protocol</span></th>
            <th scope="row"><span>Data Flows</span></th>
            <th scope="row"><span>Contract URL</span></th>
            <th scope="row"><span>Document URL</span></th>
            <th scope="row"><span>Frequency</span></th>
            <th scope="row"><span>Format</span></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="step in functionalFlow.flowImportLines" v-bind:key="step.id">
            <td>
              {{ step.order }}.
              <span>{{ step.description }}</span>
            </td>
            <td>
              <select v-if="step.potentialInterfaces" v-model="step.selectedInterface" @change="changeInterface(step)">
                <option value=""></option>
                <option v-for="inter in step.potentialInterfaces" :key="inter.id" :value="inter">{{ inter.alias }}</option>
              </select>
            </td>
            <td>
              <router-link :to="{ name: 'ApplicationView', params: { applicationId: step.source.id } }" v-if="step.source">
                {{ step.source.name }}
              </router-link>
              <span v-else class="alert alert-danger">Not imported</span>
            </td>
            <td>
              <router-link :to="{ name: 'ApplicationView', params: { applicationId: step.target.id } }" v-if="step.target">
                {{ step.target.name }}
              </router-link>
              <span v-else class="alert alert-danger">Not imported</span>
            </td>
            <td>
              <router-link v-if="step.protocol" :to="{ name: 'ProtocolView', params: { protocolId: step.protocol.id } }">
                {{ step.protocol.name }}
              </router-link>
              <span v-else class="alert alert-warning">Not imported</span>
            </td>
            <td>
              <select v-if="step.potentialDataFlows" v-model="step.selectedDataFlow">
                <option value=""></option>
                <option v-for="dataFlow in step.potentialDataFlows" :key="dataFlow.id" :value="dataFlow">{{ dataFlow.id }}</option>
              </select>
            </td>
            <td>
              <span v-if="step.selectedDataFlow && step.selectedDataFlow.contractURL"
                >{{ step.selectedDataFlow.contractURL.substring(0, 50) }}...</span
              >
            </td>
            <td>
              <span v-if="step.selectedDataFlow && step.selectedDataFlow.documentationURL"
                >{{ step.selectedDataFlow.documentationURL.substring(0, 50) }}...</span
              >
            </td>
            <td>
              <span v-if="step.selectedDataFlow">{{ step.selectedDataFlow.frequency }}</span>
            </td>
            <td>
              <span v-if="step.selectedDataFlow && step.selectedDataFlow.format">{{ step.selectedDataFlow.format.name }}</span>
            </td>
          </tr>
        </tbody>
      </table>
      <div>
        <label for="landscape">Choose corresponding landscape to attache flow</label>
        <select v-model="selectedLandscape">
          <option v-for="landscape in existingLandscapes" :value="landscape.id" :key="landscape.id">
            {{ landscape.diagramName }}
          </option>
        </select>
      </div>
    </div>
    <div>
      <button
        type="submit"
        :disabled="!functionalFlow || functionalFlow.onError"
        class="btn btn-primary"
        data-cy="submit"
        @click="saveImport"
      >
        Save
      </button>
    </div>
  </div>
</template>

<script lang="ts" src="./import.component.ts"></script>
