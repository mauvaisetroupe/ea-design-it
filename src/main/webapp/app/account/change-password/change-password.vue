<template>
  <div>
    <div class="row justify-content-center">
      <div class="col-md-8 toastify-container">
        <h2 v-if="account" id="password-title">
          <span>
            Password for [<strong>{{ username }}</strong
            >]</span
          >
        </h2>

        <div class="alert alert-success" role="alert" v-if="success">
          <strong>Password changed!</strong>
        </div>
        <div class="alert alert-danger" role="alert" v-if="error">
          <strong>An error has occurred!</strong> The password could not be changed.
        </div>

        <div class="alert alert-danger" role="alert" v-if="doNotMatch">The password and its confirmation do not match!</div>

        <form name="form" role="form" id="password-form" v-on:submit.prevent="changePassword()">
          <div class="form-group">
            <label class="form-control-label" for="currentPassword">Current password</label>
            <input
              type="password"
              class="form-control"
              id="currentPassword"
              name="currentPassword"
              :class="{ valid: !$v.resetPassword.currentPassword.$invalid, invalid: $v.resetPassword.currentPassword.$invalid }"
              v-model="$v.resetPassword.currentPassword.$model"
              required
              data-cy="currentPassword"
            />
            <div v-if="$v.resetPassword.currentPassword.$anyDirty && $v.resetPassword.currentPassword.$invalid">
              <small class="form-text text-danger" v-if="!$v.resetPassword.currentPassword.required"> Your password is required. </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="newPassword">New password</label>
            <input
              type="password"
              class="form-control"
              id="newPassword"
              name="newPassword"
              :class="{ valid: !$v.resetPassword.newPassword.$invalid, invalid: $v.resetPassword.newPassword.$invalid }"
              v-model="$v.resetPassword.newPassword.$model"
              minlength="4"
              maxlength="50"
              required
              data-cy="newPassword"
            />
            <div v-if="$v.resetPassword.newPassword.$anyDirty && $v.resetPassword.newPassword.$invalid">
              <small class="form-text text-danger" v-if="!$v.resetPassword.newPassword.required"> Your password is required. </small>
              <small class="form-text text-danger" v-if="!$v.resetPassword.newPassword.minLength">
                Your password is required to be at least 4 characters.
              </small>
              <small class="form-text text-danger" v-if="!$v.resetPassword.newPassword.maxLength">
                Your password cannot be longer than 50 characters.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="confirmPassword">New password confirmation</label>
            <input
              type="password"
              class="form-control"
              id="confirmPassword"
              name="confirmPassword"
              :class="{ valid: !$v.resetPassword.confirmPassword.$invalid, invalid: $v.resetPassword.confirmPassword.$invalid }"
              v-model="$v.resetPassword.confirmPassword.$model"
              minlength="4"
              maxlength="50"
              required
              data-cy="confirmPassword"
            />
            <div v-if="$v.resetPassword.confirmPassword.$anyDirty && $v.resetPassword.confirmPassword.$invalid">
              <small class="form-text text-danger" v-if="!$v.resetAccount.confirmPassword.sameAsPassword">
                The password and its confirmation do not match!
              </small>
            </div>
          </div>

          <button type="submit" :disabled="$v.resetPassword.$invalid" class="btn btn-primary" data-cy="submit">Save</button>
        </form>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./change-password.component.ts"></script>
