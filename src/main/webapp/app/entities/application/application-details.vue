<template>
  <div class="row justify-content-center">
    <div class="col-12">
      <div v-if="application">
        <h2 class="jh-entity-heading" data-cy="applicationDetailsHeading"><span>Application</span> - {{ application.name }}</h2>
        <dl class="row jh-entity-details">
          <dt>
            <span>Alias</span>
          </dt>
          <dd>
            <span>{{ application.alias }}</span>
          </dd>
          <dt>
            <span>Name</span>
          </dt>
          <dd>
            <span>{{ application.name }}</span>
          </dd>
          <dt>
            <span>Description</span>
          </dt>
          <dd>
            <span>{{ application.description }}</span>
          </dd>
          <dt>
            <span>Comment</span>
          </dt>
          <dd>
            <span>{{ application.comment }}</span>
          </dd>
          <dt>
            <span>Documentation URL</span>
          </dt>
          <dd>
            <span
              ><a v-bind:href="application.documentationURL">{{ application.documentationURL }}</a></span
            >
          </dd>
          <dt>
            <span>Start Date</span>
          </dt>
          <dd>
            <span>{{ application.startDate }}</span>
          </dd>
          <dt>
            <span>End Date</span>
          </dt>
          <dd>
            <span>{{ application.endDate }}</span>
          </dd>
          <dt>
            <span>Application Type</span>
          </dt>
          <dd>
            <span>{{ application.applicationType }}</span>
          </dd>
          <dt>
            <span>Software Type</span>
          </dt>
          <dd>
            <span>{{ application.softwareType }}</span>
          </dd>
          <dt>
            <span>Nickname</span>
          </dt>
          <dd>
            <span>{{ application.nickname }}</span>
          </dd>
          <dt>
            <span>Owner</span>
          </dt>
          <dd>
            <div v-if="application.owner">
              <router-link :to="{ name: 'OwnerView', params: { ownerId: application.owner.id } }">{{ application.owner.name }}</router-link>
            </div>
          </dd>
          <dt>
            <span>It Owner</span>
          </dt>
          <dd>
            <div v-if="application.itOwner">
              <router-link :to="{ name: 'OwnerView', params: { ownerId: application.itOwner.id } }">{{
                application.itOwner.name
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span>Business Owner</span>
          </dt>
          <dd>
            <div v-if="application.businessOwner">
              <router-link :to="{ name: 'OwnerView', params: { ownerId: application.businessOwner.id } }">{{
                application.businessOwner.name
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span>Categories</span>
          </dt>
          <dd>
            <span v-for="(categories, i) in application.categories" :key="categories.id"
              >{{ i > 0 ? ', ' : '' }}
              <router-link :to="{ name: 'ApplicationCategoryView', params: { applicationCategoryId: categories.id } }">{{
                categories.name
              }}</router-link>
            </span>
          </dd>
          <dt>
            <span>Technologies</span>
          </dt>
          <dd>
            <span v-for="(technologies, i) in application.technologies" :key="technologies.id"
              >{{ i > 0 ? ', ' : '' }}
              <router-link :to="{ name: 'TechnologyView', params: { technologyId: technologies.id } }">{{ technologies.name }}</router-link>
            </span>
          </dd>
          <dt>
            <span>External IDS</span>
          </dt>
          <dd>
            <span v-for="(externalIDS, i) in application.externalIDS" :key="externalIDS.id"
              >{{ i > 0 ? ', ' : '' }}
              <router-link :to="{ name: 'ExternalReferenceView', params: { externalReferenceId: externalIDS.id } }">{{
                externalIDS.externalID
              }}</router-link>
            </span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span> Back</span>
        </button>
        <router-link
          v-if="application.id"
          :to="{ name: 'ApplicationEdit', params: { applicationId: application.id } }"
          custom
          v-slot="{ navigate }"
        >
          <button @click="navigate" class="btn btn-primary" v-if="accountService().writeOrContributor" :disabled="!isOwner(application)">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span> Edit</span>
          </button>
        </router-link>
      </div>
      <br />
      <h2>Interfaces for {{ application.name }}</h2>
      <div v-html="plantUMLImage" class="table-responsive my-5"></div>
      <div class="col-12">
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
      <div>
        <h2>Interfaces for {{ application.name }}</h2>
        <table class="table">
          <thead>
            <tr>
              <th scope="row"><span>Interface</span></th>
              <th scope="row"><span>Source</span></th>
              <th scope="row"><span>Target</span></th>
              <th scope="row"><span>Protocol</span></th>
              <th scope="row"></th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="caption in interfaces" v-bind:key="caption.id">
              <td>
                <router-link :to="{ name: 'FlowInterfaceView', params: { flowInterfaceId: caption.id } }">{{ caption.alias }}</router-link>
              </td>
              <td>
                <a v-on:click="retrieveApplication(caption.source.id)">{{ caption.source.name }}</a>
                <span v-if="caption.id && caption.sourceComponent">
                  /
                  <router-link :to="{ name: 'ApplicationComponentView', params: { applicationComponentId: caption.sourceComponent.id } }">{{
                    caption.sourceComponent.name
                  }}</router-link>
                </span>
              </td>
              <td>
                <a v-on:click="retrieveApplication(caption.target.id)">{{ caption.target.name }}</a>
                <span v-if="caption.id && caption.targetComponent">
                  /
                  <router-link :to="{ name: 'ApplicationComponentView', params: { applicationComponentId: caption.targetComponent.id } }">{{
                    caption.targetComponent.name
                  }}</router-link>
                </span>
              </td>
              <td>
                <router-link v-if="caption.protocol" :to="{ name: 'ProtocolView', params: { protocolId: caption.protocol.id } }">
                  {{ caption.protocol.name }}
                </router-link>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
      <div>
        <h2>Functional Flows for {{ application.name }}</h2>
        <table class="table table-striped" aria-describedby="functionalFlows">
          <thead>
            <tr>
              <th scope="row"><span>Functional Flow ID</span></th>
              <th scope="row"><span>Alias</span></th>
              <th scope="row"><span>Description</span></th>
              <th scope="row"><span>Landscape(s)</span></th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="functionalFlow in flows" :key="functionalFlow.id">
              <td>
                <router-link :to="{ name: 'FunctionalFlowView', params: { functionalFlowId: functionalFlow.id } }">{{
                  functionalFlow.id
                }}</router-link>
              </td>
              <td>{{ functionalFlow.alias }}</td>
              <td>{{ functionalFlow.description }}</td>
              <td>
                <span v-for="(landscape, i) in functionalFlow.landscapes" :key="landscape.id">
                  {{ i > 0 ? ', ' : '' }}
                  <router-link :to="{ name: 'LandscapeViewView', params: { landscapeViewId: landscape.id } }">{{
                    landscape.diagramName
                  }}</router-link>
                </span>
              </td>
              <td></td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
    <div class="col-12" v-if="consolidatedCapabilities.length > 0">
      <h2>Capabilities for {{ application.name }}</h2>
      <CapabilityComponent
        :capability="lco"
        @retrieveCapability="routeToCapability"
        :showSliders="true"
        :showPath="false"
        :defaultNbLevel="1"
        :defaultShowApplications="false"
      ></CapabilityComponent>
    </div>
  </div>
</template>

<style scoped>
.common {
  font-weight: normal;
  border: solid;
  border-color: rgb(110, 110, 110);
  border-width: 1px;
  padding: 5px;
  margin: 5px;
  box-shadow: 1px 1px 2px #aaa;
}

.alpha {
  background-color: white;
}

.beta {
  background-color: #d0ecf0c4;
  max-width: 300px;
}

.capatext {
  font-weight: normal;
  color: rgb(59, 59, 59);
}
</style>

<script lang="ts" src="./application-details.component.ts"></script>
