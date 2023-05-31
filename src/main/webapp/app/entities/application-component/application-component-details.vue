<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="applicationComponent">
        <h2 class="jh-entity-heading" data-cy="applicationComponentDetailsHeading">
          <span>ApplicationComponent</span> {{ applicationComponent.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span>Alias</span>
          </dt>
          <dd>
            <span>{{ applicationComponent.alias }}</span>
          </dd>
          <dt>
            <span>Name</span>
          </dt>
          <dd>
            <span>{{ applicationComponent.name }}</span>
          </dd>
          <dt>
            <span>Description</span>
          </dt>
          <dd>
            <span>{{ applicationComponent.description }}</span>
          </dd>
          <dt>
            <span>Comment</span>
          </dt>
          <dd>
            <span>{{ applicationComponent.comment }}</span>
          </dd>
          <dt>
            <span>Documentation URL</span>
          </dt>
          <dd>
            <span>{{ applicationComponent.documentationURL }}</span>
          </dd>
          <dt>
            <span>Start Date</span>
          </dt>
          <dd>
            <span>{{ applicationComponent.startDate }}</span>
          </dd>
          <dt>
            <span>End Date</span>
          </dt>
          <dd>
            <span>{{ applicationComponent.endDate }}</span>
          </dd>
          <dt>
            <span>Application Type</span>
          </dt>
          <dd>
            <span>{{ applicationComponent.applicationType }}</span>
          </dd>
          <dt>
            <span>Software Type</span>
          </dt>
          <dd>
            <span>{{ applicationComponent.softwareType }}</span>
          </dd>
          <dt>
            <span>Display In Landscape</span>
          </dt>
          <dd>
            <span>{{ applicationComponent.displayInLandscape }}</span>
          </dd>
          <dt>
            <span>Application</span>
          </dt>
          <dd>
            <div v-if="applicationComponent.application">
              <router-link :to="{ name: 'ApplicationView', params: { applicationId: applicationComponent.application.id } }">{{
                applicationComponent.application.name
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span>Categories</span>
          </dt>
          <dd>
            <span v-for="(categories, i) in applicationComponent.categories" :key="categories.id"
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
            <span v-for="(technologies, i) in applicationComponent.technologies" :key="technologies.id"
              >{{ i > 0 ? ', ' : '' }}
              <router-link :to="{ name: 'TechnologyView', params: { technologyId: technologies.id } }">{{ technologies.name }}</router-link>
            </span>
          </dd>
          <dt>
            <span>External IDS</span>
          </dt>
          <dd>
            <span v-for="(externalIDS, i) in applicationComponent.externalIDS" :key="externalIDS.id"
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
          v-if="applicationComponent.id"
          :to="{ name: 'ApplicationComponentEdit', params: { applicationComponentId: applicationComponent.id } }"
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

<script lang="ts" src="./application-component-details.component.ts"></script>
