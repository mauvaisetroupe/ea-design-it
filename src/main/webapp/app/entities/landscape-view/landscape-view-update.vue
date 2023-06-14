<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2 id="eaDesignItApp.landscapeView.home.createOrEditLabel" data-cy="LandscapeViewCreateUpdateHeading">
          Create or edit a LandscapeView
        </h2>
        <div>
          <div class="form-group" v-if="landscapeView.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="landscapeView.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="landscape-view-viewpoint">Viewpoint</label>
            <select
              class="form-control"
              name="viewpoint"
              :class="{ valid: !$v.landscapeView.viewpoint.$invalid, invalid: $v.landscapeView.viewpoint.$invalid }"
              v-model="$v.landscapeView.viewpoint.$model"
              id="landscape-view-viewpoint"
              data-cy="viewpoint"
            >
              <option v-for="viewPoint in viewPointValues" :key="viewPoint" v-bind:value="viewPoint">{{ viewPoint }}</option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="landscape-view-diagramName">Diagram Name</label>
            <input
              type="text"
              class="form-control"
              name="diagramName"
              id="landscape-view-diagramName"
              data-cy="diagramName"
              :class="{ valid: !$v.landscapeView.diagramName.$invalid, invalid: $v.landscapeView.diagramName.$invalid }"
              v-model="$v.landscapeView.diagramName.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="landscape-view-compressedDrawXML">Compressed Draw XML</label>
            <textarea
              class="form-control"
              name="compressedDrawXML"
              id="landscape-view-compressedDrawXML"
              data-cy="compressedDrawXML"
              :class="{ valid: !$v.landscapeView.compressedDrawXML.$invalid, invalid: $v.landscapeView.compressedDrawXML.$invalid }"
              v-model="$v.landscapeView.compressedDrawXML.$model"
            ></textarea>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="landscape-view-compressedDrawSVG">Compressed Draw SVG</label>
            <textarea
              class="form-control"
              name="compressedDrawSVG"
              id="landscape-view-compressedDrawSVG"
              data-cy="compressedDrawSVG"
              :class="{ valid: !$v.landscapeView.compressedDrawSVG.$invalid, invalid: $v.landscapeView.compressedDrawSVG.$invalid }"
              v-model="$v.landscapeView.compressedDrawSVG.$model"
            ></textarea>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="landscape-view-owner">Owner</label>
            <select class="form-control" id="landscape-view-owner" data-cy="owner" name="owner" v-model="landscapeView.owner">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="landscapeView.owner && ownerOption.id === landscapeView.owner.id ? landscapeView.owner : ownerOption"
                v-for="ownerOption in owners"
                :key="ownerOption.id"
              >
                {{ ownerOption.name }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label for="landscape-view-flows">Flows</label>
            <select
              class="form-control"
              id="landscape-view-flows"
              data-cy="flows"
              multiple
              name="flows"
              v-if="landscapeView.flows !== undefined"
              v-model="landscapeView.flows"
            >
              <option
                v-bind:value="getSelected(landscapeView.flows, functionalFlowOption)"
                v-for="functionalFlowOption in functionalFlows"
                :key="functionalFlowOption.id"
              >
                {{ functionalFlowOption.alias }}
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
            :disabled="$v.landscapeView.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span>Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./landscape-view-update.component.ts"></script>
