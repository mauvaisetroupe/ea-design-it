<template>
  <div class="row justify-content-center">
    <div class="col-8">
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
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span> Edit</span>
          </button>
        </router-link>
      </div>
      <div>
        <br />
      </div>
      <br />
      <h2>PlantUML preview</h2>
      <div v-html="plantUMLImage"></div>
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
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="caption in captions" v-bind:key="caption.id">
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
              <router-link v-if="caption.protocol" :to="{ name: 'ProtocolView', params: { protocolId: caption.protocol.id } }">
                {{ caption.protocol.name }}
              </router-link>
            </td>
          </tr>
        </tbody>
      </table>
      <h2>Draw.io</h2>
      <div v-if="drawIoSVG && !isEditing">
        <div v-html="drawIoSVG" />
        [ <a v-on:click="editDiagram()">Edit diagram</a> ]
        <span v-if="drawIOToBeSaved">[ <a v-on:click="saveDiagram()">Save diagram</a> ]</span>
        <span v-if="!drawIOToBeSaved">[ <a v-on:click="deleteDiagram()">Delete diagram</a> ]</span>
      </div>
      <div v-if="!drawIoSVG">
        <div>No preview available, <a v-on:click="editDiagram()">[ Generate and edit diagram ]</a></div>
        <div>(use Arrange > Layout > Vertical Flow or Arrange > Layout > Organic to distribute the first diagram components)</div>
      </div>
      <div v-if="!isHidden">
        <iframe
          :v-if="!isHidden"
          id="myDiv"
          src="https://embed.diagrams.net/?nav=1&edit=_blank&layers=1&highlight=0000ff&embed=1&noSaveBtn=1&libraries=1&proto=json"
        />
      </div>
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
</style>

<script lang="ts" src="./landscape-view-details.component.ts"></script>
