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
            <label class="form-control-label" for="application-component-alias">Alias</label>
            <input
              type="text"
              class="form-control"
              name="alias"
              id="application-component-alias"
              data-cy="alias"
              :class="{ valid: !$v.applicationComponent.alias.$invalid, invalid: $v.applicationComponent.alias.$invalid }"
              v-model="$v.applicationComponent.alias.$model"
            />
            <div v-if="$v.applicationComponent.alias.$anyDirty && $v.applicationComponent.alias.$invalid"></div>
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
              required
            />
            <div v-if="$v.applicationComponent.name.$anyDirty && $v.applicationComponent.name.$invalid">
              <small class="form-text text-danger" v-if="!$v.applicationComponent.name.required"> This field is required. </small>
            </div>
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
            <div v-if="$v.applicationComponent.comment.$anyDirty && $v.applicationComponent.comment.$invalid">
              <small class="form-text text-danger" v-if="!$v.applicationComponent.comment.maxLength">
                This field cannot be longer than 500 characters.
              </small>
            </div>
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
            <div v-if="$v.applicationComponent.documentationURL.$anyDirty && $v.applicationComponent.documentationURL.$invalid">
              <small class="form-text text-danger" v-if="!$v.applicationComponent.documentationURL.maxLength">
                This field cannot be longer than 500 characters.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="application-component-startDate">Start Date</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="application-component-startDate"
                  v-model="$v.applicationComponent.startDate.$model"
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
                id="application-component-startDate"
                data-cy="startDate"
                type="text"
                class="form-control"
                name="startDate"
                :class="{ valid: !$v.applicationComponent.startDate.$invalid, invalid: $v.applicationComponent.startDate.$invalid }"
                v-model="$v.applicationComponent.startDate.$model"
              />
            </b-input-group>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="application-component-endDate">End Date</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="application-component-endDate"
                  v-model="$v.applicationComponent.endDate.$model"
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
                id="application-component-endDate"
                data-cy="endDate"
                type="text"
                class="form-control"
                name="endDate"
                :class="{ valid: !$v.applicationComponent.endDate.$invalid, invalid: $v.applicationComponent.endDate.$invalid }"
                v-model="$v.applicationComponent.endDate.$model"
              />
            </b-input-group>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="application-component-applicationType">Application Type</label>
            <select
              class="form-control"
              name="applicationType"
              :class="{
                valid: !$v.applicationComponent.applicationType.$invalid,
                invalid: $v.applicationComponent.applicationType.$invalid,
              }"
              v-model="$v.applicationComponent.applicationType.$model"
              id="application-component-applicationType"
              data-cy="applicationType"
            >
              <option v-for="applicationType in applicationTypeValues" :key="applicationType" v-bind:value="applicationType">
                {{ applicationType }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="application-component-softwareType">Software Type</label>
            <select
              class="form-control"
              name="softwareType"
              :class="{ valid: !$v.applicationComponent.softwareType.$invalid, invalid: $v.applicationComponent.softwareType.$invalid }"
              v-model="$v.applicationComponent.softwareType.$model"
              id="application-component-softwareType"
              data-cy="softwareType"
            >
              <option v-for="softwareType in softwareTypeValues" :key="softwareType" v-bind:value="softwareType">{{ softwareType }}</option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="application-component-displayInLandscape">Display In Landscape</label>
            <input
              type="checkbox"
              class="form-check"
              name="displayInLandscape"
              id="application-component-displayInLandscape"
              data-cy="displayInLandscape"
              :class="{
                valid: !$v.applicationComponent.displayInLandscape.$invalid,
                invalid: $v.applicationComponent.displayInLandscape.$invalid,
              }"
              v-model="$v.applicationComponent.displayInLandscape.$model"
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
          <div class="form-group">
            <label for="application-component-categories">Categories</label>
            <select
              class="form-control"
              id="application-component-categories"
              data-cy="categories"
              multiple
              name="categories"
              v-if="applicationComponent.categories !== undefined"
              v-model="applicationComponent.categories"
            >
              <option
                v-bind:value="getSelected(applicationComponent.categories, applicationCategoryOption)"
                v-for="applicationCategoryOption in applicationCategories"
                :key="applicationCategoryOption.id"
              >
                {{ applicationCategoryOption.name }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label for="application-component-technologies">Technologies</label>
            <select
              class="form-control"
              id="application-component-technologies"
              data-cy="technologies"
              multiple
              name="technologies"
              v-if="applicationComponent.technologies !== undefined"
              v-model="applicationComponent.technologies"
            >
              <option
                v-bind:value="getSelected(applicationComponent.technologies, technologyOption)"
                v-for="technologyOption in technologies"
                :key="technologyOption.id"
              >
                {{ technologyOption.name }}
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
