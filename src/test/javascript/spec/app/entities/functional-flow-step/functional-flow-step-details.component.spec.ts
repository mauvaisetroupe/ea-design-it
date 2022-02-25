/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import FunctionalFlowStepDetailComponent from '@/entities/functional-flow-step/functional-flow-step-details.vue';
import FunctionalFlowStepClass from '@/entities/functional-flow-step/functional-flow-step-details.component';
import FunctionalFlowStepService from '@/entities/functional-flow-step/functional-flow-step.service';
import router from '@/router';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('FunctionalFlowStep Management Detail Component', () => {
    let wrapper: Wrapper<FunctionalFlowStepClass>;
    let comp: FunctionalFlowStepClass;
    let functionalFlowStepServiceStub: SinonStubbedInstance<FunctionalFlowStepService>;

    beforeEach(() => {
      functionalFlowStepServiceStub = sinon.createStubInstance<FunctionalFlowStepService>(FunctionalFlowStepService);

      wrapper = shallowMount<FunctionalFlowStepClass>(FunctionalFlowStepDetailComponent, {
        store,
        localVue,
        router,
        provide: { functionalFlowStepService: () => functionalFlowStepServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundFunctionalFlowStep = { id: 123 };
        functionalFlowStepServiceStub.find.resolves(foundFunctionalFlowStep);

        // WHEN
        comp.retrieveFunctionalFlowStep(123);
        await comp.$nextTick();

        // THEN
        expect(comp.functionalFlowStep).toBe(foundFunctionalFlowStep);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundFunctionalFlowStep = { id: 123 };
        functionalFlowStepServiceStub.find.resolves(foundFunctionalFlowStep);

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
