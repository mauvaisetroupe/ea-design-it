<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2 id="eaDesignItApp.application.home.createOrEditLabel" data-cy="ApplicationCreateUpdateHeading">Create or edit a Application</h2>
        <div>
          <div class="form-group" v-if="application.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="application.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="application-alias">Alias</label>
            <input
              type="text"
              class="form-control"
              name="alias"
              id="application-alias"
              data-cy="alias"
              :class="{ valid: !$v.application.alias.$invalid, invalid: $v.application.alias.$invalid }"
              v-model="$v.application.alias.$model"
            />
            <div v-if="$v.application.alias.$anyDirty && $v.application.alias.$invalid"></div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="application-name">Name</label>
            <input
              type="text"
              class="form-control"
              name="name"
              id="application-name"
              data-cy="name"
              :class="{ valid: !$v.application.name.$invalid, invalid: $v.application.name.$invalid }"
              v-model="$v.application.name.$model"
              required
            />
            <div v-if="$v.application.name.$anyDirty && $v.application.name.$invalid">
              <small class="form-text text-danger" v-if="!$v.application.name.required"> This field is required. </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="application-description">Description</label>
            <input
              type="text"
              class="form-control"
              name="description"
              id="application-description"
              data-cy="description"
              :class="{ valid: !$v.application.description.$invalid, invalid: $v.application.description.$invalid }"
              v-model="$v.application.description.$model"
            />
            <div v-if="$v.application.description.$anyDirty && $v.application.description.$invalid">
              <small class="form-text text-danger" v-if="!$v.application.description.maxLength">
                This field cannot be longer than 1500 characters.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="application-comment">Comment</label>
            <input
              type="text"
              class="form-control"
              name="comment"
              id="application-comment"
              data-cy="comment"
              :class="{ valid: !$v.application.comment.$invalid, invalid: $v.application.comment.$invalid }"
              v-model="$v.application.comment.$model"
            />
            <div v-if="$v.application.comment.$anyDirty && $v.application.comment.$invalid">
              <small class="form-text text-danger" v-if="!$v.application.comment.maxLength">
                This field cannot be longer than 1000 characters.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="application-documentationURL">Documentation URL</label>
            <input
              type="text"
              class="form-control"
              name="documentationURL"
              id="application-documentationURL"
              data-cy="documentationURL"
              :class="{ valid: !$v.application.documentationURL.$invalid, invalid: $v.application.documentationURL.$invalid }"
              v-model="$v.application.documentationURL.$model"
            />
            <div v-if="$v.application.documentationURL.$anyDirty && $v.application.documentationURL.$invalid">
              <small class="form-text text-danger" v-if="!$v.application.documentationURL.maxLength">
                This field cannot be longer than 500 characters.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="application-startDate">Start Date</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="application-startDate"
                  v-model="$v.application.startDate.$model"
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
                id="application-startDate"
                data-cy="startDate"
                type="text"
                class="form-control"
                name="startDate"
                :class="{ valid: !$v.application.startDate.$invalid, invalid: $v.application.startDate.$invalid }"
                v-model="$v.application.startDate.$model"
              />
            </b-input-group>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="application-endDate">End Date</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="application-endDate"
                  v-model="$v.application.endDate.$model"
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
                id="application-endDate"
                data-cy="endDate"
                type="text"
                class="form-control"
                name="endDate"
                :class="{ valid: !$v.application.endDate.$invalid, invalid: $v.application.endDate.$invalid }"
                v-model="$v.application.endDate.$model"
              />
            </b-input-group>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="application-applicationType">Application Type</label>
            <select
              class="form-control"
              name="applicationType"
              :class="{ valid: !$v.application.applicationType.$invalid, invalid: $v.application.applicationType.$invalid }"
              v-model="$v.application.applicationType.$model"
              id="application-applicationType"
              data-cy="applicationType"
            >
              <option v-for="applicationType in applicationTypeValues" :key="applicationType" v-bind:value="applicationType">
                {{ applicationType }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="application-softwareType">Software Type</label>
            <select
              class="form-control"
              name="softwareType"
              :class="{ valid: !$v.application.softwareType.$invalid, invalid: $v.application.softwareType.$invalid }"
              v-model="$v.application.softwareType.$model"
              id="application-softwareType"
              data-cy="softwareType"
            >
              <option v-for="softwareType in softwareTypeValues" :key="softwareType" v-bind:value="softwareType">{{ softwareType }}</option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="application-nickname">Nickname</label>
            <input
              type="text"
              class="form-control"
              name="nickname"
              id="application-nickname"
              data-cy="nickname"
              :class="{ valid: !$v.application.nickname.$invalid, invalid: $v.application.nickname.$invalid }"
              v-model="$v.application.nickname.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="application-owner">Owner</label>
            <select class="form-control" id="application-owner" data-cy="owner" name="owner" v-model="application.owner">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="application.owner && ownerOption.id === application.owner.id ? application.owner : ownerOption"
                v-for="ownerOption in owners"
                :key="ownerOption.id"
              >
                {{ ownerOption.name }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="application-itOwner">It Owner</label>
            <select class="form-control" id="application-itOwner" data-cy="itOwner" name="itOwner" v-model="application.itOwner">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="application.itOwner && ownerOption.id === application.itOwner.id ? application.itOwner : ownerOption"
                v-for="ownerOption in owners"
                :key="ownerOption.id"
              >
                {{ ownerOption.name }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="application-businessOwner">Business Owner</label>
            <select
              class="form-control"
              id="application-businessOwner"
              data-cy="businessOwner"
              name="businessOwner"
              v-model="application.businessOwner"
            >
              <option v-bind:value="null"></option>
              <option
                v-bind:value="
                  application.businessOwner && ownerOption.id === application.businessOwner.id ? application.businessOwner : ownerOption
                "
                v-for="ownerOption in owners"
                :key="ownerOption.id"
              >
                {{ ownerOption.name }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label for="application-categories">Categories</label>
            <select
              class="form-control"
              id="application-categories"
              data-cy="categories"
              multiple
              name="categories"
              v-if="application.categories !== undefined"
              v-model="application.categories"
            >
              <option
                v-bind:value="getSelected(application.categories, applicationCategoryOption)"
                v-for="applicationCategoryOption in applicationCategories"
                :key="applicationCategoryOption.id"
              >
                {{ applicationCategoryOption.name }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label for="application-technologies">Technologies</label>
            <select
              class="form-control"
              id="application-technologies"
              data-cy="technologies"
              multiple
              name="technologies"
              v-if="application.technologies !== undefined"
              v-model="application.technologies"
            >
              <option
                v-bind:value="getSelected(application.technologies, technologyOption)"
                v-for="technologyOption in technologies"
                :key="technologyOption.id"
              >
                {{ technologyOption.name }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label for="application-externalIDS">External IDS</label>
            <select
              class="form-control"
              id="application-externalIDS"
              data-cy="externalIDS"
              multiple
              name="externalIDS"
              v-if="application.externalIDS !== undefined"
              v-model="application.externalIDS"
            >
              <option
                v-bind:value="getSelected(application.externalIDS, externalReferenceOption)"
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
            :disabled="$v.application.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span>Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./application-update.component.ts"></script>
