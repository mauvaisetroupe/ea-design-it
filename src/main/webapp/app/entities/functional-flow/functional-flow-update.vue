<template>
  <div>
    <div class="row">
      <div class="col-12">
        <h2 id="eaDesignItApp.functionalFlow.home.createOrEditLabel" data-cy="FunctionalFlowCreateUpdateHeading">
          <span v-if="functionalFlow && functionalFlow">
            <font-awesome-icon icon="project-diagram" style="color: Tomato; font-size: 0.7em"></font-awesome-icon>
            <span class="text-primary font-weight-bold">FUNCTIONAL FLOW</span> - {{ functionalFlow.alias }} -
            {{ functionalFlow.description }}
          </span>
          <span v-else>
            <font-awesome-icon icon="project-diagram" style="color: Tomato; font-size: 0.7em"></font-awesome-icon> Create a
            <span class="text-primary font-weight-bold">FUNCTIONAL FLOW</span>
          </span>
        </h2>
      </div>
    </div>
    <div class="col-12">
      <b-tabs content-class="mt-3" card pills @input="tabChanged" v-model="tabIndex">
        <b-tab title="Information">
          <div>
            <div class="form-group row" v-if="functionalFlow.id">
              <label for="id" class="col-sm-2 col-form-label">ID</label>
              <input type="text" class="form-control col-sm-10" id="id" name="id" v-model="functionalFlow.id" readonly />
            </div>
            <div class="form-group row">
              <label class="col-sm-2 col-form-label" for="functional-flow-alias">Alias</label>
              <input
                type="text"
                class="form-control col-sm-10"
                name="alias"
                id="functional-flow-alias"
                data-cy="alias"
                :class="{ valid: !v$.alias.$invalid, invalid: v$.alias.$invalid }"
                v-model="v$.alias.$model"
              />
              <div v-if="v$.alias.$anyDirty && v$.alias.$invalid"></div>
            </div>
            <div class="form-group row">
              <label class="col-sm-2 col-form-label" for="functional-flow-description">Description</label>
              <input
                type="text"
                class="form-control col-sm-10"
                name="description"
                id="functional-flow-description"
                data-cy="description"
                :class="{ valid: !v$.description.$invalid, invalid: v$.description.$invalid }"
                v-model="v$.description.$model"
              />
              <div v-if="v$.description.$anyDirty && v$.description.$invalid">
                <small class="form-text text-danger" v-if="!v$.description.maxLength">
                  This field cannot be longer than 1500 characters.
                </small>
              </div>
            </div>
            <div class="form-group row">
              <label class="col-sm-2 col-form-label" for="functional-flow-comment">Comment</label>
              <input
                type="text"
                class="form-control col-sm-10"
                name="comment"
                id="functional-flow-comment"
                data-cy="comment"
                :class="{ valid: !v$.comment.$invalid, invalid: v$.comment.$invalid }"
                v-model="v$.comment.$model"
              />
              <div v-if="v$.comment.$anyDirty && v$.comment.$invalid">
                <small class="form-text text-danger" v-if="!v$.comment.maxLength">
                  This field cannot be longer than 1000 characters.
                </small>
              </div>
            </div>
            <div class="form-group row">
              <label class="col-sm-2 col-form-label" for="functional-flow-status">Status</label>
              <input
                type="text"
                class="form-control col-sm-10"
                name="status"
                id="functional-flow-status"
                data-cy="status"
                :class="{ valid: !v$.status.$invalid, invalid: v$.status.$invalid }"
                v-model="v$.status.$model"
              />
            </div>
            <div class="form-group row">
              <label class="col-sm-2 col-form-label" for="functional-flow-documentationURL">Documentation URL</label>
              <input
                type="text"
                class="form-control col-sm-10"
                name="documentationURL"
                id="functional-flow-documentationURL"
                data-cy="documentationURL"
                :class="{ valid: !v$.documentationURL.$invalid, invalid: v$.documentationURL.$invalid }"
                v-model="v$.documentationURL.$model"
              />
              <div v-if="v$.documentationURL.$anyDirty && v$.documentationURL.$invalid">
                <small class="form-text text-danger" v-if="!v$.documentationURL.maxLength">
                  This field cannot be longer than 500 characters.
                </small>
              </div>
            </div>
            <div class="form-group row">
              <label class="col-sm-2 col-form-label" for="functional-flow-documentationURL2">Documentation URL 2</label>
              <input
                type="text"
                class="form-control col-sm-10"
                name="documentationURL2"
                id="functional-flow-documentationURL2"
                data-cy="documentationURL2"
                :class="{ valid: !v$.documentationURL2.$invalid, invalid: v$.documentationURL2.$invalid }"
                v-model="v$.documentationURL2.$model"
              />
              <div v-if="v$.documentationURL2.$anyDirty && v$.documentationURL2.$invalid">
                <small class="form-text text-danger" v-if="!v$.documentationURL2.maxLength">
                  This field cannot be longer than 500 characters.
                </small>
              </div>
            </div>
            <div class="form-group row">
              <label class="col-sm-2 col-form-label" for="functional-flow-startDate">Start Date</label>
              <b-input-group class="col-sm-10">
                <b-input-group-prepend>
                  <b-form-datepicker
                    aria-controls="functional-flow-startDate"
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
                  id="functional-flow-startDate"
                  data-cy="startDate"
                  type="text"
                  class="form-control"
                  name="startDate"
                  :class="{ valid: !v$.startDate.$invalid, invalid: v$.startDate.$invalid }"
                  v-model="v$.startDate.$model"
                />
              </b-input-group>
            </div>
            <div class="form-group row">
              <label class="col-sm-2 col-form-label" for="functional-flow-endDate">End Date</label>
              <b-input-group class="col-sm-10">
                <b-input-group-prepend>
                  <b-form-datepicker
                    aria-controls="functional-flow-endDate"
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
                  id="functional-flow-endDate"
                  data-cy="endDate"
                  type="text"
                  class="form-control"
                  name="endDate"
                  :class="{ valid: !v$.endDate.$invalid, invalid: v$.endDate.$invalid }"
                  v-model="v$.endDate.$model"
                />
              </b-input-group>
            </div>
            <div class="form-group row">
              <label class="col-sm-2 col-form-label" for="functional-flow-owner">Owner</label>
              <select class="form-control col-sm-10" id="functional-flow-owner" data-cy="owner" name="owner" v-model="functionalFlow.owner">
                <option v-bind:value="null"></option>
                <option
                  v-bind:value="functionalFlow.owner && ownerOption.id === functionalFlow.owner.id ? functionalFlow.owner : ownerOption"
                  v-for="ownerOption in owners"
                  :key="ownerOption.id"
                >
                  {{ ownerOption.name }}
                </option>
              </select>
            </div>
          </div>
        </b-tab>
        <b-tab title="Schema" active>
          <div class="row">
            <div class="col-10 autocomplete">
              <!-- <b-navbar toggleable="lg" type="dark" variant="info" class="col-10">
                <b-navbar-toggle target="nav-collapse"></b-navbar-toggle>
                <b-collapse id="nav-collapse" is-nav>
                  <b-navbar-nav class="ml-auto">
                    <b-nav-form>
                      <b-form-input size="sm" class="mr-sm-2" placeholder="Search"></b-form-input>
                      <b-button size="sm" class="my-2 my-sm-0" type="submit">Search</b-button>
                    </b-nav-form>
                  </b-navbar-nav>
                </b-collapse>
              </b-navbar> -->

              <textarea
                style="width: 100%; min-width: 600px"
                :rows="textareaNbLine"
                v-model="plantuml"
                @focusout="focusout"
                @focus="focus"
                @keydown.enter="chooseItem"
                @keydown.tab="chooseItem"
                @keydown.arrow-down="moveDown"
                @keydown.arrow-up="moveUp"
              ></textarea>

              <ul class="autocomplete-list" v-if="searchMatch.length > 0">
                <li
                  :key="'li' + index"
                  :class="{ active: selectedIndex === index }"
                  v-for="(result, index) in searchMatch"
                  @click="selectItem(index), chooseItem($event)"
                  v-html="highlightWord(result)"
                ></li>
              </ul>
            </div>
            <div class="col-2">
              <div class="row p-1">
                <button
                  type="submit"
                  class="btn btn-primary"
                  data-cy="submit"
                  @click="getPlantUMLImageFromString"
                  :disabled="!plantumlModified"
                >
                  Update Diagram
                </button>
              </div>
            </div>
          </div>
          <div v-html="plantUMLImage" class="table-responsive my-5"></div>
          <div v-if="previewError" class="alert alert-danger">Error during import</div>
        </b-tab>
        <b-tab title="Steps">
          <div
            class="table-responsive"
            v-if="functionalFlowImport && functionalFlowImport.flowImportLines && functionalFlowImport.flowImportLines.length > 0"
          >
            <table class="table table-striped" v-if="functionalFlowImport && functionalFlowImport.flowImportLines">
              <thead>
                <tr>
                  <th scope="row"><span>#</span></th>
                  <th scope="row"><span>Step</span></th>
                  <th scope="row"><span>Source</span></th>
                  <th scope="row"><span>Target</span></th>
                  <th scope="row"><span>Protocol</span></th>
                  <th scope="row"><span>Potential Interfaces</span></th>
                  <th scope="row"><span>Interface Name</span></th>
                  <th scope="row"><span>Group order</span></th>
                  <th scope="row"><span>Group Flow</span></th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="step in functionalFlowImport.flowImportLines" v-bind:key="step.id">
                  <td>{{ step.order + 1 }}</td>
                  <td>
                    <span>{{ step.description }}</span>
                  </td>
                  <td>
                    <span v-if="step.source">
                      {{ step.source.name }}
                    </span>
                    <span v-else class="alert alert-danger" title="Correct application name in plantuml">Not imported</span>
                  </td>
                  <td>
                    <span v-if="step.target">
                      {{ step.target.name }}
                    </span>
                    <span v-else class="alert alert-danger" title="Correct application name in plantuml">Not imported</span>
                  </td>
                  <td>
                    <span v-if="step.protocol">
                      {{ step.protocol.name }}
                    </span>
                    <span
                      v-else
                      class="alert alert-warning"
                      title="Add Protocol in plantuml adding // API or // Event at the end of concerned line"
                      >Not imported</span
                    >
                  </td>
                  <td>
                    <select
                      style="width: 100%"
                      v-model="step.selectedInterface"
                      @change="changeInterface(step)"
                      title="Choose compatible existing interface or leave blank and type new interface alias name in next field"
                      :disabled="!step.potentialInterfaces || step.potentialInterfaces.length == 0"
                    >
                      <option value="{}"></option>
                      <option v-for="inter in step.potentialInterfaces" :key="inter.id" :value="inter">
                        {{ inter.alias }}
                      </option>
                    </select>
                  </td>
                  <td>
                    <datalist id="potential-identifier">
                      <option v-for="identifier in functionalFlowImport.potentialIdentifier" :key="identifier">
                        {{ identifier }}
                      </option>
                    </datalist>
                    <input
                      list="potential-identifier"
                      type="text"
                      v-model="step.interfaceAlias"
                      :disabled="step.selectedInterface && step.selectedInterface.alias"
                      :title="
                        step.interfaceAlias === ''
                          ? 'Start typing to have proposed identifiers ok key down'
                          : 'Deselect proposed interface if you want to type new identifier'
                      "
                    />
                  </td>
                  <td>
                    <span v-if="step.groupOrder > 0">{{ step.groupOrder }}</span>
                  </td>
                  <td>
                    <div v-if="step.groupOrder > 0">
                      <span v-if="step.groupFlowAlias">{{ step.groupFlowAlias }}</span>
                      <span v-else class="alert alert-danger" title="Correct application name in plantuml">Not imported</span>
                    </div>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
          <div v-if="creation">
            <label for="landscape">Choose corresponding landscape to attache flow</label>
            <select v-model="selectedLandscape" :disabled="landscapeGivenInParameter">
              <option v-for="landscape in allLandscapes" :key="landscape.id" :value="landscape">
                {{ landscape.diagramName }}
              </option>
            </select>
          </div>
        </b-tab>
        <b-tab title="Landscapes" disabled></b-tab>
      </b-tabs>
    </div>
    <div class="col-12">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <div>
          <button type="button" id="cancel-save" data-cy="entityCreateCancelButton" class="btn btn-secondary" v-on:click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span>Cancel</span>
          </button>
          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="v$.$invalid || isSaving || plantumlModified || !aliasesValid"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span>Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./functional-flow-update.component.ts"></script>
<style scoped>
.autocomplete-list {
  position: absolute;
  z-index: 2;
  overflow: auto;
  min-width: 250px;
  max-height: 200px;
  margin: 0;
  margin-top: 5px;
  padding: 0;
  border: 1px solid #000000;
  list-style: none;
  border-radius: 4px;
  background-color: #f6f6f6;
  box-shadow: 0 5px 25px rgba(0, 0, 0, 0.05);
}
.autocomplete-list li {
  margin: 0;
  padding: 8px 15px;
  border-bottom: 1px solid #000000;
}
.autocomplete-list li:last-child {
  border-bottom: 0;
}
.autocomplete-list li:hover,
.autocomplete-list li.active {
  background-color: #e5e10d;
}
</style>
