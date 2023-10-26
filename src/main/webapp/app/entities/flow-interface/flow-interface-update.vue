<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2 id="eaDesignItApp.flowInterface.home.createOrEditLabel" data-cy="FlowInterfaceCreateUpdateHeading">
          <font-awesome-icon icon="grip-lines" style="color: Tomato; font-size: 0.9em"></font-awesome-icon> Create or edit a FlowInterface
          <span v-if="this.$route.query.functionalFlowId"> for Function Flow {{ this.$route.query.functionalFlowId }}</span>
        </h2>
        <div>
          <div class="form-group" v-if="flowInterface.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="flowInterface.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="flow-interface-alias">Alias</label>
            <input
              type="text"
              class="form-control"
              name="alias"
              id="flow-interface-alias"
              data-cy="alias"
              :class="{ valid: !v$.alias.$invalid, invalid: v$.alias.$invalid }"
              v-model="v$.alias.$model"
              required
            />
            <div v-if="v$.alias.$anyDirty && v$.alias.$invalid">
              <small class="form-text text-danger" v-for="error of v$.alias.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="flow-interface-status">Status</label>
            <input
              type="text"
              class="form-control"
              name="status"
              id="flow-interface-status"
              data-cy="status"
              :class="{ valid: !v$.status.$invalid, invalid: v$.status.$invalid }"
              v-model="v$.status.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="flow-interface-documentationURL">Documentation URL</label>
            <input
              type="text"
              class="form-control"
              name="documentationURL"
              id="flow-interface-documentationURL"
              data-cy="documentationURL"
              :class="{ valid: !v$.documentationURL.$invalid, invalid: v$.documentationURL.$invalid }"
              v-model="v$.documentationURL.$model"
            />
            <div v-if="v$.documentationURL.$anyDirty && v$.documentationURL.$invalid">
              <small class="form-text text-danger" v-for="error of v$.documentationURL.$errors" :key="error.$uid">{{
                error.$message
              }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="flow-interface-documentationURL2">Documentation URL 2</label>
            <input
              type="text"
              class="form-control"
              name="documentationURL2"
              id="flow-interface-documentationURL2"
              data-cy="documentationURL2"
              :class="{ valid: !v$.documentationURL2.$invalid, invalid: v$.documentationURL2.$invalid }"
              v-model="v$.documentationURL2.$model"
            />
            <div v-if="v$.documentationURL2.$anyDirty && v$.documentationURL2.$invalid">
              <small class="form-text text-danger" v-for="error of v$.documentationURL2.$errors" :key="error.$uid">{{
                error.$message
              }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="flow-interface-description">Description</label>
            <input
              type="text"
              class="form-control"
              name="description"
              id="flow-interface-description"
              data-cy="description"
              :class="{ valid: !v$.description.$invalid, invalid: v$.description.$invalid }"
              v-model="v$.description.$model"
            />
            <div v-if="v$.description.$anyDirty && v$.description.$invalid">
              <small class="form-text text-danger" v-for="error of v$.description.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="flow-interface-startDate">Start Date</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="flow-interface-startDate"
                  v-model="v$.startDate.$model"
                  name="startDate"
                  class="form-control"
                  :locale="currentLanguage"
                  button-only
                  today-button
                  reset-button
                  close-button
                >
                </b-form-datepicker>
              </b-input-group-prepend>
              <b-form-input
                id="flow-interface-startDate"
                data-cy="startDate"
                type="text"
                class="form-control"
                name="startDate"
                :class="{ valid: !v$.startDate.$invalid, invalid: v$.startDate.$invalid }"
                v-model="v$.startDate.$model"
              />
            </b-input-group>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="flow-interface-endDate">End Date</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="flow-interface-endDate"
                  v-model="v$.endDate.$model"
                  name="endDate"
                  class="form-control"
                  :locale="currentLanguage"
                  button-only
                  today-button
                  reset-button
                  close-button
                >
                </b-form-datepicker>
              </b-input-group-prepend>
              <b-form-input
                id="flow-interface-endDate"
                data-cy="endDate"
                type="text"
                class="form-control"
                name="endDate"
                :class="{ valid: !v$.endDate.$invalid, invalid: v$.endDate.$invalid }"
                v-model="v$.endDate.$model"
              />
            </b-input-group>
          </div>
          <div class="form-row">
            <div class="form-group col-md-6">
              <label class="form-control-label" for="flow-interface-source">Source</label>
              <select
                class="form-control"
                id="flow-interface-source"
                data-cy="source"
                name="source"
                v-model="flowInterface.source"
                required
                @change="flowInterface.sourceComponent = null"
              >
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
            <div class="form-group col-md-6">
              <label class="form-control-label" for="flow-interface-sourceComponent">Source Component</label>
              <select
                class="form-control"
                id="flow-interface-sourceComponent"
                data-cy="sourceComponent"
                name="sourceComponent"
                v-model="flowInterface.sourceComponent"
                @change="changeSource(flowInterface.sourceComponent)"
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
            <div v-if="v$.source.$anyDirty && v$.source.$invalid">
              <small class="form-text text-danger" v-for="error of v$.source.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-row">
            <div class="form-group col-md-6">
              <label class="form-control-label" for="flow-interface-target">Target</label>
              <select
                class="form-control"
                id="flow-interface-target"
                data-cy="target"
                name="target"
                v-model="flowInterface.target"
                required
                @change="flowInterface.targetComponent = null"
              >
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
            <div v-if="v$.target.$anyDirty && v$.target.$invalid">
              <small class="form-text text-danger" v-for="error of v$.target.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>

            <div class="form-group col-md-6">
              <label class="form-control-label" for="flow-interface-targetComponent">Target Component</label>
              <select
                class="form-control"
                id="flow-interface-targetComponent"
                data-cy="targetComponent"
                name="targetComponent"
                v-model="flowInterface.targetComponent"
                @change="changeTarget(flowInterface.targetComponent)"
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
          </div>
          <div class="form-group">
            <label class="form-control-label" for="flow-interface-protocol">Protocol</label>
            <select class="form-control" id="flow-interface-protocol" data-cy="protocol" name="protocol" v-model="flowInterface.protocol">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="
                  flowInterface.protocol && protocolOption.id === flowInterface.protocol.id ? flowInterface.protocol : protocolOption
                "
                v-for="protocolOption in protocols"
                :key="protocolOption.id"
              >
                {{ protocolOption.name }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="flow-interface-owner">Owner</label>
            <select class="form-control" id="flow-interface-owner" data-cy="owner" name="owner" v-model="flowInterface.owner">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="flowInterface.owner && ownerOption.id === flowInterface.owner.id ? flowInterface.owner : ownerOption"
                v-for="ownerOption in owners"
                :key="ownerOption.id"
              >
                {{ ownerOption.name }}
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
<script lang="ts" src="./flow-interface-update.component.ts"></script>
