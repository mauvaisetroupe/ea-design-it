/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import FlowInterfaceDetails from './flow-interface-details.vue';
import FlowInterfaceService from './flow-interface.service';
import AlertService from '@/shared/alert/alert.service';

type FlowInterfaceDetailsComponentType = InstanceType<typeof FlowInterfaceDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const flowInterfaceSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('FlowInterface Management Detail Component', () => {
    let flowInterfaceServiceStub: SinonStubbedInstance<FlowInterfaceService>;
    let mountOptions: MountingOptions<FlowInterfaceDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      flowInterfaceServiceStub = sinon.createStubInstance<FlowInterfaceService>(FlowInterfaceService);

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
          flowInterfaceService: () => flowInterfaceServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        flowInterfaceServiceStub.find.resolves(flowInterfaceSample);
        route = {
          params: {
            flowInterfaceId: '' + 123,
          },
        };
        const wrapper = shallowMount(FlowInterfaceDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.flowInterface).toMatchObject(flowInterfaceSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        flowInterfaceServiceStub.find.resolves(flowInterfaceSample);
        const wrapper = shallowMount(FlowInterfaceDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
