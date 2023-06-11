<template>
  <div>
    <ol class="breadcrumb" v-if="menu">
      <li class="breadcrumb-item" v-for="(mycap, i) in path" key="id">
        <a @click="$emit('retrieveCapability', mycap.id)" class="router-link-exact-active router-link-active" target="_self">
          <span v-if="i == 0">
            <font-awesome-icon icon="home" />
            <span>Corporate Capabilities</span>
          </span>
          <span v-else>
            {{ mycap.name }}
          </span>
        </a>
      </li>
      <li class="breadcrumb-item">
        <span v-if="capability.level == -2">
          <font-awesome-icon icon="home" />
          <span>Corporate Capabilities</span>
        </span>
        <span v-else>
          {{ capability.name }}
        </span>
      </li>
    </ol>
    <div v-if="menu">
      <span style="margin: 5px"><input type="checkbox" v-model="showApplications" /> Show applications</span>
      <span style="margin: 5px">
        <label for="range">Nb level</label>
        <input type="range" :min="Math.max(0, capability.level)" max="3" class="slider" v-model="nbLevel" />{{ nbLevel }}
      </span>
    </div>
    <div>
      <CapabilityComponentItem
        :capability="capability"
        childStyle="top"
        @retrieveCapability="retrieveCapability"
        :showApplications="showApplications"
        :nbLevel="nbLevel"
      >
        <div class="d-flex flex-wrap">
          <template v-for="child1 in capability.subCapabilities">
            <CapabilityComponentItem
              :capability="child1"
              childStyle="1"
              @retrieveCapability="retrieveCapability"
              :showApplications="showApplications"
              :nbLevel="nbLevel"
            >
              <div class="d-flex flex-wrap">
                <template v-for="child2 in child1.subCapabilities">
                  <CapabilityComponentItem
                    :capability="child2"
                    childStyle="2"
                    @retrieveCapability="retrieveCapability"
                    :showApplications="showApplications"
                    :nbLevel="nbLevel"
                  >
                    <div class="d-flex flex-wrap">
                      <template v-for="child3 in child2.subCapabilities">
                        <CapabilityComponentItem
                          :capability="child3"
                          childStyle="3"
                          @retrieveCapability="retrieveCapability"
                          :showApplications="showApplications"
                          :nbLevel="nbLevel"
                          :showInheritedApplications="true"
                        >
                          <div class="d-flex flex-wrap">
                            <template v-for="child4 in child3.subCapabilities">
                              <CapabilityComponentItem
                                :capability="child4"
                                childStyle="4"
                                @retrieveCapability="retrieveCapability"
                                :showApplications="showApplications"
                                :nbLevel="nbLevel"
                                :showInheritedApplications="true"
                              >
                                <div class="d-flex flex-wrap">
                                  <template v-for="child5 in child4.subCapabilities">
                                    <CapabilityComponentItem
                                      :capability="child5"
                                      childStyle="5"
                                      @retrieveCapability="retrieveCapability"
                                      :showApplications="showApplications"
                                      :nbLevel="nbLevel"
                                      :showInheritedApplications="true"
                                    >
                                      <div class="d-flex flex-wrap">
                                        <template v-for="child6 in child5.subCapabilities">
                                          <CapabilityComponentItem
                                            :capability="child6"
                                            childStyle="6"
                                            @retrieveCapability="retrieveCapability"
                                            :showApplications="showApplications"
                                            :nbLevel="nbLevel"
                                            :showInheritedApplications="true"
                                          >
                                          </CapabilityComponentItem>
                                        </template>
                                      </div>
                                    </CapabilityComponentItem>
                                  </template>
                                </div>
                              </CapabilityComponentItem>
                            </template>
                          </div>
                        </CapabilityComponentItem>
                      </template>
                    </div>
                  </CapabilityComponentItem>
                </template>
              </div>
            </CapabilityComponentItem>
          </template>
        </div>
      </CapabilityComponentItem>
    </div>
    <div class="row border p-2 m-2">
      <div class="p-1 m-2">Legend</div>
      <div class="l-1 p-1 m-2" style="width: 100px; font-size: 10px">Domain</div>
      <div class="l0 p-1 m-2" style="width: 100px; font-size: 10px">Capability L0</div>
      <div class="l1 p-1 m-2" style="width: 100px; font-size: 10px">Capability L1</div>
      <div class="l2 p-1 m-2" style="width: 100px; font-size: 10px">Capability L2</div>
      <div class="l3 p-1 m-2" style="width: 100px; font-size: 10px">Capability L3</div>
      <div class="l-appli p-1 m-2" style="width: 120px">Application mapped on capability</div>
      <div class="l-appli2 p-1 m-2" style="width: 120px">Application mapped on sub-capability</div>
    </div>
  </div>
</template>

<style scoped>
a {
  text-decoration: none;
  font-weight: normal;
  cursor: pointer;
}

.l-2,
.l-1,
.l0,
.l1,
.l2,
.l3,
.l-appli,
.l-appli2 {
  border: solid;
  border-color: rgb(110, 110, 110);
  border-width: 1px;
  box-shadow: 1px 1px 2px #aaa;
  font-size: 10px;
  color: black;
}

.l-2 {
  /* ROOT */
  background-color: white;
}

.l-1 {
  /* SUR DOMAIN GREY */
  background-color: #e6e6e6;
}

.l0 {
  /* L0 BLUE */
  background-color: #deebf7;
}

.l1 {
  /* PURPLE */
  background-color: #e5cfee;
}

.l2 {
  background-color: white;
}

.l3 {
  background-color: rgb(233, 252, 233);
}

.l-appli {
  background-color: blueviolet;
  color: white;
}

.l-appli2 {
  background-color: chocolate;
  color: white;
}
</style>

<script lang="ts" src="./capability.component.ts"></script>
