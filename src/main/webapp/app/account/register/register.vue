<template>
  <div>
    <div class="row justify-content-center">
      <div class="col-md-8 toastify-container">
        <h1 id="register-title" data-cy="registerTitle">Registration</h1>

        <div class="alert alert-success" role="alert" v-if="success">
          <strong>Registration saved!</strong> Please check your email for confirmation.
        </div>

        <div class="alert alert-danger" role="alert" v-if="error"><strong>Registration failed!</strong> Please try again later.</div>

        <div class="alert alert-danger" role="alert" v-if="errorUserExists">
          <strong>Login name already registered!</strong> Please choose another one.
        </div>

        <div class="alert alert-danger" role="alert" v-if="errorEmailExists">
          <strong>Email is already in use!</strong> Please choose another one.
        </div>
      </div>
    </div>
    <div class="row justify-content-center">
      <div class="col-md-8">
        <form id="register-form" name="registerForm" role="form" v-on:submit.prevent="register()" v-if="!success" no-validate>
          <div class="form-group">
            <label class="form-control-label" for="username">Username</label>
            <input
              type="text"
              class="form-control"
              v-model="$v.registerAccount.login.$model"
              id="username"
              name="login"
              :class="{ valid: !$v.registerAccount.login.$invalid, invalid: $v.registerAccount.login.$invalid }"
              required
              minlength="1"
              maxlength="50"
              pattern="^[a-zA-Z0-9!#$&'*+=?^_`{|}~.-]+@?[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$"
              data-cy="username"
            />
            <div v-if="$v.registerAccount.login.$anyDirty && $v.registerAccount.login.$invalid">
              <small class="form-text text-danger" v-if="!$v.registerAccount.login.required"> Your username is required. </small>
              <small class="form-text text-danger" v-if="!$v.registerAccount.login.minLength">
                Your username is required to be at least 1 character.
              </small>
              <small class="form-text text-danger" v-if="!$v.registerAccount.login.maxLength">
                Your username cannot be longer than 50 characters.
              </small>
              <small class="form-text text-danger" v-if="!$v.registerAccount.login.pattern">
                Your username can only contain letters and digits.
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
              :class="{ valid: !$v.registerAccount.email.$invalid, invalid: $v.registerAccount.email.$invalid }"
              v-model="$v.registerAccount.email.$model"
              minlength="5"
              maxlength="254"
              email
              required
              data-cy="email"
            />
            <div v-if="$v.registerAccount.email.$anyDirty && $v.registerAccount.email.$invalid">
              <small class="form-text text-danger" v-if="!$v.registerAccount.email.required"> Your email is required. </small>
              <small class="form-text text-danger" v-if="!$v.registerAccount.email.email"> Your email is invalid. </small>
              <small class="form-text text-danger" v-if="!$v.registerAccount.email.minLength">
                Your email is required to be at least 5 characters.
              </small>
              <small class="form-text text-danger" v-if="!$v.registerAccount.email.maxLength">
                Your email cannot be longer than 100 characters.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="firstPassword">New password</label>
            <input
              type="password"
              class="form-control"
              id="firstPassword"
              name="password"
              :class="{ valid: !$v.registerAccount.password.$invalid, invalid: $v.registerAccount.password.$invalid }"
              v-model="$v.registerAccount.password.$model"
              minlength="4"
              maxlength="50"
              required
              data-cy="firstPassword"
            />
            <div v-if="$v.registerAccount.password.$anyDirty && $v.registerAccount.password.$invalid">
              <small class="form-text text-danger" v-if="!$v.registerAccount.password.required"> Your password is required. </small>
              <small class="form-text text-danger" v-if="!$v.registerAccount.password.minLength">
                Your password is required to be at least 4 characters.
              </small>
              <small class="form-text text-danger" v-if="!$v.registerAccount.password.maxLength">
                Your password cannot be longer than 50 characters.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="secondPassword">New password confirmation</label>
            <input
              type="password"
              class="form-control"
              id="secondPassword"
              name="confirmPasswordInput"
              :class="{ valid: !$v.confirmPassword.$invalid, invalid: $v.confirmPassword.$invalid }"
              v-model="$v.confirmPassword.$model"
              minlength="4"
              maxlength="50"
              required
              data-cy="secondPassword"
            />
            <div v-if="$v.confirmPassword.$dirty && $v.confirmPassword.$invalid">
              <small class="form-text text-danger" v-if="!$v.confirmPassword.required"> Your confirmation password is required. </small>
              <small class="form-text text-danger" v-if="!$v.confirmPassword.minLength">
                Your confirmation password is required to be at least 4 characters.
              </small>
              <small class="form-text text-danger" v-if="!$v.confirmPassword.maxLength">
                Your confirmation password cannot be longer than 50 characters.
              </small>
              <small class="form-text text-danger" v-if="!$v.confirmPassword.sameAsPassword">
                The password and its confirmation do not match!
              </small>
            </div>
          </div>

          <button type="submit" :disabled="$v.$invalid" class="btn btn-primary" data-cy="submit">Register</button>
        </form>
        <p></p>
        <div class="alert alert-warning">
          <span>If you want to </span>
          <a class="alert-link" v-on:click="openLogin()">sign in</a
          ><span
            >, you can try the default accounts:<br />- Administrator (login="admin" and password="admin") <br />- User (login="user" and
            password="user").</span
          >
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./register.component.ts"></script>
