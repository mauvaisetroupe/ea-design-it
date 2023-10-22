/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import ExternalSystemDetails from './external-system-details.vue';
import ExternalSystemService from './external-system.service';
import AlertService from '@/shared/alert/alert.service';

type ExternalSystemDetailsComponentType = InstanceType<typeof ExternalSystemDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const externalSystemSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('ExternalSystem Management Detail Component', () => {
    let externalSystemServiceStub: SinonStubbedInstance<ExternalSystemService>;
    let mountOptions: MountingOptions<ExternalSystemDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      externalSystemServiceStub = sinon.createStubInstance<ExternalSystemService>(ExternalSystemService);

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
          externalSystemService: () => externalSystemServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        externalSystemServiceStub.find.resolves(externalSystemSample);
        route = {
          params: {
            externalSystemId: '' + 123,
          },
        };
        const wrapper = shallowMount(ExternalSystemDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.externalSystem).toMatchObject(externalSystemSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        externalSystemServiceStub.find.resolves(externalSystemSample);
        const wrapper = shallowMount(ExternalSystemDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
