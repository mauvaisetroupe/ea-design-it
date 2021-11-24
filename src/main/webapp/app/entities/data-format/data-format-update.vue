<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2 id="eaDesignItApp.dataFormat.home.createOrEditLabel" data-cy="DataFormatCreateUpdateHeading">Create or edit a DataFormat</h2>
        <div>
          <div class="form-group" v-if="dataFormat.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="dataFormat.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="data-format-name">Name</label>
            <input
              type="text"
              class="form-control"
              name="name"
              id="data-format-name"
              data-cy="name"
              :class="{ valid: !$v.dataFormat.name.$invalid, invalid: $v.dataFormat.name.$invalid }"
              v-model="$v.dataFormat.name.$model"
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
            :disabled="$v.dataFormat.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span>Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./data-format-update.component.ts"></script>
