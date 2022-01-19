/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';

import * as config from '@/shared/config/config';
import FunctionalFlowStepUpdateComponent from '@/entities/functional-flow-step/functional-flow-step-update.vue';
import FunctionalFlowStepClass from '@/entities/functional-flow-step/functional-flow-step-update.component';
import FunctionalFlowStepService from '@/entities/functional-flow-step/functional-flow-step.service';

import FlowInterfaceService from '@/entities/flow-interface/flow-interface.service';

import FunctionalFlowService from '@/entities/functional-flow/functional-flow.service';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
const router = new Router();
localVue.use(Router);
localVue.component('font-awesome-icon', {});
localVue.component('b-input-group', {});
localVue.component('b-input-group-prepend', {});
localVue.component('b-form-datepicker', {});
localVue.component('b-form-input', {});

describe('Component Tests', () => {
  describe('FunctionalFlowStep Management Update Component', () => {
    let wrapper: Wrapper<FunctionalFlowStepClass>;
    let comp: FunctionalFlowStepClass;
    let functionalFlowStepServiceStub: SinonStubbedInstance<FunctionalFlowStepService>;

    beforeEach(() => {
      functionalFlowStepServiceStub = sinon.createStubInstance<FunctionalFlowStepService>(FunctionalFlowStepService);

      wrapper = shallowMount<FunctionalFlowStepClass>(FunctionalFlowStepUpdateComponent, {
        store,
        localVue,
        router,
        provide: {
          functionalFlowStepService: () => functionalFlowStepServiceStub,
          alertService: () => new AlertService(),

          flowInterfaceService: () => new FlowInterfaceService(),

          functionalFlowService: () => new FunctionalFlowService(),
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.functionalFlowStep = entity;
        functionalFlowStepServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(functionalFlowStepServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.functionalFlowStep = entity;
        functionalFlowStepServiceStub.create.resolves(entity);

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
        const foundFunctionalFlowStep = { id: 123 };
        functionalFlowStepServiceStub.find.resolves(foundFunctionalFlowStep);
        functionalFlowStepServiceStub.retrieve.resolves([foundFunctionalFlowStep]);

        // WHEN
        comp.beforeRouteEnter({ params: { functionalFlowStepId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.functionalFlowStep).toBe(foundFunctionalFlowStep);
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
