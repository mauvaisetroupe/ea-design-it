/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import ExternalReferenceDetails from './external-reference-details.vue';
import ExternalReferenceService from './external-reference.service';
import AlertService from '@/shared/alert/alert.service';

type ExternalReferenceDetailsComponentType = InstanceType<typeof ExternalReferenceDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const externalReferenceSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('ExternalReference Management Detail Component', () => {
    let externalReferenceServiceStub: SinonStubbedInstance<ExternalReferenceService>;
    let mountOptions: MountingOptions<ExternalReferenceDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      externalReferenceServiceStub = sinon.createStubInstance<ExternalReferenceService>(ExternalReferenceService);

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
          externalReferenceService: () => externalReferenceServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        externalReferenceServiceStub.find.resolves(externalReferenceSample);
        route = {
          params: {
            externalReferenceId: '' + 123,
          },
        };
        const wrapper = shallowMount(ExternalReferenceDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.externalReference).toMatchObject(externalReferenceSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        externalReferenceServiceStub.find.resolves(externalReferenceSample);
        const wrapper = shallowMount(ExternalReferenceDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
