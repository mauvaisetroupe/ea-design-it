import { defineComponent, inject, ref, type Ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import ApplicationCategoryService from './application-category.service';
import { type IApplicationCategory } from '@/shared/model/application-category.model';
import { useAlertService } from '@/shared/alert/alert.service';
import type AccountService from '@/account/account.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'ApplicationCategoryDetails',
  setup() {
    const applicationCategoryService = inject('applicationCategoryService', () => new ApplicationCategoryService());
    const alertService = inject('alertService', () => useAlertService(), true);
    const accountService = inject('accountService', () => new AccountService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const applicationCategory: Ref<IApplicationCategory> = ref({});

    const retrieveApplicationCategory = async applicationCategoryId => {
      try {
        const res = await applicationCategoryService().find(applicationCategoryId);
        applicationCategory.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.applicationCategoryId) {
      retrieveApplicationCategory(route.params.applicationCategoryId);
    }

    return {
      alertService,
      applicationCategory,

      previousState,
    };
  },
});
