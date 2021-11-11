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
            <div v-if="$v.application.alias.$anyDirty && $v.application.alias.$invalid">
              <small class="form-text text-danger" v-if="!$v.application.alias.pattern">
                This field should follow pattern for "Alias".
              </small>
            </div>
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
            />
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
                This field cannot be longer than 1000 characters.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="application-type">Type</label>
            <select
              class="form-control"
              name="type"
              :class="{ valid: !$v.application.type.$invalid, invalid: $v.application.type.$invalid }"
              v-model="$v.application.type.$model"
              id="application-type"
              data-cy="type"
            >
              <option v-for="applicationType in applicationTypeValues" :key="applicationType" v-bind:value="applicationType">
                {{ applicationType }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="application-technology">Technology</label>
            <input
              type="text"
              class="form-control"
              name="technology"
              id="application-technology"
              data-cy="technology"
              :class="{ valid: !$v.application.technology.$invalid, invalid: $v.application.technology.$invalid }"
              v-model="$v.application.technology.$model"
            />
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
