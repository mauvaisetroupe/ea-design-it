import { computed, defineComponent, inject, ref, type Ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import TechnologyService from './technology.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import { type ITechnology, Technology } from '@/shared/model/technology.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'TechnologyUpdate',
  setup() {
    const technologyService = inject('technologyService', () => new TechnologyService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const technology: Ref<ITechnology> = ref(new Technology());
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveTechnology = async technologyId => {
      try {
        const res = await technologyService().find(technologyId);
        technology.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.technologyId) {
      retrieveTechnology(route.params.technologyId);
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
      dataObjects: {},
    };
    const v$ = useVuelidate(validationRules, technology as any);
    v$.value.$validate();

    return {
      technologyService,
      alertService,
      technology,
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
      if (this.technology.id) {
        this.technologyService()
          .update(this.technology)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo('A Technology is updated with identifier ' + param.id);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.technologyService()
          .create(this.technology)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess('A Technology is created with identifier ' + param.id);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
