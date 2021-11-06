<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2 id="eaDesignItApp.applicationImport.home.createOrEditLabel" data-cy="ApplicationImportCreateUpdateHeading">
          Create or edit a ApplicationImport
        </h2>
        <div>
          <div class="form-group" v-if="applicationImport.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="applicationImport.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="application-import-importId">Import Id</label>
            <div class="d-flex">
              <input
                id="application-import-importId"
                data-cy="importId"
                type="datetime-local"
                class="form-control"
                name="importId"
                :class="{ valid: !$v.applicationImport.importId.$invalid, invalid: $v.applicationImport.importId.$invalid }"
                :value="convertDateTimeFromServer($v.applicationImport.importId.$model)"
                @change="updateInstantField('importId', $event)"
              />
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="application-import-excelFileName">Excel File Name</label>
            <input
              type="text"
              class="form-control"
              name="excelFileName"
              id="application-import-excelFileName"
              data-cy="excelFileName"
              :class="{ valid: !$v.applicationImport.excelFileName.$invalid, invalid: $v.applicationImport.excelFileName.$invalid }"
              v-model="$v.applicationImport.excelFileName.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="application-import-idFromExcel">Id From Excel</label>
            <input
              type="text"
              class="form-control"
              name="idFromExcel"
              id="application-import-idFromExcel"
              data-cy="idFromExcel"
              :class="{ valid: !$v.applicationImport.idFromExcel.$invalid, invalid: $v.applicationImport.idFromExcel.$invalid }"
              v-model="$v.applicationImport.idFromExcel.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="application-import-name">Name</label>
            <input
              type="text"
              class="form-control"
              name="name"
              id="application-import-name"
              data-cy="name"
              :class="{ valid: !$v.applicationImport.name.$invalid, invalid: $v.applicationImport.name.$invalid }"
              v-model="$v.applicationImport.name.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="application-import-description">Description</label>
            <input
              type="text"
              class="form-control"
              name="description"
              id="application-import-description"
              data-cy="description"
              :class="{ valid: !$v.applicationImport.description.$invalid, invalid: $v.applicationImport.description.$invalid }"
              v-model="$v.applicationImport.description.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="application-import-type">Type</label>
            <input
              type="text"
              class="form-control"
              name="type"
              id="application-import-type"
              data-cy="type"
              :class="{ valid: !$v.applicationImport.type.$invalid, invalid: $v.applicationImport.type.$invalid }"
              v-model="$v.applicationImport.type.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="application-import-technology">Technology</label>
            <input
              type="text"
              class="form-control"
              name="technology"
              id="application-import-technology"
              data-cy="technology"
              :class="{ valid: !$v.applicationImport.technology.$invalid, invalid: $v.applicationImport.technology.$invalid }"
              v-model="$v.applicationImport.technology.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="application-import-comment">Comment</label>
            <input
              type="text"
              class="form-control"
              name="comment"
              id="application-import-comment"
              data-cy="comment"
              :class="{ valid: !$v.applicationImport.comment.$invalid, invalid: $v.applicationImport.comment.$invalid }"
              v-model="$v.applicationImport.comment.$model"
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
            :disabled="$v.applicationImport.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span>Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./application-import-update.component.ts"></script>
