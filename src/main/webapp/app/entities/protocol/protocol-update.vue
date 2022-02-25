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
              :class="{ valid: !$v.protocol.name.$invalid, invalid: $v.protocol.name.$invalid }"
              v-model="$v.protocol.name.$model"
              required
            />
            <div v-if="$v.protocol.name.$anyDirty && $v.protocol.name.$invalid">
              <small class="form-text text-danger" v-if="!$v.protocol.name.required"> This field is required. </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="protocol-type">Type</label>
            <select
              class="form-control"
              name="type"
              :class="{ valid: !$v.protocol.type.$invalid, invalid: $v.protocol.type.$invalid }"
              v-model="$v.protocol.type.$model"
              id="protocol-type"
              data-cy="type"
              required
            >
              <option v-for="protocolType in protocolTypeValues" :key="protocolType" v-bind:value="protocolType">{{ protocolType }}</option>
            </select>
            <div v-if="$v.protocol.type.$anyDirty && $v.protocol.type.$invalid">
              <small class="form-text text-danger" v-if="!$v.protocol.type.required"> This field is required. </small>
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
              :class="{ valid: !$v.protocol.description.$invalid, invalid: $v.protocol.description.$invalid }"
              v-model="$v.protocol.description.$model"
            />
            <div v-if="$v.protocol.description.$anyDirty && $v.protocol.description.$invalid">
              <small class="form-text text-danger" v-if="!$v.protocol.description.maxLength">
                This field cannot be longer than 1000 characters.
              </small>
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
              :class="{ valid: !$v.protocol.scope.$invalid, invalid: $v.protocol.scope.$invalid }"
              v-model="$v.protocol.scope.$model"
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
            :disabled="$v.protocol.$invalid || isSaving"
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
