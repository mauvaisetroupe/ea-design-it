import { defineComponent, inject, ref, type Ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import DataObjectService from './data-object.service';
import { type IDataObject } from '@/shared/model/data-object.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'DataObjectDetails',
  setup() {
    const dataObjectService = inject('dataObjectService', () => new DataObjectService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const dataObject: Ref<IDataObject> = ref({});

    const retrieveDataObject = async dataObjectId => {
      try {
        const res = await dataObjectService().find(dataObjectId);
        dataObject.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.dataObjectId) {
      retrieveDataObject(route.params.dataObjectId);
    }

    return {
      alertService,
      dataObject,

      previousState,
    };
  },
});