import { defineComponent, inject, ref, type Ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import BusinessObjectService from './business-object.service';
import { type IBusinessObject } from '@/shared/model/business-object.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'BusinessObjectDetails',
  setup() {
    const businessObjectService = inject('businessObjectService', () => new BusinessObjectService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const businessObject: Ref<IBusinessObject> = ref({});

    const retrieveBusinessObject = async businessObjectId => {
      getPlantUML(businessObjectId);
      try {
        const res = await businessObjectService().find(businessObjectId);
        businessObject.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.businessObjectId) {
      retrieveBusinessObject(route.params.businessObjectId);
    }

    // plantuml
    const plantUMLImage = ref('');

    async function getPlantUML(businessObjectID) {
      try {
        const res: string = await businessObjectService().getPlantUML(businessObjectID);
        plantUMLImage.value = res;
      } catch (err) {
        console.log(err);
      }
    }

    return {
      alertService,
      businessObject,
      plantUMLImage,
      retrieveBusinessObject,
      previousState,
    };
  },
});
