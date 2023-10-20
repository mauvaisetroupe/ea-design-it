/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import DataFormatDetails from './data-format-details.vue';
import DataFormatService from './data-format.service';
import AlertService from '@/shared/alert/alert.service';

type DataFormatDetailsComponentType = InstanceType<typeof DataFormatDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const dataFormatSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('DataFormat Management Detail Component', () => {
    let dataFormatServiceStub: SinonStubbedInstance<DataFormatService>;
    let mountOptions: MountingOptions<DataFormatDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      dataFormatServiceStub = sinon.createStubInstance<DataFormatService>(DataFormatService);

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
          dataFormatService: () => dataFormatServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        dataFormatServiceStub.find.resolves(dataFormatSample);
        route = {
          params: {
            dataFormatId: '' + 123,
          },
        };
        const wrapper = shallowMount(DataFormatDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.dataFormat).toMatchObject(dataFormatSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        dataFormatServiceStub.find.resolves(dataFormatSample);
        const wrapper = shallowMount(DataFormatDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
