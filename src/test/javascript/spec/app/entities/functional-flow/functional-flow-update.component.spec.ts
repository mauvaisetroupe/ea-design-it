/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import FunctionalFlowUpdateComponent from '@/entities/functional-flow/functional-flow-update.vue';
import FunctionalFlowClass from '@/entities/functional-flow/functional-flow-update.component';
import FunctionalFlowService from '@/entities/functional-flow/functional-flow.service';

import FunctionalFlowStepService from '@/entities/functional-flow-step/functional-flow-step.service';

import OwnerService from '@/entities/owner/owner.service';

import LandscapeViewService from '@/entities/landscape-view/landscape-view.service';

import DataFlowService from '@/entities/data-flow/data-flow.service';
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
  describe('FunctionalFlow Management Update Component', () => {
    let wrapper: Wrapper<FunctionalFlowClass>;
    let comp: FunctionalFlowClass;
    let functionalFlowServiceStub: SinonStubbedInstance<FunctionalFlowService>;

    beforeEach(() => {
      functionalFlowServiceStub = sinon.createStubInstance<FunctionalFlowService>(FunctionalFlowService);

      wrapper = shallowMount<FunctionalFlowClass>(FunctionalFlowUpdateComponent, {
        store,
        localVue,
        router,
        provide: {
          functionalFlowService: () => functionalFlowServiceStub,
          alertService: () => new AlertService(),

          functionalFlowStepService: () =>
            sinon.createStubInstance<FunctionalFlowStepService>(FunctionalFlowStepService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          ownerService: () =>
            sinon.createStubInstance<OwnerService>(OwnerService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          landscapeViewService: () =>
            sinon.createStubInstance<LandscapeViewService>(LandscapeViewService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          dataFlowService: () =>
            sinon.createStubInstance<DataFlowService>(DataFlowService, {
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
        comp.functionalFlow = entity;
        functionalFlowServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(functionalFlowServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.functionalFlow = entity;
        functionalFlowServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(functionalFlowServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundFunctionalFlow = { id: 123 };
        functionalFlowServiceStub.find.resolves(foundFunctionalFlow);
        functionalFlowServiceStub.retrieve.resolves([foundFunctionalFlow]);

        // WHEN
        comp.beforeRouteEnter({ params: { functionalFlowId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.functionalFlow).toBe(foundFunctionalFlow);
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
