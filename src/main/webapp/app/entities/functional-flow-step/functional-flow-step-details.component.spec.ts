/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import FunctionalFlowStepDetails from './functional-flow-step-details.vue';
import FunctionalFlowStepService from './functional-flow-step.service';
import AlertService from '@/shared/alert/alert.service';

type FunctionalFlowStepDetailsComponentType = InstanceType<typeof FunctionalFlowStepDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const functionalFlowStepSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('FunctionalFlowStep Management Detail Component', () => {
    let functionalFlowStepServiceStub: SinonStubbedInstance<FunctionalFlowStepService>;
    let mountOptions: MountingOptions<FunctionalFlowStepDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      functionalFlowStepServiceStub = sinon.createStubInstance<FunctionalFlowStepService>(FunctionalFlowStepService);

      alertService = new AlertService({
        bvToast: {
          toast: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          'font-awesome-icon': true,
          'router-link': true,
        },
        provide: {
          alertService,
          functionalFlowStepService: () => functionalFlowStepServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        functionalFlowStepServiceStub.find.resolves(functionalFlowStepSample);
        route = {
          params: {
            functionalFlowStepId: '' + 123,
          },
        };
        const wrapper = shallowMount(FunctionalFlowStepDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.functionalFlowStep).toMatchObject(functionalFlowStepSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        functionalFlowStepServiceStub.find.resolves(functionalFlowStepSample);
        const wrapper = shallowMount(FunctionalFlowStepDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
