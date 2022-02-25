<template>
  <div>
    <div class="row justify-content-center">
      <div class="col-md-8 toastify-container">
        <h2 v-if="username" id="settings-title">
          <span
            >User settings for [<strong>{{ username }}</strong
            >]</span
          >
        </h2>

        <div class="alert alert-success" role="alert" v-if="success">
          <strong>Settings saved!</strong>
        </div>

        <div class="alert alert-danger" role="alert" v-if="errorEmailExists">
          <strong>Email is already in use!</strong> Please choose another one.
        </div>

        <form name="form" id="settings-form" role="form" v-on:submit.prevent="save()" v-if="settingsAccount" novalidate>
          <div class="form-group">
            <label class="form-control-label" for="firstName">First Name</label>
            <input
              type="text"
              class="form-control"
              id="firstName"
              name="firstName"
              :class="{ valid: !$v.settingsAccount.firstName.$invalid, invalid: $v.settingsAccount.firstName.$invalid }"
              v-model="$v.settingsAccount.firstName.$model"
              minlength="1"
              maxlength="50"
              required
              data-cy="firstname"
            />
            <div v-if="$v.settingsAccount.firstName.$anyDirty && $v.settingsAccount.firstName.$invalid">
              <small class="form-text text-danger" v-if="!$v.settingsAccount.firstName.required"> Your first name is required. </small>
              <small class="form-text text-danger" v-if="!$v.settingsAccount.firstName.minLength">
                Your first name is required to be at least 1 character.
              </small>
              <small class="form-text text-danger" v-if="!$v.settingsAccount.firstName.maxLength">
                Your first name cannot be longer than 50 characters.
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
              :class="{ valid: !$v.settingsAccount.lastName.$invalid, invalid: $v.settingsAccount.lastName.$invalid }"
              v-model="$v.settingsAccount.lastName.$model"
              minlength="1"
              maxlength="50"
              required
              data-cy="lastname"
            />
            <div v-if="$v.settingsAccount.lastName.$anyDirty && $v.settingsAccount.lastName.$invalid">
              <small class="form-text text-danger" v-if="!$v.settingsAccount.lastName.required"> Your last name is required. </small>
              <small class="form-text text-danger" v-if="!$v.settingsAccount.lastName.minLength">
                Your last name is required to be at least 1 character.
              </small>
              <small class="form-text text-danger" v-if="!$v.settingsAccount.lastName.maxLength">
                Your last name cannot be longer than 50 characters.
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
              :class="{ valid: !$v.settingsAccount.email.$invalid, invalid: $v.settingsAccount.email.$invalid }"
              v-model="$v.settingsAccount.email.$model"
              minlength="5"
              maxlength="254"
              email
              required
              data-cy="email"
            />
            <div v-if="$v.settingsAccount.email.$anyDirty && $v.settingsAccount.email.$invalid">
              <small class="form-text text-danger" v-if="!$v.settingsAccount.email.required"> Your email is required. </small>
              <small class="form-text text-danger" v-if="!$v.settingsAccount.email.email"> Your email is invalid. </small>
              <small class="form-text text-danger" v-if="!$v.settingsAccount.email.minLength">
                Your email is required to be at least 5 characters.
              </small>
              <small class="form-text text-danger" v-if="!$v.settingsAccount.email.maxLength">
                Your email cannot be longer than 100 characters.
              </small>
            </div>
          </div>
          <div class="form-group" v-if="languages && Object.keys(languages).length > 1">
            <label for="langKey">Language</label>
            <select class="form-control" id="langKey" name="langKey" v-model="settingsAccount.langKey" data-cy="langKey">
              <option v-for="(language, key) in languages" :value="key" :key="`lang-${key}`">{{ language.name }}</option>
            </select>
          </div>
          <button type="submit" :disabled="$v.settingsAccount.$invalid" class="btn btn-primary" data-cy="submit">Save</button>
        </form>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./settings.component.ts"></script>
