<template>
  <div class="row justify-content-center">
    <div class="col-12">
      <b-tabs content-class="mt-3" card pills>
        <b-tab title="1. Edit information" active>
          <div>
            <div class="form-group row" v-if="functionalFlow.id">
              <label for="id" class="col-sm-2 col-form-label">ID</label>
              <input type="text" class="form-control col-sm-10" id="id" name="id" v-model="functionalFlow.id" readonly />
            </div>
            <div class="form-group row">
              <label class="col-sm-2 col-form-label" for="functional-flow-alias">Alias</label>
              <input
                type="text"
                class="form-control col-sm-10"
                name="alias"
                id="functional-flow-alias"
                data-cy="alias"
                :class="{ valid: !$v.functionalFlow.alias.$invalid, invalid: $v.functionalFlow.alias.$invalid }"
                v-model="$v.functionalFlow.alias.$model"
              />
              <div v-if="$v.functionalFlow.alias.$anyDirty && $v.functionalFlow.alias.$invalid"></div>
            </div>
            <div class="form-group row">
              <label class="col-sm-2 col-form-label" for="functional-flow-description">Description</label>
              <input
                type="text"
                class="form-control col-sm-10"
                name="description"
                id="functional-flow-description"
                data-cy="description"
                :class="{ valid: !$v.functionalFlow.description.$invalid, invalid: $v.functionalFlow.description.$invalid }"
                v-model="$v.functionalFlow.description.$model"
              />
              <div v-if="$v.functionalFlow.description.$anyDirty && $v.functionalFlow.description.$invalid">
                <small class="form-text text-danger" v-if="!$v.functionalFlow.description.maxLength">
                  This field cannot be longer than 1500 characters.
                </small>
              </div>
            </div>
            <div class="form-group row">
              <label class="col-sm-2 col-form-label" for="functional-flow-comment">Comment</label>
              <input
                type="text"
                class="form-control col-sm-10"
                name="comment"
                id="functional-flow-comment"
                data-cy="comment"
                :class="{ valid: !$v.functionalFlow.comment.$invalid, invalid: $v.functionalFlow.comment.$invalid }"
                v-model="$v.functionalFlow.comment.$model"
              />
              <div v-if="$v.functionalFlow.comment.$anyDirty && $v.functionalFlow.comment.$invalid">
                <small class="form-text text-danger" v-if="!$v.functionalFlow.comment.maxLength">
                  This field cannot be longer than 1000 characters.
                </small>
              </div>
            </div>
            <div class="form-group row">
              <label class="col-sm-2 col-form-label" for="functional-flow-status">Status</label>
              <input
                type="text"
                class="form-control col-sm-10"
                name="status"
                id="functional-flow-status"
                data-cy="status"
                :class="{ valid: !$v.functionalFlow.status.$invalid, invalid: $v.functionalFlow.status.$invalid }"
                v-model="$v.functionalFlow.status.$model"
              />
            </div>
            <div class="form-group row">
              <label class="col-sm-2 col-form-label" for="functional-flow-documentationURL">Documentation URL</label>
              <input
                type="text"
                class="form-control col-sm-10"
                name="documentationURL"
                id="functional-flow-documentationURL"
                data-cy="documentationURL"
                :class="{ valid: !$v.functionalFlow.documentationURL.$invalid, invalid: $v.functionalFlow.documentationURL.$invalid }"
                v-model="$v.functionalFlow.documentationURL.$model"
              />
              <div v-if="$v.functionalFlow.documentationURL.$anyDirty && $v.functionalFlow.documentationURL.$invalid">
                <small class="form-text text-danger" v-if="!$v.functionalFlow.documentationURL.maxLength">
                  This field cannot be longer than 500 characters.
                </small>
              </div>
            </div>
            <div class="form-group row">
              <label class="col-sm-2 col-form-label" for="functional-flow-documentationURL2">Documentation URL 2</label>
              <input
                type="text"
                class="form-control col-sm-10"
                name="documentationURL2"
                id="functional-flow-documentationURL2"
                data-cy="documentationURL2"
                :class="{ valid: !$v.functionalFlow.documentationURL2.$invalid, invalid: $v.functionalFlow.documentationURL2.$invalid }"
                v-model="$v.functionalFlow.documentationURL2.$model"
              />
              <div v-if="$v.functionalFlow.documentationURL2.$anyDirty && $v.functionalFlow.documentationURL2.$invalid">
                <small class="form-text text-danger" v-if="!$v.functionalFlow.documentationURL2.maxLength">
                  This field cannot be longer than 500 characters.
                </small>
              </div>
            </div>
            <div class="form-group row">
              <label class="col-sm-2 col-form-label" for="functional-flow-startDate">Start Date</label>
              <b-input-group class="col-sm-10">
                <b-input-group-prepend>
                  <b-form-datepicker
                    aria-controls="functional-flow-startDate"
                    v-model="$v.functionalFlow.startDate.$model"
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
                  :class="{ valid: !$v.functionalFlow.startDate.$invalid, invalid: $v.functionalFlow.startDate.$invalid }"
                  v-model="$v.functionalFlow.startDate.$model"
                />
              </b-input-group>
            </div>
            <div class="form-group row">
              <label class="col-sm-2 col-form-label" for="functional-flow-endDate">End Date</label>
              <b-input-group class="col-sm-10">
                <b-input-group-prepend>
                  <b-form-datepicker
                    aria-controls="functional-flow-endDate"
                    v-model="$v.functionalFlow.endDate.$model"
                    name="endDate"
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
                  :class="{ valid: !$v.functionalFlow.endDate.$invalid, invalid: $v.functionalFlow.endDate.$invalid }"
                  v-model="$v.functionalFlow.endDate.$model"
                />
              </b-input-group>
            </div>
            <div class="form-group row">
              <label class="col-sm-2 col-form-label" for="functional-flow-owner">Owner</label>
              <select class="form-control col-sm-10" id="functional-flow-owner" data-cy="owner" name="owner" v-model="functionalFlow.owner">
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
          </div>
        </b-tab>
        <b-tab title="2. Modify plantuml">
          <h2 id="page-heading" data-cy="FlowImportHeading">Import Flow from Plantuml Sequence Diagram</h2>
          <div class="row">
            <div class="col-10">
              <textarea style="width: 100%; min-width: 600px" rows="10" v-model="plantuml"></textarea>
            </div>
            <div>
              <button type="submit" class="btn btn-primary" data-cy="submit" @click="getPlantUMLImageFromString">Update Diagram</button>
            </div>
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
          </div>
        </b-tab>
      </b-tabs>
    </div>

    <div class="col-12">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2 id="eaDesignItApp.functionalFlow.home.createOrEditLabel" data-cy="FunctionalFlowCreateUpdateHeading">
          <font-awesome-icon icon="project-diagram" style="color: Tomato; font-size: 0.7em"></font-awesome-icon> Create or edit a
          FunctionalFlow
          <span v-if="this.$route.query.landscapeViewId"> for landscape {{ this.$route.query.landscapeViewId }}</span>
        </h2>

        <div>
          <button type="button" id="cancel-save" data-cy="entityCreateCancelButton" class="btn btn-secondary" v-on:click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span>Cancel</span>
          </button>
          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="$v.functionalFlow.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span>Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./functional-flow-update.component.ts"></script>
