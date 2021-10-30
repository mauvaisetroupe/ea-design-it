<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()" v-if="userAccount">
        <h2 id="myUserLabel">Create or edit a User</h2>
        <div>
          <div class="form-group" :hidden="!userAccount.id">
            <label>ID</label>
            <input type="text" class="form-control" name="id" v-model="userAccount.id" readonly />
          </div>

          <div class="form-group">
            <label class="form-control-label">Login</label>
            <input
              type="text"
              class="form-control"
              name="login"
              :class="{ valid: !$v.userAccount.login.$invalid, invalid: $v.userAccount.login.$invalid }"
              v-model="$v.userAccount.login.$model"
            />

            <div v-if="$v.userAccount.login.$anyDirty && $v.userAccount.login.$invalid">
              <small class="form-text text-danger" v-if="!$v.userAccount.login.required"> This field is required. </small>

              <small class="form-text text-danger" v-if="!$v.userAccount.login.maxLength">
                This field cannot be longer than 50 characters.
              </small>

              <small class="form-text text-danger" v-if="!$v.userAccount.login.pattern">
                This field can only contain letters, digits and e-mail addresses.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="firstName">First Name</label>
            <input
              type="text"
              class="form-control"
              id="firstName"
              name="firstName"
              :class="{ valid: !$v.userAccount.firstName.$invalid, invalid: $v.userAccount.firstName.$invalid }"
              v-model="$v.userAccount.firstName.$model"
            />
            <div v-if="$v.userAccount.firstName.$anyDirty && $v.userAccount.firstName.$invalid">
              <small class="form-text text-danger" v-if="!$v.userAccount.firstName.maxLength">
                This field cannot be longer than 50 characters.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="lastName">Last Name</label>
            <input
              type="text"
              class="form-control"
              id="lastName"
              name="lastName"
              :class="{ valid: !$v.userAccount.lastName.$invalid, invalid: $v.userAccount.lastName.$invalid }"
              v-model="$v.userAccount.lastName.$model"
            />
            <div v-if="$v.userAccount.lastName.$anyDirty && $v.userAccount.lastName.$invalid">
              <small class="form-text text-danger" v-if="!$v.userAccount.lastName.maxLength">
                This field cannot be longer than 50 characters.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="email">Email</label>
            <input
              type="email"
              class="form-control"
              id="email"
              name="email"
              :class="{ valid: !$v.userAccount.email.$invalid, invalid: $v.userAccount.email.$invalid }"
              v-model="$v.userAccount.email.$model"
              email
              required
            />
            <div v-if="$v.userAccount.email.$anyDirty && $v.userAccount.email.$invalid">
              <small class="form-text text-danger" v-if="!$v.userAccount.email.required"> Your email is required. </small>
              <small class="form-text text-danger" v-if="!$v.userAccount.email.email"> Your email is invalid. </small>
              <small class="form-text text-danger" v-if="!$v.userAccount.email.minLength">
                Your email is required to be at least 5 characters.
              </small>
              <small class="form-text text-danger" v-if="!$v.userAccount.email.maxLength">
                Your email cannot be longer than 50 characters.
              </small>
            </div>
          </div>
          <div class="form-check">
            <label class="form-check-label" for="activated">
              <input
                class="form-check-input"
                :disabled="userAccount.id === null"
                type="checkbox"
                id="activated"
                name="activated"
                v-model="userAccount.activated"
              />
              <span>Activated</span>
            </label>
          </div>

          <div class="form-group">
            <label>Profiles</label>
            <select class="form-control" multiple name="authority" v-model="userAccount.authorities">
              <option v-for="authority of authorities" :value="authority" :key="authority">{{ authority }}</option>
            </select>
          </div>
        </div>
        <div>
          <button type="button" class="btn btn-secondary" v-on:click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span>Cancel</span>
          </button>
          <button type="submit" :disabled="$v.userAccount.$invalid || isSaving" class="btn btn-primary">
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span>Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script lang="ts" src="./user-management-edit.component.ts"></script>
