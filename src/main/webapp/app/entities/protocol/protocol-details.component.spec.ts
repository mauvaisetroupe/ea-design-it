/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import ProtocolDetails from './protocol-details.vue';
import ProtocolService from './protocol.service';
import AlertService from '@/shared/alert/alert.service';

type ProtocolDetailsComponentType = InstanceType<typeof ProtocolDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const protocolSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('Protocol Management Detail Component', () => {
    let protocolServiceStub: SinonStubbedInstance<ProtocolService>;
    let mountOptions: MountingOptions<ProtocolDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      protocolServiceStub = sinon.createStubInstance<ProtocolService>(ProtocolService);

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
          protocolService: () => protocolServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        protocolServiceStub.find.resolves(protocolSample);
        route = {
          params: {
            protocolId: '' + 123,
          },
        };
        const wrapper = shallowMount(ProtocolDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.protocol).toMatchObject(protocolSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        protocolServiceStub.find.resolves(protocolSample);
        const wrapper = shallowMount(ProtocolDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
