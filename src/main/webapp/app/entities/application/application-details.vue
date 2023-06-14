<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="application">
        <h2 class="jh-entity-heading" data-cy="applicationDetailsHeading"><span>Application</span> {{ application.id }}</h2>
        <dl class="row jh-entity-details">
          <dt>
            <span>Alias</span>
          </dt>
          <dd>
            <span>{{ application.alias }}</span>
          </dd>
          <dt>
            <span>Name</span>
          </dt>
          <dd>
            <span>{{ application.name }}</span>
          </dd>
          <dt>
            <span>Description</span>
          </dt>
          <dd>
            <span>{{ application.description }}</span>
          </dd>
          <dt>
            <span>Comment</span>
          </dt>
          <dd>
            <span>{{ application.comment }}</span>
          </dd>
          <dt>
            <span>Documentation URL</span>
          </dt>
          <dd>
            <span>{{ application.documentationURL }}</span>
          </dd>
          <dt>
            <span>Start Date</span>
          </dt>
          <dd>
            <span>{{ application.startDate }}</span>
          </dd>
          <dt>
            <span>End Date</span>
          </dt>
          <dd>
            <span>{{ application.endDate }}</span>
          </dd>
          <dt>
            <span>Application Type</span>
          </dt>
          <dd>
            <span>{{ application.applicationType }}</span>
          </dd>
          <dt>
            <span>Software Type</span>
          </dt>
          <dd>
            <span>{{ application.softwareType }}</span>
          </dd>
          <dt>
            <span>Nickname</span>
          </dt>
          <dd>
            <span>{{ application.nickname }}</span>
          </dd>
          <dt>
            <span>Owner</span>
          </dt>
          <dd>
            <div v-if="application.owner">
              <router-link :to="{ name: 'OwnerView', params: { ownerId: application.owner.id } }">{{ application.owner.name }}</router-link>
            </div>
          </dd>
          <dt>
            <span>It Owner</span>
          </dt>
          <dd>
            <div v-if="application.itOwner">
              <router-link :to="{ name: 'OwnerView', params: { ownerId: application.itOwner.id } }">{{
                application.itOwner.name
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span>Business Owner</span>
          </dt>
          <dd>
            <div v-if="application.businessOwner">
              <router-link :to="{ name: 'OwnerView', params: { ownerId: application.businessOwner.id } }">{{
                application.businessOwner.name
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span>Categories</span>
          </dt>
          <dd>
            <span v-for="(categories, i) in application.categories" :key="categories.id"
              >{{ i > 0 ? ', ' : '' }}
              <router-link :to="{ name: 'ApplicationCategoryView', params: { applicationCategoryId: categories.id } }">{{
                categories.name
              }}</router-link>
            </span>
          </dd>
          <dt>
            <span>Technologies</span>
          </dt>
          <dd>
            <span v-for="(technologies, i) in application.technologies" :key="technologies.id"
              >{{ i > 0 ? ', ' : '' }}
              <router-link :to="{ name: 'TechnologyView', params: { technologyId: technologies.id } }">{{ technologies.name }}</router-link>
            </span>
          </dd>
          <dt>
            <span>External IDS</span>
          </dt>
          <dd>
            <span v-for="(externalIDS, i) in application.externalIDS" :key="externalIDS.id"
              >{{ i > 0 ? ', ' : '' }}
              <router-link :to="{ name: 'ExternalReferenceView', params: { externalReferenceId: externalIDS.id } }">{{
                externalIDS.externalID
              }}</router-link>
            </span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span> Back</span>
        </button>
        <router-link
          v-if="application.id"
          :to="{ name: 'ApplicationEdit', params: { applicationId: application.id } }"
          custom
          v-slot="{ navigate }"
        >
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./application-details.component.ts"></script>
