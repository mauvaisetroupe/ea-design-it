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
            <input
              type="text"
              class="form-control"
              name="importId"
              id="application-import-importId"
              data-cy="importId"
              :class="{ valid: !$v.applicationImport.importId.$invalid, invalid: $v.applicationImport.importId.$invalid }"
              v-model="$v.applicationImport.importId.$model"
            />
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
            <label class="form-control-label" for="application-import-softwareType">Software Type</label>
            <input
              type="text"
              class="form-control"
              name="softwareType"
              id="application-import-softwareType"
              data-cy="softwareType"
              :class="{ valid: !$v.applicationImport.softwareType.$invalid, invalid: $v.applicationImport.softwareType.$invalid }"
              v-model="$v.applicationImport.softwareType.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="application-import-category1">Category 1</label>
            <input
              type="text"
              class="form-control"
              name="category1"
              id="application-import-category1"
              data-cy="category1"
              :class="{ valid: !$v.applicationImport.category1.$invalid, invalid: $v.applicationImport.category1.$invalid }"
              v-model="$v.applicationImport.category1.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="application-import-category2">Category 2</label>
            <input
              type="text"
              class="form-control"
              name="category2"
              id="application-import-category2"
              data-cy="category2"
              :class="{ valid: !$v.applicationImport.category2.$invalid, invalid: $v.applicationImport.category2.$invalid }"
              v-model="$v.applicationImport.category2.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="application-import-category3">Category 3</label>
            <input
              type="text"
              class="form-control"
              name="category3"
              id="application-import-category3"
              data-cy="category3"
              :class="{ valid: !$v.applicationImport.category3.$invalid, invalid: $v.applicationImport.category3.$invalid }"
              v-model="$v.applicationImport.category3.$model"
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
            <label class="form-control-label" for="application-import-documentation">Documentation</label>
            <input
              type="text"
              class="form-control"
              name="documentation"
              id="application-import-documentation"
              data-cy="documentation"
              :class="{ valid: !$v.applicationImport.documentation.$invalid, invalid: $v.applicationImport.documentation.$invalid }"
              v-model="$v.applicationImport.documentation.$model"
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
          <div class="form-group">
            <label class="form-control-label" for="application-import-importStatus">Import Status</label>
            <select
              class="form-control"
              name="importStatus"
              :class="{ valid: !$v.applicationImport.importStatus.$invalid, invalid: $v.applicationImport.importStatus.$invalid }"
              v-model="$v.applicationImport.importStatus.$model"
              id="application-import-importStatus"
              data-cy="importStatus"
            >
              <option v-for="importStatus in importStatusValues" :key="importStatus" v-bind:value="importStatus">{{ importStatus }}</option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="application-import-importStatusMessage">Import Status Message</label>
            <input
              type="text"
              class="form-control"
              name="importStatusMessage"
              id="application-import-importStatusMessage"
              data-cy="importStatusMessage"
              :class="{
                valid: !$v.applicationImport.importStatusMessage.$invalid,
                invalid: $v.applicationImport.importStatusMessage.$invalid,
              }"
              v-model="$v.applicationImport.importStatusMessage.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="application-import-existingApplicationID">Existing Application ID</label>
            <input
              type="text"
              class="form-control"
              name="existingApplicationID"
              id="application-import-existingApplicationID"
              data-cy="existingApplicationID"
              :class="{
                valid: !$v.applicationImport.existingApplicationID.$invalid,
                invalid: $v.applicationImport.existingApplicationID.$invalid,
              }"
              v-model="$v.applicationImport.existingApplicationID.$model"
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
