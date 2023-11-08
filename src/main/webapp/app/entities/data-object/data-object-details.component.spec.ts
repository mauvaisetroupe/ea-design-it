/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import DataObjectDetails from './data-object-details.vue';
import DataObjectService from './data-object.service';
import AlertService from '@/shared/alert/alert.service';

type DataObjectDetailsComponentType = InstanceType<typeof DataObjectDetails>;
const accountService = { hasAnyAuthorityAndCheckAuth: vitest.fn().mockImplementation(() => Promise.resolve(true)) };

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const dataObjectSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('DataObject Management Detail Component', () => {
    let dataObjectServiceStub: SinonStubbedInstance<DataObjectService>;
    let mountOptions: MountingOptions<DataObjectDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      dataObjectServiceStub = sinon.createStubInstance<DataObjectService>(DataObjectService);

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
          dataObjectService: () => dataObjectServiceStub,
          accountService,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        dataObjectServiceStub.find.resolves(dataObjectSample);
        route = {
          params: {
            dataObjectId: '' + 123,
          },
        };
        const wrapper = shallowMount(DataObjectDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.dataObject).toMatchObject(dataObjectSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        dataObjectServiceStub.find.resolves(dataObjectSample);
        const wrapper = shallowMount(DataObjectDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
