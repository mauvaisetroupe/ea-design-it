<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="dataFormat">
        <h2 class="jh-entity-heading" data-cy="dataFormatDetailsHeading"><span>Data Format</span> {{ dataFormat.id }}</h2>
        <dl class="row jh-entity-details">
          <dt>
            <span>Name</span>
          </dt>
          <dd>
            <span>{{ dataFormat.name }}</span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>Back</span>
        </button>
        <router-link
          v-if="dataFormat.id"
          :to="{ name: 'DataFormatEdit', params: { dataFormatId: dataFormat.id } }"
          custom
          v-slot="{ navigate }"
        >
          <button @click="navigate" class="btn btn-primary" v-if="accountService.writeAuthorities">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./data-format-details.component.ts"></script>
