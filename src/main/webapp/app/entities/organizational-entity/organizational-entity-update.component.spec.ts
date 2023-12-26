/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import OrganizationalEntityUpdate from './organizational-entity-update.vue';
import OrganizationalEntityService from './organizational-entity.service';
import AlertService from '@/shared/alert/alert.service';

type OrganizationalEntityUpdateComponentType = InstanceType<typeof OrganizationalEntityUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const organizationalEntitySample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<OrganizationalEntityUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('OrganizationalEntity Management Update Component', () => {
    let comp: OrganizationalEntityUpdateComponentType;
    let organizationalEntityServiceStub: SinonStubbedInstance<OrganizationalEntityService>;

    beforeEach(() => {
      route = {};
      organizationalEntityServiceStub = sinon.createStubInstance<OrganizationalEntityService>(OrganizationalEntityService);
      organizationalEntityServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

      alertService = new AlertService({
        bvToast: {
          toast: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          'font-awesome-icon': true,
          'b-input-group': true,
          'b-input-group-prepend': true,
          'b-form-datepicker': true,
          'b-form-input': true,
        },
        provide: {
          alertService,
          organizationalEntityService: () => organizationalEntityServiceStub,
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(OrganizationalEntityUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.organizationalEntity = organizationalEntitySample;
        organizationalEntityServiceStub.update.resolves(organizationalEntitySample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(organizationalEntityServiceStub.update.calledWith(organizationalEntitySample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        organizationalEntityServiceStub.create.resolves(entity);
        const wrapper = shallowMount(OrganizationalEntityUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.organizationalEntity = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(organizationalEntityServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        organizationalEntityServiceStub.find.resolves(organizationalEntitySample);
        organizationalEntityServiceStub.retrieve.resolves([organizationalEntitySample]);

        // WHEN
        route = {
          params: {
            organizationalEntityId: '' + organizationalEntitySample.id,
          },
        };
        const wrapper = shallowMount(OrganizationalEntityUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.organizationalEntity).toMatchObject(organizationalEntitySample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        organizationalEntityServiceStub.find.resolves(organizationalEntitySample);
        const wrapper = shallowMount(OrganizationalEntityUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
