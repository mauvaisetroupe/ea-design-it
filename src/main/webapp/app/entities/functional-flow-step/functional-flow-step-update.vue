<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2 id="eaDesignItApp.functionalFlowStep.home.createOrEditLabel" data-cy="FunctionalFlowStepCreateUpdateHeading">
          Create or edit a Functional Flow Step
        </h2>
        <div>
          <div class="form-group" v-if="functionalFlowStep.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="functionalFlowStep.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="functional-flow-step-description">Description</label>
            <input
              type="text"
              class="form-control"
              name="description"
              id="functional-flow-step-description"
              data-cy="description"
              :class="{ valid: !v$.description.$invalid, invalid: v$.description.$invalid }"
              v-model="v$.description.$model"
            />
            <div v-if="v$.description.$anyDirty && v$.description.$invalid">
              <small class="form-text text-danger" v-for="error of v$.description.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="functional-flow-step-stepOrder">Step Order</label>
            <input
              type="number"
              class="form-control"
              name="stepOrder"
              id="functional-flow-step-stepOrder"
              data-cy="stepOrder"
              :class="{ valid: !v$.stepOrder.$invalid, invalid: v$.stepOrder.$invalid }"
              v-model.number="v$.stepOrder.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="functional-flow-step-flowInterface">Flow Interface</label>
            <select
              class="form-control"
              id="functional-flow-step-flowInterface"
              data-cy="flowInterface"
              name="flowInterface"
              v-model="functionalFlowStep.flowInterface"
              required
            >
              <option v-if="!functionalFlowStep.flowInterface" v-bind:value="null" selected></option>
              <option
                v-bind:value="
                  functionalFlowStep.flowInterface && flowInterfaceOption.id === functionalFlowStep.flowInterface.id
                    ? functionalFlowStep.flowInterface
                    : flowInterfaceOption
                "
                v-for="flowInterfaceOption in flowInterfaces"
                :key="flowInterfaceOption.id"
              >
                {{ flowInterfaceOption.alias }}
              </option>
            </select>
          </div>
          <div v-if="v$.flowInterface.$anyDirty && v$.flowInterface.$invalid">
            <small class="form-text text-danger" v-for="error of v$.flowInterface.$errors" :key="error.$uid">{{ error.$message }}</small>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="functional-flow-step-group">Group</label>
            <select class="form-control" id="functional-flow-step-group" data-cy="group" name="group" v-model="functionalFlowStep.group">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="
                  functionalFlowStep.group && flowGroupOption.id === functionalFlowStep.group.id
                    ? functionalFlowStep.group
                    : flowGroupOption
                "
                v-for="flowGroupOption in flowGroups"
                :key="flowGroupOption.id"
              >
                {{ flowGroupOption.alias }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="functional-flow-step-flow">Flow</label>
            <select
              class="form-control"
              id="functional-flow-step-flow"
              data-cy="flow"
              name="flow"
              v-model="functionalFlowStep.flow"
              required
            >
              <option v-if="!functionalFlowStep.flow" v-bind:value="null" selected></option>
              <option
                v-bind:value="
                  functionalFlowStep.flow && functionalFlowOption.id === functionalFlowStep.flow.id
                    ? functionalFlowStep.flow
                    : functionalFlowOption
                "
                v-for="functionalFlowOption in functionalFlows"
                :key="functionalFlowOption.id"
              >
                {{ functionalFlowOption.alias }}
              </option>
            </select>
          </div>
          <div v-if="v$.flow.$anyDirty && v$.flow.$invalid">
            <small class="form-text text-danger" v-for="error of v$.flow.$errors" :key="error.$uid">{{ error.$message }}</small>
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
<script lang="ts" src="./functional-flow-step-update.component.ts"></script>
