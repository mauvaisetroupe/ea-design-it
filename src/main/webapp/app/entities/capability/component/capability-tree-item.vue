<template>
  <div>
      <div
        :class="{bold: isFolder}"
        @click="toggle"
      >
        {{ fullname }}
        <span v-if="isFolder">
          <span v-if="isOpen" class="text-info">
            <font-awesome-icon icon="minus-circle"></font-awesome-icon>
          </span>
          <span v-else class="text-primary">
              <font-awesome-icon icon="plus-circle"></font-awesome-icon>
          </span>    
        </span>
      </div>
      <div v-show="isOpen" v-if="isFolder">
        <CapabilityTreeItemComponent
          v-for="(child, index) in capability.subCapabilities"
          :key="index"
          :capability="child"
        ></CapabilityTreeItemComponent>
      </div>
    </div>    
</template>
<script lang="ts" src="./capability-tree-item.component.ts"></script>