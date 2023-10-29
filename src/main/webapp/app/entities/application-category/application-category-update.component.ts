import { computed, defineComponent, inject, ref, type Ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import ApplicationCategoryService from './application-category.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import { type IApplicationCategory, ApplicationCategory } from '@/shared/model/application-category.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'ApplicationCategoryUpdate',
  setup() {
    const applicationCategoryService = inject('applicationCategoryService', () => new ApplicationCategoryService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const applicationCategory: Ref<IApplicationCategory> = ref(new ApplicationCategory());
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveApplicationCategory = async applicationCategoryId => {
      try {
        const res = await applicationCategoryService().find(applicationCategoryId);
        applicationCategory.value = res;
      } catch (error) {
        alertService.showAnyError(error);
      }
    };

    if (route.params?.applicationCategoryId) {
      retrieveApplicationCategory(route.params.applicationCategoryId);
    }

    const initRelationships = () => {};

    initRelationships();

    const validations = useValidation();
    const validationRules = {
      name: {
        required: validations.required('This field is required.'),
      },
      type: {},
      description: {
        maxLength: validations.maxLength('This field cannot be longer than 250 characters.', 250),
      },
      applications: {},
      components: {},
    };
    const v$ = useVuelidate(validationRules, applicationCategory as any);
    v$.value.$validate();

    return {
      applicationCategoryService,
      alertService,
      applicationCategory,
      previousState,
      isSaving,
      currentLanguage,
      v$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.applicationCategory.id) {
        this.applicationCategoryService()
          .update(this.applicationCategory)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo('A ApplicationCategory is updated with identifier ' + param.id);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showAnyError(error);
          });
      } else {
        this.applicationCategoryService()
          .create(this.applicationCategory)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess('A ApplicationCategory is created with identifier ' + param.id);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showAnyError(error);
          });
      }
    },
  },
});
