<template>
  <div class="row justify-content-center">
    <div class="col-12">
      <div v-if="landscapeView">
        <h2 class="jh-entity-heading" data-cy="landscapeViewDetailsHeading">
          <span><font-awesome-icon icon="map" style="color: Tomato; font-size: 0.7em"></font-awesome-icon></span>
          <span class="text-primary font-weight-bold">LANDSCAPE</span><span> - {{ landscapeView.diagramName }}</span>
        </h2>
      </div>
    </div>
    <div class="col-12">
      <b-tabs content-class="mt-3" active-nav-item-class="bg-info" card pills v-model="tabIndex">
        <b-tab title="Information" id="tab-info">
          <div class="row">
            <dl class="row jh-entity-details">
              <dt>
                <span>Viewpoint</span>
              </dt>
              <dd>
                <span>{{ landscapeView.viewpoint }}</span>
              </dd>
              <dt>
                <span>Diagram Name</span>
              </dt>
              <dd>
                <span>{{ landscapeView.diagramName }}</span>
              </dd>
              <dt>
                <span>Owner</span>
              </dt>
              <dd>
                <div v-if="landscapeView.owner">
                  <router-link :to="{ name: 'OwnerView', params: { ownerId: landscapeView.owner.id } }">{{
                    landscapeView.owner.name
                  }}</router-link>
                </div>
              </dd>
            </dl>
          </div>
        </b-tab>
        <b-tab title="Schema" id="tab-schema">
          <div class="row">
            <div class="table-responsive my-5">
              <div v-html="plantUMLImage" class="table-responsive"></div>
              <div class="col-12">
                <button
                  class="btn btn-warning"
                  v-on:click="exportPlantUML()"
                  style="font-size: 0.7em; padding: 3px; margin: 3px"
                  v-if="plantUMLImage"
                >
                  <span>Export plantuml</span>
                </button>
                <button
                  class="btn btn-secondary"
                  v-on:click="changeLayout()"
                  style="font-size: 0.7em; padding: 3px; margin: 3px"
                  v-if="plantUMLImage"
                  :disabled="refreshingPlantuml"
                >
                  <font-awesome-icon icon="sync" :spin="refreshingPlantuml"></font-awesome-icon>
                  <span>Change Layout ({{ layout }})</span>
                </button>
                <button
                  class="btn btn-secondary"
                  v-on:click="doGroupComponents()"
                  style="font-size: 0.7em; padding: 3px; margin: 3px"
                  v-if="plantUMLImage"
                  :disabled="refreshingPlantuml"
                >
                  <font-awesome-icon icon="sync" :spin="refreshingPlantuml"></font-awesome-icon>
                  <span>{{ groupComponents ? 'Ungroup Components' : 'Group components' }} </span>
                </button>
                <button
                  class="btn btn-secondary"
                  v-on:click="doShowLabels()"
                  style="font-size: 0.7em; padding: 3px; margin: 3px"
                  v-if="plantUMLImage"
                  :disabled="refreshingPlantuml"
                >
                  <font-awesome-icon icon="sync" :spin="refreshingPlantuml"></font-awesome-icon>
                  <span>{{ showLabels ? 'Hide Labels' : 'Show Labels' }} </span>
                </button>
                <br /><br />
              </div>
            </div>
          </div>
        </b-tab>
        <b-tab title="Functional Flows" id="tab-flows">
          <div class="row">
            <table class="table table-striped">
              <thead>
                <tr>
                  <th scope="row"><span>Flow</span></th>
                  <th scope="row"><span>Description</span></th>
                  <th scope="row"><span>Applications</span></th>
                  <th scope="row"></th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="(functionalFlow, index) in landscapeView.flows" :key="functionalFlow.id" data-cy="landscape-functional-flows">
                  <td>
                    <div>
                      <router-link
                        :to="{ name: 'FunctionalFlowView', params: { functionalFlowId: functionalFlow.id } }"
                        v-if="functionalFlow"
                      >
                        {{ functionalFlow.alias }}
                      </router-link>
                    </div>
                  </td>
                  <td>
                    <span>
                      <span>{{ functionalFlow.description }}</span>
                    </span>
                  </td>
                  <td>
                    <span v-for="(app, i) in functionalFlow.allApplications" :key="app.id">
                      {{ i > 0 ? ', ' : '' }}
                      <router-link :to="{ name: 'ApplicationView', params: { applicationId: app.id } }">{{ app.name }}</router-link>
                    </span>
                  </td>
                  <td class="text-right">
                    <div class="btn-group">
                      <router-link
                        :to="{ name: 'FunctionalFlowView', params: { functionalFlowId: functionalFlow.id } }"
                        custom
                        v-slot="{ navigate }"
                      >
                        <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                          <font-awesome-icon icon="eye"></font-awesome-icon>
                          <span class="d-none d-md-inline">View</span>
                        </button>
                      </router-link>
                      <router-link
                        v-if="accountService().writeAuthorities"
                        :to="{ name: 'FunctionalFlowEdit', params: { functionalFlowId: functionalFlow.id } }"
                        custom
                        v-slot="{ navigate }"
                      >
                        <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                          <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                          <span class="d-none d-md-inline">Edit</span>
                        </button>
                      </router-link>
                      <b-button
                        v-if="accountService().writeAuthorities"
                        variant="warning"
                        class="btn btn-sm"
                        @click="prepareToDetach(index)"
                      >
                        <font-awesome-icon icon="times"></font-awesome-icon>
                        <span class="d-none d-md-inline">Detach</span>
                      </b-button>
                    </div>
                  </td>
                </tr>
              </tbody>
            </table>
            <div class="col-12">
              <span class="float-right">
                <button
                  class="btn btn-primary jh-create-entity create-functional-flow"
                  v-if="accountService().writeAuthorities"
                  title="Add existing Functional flow, from other landscape, with same description, same interfaces"
                  @click="openSearchFlow()"
                >
                  <font-awesome-icon icon="plus"></font-awesome-icon>
                  <span>Add exisintg Functional Flow</span>
                </button>

                <router-link
                  :to="{ name: 'FunctionalFlowCreate', query: { landscapeViewId: landscapeView.id } }"
                  custom
                  v-slot="{ navigate }"
                  v-if="accountService().writeAuthorities"
                >
                  <button
                    @click="navigate"
                    id="jh-create-entity"
                    data-cy="entityCreateButton"
                    class="btn btn-primary jh-create-entity create-functional-flow"
                    title="Create a new Functional Flow, based on existing Interfaces or creating new Interfaces"
                  >
                    <font-awesome-icon icon="plus"></font-awesome-icon>
                    <span> Create a new Functional Flow </span>
                  </button>
                </router-link>
              </span>
            </div>
          </div>
        </b-tab>
        <b-tab title="Capabilities" id="tab-capa">
          <div class="col-12" v-if="consolidatedCapability && consolidatedCapability.length > 0">
            <h2>Capabilities for {{ landscapeView.diagramName }}</h2>
            <CapabilityComponent
              :capability="consolidatedCapability[0]"
              :showSliders="true"
              :showPath="false"
              :nbLevel="4"
              :defaultShowApplications="true"
              :defaultNbLevel="1"
            ></CapabilityComponent>
          </div>
        </b-tab>
        <b-tab title="DrawIO" id="tab-drawio">
          <div class="row">
            <div v-if="!drawIoSVG">
              <button @click="editDiagram()" class="btn btn-warning" v-if="accountService().writeAuthorities">
                <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span> Generate diagram</span>
              </button>
              <div>No preview available</div>
              <div v-if="accountService().writeAuthorities">
                Generate diagram and use Arrange > Layout > Vertical Flow or Arrange > Layout > Organic to distribute the first diagram
                components
              </div>
            </div>
            <div v-if="drawIoSVG && !isEditing" class="table-responsive m-5">
              <div v-html="drawIoSVG" />
            </div>

            <div class="col-12">
              <span v-if="!isEditing" class="float-right">
                <button @click="editDiagram()" class="btn btn-warning" v-if="accountService().writeAuthorities && drawIoSVG">
                  <font-awesome-icon icon="pencil-alt"></font-awesome-icon><span> Edit diagram</span>
                </button>
                <button
                  @click="saveDiagram()"
                  class="btn btn-primary"
                  v-if="accountService().writeAuthorities && drawIoSVG && drawIOToBeSaved"
                >
                  <font-awesome-icon icon="pencil-alt"></font-awesome-icon><span> Save diagram</span>
                </button>
                <button
                  @click="prepareRemove()"
                  class="btn btn-danger"
                  v-if="accountService().writeAuthorities && drawIoSVG && !drawIOToBeSaved"
                >
                  <font-awesome-icon icon="times"></font-awesome-icon><span> Delete Diagram</span>
                </button>
                <button @click="exportDrawIOXML()" class="btn btn-info" v-if="drawIoSVG && !isEditing">
                  <font-awesome-icon icon="eye"></font-awesome-icon><span> Export diagram</span>
                </button>
              </span>
            </div>
            <div v-if="!isHidden">
              <iframe
                :v-if="!isHidden"
                id="myDiv"
                src="https://embed.diagrams.net/?nav=1&edit=_blank&layers=1&highlight=0000ff&embed=1&noSaveBtn=1&libraries=1&proto=json"
                style="z-index: 999999"
              />
            </div>
          </div>
        </b-tab>
      </b-tabs>
    </div>
    <div class="col-12">
      <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
        <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span> Back</span>
      </button>
      <router-link
        v-if="landscapeView.id && tabIndex == 0"
        :to="{ name: 'LandscapeViewEdit', params: { landscapeViewId: landscapeView.id } }"
        custom
        v-slot="{ navigate }"
      >
        <button @click="navigate" class="btn btn-primary" v-if="accountService().writeAuthorities">
          <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span> Edit</span>
        </button>
      </router-link>
    </div>
    <b-modal ref="removeDiagramEntity" id="removeDiagramEntity">
      <span slot="modal-title"
        ><span id="eaDesignItApp.landscapeView.delete.question" data-cy="landscapeViewDeleteDialogHeading"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-landscapeView-heading">Are you sure you want to delete this Landscape View?</p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-landscapeView"
          data-cy="entityConfirmDeleteButton"
          v-on:click="deleteDiagram()"
        >
          Delete
        </button>
      </div>
    </b-modal>

    <b-modal ref="detachFlowEntity" id="detachFlowEntity">
      <span slot="modal-title"
        ><span id="eaDesignItApp.landscapeView.delete.question" data-cy="landscapeViewDeleteDialogHeading"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-landscapeView-heading">Are you sure you want to detach this Functional Flow?</p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-on:click="closeDetachDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-landscapeView"
          data-cy="entityConfirmDeleteButton"
          v-on:click="detachFunctionalFlow()"
        >
          Delete
        </button>
      </div>
    </b-modal>

    <b-modal ref="addExistingEntity" id="addExistingEntity">
      <span slot="modal-title">Search for exising Functional Flow</span>
      <div class="modal-body">
        <p id="jhi-delete-landscapeView-heading">Search for exising Functional Flow</p>
        <div class="table-responsive" v-if="functionalFlows && functionalFlows.length > 0">
          <div>
            <input type="text" placeholder="Filter by text" v-model="filter" />
          </div>
          <table class="table table-striped" aria-describedby="functionalFlows">
            <thead>
              <tr>
                <th scope="row"><span>Alias</span></th>
                <th scope="row"><span>Description</span></th>
                <th></th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="functionalFlow in filteredRows" :key="functionalFlow.id" data-cy="entityTable">
                <td>
                  {{ functionalFlow.alias }}
                </td>
                <td>{{ functionalFlow.description }}</td>
                <td class="text-right">
                  <div class="btn-group">
                    <button @click="addNew(functionalFlow)" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                      <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                      <span class="d-none d-md-inline">Add</span>
                    </button>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-on:click="closeSearchFlow()">Cancel</button>
        <button type="button" class="btn btn-primary" id="jhi-confirm-delete-landscapeView" data-cy="entityConfirmDeleteButton">Add</button>
      </div>
    </b-modal>
  </div>
</template>
<style>
iframe {
  border: 5;
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  width: 100%;
  height: 100%;
}

.mycolor {
  background-color: rgba(0, 0, 0, 0.1);
}

.modal-dialog {
  max-width: 80%;
}
</style>

<script lang="ts" src="./landscape-view-details.component.ts"></script>
