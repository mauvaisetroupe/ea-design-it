<template>
  <span v-for="(obj, i) in allObjects" :key="obj.id">
    <span v-if="i > 0"> > </span>
    <span v-if="routerView && routerParamName">
      <router-link :to="{ name: routerView, params: { [routerParamName]: obj.id } }">{{ obj.name }}</router-link>
    </span>
    <span v-else>
      {{ obj.name }}
    </span>
    <span v-if="obj.generalization && generalization">
      (<span v-if="routerView && routerParamName">
        <router-link :to="{ name: routerView, params: { [routerParamName]: obj.generalization.id } }">{{
          obj.generalization.name
        }}</router-link></span
      >
      <span v-else>{{ obj.generalization.name }}</span
      >)
    </span>
  </span>
</template>
<script src="./business-data-object-fullpath.component.ts" />
