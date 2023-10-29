<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2 id="eaDesignItApp.externalReference.home.createOrEditLabel" data-cy="ExternalReferenceCreateUpdateHeading">
          Create or edit a External Reference
        </h2>
        <div>
          <div class="form-group" v-if="externalReference.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="externalReference.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="external-reference-externalID">External ID</label>
            <input
              type="text"
              class="form-control"
              name="externalID"
              id="external-reference-externalID"
              data-cy="externalID"
              :class="{ valid: !v$.externalID.$invalid, invalid: v$.externalID.$invalid }"
              v-model="v$.externalID.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="external-reference-externalSystem">External System</label>
            <select
              class="form-control"
              id="external-reference-externalSystem"
              data-cy="externalSystem"
              name="externalSystem"
              v-model="externalReference.externalSystem"
            >
              <option v-bind:value="null"></option>
              <option
                v-bind:value="
                  externalReference.externalSystem && externalSystemOption.id === externalReference.externalSystem.id
                    ? externalReference.externalSystem
                    : externalSystemOption
                "
                v-for="externalSystemOption in externalSystems"
                :key="externalSystemOption.id"
              >
                {{ externalSystemOption.externalSystemID }}
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
<script lang="ts" src="./external-reference-update.component.ts"></script>
