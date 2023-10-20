<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2 id="eaDesignItApp.dataFlow.home.createOrEditLabel" data-cy="DataFlowCreateUpdateHeading">Create or edit a Data Flow</h2>
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
              :class="{ valid: !v$.resourceName.$invalid, invalid: v$.resourceName.$invalid }"
              v-model="v$.resourceName.$model"
              required
            />
            <div v-if="v$.resourceName.$anyDirty && v$.resourceName.$invalid">
              <small class="form-text text-danger" v-for="error of v$.resourceName.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="data-flow-resourceType">Resource Type</label>
            <input
              type="text"
              class="form-control"
              name="resourceType"
              id="data-flow-resourceType"
              data-cy="resourceType"
              :class="{ valid: !v$.resourceType.$invalid, invalid: v$.resourceType.$invalid }"
              v-model="v$.resourceType.$model"
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
              :class="{ valid: !v$.description.$invalid, invalid: v$.description.$invalid }"
              v-model="v$.description.$model"
            />
            <div v-if="v$.description.$anyDirty && v$.description.$invalid">
              <small class="form-text text-danger" v-for="error of v$.description.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="data-flow-frequency">Frequency</label>
            <select
              class="form-control"
              name="frequency"
              :class="{ valid: !v$.frequency.$invalid, invalid: v$.frequency.$invalid }"
              v-model="v$.frequency.$model"
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
              :class="{ valid: !v$.contractURL.$invalid, invalid: v$.contractURL.$invalid }"
              v-model="v$.contractURL.$model"
            />
            <div v-if="v$.contractURL.$anyDirty && v$.contractURL.$invalid">
              <small class="form-text text-danger" v-for="error of v$.contractURL.$errors" :key="error.$uid">{{ error.$message }}</small>
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
              :class="{ valid: !v$.documentationURL.$invalid, invalid: v$.documentationURL.$invalid }"
              v-model="v$.documentationURL.$model"
            />
            <div v-if="v$.documentationURL.$anyDirty && v$.documentationURL.$invalid">
              <small class="form-text text-danger" v-for="error of v$.documentationURL.$errors" :key="error.$uid">{{
                error.$message
              }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="data-flow-startDate">Start Date</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="data-flow-startDate"
                  v-model="v$.startDate.$model"
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
                :class="{ valid: !v$.startDate.$invalid, invalid: v$.startDate.$invalid }"
                v-model="v$.startDate.$model"
              />
            </b-input-group>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="data-flow-endDate">End Date</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="data-flow-endDate"
                  v-model="v$.endDate.$model"
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
                :class="{ valid: !v$.endDate.$invalid, invalid: v$.endDate.$invalid }"
                v-model="v$.endDate.$model"
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
            >
              <option v-bind:value="null"></option>
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
        </div>
        <div>
          <button type="button" id="cancel-save" data-cy="entityCreateCancelButton" class="btn btn-secondary" v-on:click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span>Cancel</span>
          </button>
          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="v$.$invalid || isSaving"
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
