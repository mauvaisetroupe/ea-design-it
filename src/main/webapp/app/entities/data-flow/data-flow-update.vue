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
            <label class="form-control-label" for="data-flow-format">Format</label>
            <select
              class="form-control"
              name="format"
              :class="{ valid: !$v.dataFlow.format.$invalid, invalid: $v.dataFlow.format.$invalid }"
              v-model="$v.dataFlow.format.$model"
              id="data-flow-format"
              data-cy="format"
            >
              <option v-for="format in formatValues" :key="format" v-bind:value="format">{{ format }}</option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="data-flow-type">Type</label>
            <select
              class="form-control"
              name="type"
              :class="{ valid: !$v.dataFlow.type.$invalid, invalid: $v.dataFlow.type.$invalid }"
              v-model="$v.dataFlow.type.$model"
              id="data-flow-type"
              data-cy="type"
            >
              <option v-for="flowType in flowTypeValues" :key="flowType" v-bind:value="flowType">{{ flowType }}</option>
            </select>
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
              required
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
          <div v-if="$v.dataFlow.functionalFlows.$anyDirty && $v.dataFlow.functionalFlows.$invalid">
            <small class="form-text text-danger" v-if="!$v.dataFlow.functionalFlows.required"> This field is required. </small>
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
