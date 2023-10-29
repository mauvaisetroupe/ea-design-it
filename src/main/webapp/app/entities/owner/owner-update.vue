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
              :class="{ valid: !v$.name.$invalid, invalid: v$.name.$invalid }"
              v-model="v$.name.$model"
              required
            />
            <div v-if="v$.name.$anyDirty && v$.name.$invalid">
              <small class="form-text text-danger" v-for="error of v$.name.$errors" :key="error.$uid">{{ error.$message }}</small>
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
              :class="{ valid: !v$.firstname.$invalid, invalid: v$.firstname.$invalid }"
              v-model="v$.firstname.$model"
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
              :class="{ valid: !v$.lastname.$invalid, invalid: v$.lastname.$invalid }"
              v-model="v$.lastname.$model"
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
              :class="{ valid: !v$.email.$invalid, invalid: v$.email.$invalid }"
              v-model="v$.email.$model"
            />
            <div v-if="v$.email.$anyDirty && v$.email.$invalid">
              <small class="form-text text-danger" v-for="error of v$.email.$errors" :key="error.$uid">{{ error.$message }}</small>
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
<script lang="ts" src="./owner-update.component.ts"></script>
