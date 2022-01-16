<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="capability">
        <h2 class="jh-entity-heading" data-cy="capabilityDetailsHeading"><span>Capability</span> {{ capability.name }}</h2>
        <dl class="row jh-entity-details">
          <dt>
            <span>Id</span>
          </dt>
          <dd>
            <span>{{ capability.id }}</span>
          </dd>
          <dt>
            <span>Description</span>
          </dt>
          <dd>
            <span>{{ capability.description }}</span>
          </dd>
          <dt>
            <span>Comment</span>
          </dt>
          <dd>
            <span>{{ capability.comment }}</span>
          </dd>
          <dt>
            <span>Level</span>
          </dt>
          <dd>
            <span>{{ capability.level }}</span>
          </dd>
          <dt>
            <span>Parent</span>
          </dt>
          <dd>
            <div v-if="capability.parent">
              <a @click="retrieveCapability(capability.parent.id)">{{ capability.parent.name }}</a>
            </div>
          </dd>
          <dt>
            <span>Applications</span>
          </dt>
          <dd>
            <span v-for="(application, i) in capability.applications" :key="application.id"
              >{{ i > 0 ? ', ' : '' }}
              <router-link :to="{ name: 'ApplicationView', params: { applicationId: application.id } }">{{ application.name }}</router-link>
            </span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span> Back</span>
        </button>
        <router-link
          v-if="capability.id"
          :to="{ name: 'CapabilityEdit', params: { capabilityId: capability.id } }"
          custom
          v-slot="{ navigate }"
        >
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
    <div class="col-8">
      <div class="mt-5">
        <h2>Graphical view</h2>
        <div class="common" :class="capability.subCapabilities.length > 0 ? 'alpha' : 'beta'">
          <div :title="capability.description">{{ capability.level }}. {{ capability.name }}</div>
          <div v-if="capability.subCapabilities" class="d-flex flex-wrap">
            <div
              v-for="child1 in capability.subCapabilities"
              :key="child1.id"
              class="common"
              :class="child1.subCapabilities.length > 0 ? 'alpha' : 'beta'"
            >
              <div :title="child1.description">
                {{ child1.level }}. <a @click="retrieveCapability(child1.id)">{{ child1.name }}</a>
              </div>
              <div v-if="child1.subCapabilities" class="d-flex flex-wrap">
                <div
                  v-for="child2 in child1.subCapabilities"
                  :key="child2.id"
                  class="common"
                  :class="child2.subCapabilities.length > 0 ? 'alpha' : 'beta'"
                >
                  <div :title="child2.description">
                    {{ child2.level }}. <a @click="retrieveCapability(child2.id)">{{ child2.name }}</a>
                  </div>
                  <div v-if="child2.subCapabilities" class="d-flex flex-wrap">
                    <div
                      v-for="child3 in child2.subCapabilities"
                      :key="child3.id"
                      class="common"
                      :class="child3.subCapabilities.length > 0 ? 'alpha' : 'beta'"
                    >
                      <div>
                        {{ child3.level }}. <a @click="retrieveCapability(child3.id)">{{ child3.name }}</a>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.common {
  font-weight: bold;
  border: solid;
  border-color: black;
  border-width: 3px;
  padding: 5px;
  margin: 10px;
  box-shadow: 8px 8px 12px #aaa;
}

.alpha {
  background-color: white;
}

.beta {
  background-color: #fefece;
  max-width: 300px;
}
</style>

<script lang="ts" src="./capability-details.component.ts"></script>
