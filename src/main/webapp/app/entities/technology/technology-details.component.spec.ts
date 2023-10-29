/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import TechnologyDetails from './technology-details.vue';
import TechnologyService from './technology.service';
import AlertService from '@/shared/alert/alert.service';

type TechnologyDetailsComponentType = InstanceType<typeof TechnologyDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const technologySample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('Technology Management Detail Component', () => {
    let technologyServiceStub: SinonStubbedInstance<TechnologyService>;
    let mountOptions: MountingOptions<TechnologyDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      technologyServiceStub = sinon.createStubInstance<TechnologyService>(TechnologyService);

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
          technologyService: () => technologyServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        technologyServiceStub.find.resolves(technologySample);
        route = {
          params: {
            technologyId: '' + 123,
          },
        };
        const wrapper = shallowMount(TechnologyDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.technology).toMatchObject(technologySample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        technologyServiceStub.find.resolves(technologySample);
        const wrapper = shallowMount(TechnologyDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
