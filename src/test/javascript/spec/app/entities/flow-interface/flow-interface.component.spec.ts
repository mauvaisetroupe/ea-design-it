/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';

import * as config from '@/shared/config/config';
import FlowInterfaceComponent from '@/entities/flow-interface/flow-interface.vue';
import FlowInterfaceClass from '@/entities/flow-interface/flow-interface.component';
import FlowInterfaceService from '@/entities/flow-interface/flow-interface.service';
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
  describe('FlowInterface Management Component', () => {
    let wrapper: Wrapper<FlowInterfaceClass>;
    let comp: FlowInterfaceClass;
    let flowInterfaceServiceStub: SinonStubbedInstance<FlowInterfaceService>;

    beforeEach(() => {
      flowInterfaceServiceStub = sinon.createStubInstance<FlowInterfaceService>(FlowInterfaceService);
      flowInterfaceServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<FlowInterfaceClass>(FlowInterfaceComponent, {
        store,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          flowInterfaceService: () => flowInterfaceServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      flowInterfaceServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllFlowInterfaces();
      await comp.$nextTick();

      // THEN
      expect(flowInterfaceServiceStub.retrieve.called).toBeTruthy();
      expect(comp.flowInterfaces[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      flowInterfaceServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      comp.removeFlowInterface();
      await comp.$nextTick();

      // THEN
      expect(flowInterfaceServiceStub.delete.called).toBeTruthy();
      expect(flowInterfaceServiceStub.retrieve.callCount).toEqual(1);
    });
  });
});
