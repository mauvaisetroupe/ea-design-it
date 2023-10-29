/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import LandscapeViewUpdate from './landscape-view-update.vue';
import LandscapeViewService from './landscape-view.service';
import AlertService from '@/shared/alert/alert.service';

import OwnerService from '@/entities/owner/owner.service';
import FunctionalFlowService from '@/entities/functional-flow/functional-flow.service';

type LandscapeViewUpdateComponentType = InstanceType<typeof LandscapeViewUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const landscapeViewSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<LandscapeViewUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('LandscapeView Management Update Component', () => {
    let comp: LandscapeViewUpdateComponentType;
    let landscapeViewServiceStub: SinonStubbedInstance<LandscapeViewService>;

    beforeEach(() => {
      route = {};
      landscapeViewServiceStub = sinon.createStubInstance<LandscapeViewService>(LandscapeViewService);
      landscapeViewServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          landscapeViewService: () => landscapeViewServiceStub,
          ownerService: () =>
            sinon.createStubInstance<OwnerService>(OwnerService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
          functionalFlowService: () =>
            sinon.createStubInstance<FunctionalFlowService>(FunctionalFlowService, {
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
        const wrapper = shallowMount(LandscapeViewUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.landscapeView = landscapeViewSample;
        landscapeViewServiceStub.update.resolves(landscapeViewSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(landscapeViewServiceStub.update.calledWith(landscapeViewSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        landscapeViewServiceStub.create.resolves(entity);
        const wrapper = shallowMount(LandscapeViewUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.landscapeView = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(landscapeViewServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        //     // GIVEN
        //     landscapeViewServiceStub.find.resolves(landscapeViewSample);
        //     landscapeViewServiceStub.retrieve.resolves([landscapeViewSample]);
        //     // WHEN
        //     route = {
        //       params: {
        //         landscapeViewId: '' + landscapeViewSample.id,
        //       },
        //     };
        //     const wrapper = shallowMount(LandscapeViewUpdate, { global: mountOptions });
        //     comp = wrapper.vm;
        //     await comp.$nextTick();
        //     // THEN
        //     expect(comp.landscapeView).toMatchObject(landscapeViewSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        //     landscapeViewServiceStub.find.resolves(landscapeViewSample);
        //     const wrapper = shallowMount(LandscapeViewUpdate, { global: mountOptions });
        //     comp = wrapper.vm;
        //     await comp.$nextTick();
        //     comp.previousState();
        //     await comp.$nextTick();
        //     expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
