/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import OwnerDetails from './owner-details.vue';
import OwnerService from './owner.service';
import AlertService from '@/shared/alert/alert.service';

type OwnerDetailsComponentType = InstanceType<typeof OwnerDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const ownerSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('Owner Management Detail Component', () => {
    let ownerServiceStub: SinonStubbedInstance<OwnerService>;
    let mountOptions: MountingOptions<OwnerDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      ownerServiceStub = sinon.createStubInstance<OwnerService>(OwnerService);

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
          ownerService: () => ownerServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        ownerServiceStub.find.resolves(ownerSample);
        route = {
          params: {
            ownerId: '' + 123,
          },
        };
        const wrapper = shallowMount(OwnerDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.owner).toMatchObject(ownerSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        ownerServiceStub.find.resolves(ownerSample);
        const wrapper = shallowMount(OwnerDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
