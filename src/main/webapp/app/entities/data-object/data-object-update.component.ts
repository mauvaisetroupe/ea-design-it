import { computed, defineComponent, inject, ref, type Ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import DataObjectService from './data-object.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import OwnerService from '@/entities/owner/owner.service';
import { type IOwner } from '@/shared/model/owner.model';
import ApplicationService from '@/entities/application/application.service';
import { type IApplication } from '@/shared/model/application.model';
import TechnologyService from '@/entities/technology/technology.service';
import { type ITechnology } from '@/shared/model/technology.model';
import BusinessObjectService from '@/entities/business-object/business-object.service';
import { type IBusinessObject } from '@/shared/model/business-object.model';
import { type IDataObject, DataObject } from '@/shared/model/data-object.model';
import { DataObjectType } from '@/shared/model/enumerations/data-object-type.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'DataObjectUpdate',
  setup() {
    const dataObjectService = inject('dataObjectService', () => new DataObjectService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const dataObject: Ref<IDataObject> = ref(new DataObject());

    const dataObjects: Ref<IDataObject[]> = ref([]);

    const ownerService = inject('ownerService', () => new OwnerService());

    const owners: Ref<IOwner[]> = ref([]);

    const applicationService = inject('applicationService', () => new ApplicationService());

    const applications: Ref<IApplication[]> = ref([]);

    const technologyService = inject('technologyService', () => new TechnologyService());

    const technologies: Ref<ITechnology[]> = ref([]);

    const businessObjectService = inject('businessObjectService', () => new BusinessObjectService());

    const businessObjects: Ref<IBusinessObject[]> = ref([]);
    const dataObjectTypeValues: Ref<string[]> = ref(Object.keys(DataObjectType));
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

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

    const initRelationships = () => {
      dataObjectService()
        .retrieve()
        .then(res => {
          dataObjects.value = res.data;
        });
      ownerService()
        .retrieve()
        .then(res => {
          owners.value = res.data;
        });
      applicationService()
        .retrieve()
        .then(res => {
          applications.value = res.data;
        });
      technologyService()
        .retrieve()
        .then(res => {
          technologies.value = res.data;
        });
      businessObjectService()
        .retrieve()
        .then(res => {
          businessObjects.value = res.data;
        });
    };

    initRelationships();

    const validations = useValidation();
    const validationRules = {
      name: {
        required: validations.required('This field is required.'),
      },
      type: {},
      components: {},
      owner: {},
      application: {},
      technologies: {},
      businessObject: {},
      container: {},
    };
    const v$ = useVuelidate(validationRules, dataObject as any);
    v$.value.$validate();

    return {
      dataObjectService,
      alertService,
      dataObject,
      previousState,
      dataObjectTypeValues,
      isSaving,
      currentLanguage,
      dataObjects,
      owners,
      applications,
      technologies,
      businessObjects,
      v$,
    };
  },
  created(): void {
    this.dataObject.technologies = [];
  },
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.dataObject.id) {
        this.dataObjectService()
          .update(this.dataObject)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo('A DataObject is updated with identifier ' + param.id);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.dataObjectService()
          .create(this.dataObject)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess('A DataObject is created with identifier ' + param.id);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },

    getSelected(selectedVals, option): any {
      if (selectedVals) {
        return selectedVals.find(value => option.id === value.id) ?? option;
      }
      return option;
    },
  },
});
