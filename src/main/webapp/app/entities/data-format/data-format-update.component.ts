import { computed, defineComponent, inject, ref, type Ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import DataFormatService from './data-format.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import { type IDataFormat, DataFormat } from '@/shared/model/data-format.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'DataFormatUpdate',
  setup() {
    const dataFormatService = inject('dataFormatService', () => new DataFormatService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const dataFormat: Ref<IDataFormat> = ref(new DataFormat());
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveDataFormat = async dataFormatId => {
      try {
        const res = await dataFormatService().find(dataFormatId);
        dataFormat.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.dataFormatId) {
      retrieveDataFormat(route.params.dataFormatId);
    }

    const validations = useValidation();
    const validationRules = {
      name: {
        required: validations.required('This field is required.'),
      },
    };
    const v$ = useVuelidate(validationRules, dataFormat as any);
    v$.value.$validate();

    return {
      dataFormatService,
      alertService,
      dataFormat,
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
      if (this.dataFormat.id) {
        this.dataFormatService()
          .update(this.dataFormat)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo('A DataFormat is updated with identifier ' + param.id);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.dataFormatService()
          .create(this.dataFormat)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess('A DataFormat is created with identifier ' + param.id);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
