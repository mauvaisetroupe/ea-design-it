import { computed, defineComponent, inject, ref, type Ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import ApplicationService from './application.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import OwnerService from '@/entities/owner/owner.service';
import { type IOwner } from '@/shared/model/owner.model';
import OrganizationalEntityService from '@/entities/organizational-entity/organizational-entity.service';
import { type IOrganizationalEntity } from '@/shared/model/organizational-entity.model';
import ApplicationCategoryService from '@/entities/application-category/application-category.service';
import { type IApplicationCategory } from '@/shared/model/application-category.model';
import TechnologyService from '@/entities/technology/technology.service';
import { type ITechnology } from '@/shared/model/technology.model';
import ExternalReferenceService from '@/entities/external-reference/external-reference.service';
import { type IExternalReference } from '@/shared/model/external-reference.model';
import { type IApplication, Application } from '@/shared/model/application.model';
import { ApplicationType } from '@/shared/model/enumerations/application-type.model';
import { SoftwareType } from '@/shared/model/enumerations/software-type.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'ApplicationUpdate',
  setup() {
    const applicationService = inject('applicationService', () => new ApplicationService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const application: Ref<IApplication> = ref(new Application());

    const ownerService = inject('ownerService', () => new OwnerService());

    const owners: Ref<IOwner[]> = ref([]);

    const organizationalEntityService = inject('organizationalEntityService', () => new OrganizationalEntityService());

    const organizationalEntities: Ref<IOrganizationalEntity[]> = ref([]);

    const applicationCategoryService = inject('applicationCategoryService', () => new ApplicationCategoryService());

    const applicationCategories: Ref<IApplicationCategory[]> = ref([]);

    const technologyService = inject('technologyService', () => new TechnologyService());

    const technologies: Ref<ITechnology[]> = ref([]);

    const externalReferenceService = inject('externalReferenceService', () => new ExternalReferenceService());

    const externalReferences: Ref<IExternalReference[]> = ref([]);
    const applicationTypeValues: Ref<string[]> = ref(Object.keys(ApplicationType));
    const softwareTypeValues: Ref<string[]> = ref(Object.keys(SoftwareType));
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveApplication = async applicationId => {
      try {
        const res = await applicationService().find(applicationId);
        application.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.applicationId) {
      retrieveApplication(route.params.applicationId);
    }

    const initRelationships = () => {
      ownerService()
        .retrieve()
        .then(res => {
          owners.value = res.data;
        });
      organizationalEntityService()
        .retrieve()
        .then(res => {
          organizationalEntities.value = res.data;
        });
      applicationCategoryService()
        .retrieve()
        .then(res => {
          applicationCategories.value = res.data;
        });
      technologyService()
        .retrieve()
        .then(res => {
          technologies.value = res.data;
        });
      externalReferenceService()
        .retrieve()
        .then(res => {
          externalReferences.value = res.data;
        });
    };

    initRelationships();

    const validations = useValidation();
    const validationRules = {
      alias: {},
      name: {
        required: validations.required('This field is required.'),
      },
      description: {
        maxLength: validations.maxLength('This field cannot be longer than 1500 characters.', 1500),
      },
      comment: {
        maxLength: validations.maxLength('This field cannot be longer than 1000 characters.', 1000),
      },
      documentationURL: {
        maxLength: validations.maxLength('This field cannot be longer than 500 characters.', 500),
      },
      startDate: {},
      endDate: {},
      applicationType: {},
      softwareType: {},
      nickname: {},
      owner: {},
      itOwner: {},
      businessOwner: {},
      organizationalEntity: {},
      categories: {},
      technologies: {},
      externalIDS: {},
      applicationsLists: {},
      capabilityApplicationMappings: {},
      dataObjects: {},
    };
    const v$ = useVuelidate(validationRules, application as any);
    v$.value.$validate();

    return {
      applicationService,
      alertService,
      application,
      previousState,
      applicationTypeValues,
      softwareTypeValues,
      isSaving,
      currentLanguage,
      owners,
      organizationalEntities,
      applicationCategories,
      technologies,
      externalReferences,
      v$,
    };
  },
  created(): void {
    this.application.categories = [];
    this.application.technologies = [];
    this.application.externalIDS = [];
  },
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.application.id) {
        this.applicationService()
          .update(this.application)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo('A Application is updated with identifier ' + param.id);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.applicationService()
          .create(this.application)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess('A Application is created with identifier ' + param.id);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },

    getSelected(selectedVals, option): any {
      if (selectedVals) {
        return selectedVals.find(value => option.id === value.id) ?? option;
      }
      return option;
    },
  },
});
