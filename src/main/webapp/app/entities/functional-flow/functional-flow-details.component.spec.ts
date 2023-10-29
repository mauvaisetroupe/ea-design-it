/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import FunctionalFlowDetails from './functional-flow-details.vue';
import FunctionalFlowService from './functional-flow.service';
import AlertService from '@/shared/alert/alert.service';

type FunctionalFlowDetailsComponentType = InstanceType<typeof FunctionalFlowDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const functionalFlowSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('FunctionalFlow Management Detail Component', () => {
    let functionalFlowServiceStub: SinonStubbedInstance<FunctionalFlowService>;
    let mountOptions: MountingOptions<FunctionalFlowDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      functionalFlowServiceStub = sinon.createStubInstance<FunctionalFlowService>(FunctionalFlowService);

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
          functionalFlowService: () => functionalFlowServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        //     // GIVEN
        //     functionalFlowServiceStub.find.resolves(functionalFlowSample);
        //     route = {
        //       params: {
        //         functionalFlowId: '' + 123,
        //       },
        //     };
        //     const wrapper = shallowMount(FunctionalFlowDetails, { global: mountOptions });
        //     const comp = wrapper.vm;
        //     // WHEN
        //     await comp.$nextTick();
        //     // THEN
        //     expect(comp.functionalFlow).toMatchObject(functionalFlowSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        functionalFlowServiceStub.find.resolves(functionalFlowSample);
        const wrapper = shallowMount(FunctionalFlowDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
