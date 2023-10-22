/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import FlowGroupDetails from './flow-group-details.vue';
import FlowGroupService from './flow-group.service';
import AlertService from '@/shared/alert/alert.service';

type FlowGroupDetailsComponentType = InstanceType<typeof FlowGroupDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const flowGroupSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('FlowGroup Management Detail Component', () => {
    let flowGroupServiceStub: SinonStubbedInstance<FlowGroupService>;
    let mountOptions: MountingOptions<FlowGroupDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      flowGroupServiceStub = sinon.createStubInstance<FlowGroupService>(FlowGroupService);

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
          flowGroupService: () => flowGroupServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        flowGroupServiceStub.find.resolves(flowGroupSample);
        route = {
          params: {
            flowGroupId: '' + 123,
          },
        };
        const wrapper = shallowMount(FlowGroupDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.flowGroup).toMatchObject(flowGroupSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        flowGroupServiceStub.find.resolves(flowGroupSample);
        const wrapper = shallowMount(FlowGroupDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
