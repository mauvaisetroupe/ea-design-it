<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2 id="eaDesignItApp.dataFlow.home.createOrEditLabel" data-cy="DataFlowCreateUpdateHeading">Create or edit a DataFlow</h2>
        <div>
          <div class="form-group" v-if="dataFlow.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="dataFlow.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="data-flow-resourceName">Resource Name</label>
            <input
              type="text"
              class="form-control"
              name="resourceName"
              id="data-flow-resourceName"
              data-cy="resourceName"
              :class="{ valid: !$v.dataFlow.resourceName.$invalid, invalid: $v.dataFlow.resourceName.$invalid }"
              v-model="$v.dataFlow.resourceName.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="data-flow-resourceType">Resource Type</label>
            <input
              type="text"
              class="form-control"
              name="resourceType"
              id="data-flow-resourceType"
              data-cy="resourceType"
              :class="{ valid: !$v.dataFlow.resourceType.$invalid, invalid: $v.dataFlow.resourceType.$invalid }"
              v-model="$v.dataFlow.resourceType.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="data-flow-description">Description</label>
            <input
              type="text"
              class="form-control"
              name="description"
              id="data-flow-description"
              data-cy="description"
              :class="{ valid: !$v.dataFlow.description.$invalid, invalid: $v.dataFlow.description.$invalid }"
              v-model="$v.dataFlow.description.$model"
            />
            <div v-if="$v.dataFlow.description.$anyDirty && $v.dataFlow.description.$invalid">
              <small class="form-text text-danger" v-if="!$v.dataFlow.description.maxLength">
                This field cannot be longer than 1000 characters.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="data-flow-frequency">Frequency</label>
            <select
              class="form-control"
              name="frequency"
              :class="{ valid: !$v.dataFlow.frequency.$invalid, invalid: $v.dataFlow.frequency.$invalid }"
              v-model="$v.dataFlow.frequency.$model"
              id="data-flow-frequency"
              data-cy="frequency"
            >
              <option v-for="frequency in frequencyValues" :key="frequency" v-bind:value="frequency">{{ frequency }}</option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="data-flow-contractURL">Contract URL</label>
            <input
              type="text"
              class="form-control"
              name="contractURL"
              id="data-flow-contractURL"
              data-cy="contractURL"
              :class="{ valid: !$v.dataFlow.contractURL.$invalid, invalid: $v.dataFlow.contractURL.$invalid }"
              v-model="$v.dataFlow.contractURL.$model"
            />
            <div v-if="$v.dataFlow.contractURL.$anyDirty && $v.dataFlow.contractURL.$invalid">
              <small class="form-text text-danger" v-if="!$v.dataFlow.contractURL.maxLength">
                This field cannot be longer than 500 characters.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="data-flow-documentationURL">Documentation URL</label>
            <input
              type="text"
              class="form-control"
              name="documentationURL"
              id="data-flow-documentationURL"
              data-cy="documentationURL"
              :class="{ valid: !$v.dataFlow.documentationURL.$invalid, invalid: $v.dataFlow.documentationURL.$invalid }"
              v-model="$v.dataFlow.documentationURL.$model"
            />
            <div v-if="$v.dataFlow.documentationURL.$anyDirty && $v.dataFlow.documentationURL.$invalid">
              <small class="form-text text-danger" v-if="!$v.dataFlow.documentationURL.maxLength">
                This field cannot be longer than 500 characters.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="data-flow-startDate">Start Date</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="data-flow-startDate"
                  v-model="$v.dataFlow.startDate.$model"
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
                id="data-flow-startDate"
                data-cy="startDate"
                type="text"
                class="form-control"
                name="startDate"
                :class="{ valid: !$v.dataFlow.startDate.$invalid, invalid: $v.dataFlow.startDate.$invalid }"
                v-model="$v.dataFlow.startDate.$model"
              />
            </b-input-group>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="data-flow-endDate">End Date</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="data-flow-endDate"
                  v-model="$v.dataFlow.endDate.$model"
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
                id="data-flow-endDate"
                data-cy="endDate"
                type="text"
                class="form-control"
                name="endDate"
                :class="{ valid: !$v.dataFlow.endDate.$invalid, invalid: $v.dataFlow.endDate.$invalid }"
                v-model="$v.dataFlow.endDate.$model"
              />
            </b-input-group>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="data-flow-format">Format</label>
            <select class="form-control" id="data-flow-format" data-cy="format" name="format" v-model="dataFlow.format">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="dataFlow.format && dataFormatOption.id === dataFlow.format.id ? dataFlow.format : dataFormatOption"
                v-for="dataFormatOption in dataFormats"
                :key="dataFormatOption.id"
              >
                {{ dataFormatOption.name }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label for="data-flow-functionalFlows">Functional Flows</label>
            <select
              class="form-control"
              id="data-flow-functionalFlows"
              data-cy="functionalFlows"
              multiple
              name="functionalFlows"
              v-if="dataFlow.functionalFlows !== undefined"
              v-model="dataFlow.functionalFlows"
            >
              <option
                v-bind:value="getSelected(dataFlow.functionalFlows, functionalFlowOption)"
                v-for="functionalFlowOption in functionalFlows"
                :key="functionalFlowOption.id"
              >
                {{ functionalFlowOption.alias }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="data-flow-flowInterface">Flow Interface</label>
            <select
              class="form-control"
              id="data-flow-flowInterface"
              data-cy="flowInterface"
              name="flowInterface"
              v-model="dataFlow.flowInterface"
              required
            >
              <option v-if="!dataFlow.flowInterface" v-bind:value="null" selected></option>
              <option
                v-bind:value="
                  dataFlow.flowInterface && flowInterfaceOption.id === dataFlow.flowInterface.id
                    ? dataFlow.flowInterface
                    : flowInterfaceOption
                "
                v-for="flowInterfaceOption in flowInterfaces"
                :key="flowInterfaceOption.id"
              >
                {{ flowInterfaceOption.alias }}
              </option>
            </select>
          </div>
          <div v-if="$v.dataFlow.flowInterface.$anyDirty && $v.dataFlow.flowInterface.$invalid">
            <small class="form-text text-danger" v-if="!$v.dataFlow.flowInterface.required"> This field is required. </small>
          </div>
        </div>
        <div>
          <button type="button" id="cancel-save" data-cy="entityCreateCancelButton" class="btn btn-secondary" v-on:click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span>Cancel</span>
          </button>
          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="$v.dataFlow.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span>Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./data-flow-update.component.ts"></script>
