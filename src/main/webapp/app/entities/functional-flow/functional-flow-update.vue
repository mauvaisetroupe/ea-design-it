<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2 id="eaDesignItApp.functionalFlow.home.createOrEditLabel" data-cy="FunctionalFlowCreateUpdateHeading">
          Create or edit a Functional Flow
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
              :class="{ valid: !v$.alias.$invalid, invalid: v$.alias.$invalid }"
              v-model="v$.alias.$model"
            />
            <div v-if="v$.alias.$anyDirty && v$.alias.$invalid">
              <small class="form-text text-danger" v-for="error of v$.alias.$errors" :key="error.$uid">{{ error.$message }}</small>
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
              :class="{ valid: !v$.description.$invalid, invalid: v$.description.$invalid }"
              v-model="v$.description.$model"
            />
            <div v-if="v$.description.$anyDirty && v$.description.$invalid">
              <small class="form-text text-danger" v-for="error of v$.description.$errors" :key="error.$uid">{{ error.$message }}</small>
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
              :class="{ valid: !v$.comment.$invalid, invalid: v$.comment.$invalid }"
              v-model="v$.comment.$model"
            />
            <div v-if="v$.comment.$anyDirty && v$.comment.$invalid">
              <small class="form-text text-danger" v-for="error of v$.comment.$errors" :key="error.$uid">{{ error.$message }}</small>
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
              :class="{ valid: !v$.status.$invalid, invalid: v$.status.$invalid }"
              v-model="v$.status.$model"
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
            <label class="form-control-label" for="functional-flow-documentationURL2">Documentation URL 2</label>
            <input
              type="text"
              class="form-control"
              name="documentationURL2"
              id="functional-flow-documentationURL2"
              data-cy="documentationURL2"
              :class="{ valid: !v$.documentationURL2.$invalid, invalid: v$.documentationURL2.$invalid }"
              v-model="v$.documentationURL2.$model"
            />
            <div v-if="v$.documentationURL2.$anyDirty && v$.documentationURL2.$invalid">
              <small class="form-text text-danger" v-for="error of v$.documentationURL2.$errors" :key="error.$uid">{{
                error.$message
              }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="functional-flow-startDate">Start Date</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="functional-flow-startDate"
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
                id="functional-flow-startDate"
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
            <label class="form-control-label" for="functional-flow-endDate">End Date</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="functional-flow-endDate"
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
                id="functional-flow-endDate"
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
            <label class="form-control-label" for="functional-flow-owner">Owner</label>
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
<script lang="ts" src="./functional-flow-update.component.ts"></script>
