<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2 id="eaDesignItApp.externalSystem.home.createOrEditLabel" data-cy="ExternalSystemCreateUpdateHeading">
          Create or edit a ExternalSystem
        </h2>
        <div>
          <div class="form-group" v-if="externalSystem.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="externalSystem.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="external-system-externalSystemID">External System ID</label>
            <input
              type="text"
              class="form-control"
              name="externalSystemID"
              id="external-system-externalSystemID"
              data-cy="externalSystemID"
              :class="{ valid: !$v.externalSystem.externalSystemID.$invalid, invalid: $v.externalSystem.externalSystemID.$invalid }"
              v-model="$v.externalSystem.externalSystemID.$model"
            />
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
            :disabled="$v.externalSystem.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span>Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./external-system-update.component.ts"></script>
