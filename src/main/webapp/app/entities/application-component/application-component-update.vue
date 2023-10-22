<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2 id="eaDesignItApp.applicationComponent.home.createOrEditLabel" data-cy="ApplicationComponentCreateUpdateHeading">
          Create or edit a Application Component
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
              :class="{ valid: !v$.alias.$invalid, invalid: v$.alias.$invalid }"
              v-model="v$.alias.$model"
            />
            <div v-if="v$.alias.$anyDirty && v$.alias.$invalid">
              <small class="form-text text-danger" v-for="error of v$.alias.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="application-component-name">Name</label>
            <input
              type="text"
              class="form-control"
              name="name"
              id="application-component-name"
              data-cy="name"
              :class="{ valid: !v$.name.$invalid, invalid: v$.name.$invalid }"
              v-model="v$.name.$model"
              required
            />
            <div v-if="v$.name.$anyDirty && v$.name.$invalid">
              <small class="form-text text-danger" v-for="error of v$.name.$errors" :key="error.$uid">{{ error.$message }}</small>
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
              :class="{ valid: !v$.description.$invalid, invalid: v$.description.$invalid }"
              v-model="v$.description.$model"
            />
            <div v-if="v$.description.$anyDirty && v$.description.$invalid">
              <small class="form-text text-danger" v-for="error of v$.description.$errors" :key="error.$uid">{{ error.$message }}</small>
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
              :class="{ valid: !v$.comment.$invalid, invalid: v$.comment.$invalid }"
              v-model="v$.comment.$model"
            />
            <div v-if="v$.comment.$anyDirty && v$.comment.$invalid">
              <small class="form-text text-danger" v-for="error of v$.comment.$errors" :key="error.$uid">{{ error.$message }}</small>
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
            <label class="form-control-label" for="application-component-startDate">Start Date</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="application-component-startDate"
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
                id="application-component-startDate"
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
            <label class="form-control-label" for="application-component-endDate">End Date</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="application-component-endDate"
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
                id="application-component-endDate"
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
            <label class="form-control-label" for="application-component-applicationType">Application Type</label>
            <select
              class="form-control"
              name="applicationType"
              :class="{ valid: !v$.applicationType.$invalid, invalid: v$.applicationType.$invalid }"
              v-model="v$.applicationType.$model"
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
              :class="{ valid: !v$.softwareType.$invalid, invalid: v$.softwareType.$invalid }"
              v-model="v$.softwareType.$model"
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
              :class="{ valid: !v$.displayInLandscape.$invalid, invalid: v$.displayInLandscape.$invalid }"
              v-model="v$.displayInLandscape.$model"
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
          <div v-if="v$.application.$anyDirty && v$.application.$invalid">
            <small class="form-text text-danger" v-for="error of v$.application.$errors" :key="error.$uid">{{ error.$message }}</small>
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
          <div class="form-group">
            <label for="application-component-externalIDS">External IDS</label>
            <select
              class="form-control"
              id="application-component-externalIDS"
              data-cy="externalIDS"
              multiple
              name="externalIDS"
              v-if="applicationComponent.externalIDS !== undefined"
              v-model="applicationComponent.externalIDS"
            >
              <option
                v-bind:value="getSelected(applicationComponent.externalIDS, externalReferenceOption)"
                v-for="externalReferenceOption in externalReferences"
                :key="externalReferenceOption.id"
              >
                {{ externalReferenceOption.externalID }}
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
<script lang="ts" src="./application-component-update.component.ts"></script>
