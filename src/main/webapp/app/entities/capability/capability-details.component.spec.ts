/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import CapabilityDetails from './capability-details.vue';
import CapabilityService from './capability.service';
import AlertService from '@/shared/alert/alert.service';

type CapabilityDetailsComponentType = InstanceType<typeof CapabilityDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const capabilitySample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('Capability Management Detail Component', () => {
    let capabilityServiceStub: SinonStubbedInstance<CapabilityService>;
    let mountOptions: MountingOptions<CapabilityDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      capabilityServiceStub = sinon.createStubInstance<CapabilityService>(CapabilityService);

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
          capabilityService: () => capabilityServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        capabilityServiceStub.find.resolves(capabilitySample);
        route = {
          params: {
            capabilityId: '' + 123,
          },
        };
        const wrapper = shallowMount(CapabilityDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.capability).toMatchObject(capabilitySample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        capabilityServiceStub.find.resolves(capabilitySample);
        const wrapper = shallowMount(CapabilityDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
