<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2 id="eaDesignItApp.capability.home.createOrEditLabel" data-cy="CapabilityCreateUpdateHeading">Create or edit a Capability</h2>
        <div>
          <div class="form-group" v-if="capability.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="capability.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="capability-name">Name</label>
            <input
              type="text"
              class="form-control"
              name="name"
              id="capability-name"
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
            <label class="form-control-label" for="capability-description">Description</label>
            <input
              type="text"
              class="form-control"
              name="description"
              id="capability-description"
              data-cy="description"
              :class="{ valid: !v$.description.$invalid, invalid: v$.description.$invalid }"
              v-model="v$.description.$model"
            />
            <div v-if="v$.description.$anyDirty && v$.description.$invalid">
              <small class="form-text text-danger" v-for="error of v$.description.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="capability-comment">Comment</label>
            <input
              type="text"
              class="form-control"
              name="comment"
              id="capability-comment"
              data-cy="comment"
              :class="{ valid: !v$.comment.$invalid, invalid: v$.comment.$invalid }"
              v-model="v$.comment.$model"
            />
            <div v-if="v$.comment.$anyDirty && v$.comment.$invalid">
              <small class="form-text text-danger" v-for="error of v$.comment.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="capability-level">Level</label>
            <input
              type="number"
              class="form-control"
              name="level"
              id="capability-level"
              data-cy="level"
              :class="{ valid: !v$.level.$invalid, invalid: v$.level.$invalid }"
              v-model.number="v$.level.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="capability-parent">Parent</label>
            <select class="form-control" id="capability-parent" data-cy="parent" name="parent" v-model="capability.parent">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="capability.parent && capabilityOption.id === capability.parent.id ? capability.parent : capabilityOption"
                v-for="capabilityOption in capabilities"
                :key="capabilityOption.id"
              >
                {{ capabilityOption.name }}
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
<script lang="ts" src="./capability-update.component.ts"></script>
