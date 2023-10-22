<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2 id="eaDesignItApp.flowGroup.home.createOrEditLabel" data-cy="FlowGroupCreateUpdateHeading">Create or edit a Flow Group</h2>
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
              :class="{ valid: !v$.title.$invalid, invalid: v$.title.$invalid }"
              v-model="v$.title.$model"
            />
            <div v-if="v$.title.$anyDirty && v$.title.$invalid">
              <small class="form-text text-danger" v-for="error of v$.title.$errors" :key="error.$uid">{{ error.$message }}</small>
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
              :class="{ valid: !v$.url.$invalid, invalid: v$.url.$invalid }"
              v-model="v$.url.$model"
            />
            <div v-if="v$.url.$anyDirty && v$.url.$invalid">
              <small class="form-text text-danger" v-for="error of v$.url.$errors" :key="error.$uid">{{ error.$message }}</small>
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
              :class="{ valid: !v$.description.$invalid, invalid: v$.description.$invalid }"
              v-model="v$.description.$model"
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
          <div v-if="v$.steps.$anyDirty && v$.steps.$invalid">
            <small class="form-text text-danger" v-for="error of v$.steps.$errors" :key="error.$uid">{{ error.$message }}</small>
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
<script lang="ts" src="./flow-group-update.component.ts"></script>
