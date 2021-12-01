<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2 id="eaDesignItApp.applicationCategory.home.createOrEditLabel" data-cy="ApplicationCategoryCreateUpdateHeading">
          Create or edit a ApplicationCategory
        </h2>
        <div>
          <div class="form-group" v-if="applicationCategory.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="applicationCategory.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="application-category-name">Name</label>
            <input
              type="text"
              class="form-control"
              name="name"
              id="application-category-name"
              data-cy="name"
              :class="{ valid: !$v.applicationCategory.name.$invalid, invalid: $v.applicationCategory.name.$invalid }"
              v-model="$v.applicationCategory.name.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="application-category-type">Type</label>
            <input
              type="text"
              class="form-control"
              name="type"
              id="application-category-type"
              data-cy="type"
              :class="{ valid: !$v.applicationCategory.type.$invalid, invalid: $v.applicationCategory.type.$invalid }"
              v-model="$v.applicationCategory.type.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="application-category-description">Description</label>
            <input
              type="text"
              class="form-control"
              name="description"
              id="application-category-description"
              data-cy="description"
              :class="{ valid: !$v.applicationCategory.description.$invalid, invalid: $v.applicationCategory.description.$invalid }"
              v-model="$v.applicationCategory.description.$model"
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
            :disabled="$v.applicationCategory.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span>Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./application-category-update.component.ts"></script>
