/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import FlowGroupUpdate from './flow-group-update.vue';
import FlowGroupService from './flow-group.service';
import AlertService from '@/shared/alert/alert.service';

import FunctionalFlowService from '@/entities/functional-flow/functional-flow.service';

type FlowGroupUpdateComponentType = InstanceType<typeof FlowGroupUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const flowGroupSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<FlowGroupUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('FlowGroup Management Update Component', () => {
    let comp: FlowGroupUpdateComponentType;
    let flowGroupServiceStub: SinonStubbedInstance<FlowGroupService>;

    beforeEach(() => {
      route = {};
      flowGroupServiceStub = sinon.createStubInstance<FlowGroupService>(FlowGroupService);
      flowGroupServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          flowGroupService: () => flowGroupServiceStub,
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
        const wrapper = shallowMount(FlowGroupUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.flowGroup = flowGroupSample;
        flowGroupServiceStub.update.resolves(flowGroupSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(flowGroupServiceStub.update.calledWith(flowGroupSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        flowGroupServiceStub.create.resolves(entity);
        const wrapper = shallowMount(FlowGroupUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.flowGroup = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(flowGroupServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        flowGroupServiceStub.find.resolves(flowGroupSample);
        flowGroupServiceStub.retrieve.resolves([flowGroupSample]);

        // WHEN
        route = {
          params: {
            flowGroupId: '' + flowGroupSample.id,
          },
        };
        const wrapper = shallowMount(FlowGroupUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.flowGroup).toMatchObject(flowGroupSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        flowGroupServiceStub.find.resolves(flowGroupSample);
        const wrapper = shallowMount(FlowGroupUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
