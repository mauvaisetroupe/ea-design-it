<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="owner">
        <h2 class="jh-entity-heading" data-cy="ownerDetailsHeading"><span>Owner</span> {{ owner.id }}</h2>
        <dl class="row jh-entity-details">
          <dt>
            <span>Name</span>
          </dt>
          <dd>
            <span>{{ owner.name }}</span>
          </dd>
          <dt>
            <span>Firstname</span>
          </dt>
          <dd>
            <span>{{ owner.firstname }}</span>
          </dd>
          <dt>
            <span>Lastname</span>
          </dt>
          <dd>
            <span>{{ owner.lastname }}</span>
          </dd>
          <dt>
            <span>Email</span>
          </dt>
          <dd>
            <span>{{ owner.email }}</span>
          </dd>
          <dt>
            <span>Users</span>
          </dt>
          <dd>
            <span v-for="(users, i) in owner.users" :key="users.id"
              >{{ i > 0 ? ', ' : '' }}
              {{ users.login }}
            </span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span> Back</span>
        </button>
        <router-link v-if="owner.id" :to="{ name: 'OwnerEdit', params: { ownerId: owner.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary" v-if="accountService().writeAuthorities">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./owner-details.component.ts"></script>
