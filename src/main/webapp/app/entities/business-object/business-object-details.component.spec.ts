/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import BusinessObjectDetails from './business-object-details.vue';
import BusinessObjectService from './business-object.service';
import AlertService from '@/shared/alert/alert.service';

type BusinessObjectDetailsComponentType = InstanceType<typeof BusinessObjectDetails>;
const accountService = { hasAnyAuthorityAndCheckAuth: vitest.fn().mockImplementation(() => Promise.resolve(true)) };

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const businessObjectSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('BusinessObject Management Detail Component', () => {
    let businessObjectServiceStub: SinonStubbedInstance<BusinessObjectService>;
    let mountOptions: MountingOptions<BusinessObjectDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      businessObjectServiceStub = sinon.createStubInstance<BusinessObjectService>(BusinessObjectService);

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
          businessObjectService: () => businessObjectServiceStub,
          accountService,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        businessObjectServiceStub.find.resolves(businessObjectSample);
        route = {
          params: {
            businessObjectId: '' + 123,
          },
        };
        const wrapper = shallowMount(BusinessObjectDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.businessObject).toMatchObject(businessObjectSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        businessObjectServiceStub.find.resolves(businessObjectSample);
        const wrapper = shallowMount(BusinessObjectDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
