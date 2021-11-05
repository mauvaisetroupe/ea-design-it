<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2 id="eaDesignItApp.flowInterface.home.createOrEditLabel" data-cy="FlowInterfaceCreateUpdateHeading">
          Create or edit a FlowInterface
        </h2>
        <div>
          <div class="form-group" v-if="flowInterface.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="flowInterface.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="flow-interface-protocol">Protocol</label>
            <select
              class="form-control"
              name="protocol"
              :class="{ valid: !$v.flowInterface.protocol.$invalid, invalid: $v.flowInterface.protocol.$invalid }"
              v-model="$v.flowInterface.protocol.$model"
              id="flow-interface-protocol"
              data-cy="protocol"
            >
              <option v-for="protocol in protocolValues" :key="protocol" v-bind:value="protocol">{{ protocol }}</option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="flow-interface-status">Status</label>
            <input
              type="text"
              class="form-control"
              name="status"
              id="flow-interface-status"
              data-cy="status"
              :class="{ valid: !$v.flowInterface.status.$invalid, invalid: $v.flowInterface.status.$invalid }"
              v-model="$v.flowInterface.status.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="flow-interface-source">Source</label>
            <select class="form-control" id="flow-interface-source" data-cy="source" name="source" v-model="flowInterface.source" required>
              <option v-if="!flowInterface.source" v-bind:value="null" selected></option>
              <option
                v-bind:value="
                  flowInterface.source && applicationOption.id === flowInterface.source.id ? flowInterface.source : applicationOption
                "
                v-for="applicationOption in applications"
                :key="applicationOption.id"
              >
                {{ applicationOption.name }}
              </option>
            </select>
          </div>
          <div v-if="$v.flowInterface.source.$anyDirty && $v.flowInterface.source.$invalid">
            <small class="form-text text-danger" v-if="!$v.flowInterface.source.required"> This field is required. </small>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="flow-interface-target">Target</label>
            <select class="form-control" id="flow-interface-target" data-cy="target" name="target" v-model="flowInterface.target" required>
              <option v-if="!flowInterface.target" v-bind:value="null" selected></option>
              <option
                v-bind:value="
                  flowInterface.target && applicationOption.id === flowInterface.target.id ? flowInterface.target : applicationOption
                "
                v-for="applicationOption in applications"
                :key="applicationOption.id"
              >
                {{ applicationOption.name }}
              </option>
            </select>
          </div>
          <div v-if="$v.flowInterface.target.$anyDirty && $v.flowInterface.target.$invalid">
            <small class="form-text text-danger" v-if="!$v.flowInterface.target.required"> This field is required. </small>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="flow-interface-sourceComponent">Source Component</label>
            <select
              class="form-control"
              id="flow-interface-sourceComponent"
              data-cy="sourceComponent"
              name="sourceComponent"
              v-model="flowInterface.sourceComponent"
            >
              <option v-bind:value="null"></option>
              <option
                v-bind:value="
                  flowInterface.sourceComponent && applicationComponentOption.id === flowInterface.sourceComponent.id
                    ? flowInterface.sourceComponent
                    : applicationComponentOption
                "
                v-for="applicationComponentOption in applicationComponents"
                :key="applicationComponentOption.id"
              >
                {{ applicationComponentOption.name }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="flow-interface-targetComponent">Target Component</label>
            <select
              class="form-control"
              id="flow-interface-targetComponent"
              data-cy="targetComponent"
              name="targetComponent"
              v-model="flowInterface.targetComponent"
            >
              <option v-bind:value="null"></option>
              <option
                v-bind:value="
                  flowInterface.targetComponent && applicationComponentOption.id === flowInterface.targetComponent.id
                    ? flowInterface.targetComponent
                    : applicationComponentOption
                "
                v-for="applicationComponentOption in applicationComponents"
                :key="applicationComponentOption.id"
              >
                {{ applicationComponentOption.name }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="flow-interface-owner">Owner</label>
            <select class="form-control" id="flow-interface-owner" data-cy="owner" name="owner" v-model="flowInterface.owner" required>
              <option v-if="!flowInterface.owner" v-bind:value="null" selected></option>
              <option
                v-bind:value="flowInterface.owner && ownerOption.id === flowInterface.owner.id ? flowInterface.owner : ownerOption"
                v-for="ownerOption in owners"
                :key="ownerOption.id"
              >
                {{ ownerOption.name }}
              </option>
            </select>
          </div>
          <div v-if="$v.flowInterface.owner.$anyDirty && $v.flowInterface.owner.$invalid">
            <small class="form-text text-danger" v-if="!$v.flowInterface.owner.required"> This field is required. </small>
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
            :disabled="$v.flowInterface.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span>Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./flow-interface-update.component.ts"></script>
