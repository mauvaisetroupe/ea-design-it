import { defineComponent, inject, ref, type Ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import OrganizationalEntityService from './organizational-entity.service';
import { type IOrganizationalEntity } from '@/shared/model/organizational-entity.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'OrganizationalEntityDetails',
  setup() {
    const organizationalEntityService = inject('organizationalEntityService', () => new OrganizationalEntityService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const organizationalEntity: Ref<IOrganizationalEntity> = ref({});

    const retrieveOrganizationalEntity = async organizationalEntityId => {
      try {
        const res = await organizationalEntityService().find(organizationalEntityId);
        organizationalEntity.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.organizationalEntityId) {
      retrieveOrganizationalEntity(route.params.organizationalEntityId);
    }

    return {
      alertService,
      organizationalEntity,

      previousState,
    };
  },
});
