/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';

import * as config from '@/shared/config/config';
import DataFlowComponent from '@/entities/data-flow/data-flow.vue';
import DataFlowClass from '@/entities/data-flow/data-flow.component';
import DataFlowService from '@/entities/data-flow/data-flow.service';
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
  describe('DataFlow Management Component', () => {
    let wrapper: Wrapper<DataFlowClass>;
    let comp: DataFlowClass;
    let dataFlowServiceStub: SinonStubbedInstance<DataFlowService>;

    beforeEach(() => {
      dataFlowServiceStub = sinon.createStubInstance<DataFlowService>(DataFlowService);
      dataFlowServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<DataFlowClass>(DataFlowComponent, {
        store,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          dataFlowService: () => dataFlowServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      dataFlowServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllDataFlows();
      await comp.$nextTick();

      // THEN
      expect(dataFlowServiceStub.retrieve.called).toBeTruthy();
      expect(comp.dataFlows[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      dataFlowServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      comp.removeDataFlow();
      await comp.$nextTick();

      // THEN
      expect(dataFlowServiceStub.delete.called).toBeTruthy();
      expect(dataFlowServiceStub.retrieve.callCount).toEqual(1);
    });
  });
});
