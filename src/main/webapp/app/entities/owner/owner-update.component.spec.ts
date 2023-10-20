/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import OwnerUpdate from './owner-update.vue';
import OwnerService from './owner.service';
import AlertService from '@/shared/alert/alert.service';

import UserService from '@/entities/user/user.service';

type OwnerUpdateComponentType = InstanceType<typeof OwnerUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const ownerSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<OwnerUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('Owner Management Update Component', () => {
    let comp: OwnerUpdateComponentType;
    let ownerServiceStub: SinonStubbedInstance<OwnerService>;

    beforeEach(() => {
      route = {};
      ownerServiceStub = sinon.createStubInstance<OwnerService>(OwnerService);
      ownerServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          ownerService: () => ownerServiceStub,

          userService: () =>
            sinon.createStubInstance<UserService>(UserService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(OwnerUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.owner = ownerSample;
        ownerServiceStub.update.resolves(ownerSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(ownerServiceStub.update.calledWith(ownerSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        ownerServiceStub.create.resolves(entity);
        const wrapper = shallowMount(OwnerUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.owner = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(ownerServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        ownerServiceStub.find.resolves(ownerSample);
        ownerServiceStub.retrieve.resolves([ownerSample]);

        // WHEN
        route = {
          params: {
            ownerId: '' + ownerSample.id,
          },
        };
        const wrapper = shallowMount(OwnerUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.owner).toMatchObject(ownerSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        ownerServiceStub.find.resolves(ownerSample);
        const wrapper = shallowMount(OwnerUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
