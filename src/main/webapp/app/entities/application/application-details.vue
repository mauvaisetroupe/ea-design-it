<template>
  <div>
    <div class="row">
      <div class="col-12">
        <h2 class="jh-entity-heading" data-cy="applicationDetailsHeading"><span>Application</span> - {{ application.name }}</h2>
      </div>
    </div>

    <div class="col-12">
      <b-tabs content-class="mt-3" active-nav-item-class="bg-info" card pills v-model="tabIndex">
        <b-tab title="Information">
          <div class="row">
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
                  <router-link :to="{ name: 'OwnerView', params: { ownerId: application.owner.id } }">{{
                    application.owner.name
                  }}</router-link>
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
                  <router-link :to="{ name: 'TechnologyView', params: { technologyId: technologies.id } }">{{
                    technologies.name
                  }}</router-link>
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
          </div>
        </b-tab>
        <b-tab title="Interfaces">
          <div class="row">
            <div class="col-12">
              <div class="row m-2">
                <b-pagination
                  v-model="interfaceCurrentPage"
                  :total-rows="interfaces.length"
                  :per-page="interfacePerPage"
                  aria-controls="my-table"
                  class="m-0"
                ></b-pagination>
                <input type="text" v-model="interfaceFilter" placeholder="Text filter" class="ml-5" />
              </div>
              <div class="col-12">
                <b-table
                  striped
                  :items="interfaces"
                  :fields="['alias', 'source', 'target', 'protocol']"
                  :perPage="interfacePerPage"
                  :current-page="interfaceCurrentPage"
                  :filter="interfaceFilter"
                  :filter-included-fields="['alias', 'source', 'target', 'protocol', 'sourceComponent', 'targetComponent']"
                  class="col-12"
                >
                  <template #cell(alias)="data">
                    <router-link :to="{ name: 'FlowInterfaceView', params: { flowInterfaceId: data.item.id } }">{{
                      data.item.alias
                    }}</router-link>
                  </template>

                  <template #cell(source)="data">
                    <a v-on:click="retrieveApplication(data.item.source.id)" class="text-primary">{{ data.item.source.name }}</a>
                    <span v-if="data.item.id && data.item.sourceComponent">
                      /
                      <router-link
                        :to="{ name: 'ApplicationComponentView', params: { applicationComponentId: data.item.sourceComponent.id } }"
                        >{{ data.item.sourceComponent.name }}
                      </router-link>
                    </span>
                  </template>

                  <template #cell(target)="data">
                    <a v-on:click="retrieveApplication(data.item.target.id)" class="text-primary">{{ data.item.target.name }}</a>
                    <span v-if="data.item.id && data.item.targetComponent">
                      /
                      <router-link
                        :to="{ name: 'ApplicationComponentView', params: { applicationComponentId: data.item.targetComponent.id } }"
                        >{{ data.item.targetComponent.name }}
                      </router-link>
                    </span>
                  </template>

                  <template #cell(protocol)="data">
                    {{ data.item.protocol ? data.item.protocol.name : '' }}
                  </template>
                </b-table>
              </div>
            </div>
          </div>
        </b-tab>
        <b-tab title="Schema (interfaces)">
          <div v-html="plantUMLImage" class="table-responsive my-5" v-if="plantUMLImage"></div>
          <div v-else class="container d-flex justify-content-center align-items-center" style="height: 50vh">
            <font-awesome-icon icon="sync" :spin="true" size="6x" style="color: #cccccc"></font-awesome-icon>
          </div>
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
            >
              <font-awesome-icon icon="sync" :spin="refreshingPlantuml"></font-awesome-icon>
              <span>{{ groupComponents ? 'Ungroup Components' : 'Group components' }} </span>
            </button>
            <button
              class="btn btn-secondary"
              v-on:click="doShowLabels()"
              style="font-size: 0.7em; padding: 3px; margin: 3px"
              v-if="plantUMLImage"
            >
              <font-awesome-icon icon="sync" :spin="refreshingPlantuml"></font-awesome-icon>
              <span>{{ showLabels ? 'Hide Labels' : 'Show Labels' }} </span>
            </button>
          </div>
        </b-tab>
        <b-tab title="Components">
          <div v-html="applicationStructurePlantUMLImage" class="table-responsive my-5"></div>
          <b-table :items="application.applicationsLists" :fields="['alias', 'name']">
            <template #cell(alias)="data">
              <router-link :to="{ name: 'ApplicationComponentView', params: { applicationComponentId: data.item.id } }">{{
                data.item.alias
              }}</router-link>
            </template>
          </b-table>
        </b-tab>
        <b-tab title="Functional Flows">
          <div class="row">
            <div class="row m-2">
              <b-pagination
                v-model="flowCurrentPage"
                :total-rows="flows.length"
                :per-page="flowPerPage"
                aria-controls="my-table"
                class="m-0"
              ></b-pagination>
              <input type="text" v-model="flowFilter" placeholder="Text filter" class="ml-5" />
            </div>
            <div class="row col-12">
              <b-table
                striped
                :items="flows"
                :fields="['alias', 'description', 'landscapes']"
                :perPage="flowPerPage"
                :current-page="flowCurrentPage"
                :filter="flowFilter"
                :filter-included-fields="['alias', 'description']"
                class="col-12"
              >
                <template #cell(alias)="data">
                  <router-link :to="{ name: 'FunctionalFlowView', params: { functionalFlowId: data.item.id } }">{{
                    data.item.alias
                  }}</router-link>
                </template>

                <template #cell(landscapes)="data">
                  <span v-for="(landscape, i) in data.item.landscapes" :key="landscape.id">
                    {{ i > 0 ? ', ' : '' }}
                    <router-link :to="{ name: 'LandscapeViewView', params: { landscapeViewId: landscape.id } }">{{
                      landscape.diagramName
                    }}</router-link>
                  </span>
                </template>
              </b-table>
            </div>
          </div>
        </b-tab>
        <b-tab title="Capabilities">
          <div class="row">
            <div class="col-12" v-if="consolidatedCapabilities">
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
        </b-tab>
        <b-tab title="Data Objects">
          <div class="row">
            <div class="col-12">
              <div class="row m-2">
                <b-pagination
                  v-model="datObjectCurrentPage"
                  :total-rows="application.dataObjects?.length"
                  :per-page="dataObjectPerPage"
                  aria-controls="my-table"
                  class="m-0"
                ></b-pagination>
                <input type="text" v-model="dataObjectFilter" placeholder="Text filter" class="ml-5" />
              </div>

              <div class="col-12">
                <b-table
                  striped
                  :items="application.dataObjects"
                  :perPage="dataObjectPerPage"
                  :current-page="datObjectCurrentPage"
                  :fields="['businessObject', { key: 'name', label: 'Data Object' }, 'type']"
                  :filter="dataObjectFilter"
                  :filter-included-fields="['businessObject', 'name', 'type']"
                  class="col-12"
                >
                  <template #cell(name)="data">
                    <BusinessAndDataObjectFullpath
                      :objectWithParent="data.item"
                      routerView="DataObjectView"
                      routerParamName="dataObjectId"
                    />
                  </template>

                  <template #cell(businessObject)="data">
                    <BusinessAndDataObjectFullpath
                      :objectWithParent="data.item.businessObject"
                      routerView="BusinessObjectView"
                      routerParamName="businessObjectId"
                    />
                  </template>
                </b-table>
              </div>
            </div>
          </div>
        </b-tab>
      </b-tabs>
    </div>
    <div class="col-12">
      <div v-if="application">
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>Back</span>
        </button>
        <router-link
          v-if="application.id"
          :to="{ name: 'ApplicationEdit', params: { applicationId: application.id } }"
          custom
          v-slot="{ navigate }"
        >
          <button
            @click="navigate"
            class="btn btn-primary"
            v-if="accountService.writeOrContributor"
            :disabled="!isOwner(application) || tabIndex != 0"
          >
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./application-details.component.ts"></script>
