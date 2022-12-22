/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import FlowGroupUpdateComponent from '@/entities/flow-group/flow-group-update.vue';
import FlowGroupClass from '@/entities/flow-group/flow-group-update.component';
import FlowGroupService from '@/entities/flow-group/flow-group.service';

import FunctionalFlowService from '@/entities/functional-flow/functional-flow.service';

import FunctionalFlowStepService from '@/entities/functional-flow-step/functional-flow-step.service';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
const router = new Router();
localVue.use(Router);
localVue.use(ToastPlugin);
localVue.component('font-awesome-icon', {});
localVue.component('b-input-group', {});
localVue.component('b-input-group-prepend', {});
localVue.component('b-form-datepicker', {});
localVue.component('b-form-input', {});

describe('Component Tests', () => {
  describe('FlowGroup Management Update Component', () => {
    let wrapper: Wrapper<FlowGroupClass>;
    let comp: FlowGroupClass;
    let flowGroupServiceStub: SinonStubbedInstance<FlowGroupService>;

    beforeEach(() => {
      flowGroupServiceStub = sinon.createStubInstance<FlowGroupService>(FlowGroupService);

      wrapper = shallowMount<FlowGroupClass>(FlowGroupUpdateComponent, {
        store,
        localVue,
        router,
        provide: {
          flowGroupService: () => flowGroupServiceStub,
          alertService: () => new AlertService(),

          functionalFlowService: () =>
            sinon.createStubInstance<FunctionalFlowService>(FunctionalFlowService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          functionalFlowStepService: () =>
            sinon.createStubInstance<FunctionalFlowStepService>(FunctionalFlowStepService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.flowGroup = entity;
        flowGroupServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(flowGroupServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.flowGroup = entity;
        flowGroupServiceStub.create.resolves(entity);

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
        const foundFlowGroup = { id: 123 };
        flowGroupServiceStub.find.resolves(foundFlowGroup);
        flowGroupServiceStub.retrieve.resolves([foundFlowGroup]);

        // WHEN
        comp.beforeRouteEnter({ params: { flowGroupId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.flowGroup).toBe(foundFlowGroup);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        comp.previousState();
        await comp.$nextTick();

        expect(comp.$router.currentRoute.fullPath).toContain('/');
      });
    });
  });
});
