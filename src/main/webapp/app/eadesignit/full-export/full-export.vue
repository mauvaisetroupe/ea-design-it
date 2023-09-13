<template>
  <div>
    <h2>Export</h2>
    <h3>Applications</h3>
    <div class="row m-3">
      <div class="col-12">
        <input type="checkbox" disabled="true" v-model="applications" />
        <label>Application</label>
      </div>
      <div class="col-12">
        <input type="checkbox" disabled="true" v-model="applicationComponents" />
        <label>Application Component</label>
      </div>
      <div class="col-12">
        <input type="checkbox" v-model="owner" />
        <label>Owner</label>
      </div>
      <div class="col-12">
        <input type="checkbox" v-model="externalSystem" />
        <label>External System</label>
      </div>
    </div>
    <div>
      <h3>Landscapes</h3>
      [<a @click="selectAllLansdcape">Select All</a>] [<a @click="selectNoLandscape">Select None</a>]
    </div>
    <div class="row m-3">
      <div class="col-4" v-for="landscape in landscapes" :key="landscape">
        <input type="checkbox" v-model="checkedLandscapes" :value="landscape" />
        <label>{{ landscape.diagramName.substring(0, 40) }}</label>
      </div>
    </div>
    <h3>Capabilities</h3>
    <div class="row m-3">
      <div class="col-4">
        <input type="checkbox" v-model="capabilities" />
        <label>Capabilities</label>
      </div>
    </div>
    <div>
      <h3>Capabilities In Landscape</h3>
      [<a @click="selectAllMapping">Select All</a>] [<a @click="selectNoMapping">Select None</a>]
    </div>
    <div class="row m-3">
      <div class="col-4" v-for="mapping in capabilitiesMapping" :key="mapping">
        <input type="checkbox" v-model="checkedCapabilitiesMapping" :value="mapping" />
        <label>{{ mapping.diagramName.substring(0, 40) }}</label>
      </div>
    </div>
    <div class="row m-3">
      <div class="col-12">
        <input type="checkbox" v-model="capabilitiesMappingWithNoLandscape" />
        <label>Capabilities / Application mapping with no landscape association</label>
      </div>
    </div>
    <div class="form-group">
      <button type="submit" class="btn btn-primary mb-2" v-on:click="exportExcel()" :disabled="isFetching">
        <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Export database in Excel</span>
      </button>
    </div>
  </div>
</template>

<script lang="ts" src="./full-export.component.ts"></script>
<style>
.rederror {
  background-color: red;
  color: white;
}
</style>
