/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import LandscapeViewDetails from './landscape-view-details.vue';
import LandscapeViewService from './landscape-view.service';
import AlertService from '@/shared/alert/alert.service';

type LandscapeViewDetailsComponentType = InstanceType<typeof LandscapeViewDetails>;
const accountService = { hasAnyAuthorityAndCheckAuth: vitest.fn().mockImplementation(() => Promise.resolve(true)) };

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const landscapeViewSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('LandscapeView Management Detail Component', () => {
    let landscapeViewServiceStub: SinonStubbedInstance<LandscapeViewService>;
    let mountOptions: MountingOptions<LandscapeViewDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      landscapeViewServiceStub = sinon.createStubInstance<LandscapeViewService>(LandscapeViewService);

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
          landscapeViewService: () => landscapeViewServiceStub,
          accountService,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // // GIVEN
        // landscapeViewServiceStub.find.resolves(landscapeViewSample);
        // route = {
        //   params: {
        //     landscapeViewId: '' + 123,
        //   },
        // };
        // const wrapper = shallowMount(LandscapeViewDetails, { global: mountOptions });
        // const comp = wrapper.vm;
        // // WHEN
        // await comp.$nextTick();
        // // THEN
        // expect(comp.landscapeView).toMatchObject(landscapeViewSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        landscapeViewServiceStub.find.resolves(landscapeViewSample);
        const wrapper = shallowMount(LandscapeViewDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
