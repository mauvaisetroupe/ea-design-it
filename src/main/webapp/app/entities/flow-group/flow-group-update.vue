<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2 id="eaDesignItApp.flowGroup.home.createOrEditLabel" data-cy="FlowGroupCreateUpdateHeading">Create or edit a FlowGroup</h2>
        <div>
          <div class="form-group" v-if="flowGroup.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="flowGroup.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="flow-group-title">Title</label>
            <input
              type="text"
              class="form-control"
              name="title"
              id="flow-group-title"
              data-cy="title"
              :class="{ valid: !$v.flowGroup.title.$invalid, invalid: $v.flowGroup.title.$invalid }"
              v-model="$v.flowGroup.title.$model"
            />
            <div v-if="$v.flowGroup.title.$anyDirty && $v.flowGroup.title.$invalid">
              <small class="form-text text-danger" v-if="!$v.flowGroup.title.maxLength">
                This field cannot be longer than 100 characters.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="flow-group-url">Url</label>
            <input
              type="text"
              class="form-control"
              name="url"
              id="flow-group-url"
              data-cy="url"
              :class="{ valid: !$v.flowGroup.url.$invalid, invalid: $v.flowGroup.url.$invalid }"
              v-model="$v.flowGroup.url.$model"
            />
            <div v-if="$v.flowGroup.url.$anyDirty && $v.flowGroup.url.$invalid">
              <small class="form-text text-danger" v-if="!$v.flowGroup.url.maxLength">
                This field cannot be longer than 500 characters.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="flow-group-description">Description</label>
            <input
              type="text"
              class="form-control"
              name="description"
              id="flow-group-description"
              data-cy="description"
              :class="{ valid: !$v.flowGroup.description.$invalid, invalid: $v.flowGroup.description.$invalid }"
              v-model="$v.flowGroup.description.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="flow-group-flow">Flow</label>
            <select class="form-control" id="flow-group-flow" data-cy="flow" name="flow" v-model="flowGroup.flow">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="flowGroup.flow && functionalFlowOption.id === flowGroup.flow.id ? flowGroup.flow : functionalFlowOption"
                v-for="functionalFlowOption in functionalFlows"
                :key="functionalFlowOption.id"
              >
                {{ functionalFlowOption.alias }} - {{ functionalFlowOption.description }}
              </option>
            </select>
          </div>
          <div v-if="$v.flowGroup.steps.$anyDirty && $v.flowGroup.steps.$invalid">
            <small class="form-text text-danger" v-if="!$v.flowGroup.steps.required"> This field is required. </small>
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
            :disabled="$v.flowGroup.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span>Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./flow-group-update.component.ts"></script>
