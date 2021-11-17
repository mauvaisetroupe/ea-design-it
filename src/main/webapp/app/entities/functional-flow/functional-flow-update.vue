<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2 id="eaDesignItApp.functionalFlow.home.createOrEditLabel" data-cy="FunctionalFlowCreateUpdateHeading">
          Create or edit a FunctionalFlow
        </h2>
        <div>
          <div class="form-group" v-if="functionalFlow.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="functionalFlow.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="functional-flow-alias">Alias</label>
            <input
              type="text"
              class="form-control"
              name="alias"
              id="functional-flow-alias"
              data-cy="alias"
              :class="{ valid: !$v.functionalFlow.alias.$invalid, invalid: $v.functionalFlow.alias.$invalid }"
              v-model="$v.functionalFlow.alias.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="functional-flow-description">Description</label>
            <input
              type="text"
              class="form-control"
              name="description"
              id="functional-flow-description"
              data-cy="description"
              :class="{ valid: !$v.functionalFlow.description.$invalid, invalid: $v.functionalFlow.description.$invalid }"
              v-model="$v.functionalFlow.description.$model"
            />
            <div v-if="$v.functionalFlow.description.$anyDirty && $v.functionalFlow.description.$invalid">
              <small class="form-text text-danger" v-if="!$v.functionalFlow.description.maxLength">
                This field cannot be longer than 1000 characters.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="functional-flow-comment">Comment</label>
            <input
              type="text"
              class="form-control"
              name="comment"
              id="functional-flow-comment"
              data-cy="comment"
              :class="{ valid: !$v.functionalFlow.comment.$invalid, invalid: $v.functionalFlow.comment.$invalid }"
              v-model="$v.functionalFlow.comment.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="functional-flow-status">Status</label>
            <input
              type="text"
              class="form-control"
              name="status"
              id="functional-flow-status"
              data-cy="status"
              :class="{ valid: !$v.functionalFlow.status.$invalid, invalid: $v.functionalFlow.status.$invalid }"
              v-model="$v.functionalFlow.status.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="functional-flow-documentationURL">Documentation URL</label>
            <input
              type="text"
              class="form-control"
              name="documentationURL"
              id="functional-flow-documentationURL"
              data-cy="documentationURL"
              :class="{ valid: !$v.functionalFlow.documentationURL.$invalid, invalid: $v.functionalFlow.documentationURL.$invalid }"
              v-model="$v.functionalFlow.documentationURL.$model"
            />
          </div>
          <div class="form-group">
            <label for="functional-flow-interfaces">Interfaces</label>
            <select
              class="form-control"
              id="functional-flow-interfaces"
              data-cy="interfaces"
              multiple
              name="interfaces"
              v-if="functionalFlow.interfaces !== undefined"
              v-model="functionalFlow.interfaces"
            >
              <option
                v-bind:value="getSelected(functionalFlow.interfaces, flowInterfaceOption)"
                v-for="flowInterfaceOption in flowInterfaces"
                :key="flowInterfaceOption.id"
              >
                {{ flowInterfaceOption.alias }}
              </option>
            </select>
          </div>
          <div v-if="$v.functionalFlow.landscapes.$anyDirty && $v.functionalFlow.landscapes.$invalid">
            <small class="form-text text-danger" v-if="!$v.functionalFlow.landscapes.required"> This field is required. </small>
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
