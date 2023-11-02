<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2 id="eaDesignItApp.dataObject.home.createOrEditLabel" data-cy="DataObjectCreateUpdateHeading">Create or edit a Data Object</h2>
        <div>
          <div class="form-group" v-if="dataObject.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="dataObject.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="data-object-name">Name</label>
            <input
              type="text"
              class="form-control"
              name="name"
              id="data-object-name"
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
            <label class="form-control-label" for="data-object-type">Type</label>
            <select
              class="form-control"
              name="type"
              :class="{ valid: !v$.type.$invalid, invalid: v$.type.$invalid }"
              v-model="v$.type.$model"
              id="data-object-type"
              data-cy="type"
            >
              <option v-for="dataObjectType in dataObjectTypeValues" :key="dataObjectType" v-bind:value="dataObjectType">
                {{ dataObjectType }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="data-object-owner">Owner</label>
            <select class="form-control" id="data-object-owner" data-cy="owner" name="owner" v-model="dataObject.owner">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="dataObject.owner && ownerOption.id === dataObject.owner.id ? dataObject.owner : ownerOption"
                v-for="ownerOption in owners"
                :key="ownerOption.id"
              >
                {{ ownerOption.name }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="data-object-application">Application</label>
            <select
              class="form-control"
              id="data-object-application"
              data-cy="application"
              name="application"
              v-model="dataObject.application"
            >
              <option v-bind:value="null"></option>
              <option
                v-bind:value="
                  dataObject.application && applicationOption.id === dataObject.application.id ? dataObject.application : applicationOption
                "
                v-for="applicationOption in applications"
                :key="applicationOption.id"
              >
                {{ applicationOption.name }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label for="data-object-technologies">Technologies</label>
            <select
              class="form-control"
              id="data-object-technologies"
              data-cy="technologies"
              multiple
              name="technologies"
              v-if="dataObject.technologies !== undefined"
              v-model="dataObject.technologies"
            >
              <option
                v-bind:value="getSelected(dataObject.technologies, technologyOption)"
                v-for="technologyOption in technologies"
                :key="technologyOption.id"
              >
                {{ technologyOption.name }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="data-object-businessObject">Business Object</label>
            <select
              class="form-control"
              id="data-object-businessObject"
              data-cy="businessObject"
              name="businessObject"
              v-model="dataObject.businessObject"
            >
              <option v-bind:value="null"></option>
              <option
                v-bind:value="
                  dataObject.businessObject && businessObjectOption.id === dataObject.businessObject.id
                    ? dataObject.businessObject
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
            <label class="form-control-label" for="data-object-container">Container</label>
            <select class="form-control" id="data-object-container" data-cy="container" name="container" v-model="dataObject.container">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="
                  dataObject.container && dataObjectOption.id === dataObject.container.id ? dataObject.container : dataObjectOption
                "
                v-for="dataObjectOption in dataObjects"
                :key="dataObjectOption.id"
              >
                {{ dataObjectOption.name }}
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
<script lang="ts" src="./data-object-update.component.ts"></script>
