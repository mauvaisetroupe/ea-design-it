<template>
  <div>
    <h2 id="page-heading" data-cy="FlowInterfaceHeading">
      <span id="flow-interface-heading"
        ><font-awesome-icon icon="grip-lines" style="color: Tomato; font-size: 0.9em"></font-awesome-icon> Flow Interfaces</span
      >
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refresh list</span>
        </button>
        <router-link :to="{ name: 'FlowInterfaceCreate' }" custom v-slot="{ navigate }" v-if="accountService.writeAuthorities">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-flow-interface"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>Create a new Flow Interface</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && flowInterfaces && flowInterfaces.length === 0">
      <span>No Flow Interfaces found</span>
    </div>

    <div class="row">
      <div class="col-12">
        <div class="col-12">
          <b-table
            striped
            borderless
            :items="filteredRows"
            :fields="['id', 'alias', 'source', 'target', 'protocol', { key: 'actions', label: '', tdClass: 'text-right' }]"
            :perPage="perPage"
            :current-page="currentPage"
            :filter-included-fields="['alias', 'source', 'target', 'protocol']"
            class="col-12"
          >
            <template #thead-top="data">
              <b-tr>
                <b-th></b-th>
                <b-th><input type="text" v-model="filterAlias" :placeholder="'Text filter ' + data.fields[0].label" /></b-th>
                <b-th><input type="text" v-model="filterSource" :placeholder="'Text filter ' + data.fields[1].label" /></b-th>
                <b-th><input type="text" v-model="filterTarget" :placeholder="'Text filter ' + data.fields[2].label" /></b-th>
                <b-th><input type="text" v-model="filterProtocol" :placeholder="'Text filter ' + data.fields[3].label" /></b-th>
                <b-th class="float-right">
                  <b-pagination
                    v-model="currentPage"
                    :total-rows="filteredRows.length"
                    :per-page="perPage"
                    aria-controls="my-table"
                    class="m-0"
                  ></b-pagination>
                </b-th>
              </b-tr>
            </template>

            <template #cell(id)="data">
              <router-link :to="{ name: 'FlowInterfaceView', params: { flowInterfaceId: data.item.id } }">{{ data.item.id }}</router-link>
            </template>

            <template #cell(alias)="data">
              <router-link :to="{ name: 'FlowInterfaceView', params: { flowInterfaceId: data.item.id } }">{{
                data.item.alias
              }}</router-link>
            </template>

            <template #cell(source)="data">
              <router-link :to="{ name: 'ApplicationView', params: { applicationId: data.item.source.id } }"
                >{{ data.item.source.name }}
              </router-link>

              <span v-if="data.item.id && data.item.sourceComponent">
                /
                <router-link :to="{ name: 'ApplicationComponentView', params: { applicationComponentId: data.item.sourceComponent.id } }"
                  >{{ data.item.sourceComponent.name }}
                </router-link>
              </span>
            </template>

            <template #cell(target)="data">
              <router-link :to="{ name: 'ApplicationView', params: { applicationId: data.item.target.id } }"
                >{{ data.item.target.name }}
              </router-link>

              <span v-if="data.item.id && data.item.targetComponent">
                /
                <router-link :to="{ name: 'ApplicationComponentView', params: { applicationComponentId: data.item.targetComponent.id } }"
                  >{{ data.item.targetComponent.name }}
                </router-link>
              </span>
            </template>

            <template #cell(protocol)="data">
              <router-link :to="{ name: 'ProtocolView', params: { protocolId: data.item.protocol.id } }" v-if="data.item.protocol">{{
                data.item.protocol.name
              }}</router-link>
            </template>

            <template #cell(actions)="data">
              <div class="btn-group">
                <router-link :to="{ name: 'FlowInterfaceView', params: { flowInterfaceId: data.item.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'FlowInterfaceEdit', params: { flowInterfaceId: data.item.id } }" custom v-slot="{ navigate }">
                  <button
                    @click="navigate"
                    class="btn btn-primary btn-sm edit"
                    data-cy="entityEditButton"
                    v-if="accountService().writeOrContributor"
                    :disabled="!isOwner(data.item)"
                  >
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-if="accountService().deleteAuthorities"
                  v-on:click="prepareRemove(data.item)"
                  variant="danger"
                  class="btn btn-sm"
                  data-cy="entityDeleteButton"
                  v-b-modal.removeEntity
                  :disabled="data.item.steps && data.item.steps.length > 0"
                  :title="
                    !data.item.steps || data.item.steps.length == 0
                      ? ''
                      : 'Cannot be deleted, please detache from all functional flows first'
                  "
                >
                  <font-awesome-icon icon="times"></font-awesome-icon>
                  <span class="d-none d-md-inline">Delete</span>
                </b-button>
              </div>
            </template>
          </b-table>
        </div>
      </div>
    </div>
    <b-modal ref="removeEntity" id="removeEntity">
      <template #modal-title>
        <span id="eaDesignItApp.flowInterface.delete.question" data-cy="flowInterfaceDeleteDialogHeading">Confirm delete operation</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-flowInterface-heading">Are you sure you want to delete Flow Interface {{ removeId }}?</p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-on:click="closeDialog()">Cancel</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-flowInterface"
            data-cy="entityConfirmDeleteButton"
            v-on:click="removeFlowInterface()"
          >
            Delete
          </button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./flow-interface.component.ts"></script>
