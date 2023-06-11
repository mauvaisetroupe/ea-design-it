<template>
  <div>
    <ol class="breadcrumb">
      <li class="breadcrumb-item" v-for="(mycap, i) in path" key="id">
        <a @click="$emit('retrieveCapability', mycap.id)" class="router-link-exact-active router-link-active" target="_self">
          <span v-if="i == 0">
            <font-awesome-icon icon="home" />
            <span>Home</span>
          </span>
          <span v-else>
            {{ mycap.name }}
          </span>
        </a>
      </li>
      <li class="breadcrumb-item">
        <span v-if="capability.level == -2">
          <font-awesome-icon icon="home" />
          <span>Home</span>
        </span>
        <span v-else>
          {{ capability.name }}
        </span>
      </li>
    </ol>
    <div class="capa-top"><input type="checkbox" v-model="showApplication" /> Show applications</div>
    <div :class="'capa-top c' + capability.level">
      <!-- 0 title -->
      <div :title="capability.description" v-if="capability.level > -2">{{ capability.name }}</div>
      <!-- 0 appli -->
      <div v-for="appli in capability.applications" class="appli" v-if="showApplication">{{ appli.name }}</div>

      <!-- 0 container for children -->
      <div v-if="capability.subCapabilities" class="d-flex flex-wrap">
        <div v-for="child1 in capability.subCapabilities" :key="child1.id" :class="'capa-child-1 c' + child1.level">
          <!-- 1 title -->
          <div :title="child1.description">
            <a @click="$emit('retrieveCapability', child1.id)" v-if="child1.level <= 0">{{ child1.name }}</a>
            <span v-else>{{ child1.name }}</span>
          </div>
          <!-- 1 appli -->
          <div v-for="appli in child1.applications" class="appli" v-if="showApplication">{{ appli.name }}</div>

          <!-- 1 container for children -->
          <div v-if="child1.subCapabilities" class="d-flex flex-wrap">
            <div v-for="child2 in child1.subCapabilities" :key="child2.id" :class="'capa-child-2 c' + child2.level">
              <!-- 2 title -->
              <div :title="child2.description">
                <a @click="$emit('retrieveCapability', child2.id)" v-if="child2.level <= 0">{{ child2.name }}</a>
                <span v-else>{{ child2.name }}</span>
              </div>
              <!-- 2 appli -->
              <div v-for="appli in child2.applications" class="appli" v-if="showApplication">{{ appli.name }}</div>

              <!-- 2 container for children -->
              <div v-if="child2.subCapabilities" class="d-flex flex-wrap">
                <div v-for="child3 in child2.subCapabilities" :key="child3.id" :class="'capa-child-3 c' + child3.level">
                  <!-- 3 title -->
                  <div>
                    <a @click="$emit('retrieveCapability', child3.id)" v-if="child3.level <= 0">{{ child3.name }}</a>
                    <span v-else>{{ child3.name }}</span>
                  </div>
                  <!-- 3 appli -->
                  <div v-for="appli in child3.applications" class="appli" v-if="showApplication">{{ appli.name }}</div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="row border p-2 m-2">
      <div class="p-1 m-2">Legend</div>
      <div class="c-1 p-1 m-2" style="width: 80px">Domain</div>
      <div class="c0 p-1 m-2" style="width: 80px">L0</div>
      <div class="c1 p-1 m-2" style="width: 80px">L1</div>
      <div class="c2 p-1 m-2" style="width: 80px">L2</div>
      <div class="c3 p-1 m-2" style="width: 80px">L3</div>
    </div>
  </div>
</template>

<style scoped>
a {
  text-decoration: none;
  font-weight: normal;
  cursor: pointer;
}

.capa-top,
.capa-child-1,
.capa-child-2 {
  min-height: 70px;
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

.appli {
  background-color: blueviolet;
  color: white;
  max-width: 200px;
  margin: 5px;
  font-size: 11px;
  text-align: center;
  border: #aaa;
  border-style: solid;
  border-width: 1px;
}
</style>

<script lang="ts" src="./capability.component.ts"></script>
