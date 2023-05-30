<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2 id="eaDesignItApp.owner.home.createOrEditLabel" data-cy="OwnerCreateUpdateHeading">Create or edit a Owner</h2>
        <div>
          <div class="form-group" v-if="owner.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="owner.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="owner-name">Name</label>
            <input
              type="text"
              class="form-control"
              name="name"
              id="owner-name"
              data-cy="name"
              :class="{ valid: !$v.owner.name.$invalid, invalid: $v.owner.name.$invalid }"
              v-model="$v.owner.name.$model"
              required
            />
            <div v-if="$v.owner.name.$anyDirty && $v.owner.name.$invalid">
              <small class="form-text text-danger" v-if="!$v.owner.name.required"> This field is required. </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="owner-firstname">Firstname</label>
            <input
              type="text"
              class="form-control"
              name="firstname"
              id="owner-firstname"
              data-cy="firstname"
              :class="{ valid: !$v.owner.firstname.$invalid, invalid: $v.owner.firstname.$invalid }"
              v-model="$v.owner.firstname.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="owner-lastname">Lastname</label>
            <input
              type="text"
              class="form-control"
              name="lastname"
              id="owner-lastname"
              data-cy="lastname"
              :class="{ valid: !$v.owner.lastname.$invalid, invalid: $v.owner.lastname.$invalid }"
              v-model="$v.owner.lastname.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="owner-email">Email</label>
            <input
              type="text"
              class="form-control"
              name="email"
              id="owner-email"
              data-cy="email"
              :class="{ valid: !$v.owner.email.$invalid, invalid: $v.owner.email.$invalid }"
              v-model="$v.owner.email.$model"
            />
            <div v-if="$v.owner.email.$anyDirty && $v.owner.email.$invalid">
              <small class="form-text text-danger" v-if="!$v.owner.email.pattern"> This field should follow pattern for "Email". </small>
            </div>
          </div>
          <div class="form-group">
            <label for="owner-users">Users</label>
            <select
              class="form-control"
              id="owner-users"
              data-cy="users"
              multiple
              name="users"
              v-if="owner.users !== undefined"
              v-model="owner.users"
            >
              <option v-bind:value="getSelected(owner.users, userOption)" v-for="userOption in users" :key="userOption.id">
                {{ userOption.login }}
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
            :disabled="$v.owner.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span>Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./owner-update.component.ts"></script>
