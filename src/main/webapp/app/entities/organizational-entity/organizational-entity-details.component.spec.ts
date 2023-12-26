/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import OrganizationalEntityDetails from './organizational-entity-details.vue';
import OrganizationalEntityService from './organizational-entity.service';
import AlertService from '@/shared/alert/alert.service';

type OrganizationalEntityDetailsComponentType = InstanceType<typeof OrganizationalEntityDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const organizationalEntitySample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('OrganizationalEntity Management Detail Component', () => {
    let organizationalEntityServiceStub: SinonStubbedInstance<OrganizationalEntityService>;
    let mountOptions: MountingOptions<OrganizationalEntityDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      organizationalEntityServiceStub = sinon.createStubInstance<OrganizationalEntityService>(OrganizationalEntityService);

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
          organizationalEntityService: () => organizationalEntityServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        organizationalEntityServiceStub.find.resolves(organizationalEntitySample);
        route = {
          params: {
            organizationalEntityId: '' + 123,
          },
        };
        const wrapper = shallowMount(OrganizationalEntityDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.organizationalEntity).toMatchObject(organizationalEntitySample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        organizationalEntityServiceStub.find.resolves(organizationalEntitySample);
        const wrapper = shallowMount(OrganizationalEntityDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
