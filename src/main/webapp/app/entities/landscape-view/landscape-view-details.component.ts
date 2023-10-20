import { defineComponent, inject, ref, type Ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import LandscapeViewService from './landscape-view.service';
import useDataUtils from '@/shared/data/data-utils.service';
import { type ILandscapeView } from '@/shared/model/landscape-view.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'LandscapeViewDetails',
  setup() {
    const landscapeViewService = inject('landscapeViewService', () => new LandscapeViewService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const dataUtils = useDataUtils();

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const landscapeView: Ref<ILandscapeView> = ref({});

    const retrieveLandscapeView = async landscapeViewId => {
      try {
        const res = await landscapeViewService().find(landscapeViewId);
        landscapeView.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.landscapeViewId) {
      retrieveLandscapeView(route.params.landscapeViewId);
    }

    return {
      alertService,
      landscapeView,

      ...dataUtils,

      previousState,
    };
  },
});
