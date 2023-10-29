/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import FunctionalFlowStepUpdate from './functional-flow-step-update.vue';
import FunctionalFlowStepService from './functional-flow-step.service';
import AlertService from '@/shared/alert/alert.service';

import FlowInterfaceService from '@/entities/flow-interface/flow-interface.service';
import FlowGroupService from '@/entities/flow-group/flow-group.service';
import FunctionalFlowService from '@/entities/functional-flow/functional-flow.service';

type FunctionalFlowStepUpdateComponentType = InstanceType<typeof FunctionalFlowStepUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const functionalFlowStepSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<FunctionalFlowStepUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('FunctionalFlowStep Management Update Component', () => {
    let comp: FunctionalFlowStepUpdateComponentType;
    let functionalFlowStepServiceStub: SinonStubbedInstance<FunctionalFlowStepService>;

    beforeEach(() => {
      route = {};
      functionalFlowStepServiceStub = sinon.createStubInstance<FunctionalFlowStepService>(FunctionalFlowStepService);
      functionalFlowStepServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          functionalFlowStepService: () => functionalFlowStepServiceStub,
          flowInterfaceService: () =>
            sinon.createStubInstance<FlowInterfaceService>(FlowInterfaceService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
          flowGroupService: () =>
            sinon.createStubInstance<FlowGroupService>(FlowGroupService, {
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
        const wrapper = shallowMount(FunctionalFlowStepUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.functionalFlowStep = functionalFlowStepSample;
        functionalFlowStepServiceStub.update.resolves(functionalFlowStepSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(functionalFlowStepServiceStub.update.calledWith(functionalFlowStepSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        functionalFlowStepServiceStub.create.resolves(entity);
        const wrapper = shallowMount(FunctionalFlowStepUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.functionalFlowStep = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(functionalFlowStepServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        functionalFlowStepServiceStub.find.resolves(functionalFlowStepSample);
        functionalFlowStepServiceStub.retrieve.resolves([functionalFlowStepSample]);

        // WHEN
        route = {
          params: {
            functionalFlowStepId: '' + functionalFlowStepSample.id,
          },
        };
        const wrapper = shallowMount(FunctionalFlowStepUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.functionalFlowStep).toMatchObject(functionalFlowStepSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        functionalFlowStepServiceStub.find.resolves(functionalFlowStepSample);
        const wrapper = shallowMount(FunctionalFlowStepUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
