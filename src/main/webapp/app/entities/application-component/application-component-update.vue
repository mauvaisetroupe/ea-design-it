<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2 id="eaDesignItApp.applicationComponent.home.createOrEditLabel" data-cy="ApplicationComponentCreateUpdateHeading">
          Create or edit a ApplicationComponent
        </h2>
        <div>
          <div class="form-group" v-if="applicationComponent.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="applicationComponent.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="application-component-name">Name</label>
            <input
              type="text"
              class="form-control"
              name="name"
              id="application-component-name"
              data-cy="name"
              :class="{ valid: !$v.applicationComponent.name.$invalid, invalid: $v.applicationComponent.name.$invalid }"
              v-model="$v.applicationComponent.name.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="application-component-description">Description</label>
            <input
              type="text"
              class="form-control"
              name="description"
              id="application-component-description"
              data-cy="description"
              :class="{ valid: !$v.applicationComponent.description.$invalid, invalid: $v.applicationComponent.description.$invalid }"
              v-model="$v.applicationComponent.description.$model"
            />
            <div v-if="$v.applicationComponent.description.$anyDirty && $v.applicationComponent.description.$invalid">
              <small class="form-text text-danger" v-if="!$v.applicationComponent.description.maxLength">
                This field cannot be longer than 1000 characters.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="application-component-type">Type</label>
            <select
              class="form-control"
              name="type"
              :class="{ valid: !$v.applicationComponent.type.$invalid, invalid: $v.applicationComponent.type.$invalid }"
              v-model="$v.applicationComponent.type.$model"
              id="application-component-type"
              data-cy="type"
            >
              <option v-for="applicationType in applicationTypeValues" :key="applicationType" v-bind:value="applicationType">
                {{ applicationType }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="application-component-technology">Technology</label>
            <input
              type="text"
              class="form-control"
              name="technology"
              id="application-component-technology"
              data-cy="technology"
              :class="{ valid: !$v.applicationComponent.technology.$invalid, invalid: $v.applicationComponent.technology.$invalid }"
              v-model="$v.applicationComponent.technology.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="application-component-comment">Comment</label>
            <input
              type="text"
              class="form-control"
              name="comment"
              id="application-component-comment"
              data-cy="comment"
              :class="{ valid: !$v.applicationComponent.comment.$invalid, invalid: $v.applicationComponent.comment.$invalid }"
              v-model="$v.applicationComponent.comment.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="application-component-documentationURL">Documentation URL</label>
            <input
              type="text"
              class="form-control"
              name="documentationURL"
              id="application-component-documentationURL"
              data-cy="documentationURL"
              :class="{
                valid: !$v.applicationComponent.documentationURL.$invalid,
                invalid: $v.applicationComponent.documentationURL.$invalid,
              }"
              v-model="$v.applicationComponent.documentationURL.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="application-component-application">Application</label>
            <select
              class="form-control"
              id="application-component-application"
              data-cy="application"
              name="application"
              v-model="applicationComponent.application"
              required
            >
              <option v-if="!applicationComponent.application" v-bind:value="null" selected></option>
              <option
                v-bind:value="
                  applicationComponent.application && applicationOption.id === applicationComponent.application.id
                    ? applicationComponent.application
                    : applicationOption
                "
                v-for="applicationOption in applications"
                :key="applicationOption.id"
              >
                {{ applicationOption.name }}
              </option>
            </select>
          </div>
          <div v-if="$v.applicationComponent.application.$anyDirty && $v.applicationComponent.application.$invalid">
            <small class="form-text text-danger" v-if="!$v.applicationComponent.application.required"> This field is required. </small>
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
            :disabled="$v.applicationComponent.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span>Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./application-component-update.component.ts"></script>
