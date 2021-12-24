<template>
  <div class="row justify-content-center">
    <div class="col-12">
      <div v-if="landscapeView">
        <h2 class="jh-entity-heading" data-cy="landscapeViewDetailsHeading">
          <span>Landscape - {{ landscapeView.diagramName }}</span>
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
        --></dl>
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
      <h2>PlantUML preview</h2>
      <div v-html="plantUMLImage" class="table-responsive"></div>
      <br />
      <table class="table">
        <thead>
          <tr>
            <th scope="row"><span>Flow</span></th>
            <th scope="row"><span>Description</span></th>
            <th scope="row"><span>Interface</span></th>
            <th scope="row"><span>Source</span></th>
            <th scope="row"><span>Target</span></th>
            <th scope="row"><span>Protocol</span></th>
            <th scope="row"><span>DataFlow</span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="caption in captions" v-bind:key="caption.id" :class="caption.colored">
            <td>
              <router-link :to="{ name: 'FunctionalFlowView', params: { functionalFlowId: caption.flowID } }">{{
                caption.flowAlias
              }}</router-link>
            </td>
            <td>{{ caption.description }}</td>
            <td>
              <router-link :to="{ name: 'FlowInterfaceView', params: { flowInterfaceId: caption.interfaceID } }">{{
                caption.interfaceAlias
              }}</router-link>
            </td>
            <td>
              <router-link :to="{ name: 'ApplicationView', params: { applicationId: caption.source.id } }">
                {{ caption.source.name }}
              </router-link>
            </td>
            <td>
              <router-link :to="{ name: 'ApplicationView', params: { applicationId: caption.target.id } }">
                {{ caption.target.name }}
              </router-link>
            </td>
            <td>
              <router-link
                v-if="caption.protocol"
                :to="{ name: 'ProtocolView', params: { protocolId: caption.protocol.id } }"
                :title="caption.protocol.name"
              >
                {{ caption.protocol.type }}
              </router-link>
            </td>
            <td>
              <span v-for="(dataFlow, i) in caption.dataFlows" :key="dataFlow.id"
                >{{ i > 0 ? ', ' : '' }}
                <router-link
                  class="form-control-static"
                  :to="{ name: 'DataFlowView', params: { dataFlowId: dataFlow.id } }"
                  :title="dataFlow.resourceName"
                  >{{ dataFlow.id }}</router-link
                >
              </span>
            </td>
          </tr>
        </tbody>
      </table>
      <h2>Draw.io</h2>

      <div v-if="!drawIoSVG && accountService().writeAuthorities">
        <button @click="editDiagram()" class="btn btn-warning" v-if="accountService().writeAuthorities">
          <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span> Generate diagram</span>
        </button>
        <div>
          No preview available, Generate diagram and use Arrange > Layout > Vertical Flow or Arrange > Layout > Organic to distribute the
          first diagram components
        </div>
      </div>

      <div v-if="drawIoSVG && !isEditing">
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
    <b-modal ref="removeEntity" id="removeEntity">
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
</style>

<script lang="ts" src="./landscape-view-details.component.ts"></script>
