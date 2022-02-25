/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';

import * as config from '@/shared/config/config';
import FlowImportComponent from '@/entities/flow-import/flow-import.vue';
import FlowImportClass from '@/entities/flow-import/flow-import.component';
import FlowImportService from '@/entities/flow-import/flow-import.service';
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
  describe('FlowImport Management Component', () => {
    let wrapper: Wrapper<FlowImportClass>;
    let comp: FlowImportClass;
    let flowImportServiceStub: SinonStubbedInstance<FlowImportService>;

    beforeEach(() => {
      flowImportServiceStub = sinon.createStubInstance<FlowImportService>(FlowImportService);
      flowImportServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<FlowImportClass>(FlowImportComponent, {
        store,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          flowImportService: () => flowImportServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      flowImportServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllFlowImports();
      await comp.$nextTick();

      // THEN
      expect(flowImportServiceStub.retrieve.called).toBeTruthy();
      expect(comp.flowImports[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      flowImportServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      comp.removeFlowImport();
      await comp.$nextTick();

      // THEN
      expect(flowImportServiceStub.delete.called).toBeTruthy();
      expect(flowImportServiceStub.retrieve.callCount).toEqual(1);
    });
  });
});
