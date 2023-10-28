import { computed, defineComponent, inject, ref, type Ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import LandscapeViewService from './landscape-view.service';
import useDataUtils from '@/shared/data/data-utils.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import OwnerService from '@/entities/owner/owner.service';
import { type IOwner } from '@/shared/model/owner.model';
import FunctionalFlowService from '@/entities/functional-flow/functional-flow.service';
import { type IFunctionalFlow } from '@/shared/model/functional-flow.model';
import { type ILandscapeView, LandscapeView } from '@/shared/model/landscape-view.model';
import { ViewPoint } from '@/shared/model/enumerations/view-point.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'LandscapeViewUpdate',
  setup() {
    const landscapeViewService = inject('landscapeViewService', () => new LandscapeViewService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const landscapeView: Ref<ILandscapeView> = ref(new LandscapeView());

    const ownerService = inject('ownerService', () => new OwnerService());

    const owners: Ref<IOwner[]> = ref([]);

    const functionalFlowService = inject('functionalFlowService', () => new FunctionalFlowService());

    const functionalFlows: Ref<IFunctionalFlow[]> = ref([]);
    const viewPointValues: Ref<string[]> = ref(Object.keys(ViewPoint));
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveLandscapeView = async landscapeViewId => {
      try {
        const res = await landscapeViewService().find(landscapeViewId);
        landscapeView.value = res.landscape;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.landscapeViewId) {
      retrieveLandscapeView(route.params.landscapeViewId);
    }

    const initRelationships = () => {
      ownerService()
        .retrieve()
        .then(res => {
          owners.value = res.data;
        });
    };

    initRelationships();

    const dataUtils = useDataUtils();

    const validations = useValidation();
    const validationRules = {
      viewpoint: {},
      diagramName: {},
      compressedDrawXML: {},
      compressedDrawSVG: {},
      owner: {},
      flows: {},
      capabilityApplicationMappings: {},
    };
    const v$ = useVuelidate(validationRules, landscapeView as any);
    v$.value.$validate();

    return {
      landscapeViewService,
      alertService,
      landscapeView,
      previousState,
      viewPointValues,
      isSaving,
      currentLanguage,
      owners,
      functionalFlows,
      ...dataUtils,
      v$,
    };
  },
  created(): void {
    this.landscapeView.flows = [];
  },
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.landscapeView.id) {
        this.landscapeViewService()
          .update(this.landscapeView)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo('A LandscapeView is updated with identifier ' + param.id);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.landscapeViewService()
          .create(this.landscapeView)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess('A LandscapeView is created with identifier ' + param.id);
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
