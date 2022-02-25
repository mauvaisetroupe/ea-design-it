/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import DataFlowImportDetailComponent from '@/entities/data-flow-import/data-flow-import-details.vue';
import DataFlowImportClass from '@/entities/data-flow-import/data-flow-import-details.component';
import DataFlowImportService from '@/entities/data-flow-import/data-flow-import.service';
import router from '@/router';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('DataFlowImport Management Detail Component', () => {
    let wrapper: Wrapper<DataFlowImportClass>;
    let comp: DataFlowImportClass;
    let dataFlowImportServiceStub: SinonStubbedInstance<DataFlowImportService>;

    beforeEach(() => {
      dataFlowImportServiceStub = sinon.createStubInstance<DataFlowImportService>(DataFlowImportService);

      wrapper = shallowMount<DataFlowImportClass>(DataFlowImportDetailComponent, {
        store,
        localVue,
        router,
        provide: { dataFlowImportService: () => dataFlowImportServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundDataFlowImport = { id: 123 };
        dataFlowImportServiceStub.find.resolves(foundDataFlowImport);

        // WHEN
        comp.retrieveDataFlowImport(123);
        await comp.$nextTick();

        // THEN
        expect(comp.dataFlowImport).toBe(foundDataFlowImport);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundDataFlowImport = { id: 123 };
        dataFlowImportServiceStub.find.resolves(foundDataFlowImport);

        // WHEN
        comp.beforeRouteEnter({ params: { dataFlowImportId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.dataFlowImport).toBe(foundDataFlowImport);
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
