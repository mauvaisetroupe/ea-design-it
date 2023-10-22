import { computed, defineComponent, inject, ref, type Ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import FlowInterfaceService from './flow-interface.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import ApplicationService from '@/entities/application/application.service';
import { type IApplication } from '@/shared/model/application.model';
import ApplicationComponentService from '@/entities/application-component/application-component.service';
import { type IApplicationComponent } from '@/shared/model/application-component.model';
import ProtocolService from '@/entities/protocol/protocol.service';
import { type IProtocol } from '@/shared/model/protocol.model';
import OwnerService from '@/entities/owner/owner.service';
import { type IOwner } from '@/shared/model/owner.model';
import { type IFlowInterface, FlowInterface } from '@/shared/model/flow-interface.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'FlowInterfaceUpdate',
  setup() {
    const flowInterfaceService = inject('flowInterfaceService', () => new FlowInterfaceService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const flowInterface: Ref<IFlowInterface> = ref(new FlowInterface());

    const applicationService = inject('applicationService', () => new ApplicationService());

    const applications: Ref<IApplication[]> = ref([]);

    const applicationComponentService = inject('applicationComponentService', () => new ApplicationComponentService());

    const applicationComponents: Ref<IApplicationComponent[]> = ref([]);

    const protocolService = inject('protocolService', () => new ProtocolService());

    const protocols: Ref<IProtocol[]> = ref([]);

    const ownerService = inject('ownerService', () => new OwnerService());

    const owners: Ref<IOwner[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveFlowInterface = async flowInterfaceId => {
      try {
        const res = await flowInterfaceService().find(flowInterfaceId);
        flowInterface.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.flowInterfaceId) {
      retrieveFlowInterface(route.params.flowInterfaceId);
    }

    const initRelationships = () => {
      applicationService()
        .retrieve()
        .then(res => {
          applications.value = res.data;
        });
      applicationComponentService()
        .retrieve()
        .then(res => {
          applicationComponents.value = res.data;
        });
      protocolService()
        .retrieve()
        .then(res => {
          protocols.value = res.data;
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
      alias: {
        required: validations.required('This field is required.'),
      },
      status: {},
      documentationURL: {
        maxLength: validations.maxLength('This field cannot be longer than 500 characters.', 500),
      },
      documentationURL2: {
        maxLength: validations.maxLength('This field cannot be longer than 500 characters.', 500),
      },
      description: {
        maxLength: validations.maxLength('This field cannot be longer than 1500 characters.', 1500),
      },
      startDate: {},
      endDate: {},
      dataFlows: {},
      source: {
        required: validations.required('This field is required.'),
      },
      target: {
        required: validations.required('This field is required.'),
      },
      sourceComponent: {},
      targetComponent: {},
      protocol: {},
      owner: {},
      steps: {},
    };
    const v$ = useVuelidate(validationRules, flowInterface as any);
    v$.value.$validate();

    return {
      flowInterfaceService,
      alertService,
      flowInterface,
      previousState,
      isSaving,
      currentLanguage,
      applications,
      applicationComponents,
      protocols,
      owners,
      v$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.flowInterface.id) {
        this.flowInterfaceService()
          .update(this.flowInterface)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo('A FlowInterface is updated with identifier ' + param.id);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.flowInterfaceService()
          .create(this.flowInterface)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess('A FlowInterface is created with identifier ' + param.id);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
