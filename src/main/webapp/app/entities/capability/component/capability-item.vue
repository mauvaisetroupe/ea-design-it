<template>
  <div
    :class="'capa-child-' + childStyle + ' c' + capability.level"
    v-if="capability.level <= nbLevel"
    :key="childStyle + '-' + capability.id"
    :style="childStyle === '2' ? 'width: ' + percent + '%' : ''"
  >
    <!--  :title="'capa-child-' + childStyle + ' c' + capability.level" -->
    <!--{{ childStyle}} {{ capability.level}} {{ nbNode }} {{ (childStyle==='2')?'width: ' + percent + '%':'' }}-->
    <div :title="capability.description" v-if="capability.level > -2">
      <!--do not display ROOT-->
      <a @click="$emit('retrieveCapability', capability.id)" v-if="childStyle != 'top' && capability.level < 2">{{ capability.name }}</a>
      <span v-else>{{ capability.name }}</span>
    </div>
    <div v-else>Corporate capabilities</div>
    <div v-for="appli in getApplications()" class="appli" v-if="showApplications" :key="'APP-' + appli.id">{{ appli.name }}</div>
    <div
      v-if="showApplications && capability.level == nbLevel"
      v-for="appli in getInheritedApplications()"
      class="appli2"
      :key="'INH-' + appli.id"
    >
      {{ appli.name }}
    </div>
    <div v-if="capability.subCapabilities">
      <slot> </slot>
    </div>
  </div>
</template>
<script lang="ts" src="./capability-item.component.ts"></script>

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

.capa-child-top {
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
  font-size: 15px;
}

.capa-child-3 {
  font-weight: normal;
  padding: 5px;
  margin: 5px;
  width: 45%;
  font-size: 11px;
}

.capa-child-4 {
  font-weight: normal;
  padding: 5px;
  margin: 5px;
  font-size: 11px;
  width: 100%;
}

.capa-child-5 {
  font-weight: normal;
  padding: 5px;
  margin: 5px;
  font-size: 11px;
  width: 100%;
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
  width: 100%;
}

.c-1 {
  /* SUR DOMAIN GREY */
  background-color: #e6e6e6;
  color: black;
  width: 100%;
}

.c0 {
  /* L0 BLUE */
  background-color: #deebf7;
}

.c1 {
  /* PURPLE */
  background-color: #e5cfee;
}

/* WHITE L2 has a max width of 300px (because they are considered as leaf...) */
.c2 {
  background-color: white;
  max-width: 280px;
}

/* GREEN L3 take all the width available, thy are always in a L2 displayed*/
.c3 {
  background-color: rgb(233, 252, 233);
  width: 100%;
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

.appli2 {
  background-color: chocolate;
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
