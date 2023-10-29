/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import CapabilityApplicationMappingDetails from './capability-application-mapping-details.vue';
import CapabilityApplicationMappingService from './capability-application-mapping.service';
import AlertService from '@/shared/alert/alert.service';

type CapabilityApplicationMappingDetailsComponentType = InstanceType<typeof CapabilityApplicationMappingDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const capabilityApplicationMappingSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('CapabilityApplicationMapping Management Detail Component', () => {
    let capabilityApplicationMappingServiceStub: SinonStubbedInstance<CapabilityApplicationMappingService>;
    let mountOptions: MountingOptions<CapabilityApplicationMappingDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      capabilityApplicationMappingServiceStub =
        sinon.createStubInstance<CapabilityApplicationMappingService>(CapabilityApplicationMappingService);

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
          capabilityApplicationMappingService: () => capabilityApplicationMappingServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        capabilityApplicationMappingServiceStub.find.resolves(capabilityApplicationMappingSample);
        route = {
          params: {
            capabilityApplicationMappingId: '' + 123,
          },
        };
        const wrapper = shallowMount(CapabilityApplicationMappingDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.capabilityApplicationMapping).toMatchObject(capabilityApplicationMappingSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        capabilityApplicationMappingServiceStub.find.resolves(capabilityApplicationMappingSample);
        const wrapper = shallowMount(CapabilityApplicationMappingDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
