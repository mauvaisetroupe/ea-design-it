import { computed, defineComponent, inject, ref, type Ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import OrganizationalEntityService from './organizational-entity.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import { type IOrganizationalEntity, OrganizationalEntity } from '@/shared/model/organizational-entity.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'OrganizationalEntityUpdate',
  setup() {
    const organizationalEntityService = inject('organizationalEntityService', () => new OrganizationalEntityService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const organizationalEntity: Ref<IOrganizationalEntity> = ref(new OrganizationalEntity());
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

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

    const initRelationships = () => {};

    initRelationships();

    const validations = useValidation();
    const validationRules = {
      name: {
        required: validations.required('This field is required.'),
      },
      applications: {},
    };
    const v$ = useVuelidate(validationRules, organizationalEntity as any);
    v$.value.$validate();

    return {
      organizationalEntityService,
      alertService,
      organizationalEntity,
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
      if (this.organizationalEntity.id) {
        this.organizationalEntityService()
          .update(this.organizationalEntity)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo('A OrganizationalEntity is updated with identifier ' + param.id);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.organizationalEntityService()
          .create(this.organizationalEntity)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess('A OrganizationalEntity is created with identifier ' + param.id);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
