<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2 id="eaDesignItApp.eventData.home.createOrEditLabel" data-cy="EventDataCreateUpdateHeading">Create or edit a EventData</h2>
        <div>
          <div class="form-group" v-if="eventData.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="eventData.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="event-data-name">Name</label>
            <input
              type="text"
              class="form-control"
              name="name"
              id="event-data-name"
              data-cy="name"
              :class="{ valid: !$v.eventData.name.$invalid, invalid: $v.eventData.name.$invalid }"
              v-model="$v.eventData.name.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="event-data-contractURL">Contract URL</label>
            <input
              type="text"
              class="form-control"
              name="contractURL"
              id="event-data-contractURL"
              data-cy="contractURL"
              :class="{ valid: !$v.eventData.contractURL.$invalid, invalid: $v.eventData.contractURL.$invalid }"
              v-model="$v.eventData.contractURL.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="event-data-documentationURL">Documentation URL</label>
            <input
              type="text"
              class="form-control"
              name="documentationURL"
              id="event-data-documentationURL"
              data-cy="documentationURL"
              :class="{ valid: !$v.eventData.documentationURL.$invalid, invalid: $v.eventData.documentationURL.$invalid }"
              v-model="$v.eventData.documentationURL.$model"
            />
            <div v-if="$v.eventData.documentationURL.$anyDirty && $v.eventData.documentationURL.$invalid">
              <small class="form-text text-danger" v-if="!$v.eventData.documentationURL.maxLength">
                This field cannot be longer than 500 characters.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="event-data-startDate">Start Date</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="event-data-startDate"
                  v-model="$v.eventData.startDate.$model"
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
                id="event-data-startDate"
                data-cy="startDate"
                type="text"
                class="form-control"
                name="startDate"
                :class="{ valid: !$v.eventData.startDate.$invalid, invalid: $v.eventData.startDate.$invalid }"
                v-model="$v.eventData.startDate.$model"
              />
            </b-input-group>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="event-data-endDate">End Date</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="event-data-endDate"
                  v-model="$v.eventData.endDate.$model"
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
                id="event-data-endDate"
                data-cy="endDate"
                type="text"
                class="form-control"
                name="endDate"
                :class="{ valid: !$v.eventData.endDate.$invalid, invalid: $v.eventData.endDate.$invalid }"
                v-model="$v.eventData.endDate.$model"
              />
            </b-input-group>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="event-data-dataFlow">Data Flow</label>
            <select class="form-control" id="event-data-dataFlow" data-cy="dataFlow" name="dataFlow" v-model="eventData.dataFlow">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="eventData.dataFlow && dataFlowOption.id === eventData.dataFlow.id ? eventData.dataFlow : dataFlowOption"
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
            :disabled="$v.eventData.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span>Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./event-data-update.component.ts"></script>
