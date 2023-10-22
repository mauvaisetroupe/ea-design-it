/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import ApplicationComponentDetails from './application-component-details.vue';
import ApplicationComponentService from './application-component.service';
import AlertService from '@/shared/alert/alert.service';

type ApplicationComponentDetailsComponentType = InstanceType<typeof ApplicationComponentDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const applicationComponentSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('ApplicationComponent Management Detail Component', () => {
    let applicationComponentServiceStub: SinonStubbedInstance<ApplicationComponentService>;
    let mountOptions: MountingOptions<ApplicationComponentDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      applicationComponentServiceStub = sinon.createStubInstance<ApplicationComponentService>(ApplicationComponentService);

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
          applicationComponentService: () => applicationComponentServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        applicationComponentServiceStub.find.resolves(applicationComponentSample);
        route = {
          params: {
            applicationComponentId: '' + 123,
          },
        };
        const wrapper = shallowMount(ApplicationComponentDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.applicationComponent).toMatchObject(applicationComponentSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        applicationComponentServiceStub.find.resolves(applicationComponentSample);
        const wrapper = shallowMount(ApplicationComponentDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
