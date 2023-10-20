/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import ApplicationCategoryDetails from './application-category-details.vue';
import ApplicationCategoryService from './application-category.service';
import AlertService from '@/shared/alert/alert.service';

type ApplicationCategoryDetailsComponentType = InstanceType<typeof ApplicationCategoryDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const applicationCategorySample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('ApplicationCategory Management Detail Component', () => {
    let applicationCategoryServiceStub: SinonStubbedInstance<ApplicationCategoryService>;
    let mountOptions: MountingOptions<ApplicationCategoryDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      applicationCategoryServiceStub = sinon.createStubInstance<ApplicationCategoryService>(ApplicationCategoryService);

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
          applicationCategoryService: () => applicationCategoryServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        applicationCategoryServiceStub.find.resolves(applicationCategorySample);
        route = {
          params: {
            applicationCategoryId: '' + 123,
          },
        };
        const wrapper = shallowMount(ApplicationCategoryDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.applicationCategory).toMatchObject(applicationCategorySample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        applicationCategoryServiceStub.find.resolves(applicationCategorySample);
        const wrapper = shallowMount(ApplicationCategoryDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
