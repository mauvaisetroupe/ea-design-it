/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import DataFlowDetails from './data-flow-details.vue';
import DataFlowService from './data-flow.service';
import AlertService from '@/shared/alert/alert.service';

type DataFlowDetailsComponentType = InstanceType<typeof DataFlowDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const dataFlowSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('DataFlow Management Detail Component', () => {
    let dataFlowServiceStub: SinonStubbedInstance<DataFlowService>;
    let mountOptions: MountingOptions<DataFlowDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      dataFlowServiceStub = sinon.createStubInstance<DataFlowService>(DataFlowService);

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
          dataFlowService: () => dataFlowServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        dataFlowServiceStub.find.resolves(dataFlowSample);
        route = {
          params: {
            dataFlowId: '' + 123,
          },
        };
        const wrapper = shallowMount(DataFlowDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.dataFlow).toMatchObject(dataFlowSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        dataFlowServiceStub.find.resolves(dataFlowSample);
        const wrapper = shallowMount(DataFlowDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
