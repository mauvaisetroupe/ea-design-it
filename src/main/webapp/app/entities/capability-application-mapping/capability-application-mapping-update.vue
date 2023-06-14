<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="eaDesignItApp.capabilityApplicationMapping.home.createOrEditLabel"
          data-cy="CapabilityApplicationMappingCreateUpdateHeading"
        >
          Create or edit a CapabilityApplicationMapping
        </h2>
        <div>
          <div class="form-group" v-if="capabilityApplicationMapping.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="capabilityApplicationMapping.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="capability-application-mapping-capability">Capability</label>
            <select
              class="form-control"
              id="capability-application-mapping-capability"
              data-cy="capability"
              name="capability"
              v-model="capabilityApplicationMapping.capability"
            >
              <option v-bind:value="null"></option>
              <option
                v-bind:value="
                  capabilityApplicationMapping.capability && capabilityOption.id === capabilityApplicationMapping.capability.id
                    ? capabilityApplicationMapping.capability
                    : capabilityOption
                "
                v-for="capabilityOption in capabilities"
                :key="capabilityOption.id"
              >
                {{ capabilityOption.id }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="capability-application-mapping-application">Application</label>
            <select
              class="form-control"
              id="capability-application-mapping-application"
              data-cy="application"
              name="application"
              v-model="capabilityApplicationMapping.application"
            >
              <option v-bind:value="null"></option>
              <option
                v-bind:value="
                  capabilityApplicationMapping.application && applicationOption.id === capabilityApplicationMapping.application.id
                    ? capabilityApplicationMapping.application
                    : applicationOption
                "
                v-for="applicationOption in applications"
                :key="applicationOption.id"
              >
                {{ applicationOption.id }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label for="capability-application-mapping-landscape">Landscape</label>
            <select
              class="form-control"
              id="capability-application-mapping-landscapes"
              data-cy="landscape"
              multiple
              name="landscape"
              v-if="capabilityApplicationMapping.landscapes !== undefined"
              v-model="capabilityApplicationMapping.landscapes"
            >
              <option
                v-bind:value="getSelected(capabilityApplicationMapping.landscapes, landscapeViewOption)"
                v-for="landscapeViewOption in landscapeViews"
                :key="landscapeViewOption.id"
              >
                {{ landscapeViewOption.diagramName }}
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
            :disabled="$v.capabilityApplicationMapping.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span>Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./capability-application-mapping-update.component.ts"></script>
