<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2 id="eaDesignItApp.dataFlowItem.home.createOrEditLabel" data-cy="DataFlowItemCreateUpdateHeading">
          <font-awesome-icon icon="clone" style="color: Tomato; font-size: 0.7em"></font-awesome-icon> Create or edit a DataFlowItem
        </h2>
        <div>
          <div class="form-group" v-if="dataFlowItem.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="dataFlowItem.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="data-flow-item-resourceName">Resource Name</label>
            <input
              type="text"
              class="form-control"
              name="resourceName"
              id="data-flow-item-resourceName"
              data-cy="resourceName"
              :class="{ valid: !$v.dataFlowItem.resourceName.$invalid, invalid: $v.dataFlowItem.resourceName.$invalid }"
              v-model="$v.dataFlowItem.resourceName.$model"
              required
            />
            <div v-if="$v.dataFlowItem.resourceName.$anyDirty && $v.dataFlowItem.resourceName.$invalid">
              <small class="form-text text-danger" v-if="!$v.dataFlowItem.resourceName.required"> This field is required. </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="data-flow-item-resourceType">Resource Type</label>
            <input
              type="text"
              class="form-control"
              name="resourceType"
              id="data-flow-item-resourceType"
              data-cy="resourceType"
              :class="{ valid: !$v.dataFlowItem.resourceType.$invalid, invalid: $v.dataFlowItem.resourceType.$invalid }"
              v-model="$v.dataFlowItem.resourceType.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="data-flow-item-description">Description</label>
            <input
              type="text"
              class="form-control"
              name="description"
              id="data-flow-item-description"
              data-cy="description"
              :class="{ valid: !$v.dataFlowItem.description.$invalid, invalid: $v.dataFlowItem.description.$invalid }"
              v-model="$v.dataFlowItem.description.$model"
            />
            <div v-if="$v.dataFlowItem.description.$anyDirty && $v.dataFlowItem.description.$invalid">
              <small class="form-text text-danger" v-if="!$v.dataFlowItem.description.maxLength">
                This field cannot be longer than 1000 characters.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="data-flow-item-contractURL">Contract URL</label>
            <input
              type="text"
              class="form-control"
              name="contractURL"
              id="data-flow-item-contractURL"
              data-cy="contractURL"
              :class="{ valid: !$v.dataFlowItem.contractURL.$invalid, invalid: $v.dataFlowItem.contractURL.$invalid }"
              v-model="$v.dataFlowItem.contractURL.$model"
            />
            <div v-if="$v.dataFlowItem.contractURL.$anyDirty && $v.dataFlowItem.contractURL.$invalid">
              <small class="form-text text-danger" v-if="!$v.dataFlowItem.contractURL.maxLength">
                This field cannot be longer than 500 characters.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="data-flow-item-documentationURL">Documentation URL</label>
            <input
              type="text"
              class="form-control"
              name="documentationURL"
              id="data-flow-item-documentationURL"
              data-cy="documentationURL"
              :class="{ valid: !$v.dataFlowItem.documentationURL.$invalid, invalid: $v.dataFlowItem.documentationURL.$invalid }"
              v-model="$v.dataFlowItem.documentationURL.$model"
            />
            <div v-if="$v.dataFlowItem.documentationURL.$anyDirty && $v.dataFlowItem.documentationURL.$invalid">
              <small class="form-text text-danger" v-if="!$v.dataFlowItem.documentationURL.maxLength">
                This field cannot be longer than 500 characters.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="data-flow-item-startDate">Start Date</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="data-flow-item-startDate"
                  v-model="$v.dataFlowItem.startDate.$model"
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
                id="data-flow-item-startDate"
                data-cy="startDate"
                type="text"
                class="form-control"
                name="startDate"
                :class="{ valid: !$v.dataFlowItem.startDate.$invalid, invalid: $v.dataFlowItem.startDate.$invalid }"
                v-model="$v.dataFlowItem.startDate.$model"
              />
            </b-input-group>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="data-flow-item-endDate">End Date</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="data-flow-item-endDate"
                  v-model="$v.dataFlowItem.endDate.$model"
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
                id="data-flow-item-endDate"
                data-cy="endDate"
                type="text"
                class="form-control"
                name="endDate"
                :class="{ valid: !$v.dataFlowItem.endDate.$invalid, invalid: $v.dataFlowItem.endDate.$invalid }"
                v-model="$v.dataFlowItem.endDate.$model"
              />
            </b-input-group>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="data-flow-item-dataFlow">Data Flow</label>
            <select class="form-control" id="data-flow-item-dataFlow" data-cy="dataFlow" name="dataFlow" v-model="dataFlowItem.dataFlow">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="
                  dataFlowItem.dataFlow && dataFlowOption.id === dataFlowItem.dataFlow.id ? dataFlowItem.dataFlow : dataFlowOption
                "
                v-for="dataFlowOption in dataFlows"
                :key="dataFlowOption.id"
              >
                {{ dataFlowOption.id }}
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
            :disabled="$v.dataFlowItem.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span>Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./data-flow-item-update.component.ts"></script>
