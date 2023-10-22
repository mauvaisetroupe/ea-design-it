import { defineComponent, inject, ref, type Ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import DataFormatService from './data-format.service';
import { type IDataFormat } from '@/shared/model/data-format.model';
import { useAlertService } from '@/shared/alert/alert.service';
import type AccountService from '@/account/account.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'DataFormatDetails',
  setup() {
    const dataFormatService = inject('dataFormatService', () => new DataFormatService());
    const alertService = inject('alertService', () => useAlertService(), true);
    const accountService = inject('accountService', () => new AccountService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const dataFormat: Ref<IDataFormat> = ref({});

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

    return {
      alertService,
      dataFormat,

      previousState,
    };
  },
});
