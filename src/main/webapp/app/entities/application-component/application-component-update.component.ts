import { computed, defineComponent, inject, ref, type Ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import ApplicationComponentService from './application-component.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import ApplicationService from '@/entities/application/application.service';
import { type IApplication } from '@/shared/model/application.model';
import ApplicationCategoryService from '@/entities/application-category/application-category.service';
import { type IApplicationCategory } from '@/shared/model/application-category.model';
import TechnologyService from '@/entities/technology/technology.service';
import { type ITechnology } from '@/shared/model/technology.model';
import ExternalReferenceService from '@/entities/external-reference/external-reference.service';
import { type IExternalReference } from '@/shared/model/external-reference.model';
import { type IApplicationComponent, ApplicationComponent } from '@/shared/model/application-component.model';
import { ApplicationType } from '@/shared/model/enumerations/application-type.model';
import { SoftwareType } from '@/shared/model/enumerations/software-type.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'ApplicationComponentUpdate',
  setup() {
    const applicationComponentService = inject('applicationComponentService', () => new ApplicationComponentService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const applicationComponent: Ref<IApplicationComponent> = ref(new ApplicationComponent());

    const applicationService = inject('applicationService', () => new ApplicationService());

    const applications: Ref<IApplication[]> = ref([]);

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

    const retrieveApplicationComponent = async applicationComponentId => {
      try {
        const res = await applicationComponentService().find(applicationComponentId);
        applicationComponent.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.applicationComponentId) {
      retrieveApplicationComponent(route.params.applicationComponentId);
    }

    const initRelationships = () => {
      applicationService()
        .retrieve()
        .then(res => {
          applications.value = res.data;
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
        maxLength: validations.maxLength('This field cannot be longer than 1000 characters.', 1000),
      },
      comment: {
        maxLength: validations.maxLength('This field cannot be longer than 500 characters.', 500),
      },
      documentationURL: {
        maxLength: validations.maxLength('This field cannot be longer than 500 characters.', 500),
      },
      startDate: {},
      endDate: {},
      applicationType: {},
      softwareType: {},
      displayInLandscape: {},
      application: {
        required: validations.required('This field is required.'),
      },
      categories: {},
      technologies: {},
      externalIDS: {},
    };
    const v$ = useVuelidate(validationRules, applicationComponent as any);
    v$.value.$validate();

    return {
      applicationComponentService,
      alertService,
      applicationComponent,
      previousState,
      applicationTypeValues,
      softwareTypeValues,
      isSaving,
      currentLanguage,
      applications,
      applicationCategories,
      technologies,
      externalReferences,
      v$,
    };
  },
  created(): void {
    this.applicationComponent.categories = [];
    this.applicationComponent.technologies = [];
    this.applicationComponent.externalIDS = [];
  },
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.applicationComponent.id) {
        this.applicationComponentService()
          .update(this.applicationComponent)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo('A ApplicationComponent is updated with identifier ' + param.id);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.applicationComponentService()
          .create(this.applicationComponent)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess('A ApplicationComponent is created with identifier ' + param.id);
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
