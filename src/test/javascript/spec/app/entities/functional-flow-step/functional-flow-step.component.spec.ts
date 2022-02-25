/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import FunctionalFlowStepComponent from '@/entities/functional-flow-step/functional-flow-step.vue';
import FunctionalFlowStepClass from '@/entities/functional-flow-step/functional-flow-step.component';
import FunctionalFlowStepService from '@/entities/functional-flow-step/functional-flow-step.service';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(ToastPlugin);

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('b-badge', {});
localVue.directive('b-modal', {});
localVue.component('b-button', {});
localVue.component('router-link', {});

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  describe('FunctionalFlowStep Management Component', () => {
    let wrapper: Wrapper<FunctionalFlowStepClass>;
    let comp: FunctionalFlowStepClass;
    let functionalFlowStepServiceStub: SinonStubbedInstance<FunctionalFlowStepService>;

    beforeEach(() => {
      functionalFlowStepServiceStub = sinon.createStubInstance<FunctionalFlowStepService>(FunctionalFlowStepService);
      functionalFlowStepServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<FunctionalFlowStepClass>(FunctionalFlowStepComponent, {
        store,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          functionalFlowStepService: () => functionalFlowStepServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      functionalFlowStepServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllFunctionalFlowSteps();
      await comp.$nextTick();

      // THEN
      expect(functionalFlowStepServiceStub.retrieve.called).toBeTruthy();
      expect(comp.functionalFlowSteps[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      functionalFlowStepServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      expect(functionalFlowStepServiceStub.retrieve.callCount).toEqual(1);

      comp.removeFunctionalFlowStep();
      await comp.$nextTick();

      // THEN
      expect(functionalFlowStepServiceStub.delete.called).toBeTruthy();
      expect(functionalFlowStepServiceStub.retrieve.callCount).toEqual(2);
    });
  });
});
