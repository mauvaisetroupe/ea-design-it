/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import ApplicationDetails from './application-details.vue';
import ApplicationService from './application.service';
import AlertService from '@/shared/alert/alert.service';

type ApplicationDetailsComponentType = InstanceType<typeof ApplicationDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const applicationSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('Application Management Detail Component', () => {
    let applicationServiceStub: SinonStubbedInstance<ApplicationService>;
    let mountOptions: MountingOptions<ApplicationDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      applicationServiceStub = sinon.createStubInstance<ApplicationService>(ApplicationService);

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
          applicationService: () => applicationServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        applicationServiceStub.find.resolves(applicationSample);
        route = {
          params: {
            applicationId: '' + 123,
          },
        };
        const wrapper = shallowMount(ApplicationDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.application).toMatchObject(applicationSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        applicationServiceStub.find.resolves(applicationSample);
        const wrapper = shallowMount(ApplicationDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
