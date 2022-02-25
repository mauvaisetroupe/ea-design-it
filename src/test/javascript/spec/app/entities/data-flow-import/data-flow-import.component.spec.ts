/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import DataFlowImportComponent from '@/entities/data-flow-import/data-flow-import.vue';
import DataFlowImportClass from '@/entities/data-flow-import/data-flow-import.component';
import DataFlowImportService from '@/entities/data-flow-import/data-flow-import.service';
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
  describe('DataFlowImport Management Component', () => {
    let wrapper: Wrapper<DataFlowImportClass>;
    let comp: DataFlowImportClass;
    let dataFlowImportServiceStub: SinonStubbedInstance<DataFlowImportService>;

    beforeEach(() => {
      dataFlowImportServiceStub = sinon.createStubInstance<DataFlowImportService>(DataFlowImportService);
      dataFlowImportServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<DataFlowImportClass>(DataFlowImportComponent, {
        store,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          dataFlowImportService: () => dataFlowImportServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      dataFlowImportServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllDataFlowImports();
      await comp.$nextTick();

      // THEN
      expect(dataFlowImportServiceStub.retrieve.called).toBeTruthy();
      expect(comp.dataFlowImports[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      dataFlowImportServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      expect(dataFlowImportServiceStub.retrieve.callCount).toEqual(1);

      comp.removeDataFlowImport();
      await comp.$nextTick();

      // THEN
      expect(dataFlowImportServiceStub.delete.called).toBeTruthy();
      expect(dataFlowImportServiceStub.retrieve.callCount).toEqual(2);
    });
  });
});
