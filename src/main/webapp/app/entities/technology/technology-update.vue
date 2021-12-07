<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2 id="eaDesignItApp.technology.home.createOrEditLabel" data-cy="TechnologyCreateUpdateHeading">Create or edit a Technology</h2>
        <div>
          <div class="form-group" v-if="technology.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="technology.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="technology-name">Name</label>
            <input
              type="text"
              class="form-control"
              name="name"
              id="technology-name"
              data-cy="name"
              :class="{ valid: !$v.technology.name.$invalid, invalid: $v.technology.name.$invalid }"
              v-model="$v.technology.name.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="technology-type">Type</label>
            <input
              type="text"
              class="form-control"
              name="type"
              id="technology-type"
              data-cy="type"
              :class="{ valid: !$v.technology.type.$invalid, invalid: $v.technology.type.$invalid }"
              v-model="$v.technology.type.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="technology-description">Description</label>
            <input
              type="text"
              class="form-control"
              name="description"
              id="technology-description"
              data-cy="description"
              :class="{ valid: !$v.technology.description.$invalid, invalid: $v.technology.description.$invalid }"
              v-model="$v.technology.description.$model"
            />
            <div v-if="$v.technology.description.$anyDirty && $v.technology.description.$invalid">
              <small class="form-text text-danger" v-if="!$v.technology.description.maxLength">
                This field cannot be longer than 250 characters.
              </small>
            </div>
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
            :disabled="$v.technology.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span>Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./technology-update.component.ts"></script>
