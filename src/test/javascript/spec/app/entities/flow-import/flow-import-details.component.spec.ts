/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import FlowImportDetailComponent from '@/entities/flow-import/flow-import-details.vue';
import FlowImportClass from '@/entities/flow-import/flow-import-details.component';
import FlowImportService from '@/entities/flow-import/flow-import.service';
import router from '@/router';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('FlowImport Management Detail Component', () => {
    let wrapper: Wrapper<FlowImportClass>;
    let comp: FlowImportClass;
    let flowImportServiceStub: SinonStubbedInstance<FlowImportService>;

    beforeEach(() => {
      flowImportServiceStub = sinon.createStubInstance<FlowImportService>(FlowImportService);

      wrapper = shallowMount<FlowImportClass>(FlowImportDetailComponent, {
        store,
        localVue,
        router,
        provide: { flowImportService: () => flowImportServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundFlowImport = { id: 123 };
        flowImportServiceStub.find.resolves(foundFlowImport);

        // WHEN
        comp.retrieveFlowImport(123);
        await comp.$nextTick();

        // THEN
        expect(comp.flowImport).toBe(foundFlowImport);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundFlowImport = { id: 123 };
        flowImportServiceStub.find.resolves(foundFlowImport);

        // WHEN
        comp.beforeRouteEnter({ params: { flowImportId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.flowImport).toBe(foundFlowImport);
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
