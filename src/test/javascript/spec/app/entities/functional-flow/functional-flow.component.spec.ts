/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';

import * as config from '@/shared/config/config';
import FunctionalFlowComponent from '@/entities/functional-flow/functional-flow.vue';
import FunctionalFlowClass from '@/entities/functional-flow/functional-flow.component';
import FunctionalFlowService from '@/entities/functional-flow/functional-flow.service';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();

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
  describe('FunctionalFlow Management Component', () => {
    let wrapper: Wrapper<FunctionalFlowClass>;
    let comp: FunctionalFlowClass;
    let functionalFlowServiceStub: SinonStubbedInstance<FunctionalFlowService>;

    beforeEach(() => {
      functionalFlowServiceStub = sinon.createStubInstance<FunctionalFlowService>(FunctionalFlowService);
      functionalFlowServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<FunctionalFlowClass>(FunctionalFlowComponent, {
        store,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          functionalFlowService: () => functionalFlowServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      functionalFlowServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllFunctionalFlows();
      await comp.$nextTick();

      // THEN
      expect(functionalFlowServiceStub.retrieve.called).toBeTruthy();
      expect(comp.functionalFlows[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      functionalFlowServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      comp.removeFunctionalFlow();
      await comp.$nextTick();

      // THEN
      expect(functionalFlowServiceStub.delete.called).toBeTruthy();
      expect(functionalFlowServiceStub.retrieve.callCount).toEqual(1);
    });
  });
});
