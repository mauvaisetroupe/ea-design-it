<template>
  <div class="row justify-content-center">
    <div class="col-8">
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
            <span>Owner</span>
          </dt>
          <dd>
            <div v-if="application.owner">
              <router-link :to="{ name: 'OwnerView', params: { ownerId: application.owner.id } }">{{ application.owner.name }}</router-link>
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
      <h2>PlantUML preview</h2>
      <div v-html="plantUMLImage"></div>
      <br />
      <h3>Interfaces for {{ application.name }}</h3>
      <table class="table">
        <thead>
          <tr>
            <th scope="row"><span>Flow</span></th>
            <th scope="row"><span>Interface</span></th>
            <th scope="row"><span>Source</span></th>
            <th scope="row"><span>Target</span></th>
            <th scope="row"><span>Protocol</span></th>
            <th scope="row"><span>Data Flows</span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="caption in interfaces" v-bind:key="caption.id">
            <td>
              <span v-for="(flows, i) in caption.functionalFlows" :key="flows.id"
                >{{ i > 0 ? ', ' : '' }}
                <router-link :title="flows.description" :to="{ name: 'FunctionalFlowView', params: { functionalFlowId: flows.id } }">{{
                  flows.alias
                }}</router-link>
              </span>
            </td>
            <td>
              <router-link :to="{ name: 'FlowInterfaceView', params: { flowInterfaceId: caption.id } }">{{ caption.alias }}</router-link>
            </td>
            <td>
              <a v-on:click="retrieveApplication(caption.source.id)">{{ caption.source.name }}</a>
            </td>
            <td>
              <a v-on:click="retrieveApplication(caption.target.id)">{{ caption.target.name }}</a>
            </td>
            <td>
              <router-link v-if="caption.protocol" :to="{ name: 'ProtocolView', params: { protocolId: caption.protocol.id } }">
                {{ caption.protocol.name }}
              </router-link>
            </td>
            <td>
              <span v-for="dataflow in caption.dataFlows" :key="dataflow.id">
                <router-link :to="{ name: 'DataFlowView', params: { dataFlowId: dataflow.id } }">
                  {{ dataflow.id }}
                </router-link>
              </span>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script lang="ts" src="./application-details.component.ts"></script>
