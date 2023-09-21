<template>
  <div>
    <div class="row">
      <div class="col-10">
        <h2 id="eaDesignItApp.functionalFlow.home.createOrEditLabel" data-cy="FunctionalFlowCreateUpdateHeading">
          <font-awesome-icon icon="project-diagram" style="color: Tomato; font-size: 0.7em"></font-awesome-icon> Create or edit a
          FunctionalFlow
          <span v-if="this.$route.query.landscapeViewId"> for landscape {{ this.$route.query.landscapeViewId }}</span>
        </h2>
      </div>
      <div>
        <button type="button" id="cancel-save" data-cy="entityCreateCancelButton" class="btn btn-secondary" v-on:click="previousState()">
          <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span>Cancel</span>
        </button>
        <button type="submit" id="save-entity" data-cy="entityCreateSaveButton" :disabled="isSaving" class="btn btn-primary">
          <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span>Save</span>
        </button>
      </div>
    </div>
    <div class="col-12">
      <b-tabs content-class="mt-3" card pills>
        <b-tab title="1. Edit information" active>
          <div v-if="functionalFlow">
            <dl class="row jh-entity-details">
              <dt>
                <span>Alias</span>
              </dt>
              <dd>
                <input
                  type="text"
                  class="form-control"
                  name="alias"
                  id="functional-flow-alias"
                  data-cy="alias"
                  v-model="functionalFlow.alias"
                />
              </dd>
              <dt>
                <span>Description</span>
              </dt>
              <dd>
                <input
                  type="text"
                  class="form-control"
                  name="description"
                  id="functional-flow-description"
                  data-cy="description"
                  v-model="functionalFlow.description"
                />
              </dd>
              <dt>
                <span>Comment</span>
              </dt>
              <dd>
                <input
                  type="text"
                  class="form-control"
                  name="comment"
                  id="functional-flow-comment"
                  data-cy="comment"
                  v-model="functionalFlow.comment"
                />
              </dd>
              <dt>
                <span>Status</span>
              </dt>
              <dd>
                <input
                  type="text"
                  class="form-control"
                  name="status"
                  id="functional-flow-status"
                  data-cy="status"
                  v-model="functionalFlow.status"
                />
              </dd>
              <dt>
                <span>Documentation URL</span>
              </dt>
              <dd>
                <input
                  type="text"
                  class="form-control"
                  name="documentationURL"
                  id="functional-flow-documentationURL"
                  data-cy="documentationURL"
                  v-model="functionalFlow.documentationURL"
                />
              </dd>
              <dt>
                <span>Documentation URL 2</span>
              </dt>
              <dd>
                <input
                  type="text"
                  class="form-control"
                  name="documentationURL2"
                  id="functional-flow-documentationURL2"
                  data-cy="documentationURL2"
                  v-model="functionalFlow.documentationURL2"
                />
              </dd>
              <dt>
                <span>Start Date</span>
              </dt>
              <dd>
                <b-input-group class="mb-3">
                  <b-input-group-prepend>
                    <b-form-datepicker
                      aria-controls="functional-flow-startDate"
                      v-model="functionalFlow.startDate"
                      name="startDate"
                      class="form-control"
                      :locale="currentLanguage"
                      button-only
                      today-button
                      reset-button
                      close-button
                    >
                    </b-form-datepicker>
                  </b-input-group-prepend>
                  <b-form-input
                    id="functional-flow-startDate"
                    data-cy="startDate"
                    type="text"
                    class="form-control"
                    name="startDate"
                    v-model="functionalFlow.startDate"
                  />
                </b-input-group>
              </dd>
              <dt>
                <span>End Date</span>
              </dt>
              <dd>
                <b-input-group class="mb-3">
                  <b-input-group-prepend>
                    <b-form-datepicker
                      aria-controls="functional-flow-endDate"
                      v-model="functionalFlow.endDate"
                      name="endtDate"
                      class="form-control"
                      :locale="currentLanguage"
                      button-only
                      today-button
                      reset-button
                      close-button
                    >
                    </b-form-datepicker>
                  </b-input-group-prepend>
                  <b-form-input
                    id="functional-flow-endDate"
                    data-cy="endDate"
                    type="text"
                    class="form-control"
                    name="endDate"
                    v-model="functionalFlow.endDate"
                  />
                </b-input-group>
              </dd>
              <dt>
                <span>Owner</span>
              </dt>
              <dd>
                <div v-if="functionalFlow.owner">
                  <select class="form-control" id="functional-flow-owner" data-cy="owner" name="owner" v-model="functionalFlow.owner">
                    <option v-bind:value="null"></option>
                    <option
                      v-bind:value="functionalFlow.owner && ownerOption.id === functionalFlow.owner.id ? functionalFlow.owner : ownerOption"
                      v-for="ownerOption in owners"
                      :key="ownerOption.id"
                    >
                      {{ ownerOption.name }}
                    </option>
                  </select>
                </div>
              </dd>
            </dl>
          </div>
        </b-tab>
        <b-tab title="2. Modify plantuml">
          <h2 id="page-heading" data-cy="FlowImportHeading">Import Flow from Plantuml Sequence Diagram</h2>
          <div></div>
          <div>
            <textarea style="width: 70%; min-width: 600px" rows="30" v-model="plantuml"></textarea>
          </div>
          <div>
            <button type="submit" class="btn btn-primary" data-cy="submit" @click="getPlantUMLImageFromString">Import</button>
          </div>
          <div v-html="plantUMLImage" class="table-responsive"></div>
          <div v-if="importError" class="alert alert-danger">Error during import</div>
        </b-tab>
        <b-tab title="3. Choose interfaces">
          <h2 v-if="functionalFlowImport">
            {{ functionalFlow.description }}
          </h2>
          <div
            class="table-responsive"
            v-if="functionalFlowImport && functionalFlowImport.flowImportLines && functionalFlowImport.flowImportLines.length > 0"
          >
            <table class="table table-striped">
              <thead>
                <tr>
                  <th scope="row"><span>#</span></th>
                  <th scope="row"><span>Step</span></th>
                  <th scope="row"><span>Source</span></th>
                  <th scope="row"><span>Target</span></th>
                  <th scope="row"><span>Protocol</span></th>
                  <th scope="row"><span>Interface</span></th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="step in functionalFlowImport.flowImportLines" v-bind:key="step.id">
                  <td>{{ step.order }}</td>
                  <td>
                    <span>{{ step.description }}</span>
                  </td>
                  <td>
                    <span v-if="step.source">
                      {{ step.source.name }}
                    </span>
                    <span v-else class="alert alert-danger">Not imported</span>
                  </td>
                  <td>
                    <span v-if="step.target">
                      {{ step.target.name }}
                    </span>
                    <span v-else class="alert alert-danger">Not imported</span>
                  </td>
                  <td>
                    <router-link v-if="step.protocol" :to="{ name: 'ProtocolView', params: { protocolId: step.protocol.id } }">
                      {{ step.protocol.name }}
                    </router-link>
                    <span v-else class="alert alert-warning">Not imported</span>
                  </td>
                  <td>
                    <select v-if="step.potentialInterfaces" v-model="step.selectedInterface" @change="changeInterface(step)">
                      <option value=""></option>
                      <option v-for="inter in step.potentialInterfaces" :key="inter.id" :value="inter">
                        {{ inter.alias }} ({{ inter.protocol.name }})
                      </option>
                    </select>
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
        </b-tab>
      </b-tabs>
    </div>
  </div>
</template>

<script lang="ts" src="./import.component.ts"></script>
