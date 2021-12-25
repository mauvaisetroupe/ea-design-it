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
              required
            />
            <div v-if="$v.functionalFlow.alias.$anyDirty && $v.functionalFlow.alias.$invalid">
              <small class="form-text text-danger" v-if="!$v.functionalFlow.alias.required"> This field is required. </small>
            </div>
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
                This field cannot be longer than 1500 characters.
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
            <div v-if="$v.functionalFlow.comment.$anyDirty && $v.functionalFlow.comment.$invalid">
              <small class="form-text text-danger" v-if="!$v.functionalFlow.comment.maxLength">
                This field cannot be longer than 1000 characters.
              </small>
            </div>
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
            <div v-if="$v.functionalFlow.documentationURL.$anyDirty && $v.functionalFlow.documentationURL.$invalid">
              <small class="form-text text-danger" v-if="!$v.functionalFlow.documentationURL.maxLength">
                This field cannot be longer than 500 characters.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="functional-flow-documentationURL2">Documentation URL 2</label>
            <input
              type="text"
              class="form-control"
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
          <div class="form-group">
            <label class="form-control-label" for="functional-flow-startDate">Start Date</label>
            <b-input-group class="mb-3">
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
          <div class="form-group">
            <label class="form-control-label" for="functional-flow-endDate">End Date</label>
            <b-input-group class="mb-3">
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
