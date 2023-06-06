<template>
  <div>
    <ol class="breadcrumb">
      <li class="breadcrumb-item" v-for="mycap in path" key="id">
        <a @click="retrieveCapability(mycap.id)" class="router-link-exact-active router-link-active" target="_self">
          {{ mycap.name }}
        </a>
      </li>
      <li class="breadcrumb-item">{{ capability.name }}</li>
    </ol>

    <div :class="'capa-top c' + capability.level">
      <div :title="capability.description">{{ capability.name }}</div>
      <div v-if="capability.subCapabilities" class="d-flex flex-wrap">
        <div v-for="child1 in capability.subCapabilities" :key="child1.id" :class="'capa-child-1 c' + child1.level">
          <div :title="child1.description">
            <a @click="retrieveCapability(child1.id)">{{ child1.name }}</a>
          </div>
          <div v-if="child1.subCapabilities" class="d-flex flex-wrap">
            <div v-for="child2 in child1.subCapabilities" :key="child2.id" :class="'capa-child-2 c' + child2.level">
              <div :title="child2.description">
                <a @click="retrieveCapability(child2.id)">{{ child2.name }}</a>
              </div>
              <div v-if="child2.subCapabilities" class="d-flex flex-wrap">
                <div v-for="child3 in child2.subCapabilities" :key="child3.id" :class="'capa-child-3 c' + child3.level">
                  <div>
                    <a @click="retrieveCapability(child3.id)">{{ child3.name }}</a>
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
a {
  text-decoration: none;
  font-weight: normal;
  cursor: pointer;
}
.capa-top {
  font-weight: normal;
  padding: 5px;
  margin: 5px;
  width: 100%;
  font-size: 18px;
}

.capa-child-1 {
  font-weight: normal;
  padding: 5px;
  margin: 5px 5px 25px 5px;
  width: 100%;
  font-size: 18px;
}

.capa-child-2 {
  font-weight: normal;
  padding: 5px;
  margin: 5px;
  width: 16%;
  font-size: 15px;
}

.capa-child-3 {
  font-weight: normal;
  padding: 5px;
  margin: 5px;
  width: 45%;
  font-size: 11px;
}

.c-2,
.c-1,
.c0,
.c1,
.c2,
.c3 {
  border: solid;
  border-color: rgb(110, 110, 110);
  border-width: 1px;
  box-shadow: 1px 1px 2px #aaa;
}

.c-2 {
  /* ROOT */
  background-color: white;
  color: black;
}

.c-1 {
  /* SUR DOMAIN GREY */
  background-color: #e6e6e6;
  color: black;
}

.c0 {
  /* L0 BLUE */
  background-color: #deebf7;
}

.c1 {
  /* PURPLE */
  background-color: #e5cfee;
}

.c2 {
  background-color: white;
}

.c3 {
  background-color: rgb(233, 252, 233);
}
</style>

<script lang="ts" src="./capability-details.component.ts"></script>
