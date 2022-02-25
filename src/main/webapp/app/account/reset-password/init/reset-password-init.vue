<template>
  <div>
    <div class="row justify-content-center">
      <div class="col-md-8">
        <h1>Reset your password</h1>

        <div class="alert alert-warning" v-if="!success">
          <p>Enter the email address you used to register.</p>
        </div>

        <div class="alert alert-success" v-if="success">
          <p>Check your emails for details on how to reset your password.</p>
        </div>

        <form v-if="!success" name="form" role="form" v-on:submit.prevent="requestReset()">
          <div class="form-group">
            <label class="form-control-label" for="email">Email</label>
            <input
              type="email"
              class="form-control"
              id="email"
              name="email"
              :class="{ valid: !$v.resetAccount.email.$invalid, invalid: $v.resetAccount.email.$invalid }"
              v-model="$v.resetAccount.email.$model"
              minlength="5"
              maxlength="254"
              email
              required
              data-cy="emailResetPassword"
            />
            <div v-if="$v.resetAccount.email.$anyDirty && $v.resetAccount.email.$invalid">
              <small class="form-text text-danger" v-if="!$v.resetAccount.email.required"> Your email is required. </small>
              <small class="form-text text-danger" v-if="!$v.resetAccount.email.email"> Your email is invalid. </small>
              <small class="form-text text-danger" v-if="!$v.resetAccount.email.minLength">
                Your email is required to be at least 5 characters.
              </small>
              <small class="form-text text-danger" v-if="!$v.resetAccount.email.maxLength">
                Your email cannot be longer than 100 characters.
              </small>
            </div>
          </div>
          <button type="submit" :disabled="$v.resetAccount.$invalid" class="btn btn-primary" data-cy="submit">Reset</button>
        </form>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./reset-password-init.component.ts"></script>
