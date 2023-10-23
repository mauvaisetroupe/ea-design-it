import { computed, defineComponent, inject, ref, type Ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import CapabilityApplicationMappingService from './capability-application-mapping.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import CapabilityService from '@/entities/capability/capability.service';
import { type ICapability } from '@/shared/model/capability.model';
import ApplicationService from '@/entities/application/application.service';
import { type IApplication } from '@/shared/model/application.model';
import LandscapeViewService from '@/entities/landscape-view/landscape-view.service';
import { type ILandscapeView } from '@/shared/model/landscape-view.model';
import { type ICapabilityApplicationMapping, type CapabilityApplicationMapping } from '@/shared/model/capability-application-mapping.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'CapabilityApplicationMappingUpdate',
  setup() {
    const capabilityApplicationMappingService = inject(
      'capabilityApplicationMappingService',
      () => new CapabilityApplicationMappingService(),
    );
    const alertService = inject('alertService', () => useAlertService(), true);

    const capabilityApplicationMapping: Ref<ICapabilityApplicationMapping> = ref(new CapabilityApplicationMapping());

    const capabilityService = inject('capabilityService', () => new CapabilityService());

    const capabilities: Ref<ICapability[]> = ref([]);

    const applicationService = inject('applicationService', () => new ApplicationService());

    const applications: Ref<IApplication[]> = ref([]);

    const landscapeViewService = inject('landscapeViewService', () => new LandscapeViewService());

    const landscapeViews: Ref<ILandscapeView[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveCapabilityApplicationMapping = async capabilityApplicationMappingId => {
      try {
        const res = await capabilityApplicationMappingService().find(capabilityApplicationMappingId);
        capabilityApplicationMapping.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.capabilityApplicationMappingId) {
      retrieveCapabilityApplicationMapping(route.params.capabilityApplicationMappingId);
    }

    const initRelationships = () => {
      capabilityService()
        .retrieve()
        .then(res => {
          capabilities.value = res.data;
        });
      applicationService()
        .retrieve()
        .then(res => {
          applications.value = res.data;
        });
      landscapeViewService()
        .retrieve()
        .then(res => {
          landscapeViews.value = res.data;
        });
    };

    initRelationships();

    const validations = useValidation();
    const validationRules = {
      capability: {},
      application: {},
      landscapes: {},
    };
    const v$ = useVuelidate(validationRules, capabilityApplicationMapping as any);
    v$.value.$validate();

    return {
      capabilityApplicationMappingService,
      alertService,
      capabilityApplicationMapping,
      previousState,
      isSaving,
      currentLanguage,
      capabilities,
      applications,
      landscapeViews,
      v$,
    };
  },
  created(): void {
    this.capabilityApplicationMapping.landscapes = [];
  },
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.capabilityApplicationMapping.id) {
        this.capabilityApplicationMappingService()
          .update(this.capabilityApplicationMapping)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo('A CapabilityApplicationMapping is updated with identifier ' + param.id);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.capabilityApplicationMappingService()
          .create(this.capabilityApplicationMapping)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess('A CapabilityApplicationMapping is created with identifier ' + param.id);
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
