import { computed, defineComponent, inject, ref, type Ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import BusinessObjectService from './business-object.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import OwnerService from '@/entities/owner/owner.service';
import { type IOwner } from '@/shared/model/owner.model';
import { type IBusinessObject, BusinessObject } from '@/shared/model/business-object.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'BusinessObjectUpdate',
  setup() {
    const businessObjectService = inject('businessObjectService', () => new BusinessObjectService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const businessObject: Ref<IBusinessObject> = ref(new BusinessObject());

    const businessObjects: Ref<IBusinessObject[]> = ref([]);

    const ownerService = inject('ownerService', () => new OwnerService());

    const owners: Ref<IOwner[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveBusinessObject = async businessObjectId => {
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

    const initRelationships = () => {
      businessObjectService()
        .retrieve()
        .then(res => {
          businessObjects.value = res.data;
        });
      ownerService()
        .retrieve()
        .then(res => {
          owners.value = res.data;
        });
    };

    initRelationships();

    const validations = useValidation();
    const validationRules = {
      name: {
        required: validations.required('This field is required.'),
      },
      implementable: {},
      specializations: {},
      components: {},
      dataObjects: {},
      owner: {},
      generalization: {},
      container: {},
    };
    const v$ = useVuelidate(validationRules, businessObject as any);
    v$.value.$validate();

    return {
      businessObjectService,
      alertService,
      businessObject,
      previousState,
      isSaving,
      currentLanguage,
      businessObjects,
      owners,
      v$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.businessObject.id) {
        this.businessObjectService()
          .update(this.businessObject)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo('A BusinessObject is updated with identifier ' + param.id);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.businessObjectService()
          .create(this.businessObject)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess('A BusinessObject is created with identifier ' + param.id);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
