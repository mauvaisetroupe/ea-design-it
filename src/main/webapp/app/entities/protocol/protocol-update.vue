<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2 id="eaDesignItApp.protocol.home.createOrEditLabel" data-cy="ProtocolCreateUpdateHeading">Create or edit a Protocol</h2>
        <div>
          <div class="form-group" v-if="protocol.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="protocol.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="protocol-name">Name</label>
            <input
              type="text"
              class="form-control"
              name="name"
              id="protocol-name"
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
            <label class="form-control-label" for="protocol-type">Type</label>
            <select
              class="form-control"
              name="type"
              :class="{ valid: !v$.type.$invalid, invalid: v$.type.$invalid }"
              v-model="v$.type.$model"
              id="protocol-type"
              data-cy="type"
              required
            >
              <option v-for="protocolType in protocolTypeValues" :key="protocolType" v-bind:value="protocolType">{{ protocolType }}</option>
            </select>
            <div v-if="v$.type.$anyDirty && v$.type.$invalid">
              <small class="form-text text-danger" v-for="error of v$.type.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="protocol-description">Description</label>
            <input
              type="text"
              class="form-control"
              name="description"
              id="protocol-description"
              data-cy="description"
              :class="{ valid: !v$.description.$invalid, invalid: v$.description.$invalid }"
              v-model="v$.description.$model"
            />
            <div v-if="v$.description.$anyDirty && v$.description.$invalid">
              <small class="form-text text-danger" v-for="error of v$.description.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="protocol-scope">Scope</label>
            <input
              type="text"
              class="form-control"
              name="scope"
              id="protocol-scope"
              data-cy="scope"
              :class="{ valid: !v$.scope.$invalid, invalid: v$.scope.$invalid }"
              v-model="v$.scope.$model"
            />
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
<script lang="ts" src="./protocol-update.component.ts"></script>
