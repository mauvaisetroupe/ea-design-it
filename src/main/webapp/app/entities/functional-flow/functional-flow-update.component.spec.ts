/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import FunctionalFlowUpdate from './functional-flow-update.vue';
import FunctionalFlowService from './functional-flow.service';
import AlertService from '@/shared/alert/alert.service';

import OwnerService from '@/entities/owner/owner.service';
import LandscapeViewService from '../landscape-view/landscape-view.service';
import ApplicationService from '../application/application.service';

type FunctionalFlowUpdateComponentType = InstanceType<typeof FunctionalFlowUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const functionalFlowSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<FunctionalFlowUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('FunctionalFlow Management Update Component', () => {
    let comp: FunctionalFlowUpdateComponentType;
    let functionalFlowServiceStub: SinonStubbedInstance<FunctionalFlowService>;

    beforeEach(() => {
      route = { query: {} };
      functionalFlowServiceStub = sinon.createStubInstance<FunctionalFlowService>(FunctionalFlowService);
      functionalFlowServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));
      functionalFlowServiceStub.retrieve.resolves(Promise.resolve([]));

      const landscapeViewServiceStub = sinon.createStubInstance<LandscapeViewService>(LandscapeViewService);
      landscapeViewServiceStub.retrieve.resolves({});

      const applicationServiceStub = sinon.createStubInstance<ApplicationService>(ApplicationService);
      applicationServiceStub.retrieve.resolves({});

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
          functionalFlowService: () => functionalFlowServiceStub,
          ownerService: () =>
            sinon.createStubInstance<OwnerService>(OwnerService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
          landscapeViewService: () => landscapeViewServiceStub,
          applicationService: () => applicationServiceStub,
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        //   // GIVEN
        //   const wrapper = shallowMount(FunctionalFlowUpdate, { global: mountOptions });
        //   comp = wrapper.vm;
        //   comp.functionalFlow = functionalFlowSample;
        //   functionalFlowServiceStub.update.resolves(functionalFlowSample);
        //   // WHEN
        //   comp.save();
        //   await comp.$nextTick();
        //   // THEN
        //   expect(functionalFlowServiceStub.update.calledWith(functionalFlowSample)).toBeTruthy();
        //   expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        //     // GIVEN
        //     const entity = {};
        //     functionalFlowServiceStub.create.resolves(entity);
        //     const wrapper = shallowMount(FunctionalFlowUpdate, { global: mountOptions });
        //     comp = wrapper.vm;
        //     comp.functionalFlow = entity;
        //     // WHEN
        //     comp.save();
        //     await comp.$nextTick();
        //     // THEN
        //     expect(functionalFlowServiceStub.create.calledWith(entity)).toBeTruthy();
        //     expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        functionalFlowServiceStub.find.resolves(functionalFlowSample);
        functionalFlowServiceStub.retrieve.resolves([functionalFlowSample]);

        // WHEN
        route = {
          params: {
            functionalFlowId: '' + functionalFlowSample.id,
          },
          query: {},
        };
        const wrapper = shallowMount(FunctionalFlowUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.functionalFlow).toMatchObject(functionalFlowSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        functionalFlowServiceStub.find.resolves(functionalFlowSample);
        const wrapper = shallowMount(FunctionalFlowUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
