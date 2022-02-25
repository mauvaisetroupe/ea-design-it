/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import FlowInterfaceUpdateComponent from '@/entities/flow-interface/flow-interface-update.vue';
import FlowInterfaceClass from '@/entities/flow-interface/flow-interface-update.component';
import FlowInterfaceService from '@/entities/flow-interface/flow-interface.service';

import DataFlowService from '@/entities/data-flow/data-flow.service';

import ApplicationService from '@/entities/application/application.service';

import ApplicationComponentService from '@/entities/application-component/application-component.service';

import ProtocolService from '@/entities/protocol/protocol.service';

import OwnerService from '@/entities/owner/owner.service';

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
  describe('FlowInterface Management Update Component', () => {
    let wrapper: Wrapper<FlowInterfaceClass>;
    let comp: FlowInterfaceClass;
    let flowInterfaceServiceStub: SinonStubbedInstance<FlowInterfaceService>;

    beforeEach(() => {
      flowInterfaceServiceStub = sinon.createStubInstance<FlowInterfaceService>(FlowInterfaceService);

      wrapper = shallowMount<FlowInterfaceClass>(FlowInterfaceUpdateComponent, {
        store,
        localVue,
        router,
        provide: {
          flowInterfaceService: () => flowInterfaceServiceStub,
          alertService: () => new AlertService(),

          dataFlowService: () =>
            sinon.createStubInstance<DataFlowService>(DataFlowService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          applicationService: () =>
            sinon.createStubInstance<ApplicationService>(ApplicationService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          applicationComponentService: () =>
            sinon.createStubInstance<ApplicationComponentService>(ApplicationComponentService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          protocolService: () =>
            sinon.createStubInstance<ProtocolService>(ProtocolService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          ownerService: () =>
            sinon.createStubInstance<OwnerService>(OwnerService, {
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
        comp.flowInterface = entity;
        flowInterfaceServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(flowInterfaceServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.flowInterface = entity;
        flowInterfaceServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(flowInterfaceServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundFlowInterface = { id: 123 };
        flowInterfaceServiceStub.find.resolves(foundFlowInterface);
        flowInterfaceServiceStub.retrieve.resolves([foundFlowInterface]);

        // WHEN
        comp.beforeRouteEnter({ params: { flowInterfaceId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.flowInterface).toBe(foundFlowInterface);
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
