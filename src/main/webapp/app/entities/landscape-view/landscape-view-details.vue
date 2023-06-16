<template>
  <div class="row justify-content-center">
    <div class="col-12">
      <div v-if="landscapeView">
        <h2 class="jh-entity-heading" data-cy="landscapeViewDetailsHeading">
          <span
            ><font-awesome-icon icon="map" style="color: Tomato; font-size: 0.7em"></font-awesome-icon> Landscape -
            {{ landscapeView.diagramName }}</span
          >
        </h2>
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
          <!--
          <dt>
            <span>Flows</span>
          </dt>
          <dd>
            <span v-for="(flows, i) in landscapeView.flows" :key="flows.id"
              >{{ i > 0 ? ', ' : '' }}
              <router-link :title="flows.description" :to="{ name: 'FunctionalFlowView', params: { functionalFlowId: flows.id } }">{{
                flows.alias
              }}</router-link>
            </span>
          </dd>
          -->
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span> Back</span>
        </button>
        <router-link
          v-if="landscapeView.id"
          :to="{ name: 'LandscapeViewEdit', params: { landscapeViewId: landscapeView.id } }"
          custom
          v-slot="{ navigate }"
        >
          <button @click="navigate" class="btn btn-primary" v-if="accountService().writeAuthorities">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span> Edit</span>
          </button>
        </router-link>
      </div>
      <div>
        <br />
      </div>
      <br />
      <h3>Landscape diagram</h3>
      <div>
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
          <br /><br />
        </div>
      </div>
      <h3>
        <font-awesome-icon icon="project-diagram" style="color: Tomato; font-size: 0.7em"></font-awesome-icon> Functional Flows
        <button
          class="btn"
          v-on:click="exportExcel()"
          style="font-size: 0.7em; padding: 3px; margin: 3px"
          title="Export landscape as Excel file"
        >
          <font-awesome-icon icon="file-excel" style="color: green"></font-awesome-icon>
        </button>
      </h3>
      <br />
      <table class="table">
        <thead>
          <tr>
            <th scope="row"><span>Flow</span></th>
            <th scope="row" v-if="reorderAlias"></th>
            <th scope="row" v-if="reorderAlias"></th>
            <th scope="row"><span>Description</span></th>
            <th scope="row"><span>Step</span></th>
            <th scope="row"><span>Interface</span></th>
            <th scope="row"><span>Source</span></th>
            <th scope="row"><span>Target</span></th>
            <th scope="row"><span>Protocol</span></th>
            <th scope="row"><span>DataFlow</span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <template v-for="(functionalFlow, i) in landscapeView.flows">
            <template
              v-for="(step, j) in functionalFlow.steps != null && functionalFlow.steps.length > 0 ? functionalFlow.steps : emptySteps"
            >
              <tr v-bind:key="step.id" :class="i % 2 == 0 ? 'mycolor' : ''">
                <td>
                  <div v-if="!reorderAlias || !step.flowInterface.id">
                    <router-link
                      :to="{ name: 'FunctionalFlowView', params: { functionalFlowId: functionalFlow.id } }"
                      v-if="functionalFlow && j == 0"
                    >
                      {{ functionalFlow.alias }}
                    </router-link>
                  </div>
                  <div v-if="reorderAlias && step.flowInterface.id">
                    <select @change="reorder(step, functionalFlow, $event)" :id="step.id" class="btn-success">
                      <option v-for="flow in landscapeView.flows" :key="flow.id" :value="flow.id" :selected="flow.id === functionalFlow.id">
                        {{ flow.alias ? flow.alias : flow.id }}
                      </option>
                    </select>
                  </div>
                </td>
                <td v-if="reorderAlias">
                  <font-awesome-icon
                    icon="chevron-up"
                    class="btn-success"
                    v-if="j != 0"
                    @click="swap(functionalFlow, j, j - 1)"
                  ></font-awesome-icon>
                </td>
                <td v-if="reorderAlias">
                  <font-awesome-icon
                    icon="chevron-down"
                    class="btn-success"
                    v-if="j != functionalFlow.steps.length - 1 && functionalFlow.steps.length > 1"
                    @click="swap(functionalFlow, j, j + 1)"
                  ></font-awesome-icon>
                </td>
                <td>
                  <span v-if="j == 0">
                    <span v-if="!reorderAlias">{{ functionalFlow.description }}</span>
                    <span v-else>
                      <textarea
                        style="width: 100%; min-width: 600px"
                        rows="1"
                        v-model="functionalFlow.description"
                        @change="changeDescription(functionalFlow)"
                      ></textarea>
                    </span>
                  </span>
                </td>
                <td>
                  <span v-if="step.description != 'EMPTYSTEP'">
                    {{ step.stepOrder }}.
                    <span v-if="!reorderAlias">{{ step.description }}</span>
                    <span v-else>
                      <textarea
                        style="width: 100%; min-width: 600px"
                        rows="1"
                        v-model="step.description"
                        @change="changeStepDescription(functionalFlow, step)"
                      ></textarea>
                    </span>
                  </span>
                </td>
                <td>
                  <router-link
                    v-if="step.flowInterface.id"
                    :to="{ name: 'FlowInterfaceView', params: { flowInterfaceId: step.flowInterface.id } }"
                    >{{ step.flowInterface.alias }}</router-link
                  >
                </td>
                <td>
                  <router-link
                    v-if="step.flowInterface.id"
                    :to="{ name: 'ApplicationView', params: { applicationId: step.flowInterface.source.id } }"
                  >
                    {{ step.flowInterface.source.name }}
                  </router-link>
                  <span v-if="step.flowInterface.id && step.flowInterface.sourceComponent">
                    /
                    <router-link
                      :to="{ name: 'ApplicationComponentView', params: { applicationComponentId: step.flowInterface.sourceComponent.id } }"
                      >{{ step.flowInterface.sourceComponent.name }}</router-link
                    >
                  </span>
                </td>
                <td>
                  <router-link
                    v-if="step.flowInterface.id"
                    :to="{ name: 'ApplicationView', params: { applicationId: step.flowInterface.target.id } }"
                  >
                    {{ step.flowInterface.target.name }}
                  </router-link>
                  <span v-if="step.flowInterface.id && step.flowInterface.targetComponent">
                    /
                    <router-link
                      :to="{ name: 'ApplicationComponentView', params: { applicationComponentId: step.flowInterface.targetComponent.id } }"
                      >{{ step.flowInterface.targetComponent.name }}</router-link
                    >
                  </span>
                </td>

                <td>
                  <router-link
                    v-if="step.flowInterface.protocol"
                    :to="{ name: 'ProtocolView', params: { protocolId: step.flowInterface.protocol.id } }"
                    :title="step.flowInterface.protocol.name"
                  >
                    {{ step.flowInterface.protocol.type }}
                  </router-link>
                </td>
                <td>
                  <span v-if="step.flowInterface.id">
                    <span v-for="(dataFlow, i) in step.flowInterface.dataFlows" :key="dataFlow.id"
                      >{{ i > 0 ? ', ' : '' }}
                      <router-link
                        class="form-control-static"
                        :to="{ name: 'DataFlowView', params: { dataFlowId: dataFlow.id } }"
                        :title="dataFlow.resourceName"
                        >{{ dataFlow.id }}</router-link
                      ><sup v-if="dataFlow.items && dataFlow.items.length > 0">({{ dataFlow.items.length }})</sup>
                    </span>
                  </span>
                </td>
                <td class="text-right">
                  <div class="btn-group" v-if="j == 0 && !reorderAlias">
                    <router-link
                      :to="{ name: 'FunctionalFlowView', params: { functionalFlowId: functionalFlow.id } }"
                      custom
                      v-slot="{ navigate }"
                    >
                      <button
                        @click="navigate"
                        class="btn btn-primary btn-sm edit"
                        data-cy="entityEditButton"
                        v-if="accountService().writeAuthorities"
                      >
                        <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                        <span class="d-none d-md-inline">Edit</span>
                      </button>

                      <button
                        @click="navigate"
                        class="btn btn-info btn-sm details"
                        data-cy="entityDetailsButton"
                        v-if="!accountService().writeAuthorities"
                      >
                        <font-awesome-icon icon="eye"></font-awesome-icon>
                        <span class="d-none d-md-inline">View</span>
                      </button>
                    </router-link>

                    <b-button v-if="accountService().writeAuthorities" variant="warning" class="btn btn-sm" @click="prepareToDetach(i)">
                      <font-awesome-icon icon="times"></font-awesome-icon>
                      <span class="d-none d-md-inline">Detach</span>
                    </b-button>
                  </div>
                </td>
              </tr>
            </template>
          </template>
        </tbody>
      </table>
      <div class="row">
        <div class="col-md-6">
          <button
            @click="startReorder()"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-success jh-create-entity create-functional-flow"
            title="Edit Flow Alias in order to move interfaces from on flow to another"
            v-if="accountService().writeAuthorities && !reorderAlias"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span> Organize Flows</span>
          </button>

          <button
            @click="saveReorder()"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-success jh-create-entity create-functional-flow"
            title="Edit Flow Alias in order to move interfaces from on flow to another"
            v-if="reorderAlias && (reorderAliasflowToSave.length > 0 || reorderStepToSave.length > 0)"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>Save</span>
          </button>

          <button
            @click="cancelReorder()"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-success jh-create-entity create-functional-flow"
            title="Edit Flow Alias in order to move interfaces from on flow to another"
            v-if="reorderAlias"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>Cancel</span>
          </button>
        </div>
        <div class="col-md-6 d-flex justify-content-end" v-if="!reorderAlias">
          <span>
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

      <br /><br /><br /><br /><br /><br />
      <h2>Draw.io</h2>

      <div v-if="!drawIoSVG">
        <button @click="editDiagram()" class="btn btn-warning" v-if="accountService().writeAuthorities">
          <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span> Generate diagram</span>
        </button>
        <div>No preview available</div>
        <div v-if="accountService().writeAuthorities">
          Generate diagram and use Arrange > Layout > Vertical Flow or Arrange > Layout > Organic to distribute the first diagram components
        </div>
      </div>

      <div v-if="drawIoSVG && !isEditing" class="m-5">
        <div v-html="drawIoSVG" />
      </div>

      <div class="btn-group">
        <span v-if="!isEditing">
          <button @click="editDiagram()" class="btn btn-warning" v-if="accountService().writeAuthorities && drawIoSVG">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon><span> Edit diagram</span>
          </button>
          <button @click="saveDiagram()" class="btn btn-primary" v-if="accountService().writeAuthorities && drawIoSVG && drawIOToBeSaved">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon><span> Save diagram</span>
          </button>
          <button @click="prepareRemove()" class="btn btn-danger" v-if="accountService().writeAuthorities && drawIoSVG && !drawIOToBeSaved">
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
        />
      </div>
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
          <table class="table table-striped" aria-describedby="functionalFlows">
            <thead>
              <tr>
                <th scope="row"><span>Alias</span></th>
                <th scope="row"><span>Description</span></th>
                <th scope="row"><span>Documentation URL</span></th>
                <th scope="row"><span>Documentation URL 2</span></th>
                <th scope="row"><span>Interfaces</span></th>
                <th></th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="functionalFlow in functionalFlows" :key="functionalFlow.id" data-cy="entityTable">
                <td>
                  <router-link :to="{ name: 'FunctionalFlowView', params: { functionalFlowId: functionalFlow.id } }" target="_blank">{{
                    functionalFlow.alias
                  }}</router-link>
                </td>
                <td>{{ functionalFlow.description }}</td>
                <td>
                  <a :href="functionalFlow.documentationURL" target="_blank">{{
                    functionalFlow.documentationURL ? functionalFlow.documentationURL.substring(0, 20) : ''
                  }}</a>
                </td>
                <td>
                  <a :href="functionalFlow.documentationURL2" target="_blank">{{
                    functionalFlow.documentationURL2 ? functionalFlow.documentationURL2.substring(0, 20) : ''
                  }}</a>
                </td>
                <td>
                  <span
                    v-for="(step, i) in functionalFlow.steps"
                    :set="(interfaces = step.flowInterface)"
                    :key="step.id"
                    :title="
                      '[ ' +
                      interfaces.source.name +
                      ' / ' +
                      interfaces.target.name +
                      ' ]' +
                      (interfaces.protocol ? ' (' + interfaces.protocol.type + ') ' : '')
                    "
                    >{{ i > 0 ? ', ' : '' }}
                    {{ interfaces.alias }}
                  </span>
                </td>
                <td>{{ functionalFlow.startDate }}</td>
                <td>{{ functionalFlow.endDate }}</td>
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
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-landscapeView"
          data-cy="entityConfirmDeleteButton"
          v-on:click="addNew()"
        >
          Add
        </button>
      </div>
    </b-modal>

    <div class="col-12" v-if="consolidatedCapability && consolidatedCapability.length > 0">
      <h2>Capabilities for {{ landscapeView.diagramName }}</h2>
      <CapabilityComponent
        :capability="consolidatedCapability[0]"
        :showSliders="true"
        :showPath="false"
        :nbLevel="4"
        :defaultShowApplications="false"
      ></CapabilityComponent>
    </div>
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
