<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2 id="eaDesignItApp.dataFlowItem.home.createOrEditLabel" data-cy="DataFlowItemCreateUpdateHeading">
          <font-awesome-icon icon="clone" style="color: Tomato; font-size: 0.7em"></font-awesome-icon> Create or edit a DataFlowItem
        </h2>
        <div>
          <div class="form-group" v-if="dataFlowItem.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="dataFlowItem.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="data-flow-item-resourceName">Resource Name</label>
            <input
              type="text"
              class="form-control"
              name="resourceName"
              id="data-flow-item-resourceName"
              data-cy="resourceName"
              :class="{ valid: !v$.resourceName.$invalid, invalid: v$.resourceName.$invalid }"
              v-model="v$.resourceName.$model"
              required
            />
            <div v-if="v$.resourceName.$anyDirty && v$.resourceName.$invalid">
              <small class="form-text text-danger" v-for="error of v$.resourceName.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="data-flow-item-resourceType">Resource Type</label>
            <input
              type="text"
              class="form-control"
              name="resourceType"
              id="data-flow-item-resourceType"
              data-cy="resourceType"
              :class="{ valid: !v$.resourceType.$invalid, invalid: v$.resourceType.$invalid }"
              v-model="v$.resourceType.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="data-flow-item-description">Description</label>
            <input
              type="text"
              class="form-control"
              name="description"
              id="data-flow-item-description"
              data-cy="description"
              :class="{ valid: !v$.description.$invalid, invalid: v$.description.$invalid }"
              v-model="v$.description.$model"
            />
            <div v-if="v$.description.$anyDirty && v$.description.$invalid">
              <small class="form-text text-danger" v-for="error of v$.description.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="data-flow-item-contractURL">Contract URL</label>
            <input
              type="text"
              class="form-control"
              name="contractURL"
              id="data-flow-item-contractURL"
              data-cy="contractURL"
              :class="{ valid: !v$.contractURL.$invalid, invalid: v$.contractURL.$invalid }"
              v-model="v$.contractURL.$model"
            />
            <div v-if="v$.contractURL.$anyDirty && v$.contractURL.$invalid">
              <small class="form-text text-danger" v-for="error of v$.contractURL.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="data-flow-item-documentationURL">Documentation URL</label>
            <input
              type="text"
              class="form-control"
              name="documentationURL"
              id="data-flow-item-documentationURL"
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
            <label class="form-control-label" for="data-flow-item-startDate">Start Date</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="data-flow-item-startDate"
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
                id="data-flow-item-startDate"
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
            <label class="form-control-label" for="data-flow-item-endDate">End Date</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="data-flow-item-endDate"
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
                id="data-flow-item-endDate"
                data-cy="endDate"
                type="text"
                class="form-control"
                name="endDate"
                :class="{ valid: !v$.endDate.$invalid, invalid: v$.endDate.$invalid }"
                v-model="v$.endDate.$model"
              />
            </b-input-group>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="data-flow-item-dataFlow">Data Flow</label>
            <select class="form-control" id="data-flow-item-dataFlow" data-cy="dataFlow" name="dataFlow" v-model="dataFlowItem.dataFlow">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="
                  dataFlowItem.dataFlow && dataFlowOption.id === dataFlowItem.dataFlow.id ? dataFlowItem.dataFlow : dataFlowOption
                "
                v-for="dataFlowOption in dataFlows"
                :key="dataFlowOption.id"
              >
                {{ dataFlowOption.id }}
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
<script lang="ts" src="./data-flow-item-update.component.ts"></script>
