<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2 id="eaDesignItApp.businessObject.home.createOrEditLabel" data-cy="BusinessObjectCreateUpdateHeading">
          Create or edit a Business Object
        </h2>
        <div>
          <div class="form-group" v-if="businessObject.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="businessObject.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="business-object-name">Name</label>
            <input
              type="text"
              class="form-control"
              name="name"
              id="business-object-name"
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
            <label class="form-control-label" for="business-object-implementable">Implementable</label>
            <input
              type="checkbox"
              class="form-check"
              name="implementable"
              id="business-object-implementable"
              data-cy="implementable"
              :class="{ valid: !v$.implementable.$invalid, invalid: v$.implementable.$invalid }"
              v-model="v$.implementable.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="business-object-owner">Owner</label>
            <select class="form-control" id="business-object-owner" data-cy="owner" name="owner" v-model="businessObject.owner">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="businessObject.owner && ownerOption.id === businessObject.owner.id ? businessObject.owner : ownerOption"
                v-for="ownerOption in owners"
                :key="ownerOption.id"
              >
                {{ ownerOption.name }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="business-object-generalization">Generalization</label>
            <select
              class="form-control"
              id="business-object-generalization"
              data-cy="generalization"
              name="generalization"
              v-model="businessObject.generalization"
            >
              <option v-bind:value="null"></option>
              <option
                v-bind:value="
                  businessObject.generalization && businessObjectOption.id === businessObject.generalization.id
                    ? businessObject.generalization
                    : businessObjectOption
                "
                v-for="businessObjectOption in businessObjects"
                :key="businessObjectOption.id"
              >
                {{ businessObjectOption.name }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="business-object-container">Container</label>
            <select
              class="form-control"
              id="business-object-container"
              data-cy="container"
              name="container"
              v-model="businessObject.container"
            >
              <option v-bind:value="null"></option>
              <option
                v-bind:value="
                  businessObject.container && businessObjectOption.id === businessObject.container.id
                    ? businessObject.container
                    : businessObjectOption
                "
                v-for="businessObjectOption in businessObjects"
                :key="businessObjectOption.id"
              >
                {{ businessObjectOption.name }}
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
<script lang="ts" src="./business-object-update.component.ts"></script>
