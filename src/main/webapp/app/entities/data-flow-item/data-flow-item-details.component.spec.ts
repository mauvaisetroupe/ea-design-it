/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import DataFlowItemDetails from './data-flow-item-details.vue';
import DataFlowItemService from './data-flow-item.service';
import AlertService from '@/shared/alert/alert.service';

type DataFlowItemDetailsComponentType = InstanceType<typeof DataFlowItemDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const dataFlowItemSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('DataFlowItem Management Detail Component', () => {
    let dataFlowItemServiceStub: SinonStubbedInstance<DataFlowItemService>;
    let mountOptions: MountingOptions<DataFlowItemDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      dataFlowItemServiceStub = sinon.createStubInstance<DataFlowItemService>(DataFlowItemService);

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
          dataFlowItemService: () => dataFlowItemServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        dataFlowItemServiceStub.find.resolves(dataFlowItemSample);
        route = {
          params: {
            dataFlowItemId: '' + 123,
          },
        };
        const wrapper = shallowMount(DataFlowItemDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.dataFlowItem).toMatchObject(dataFlowItemSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        dataFlowItemServiceStub.find.resolves(dataFlowItemSample);
        const wrapper = shallowMount(DataFlowItemDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
