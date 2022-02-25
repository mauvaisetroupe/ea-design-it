/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import DataFlowDetailComponent from '@/entities/data-flow/data-flow-details.vue';
import DataFlowClass from '@/entities/data-flow/data-flow-details.component';
import DataFlowService from '@/entities/data-flow/data-flow.service';
import router from '@/router';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('DataFlow Management Detail Component', () => {
    let wrapper: Wrapper<DataFlowClass>;
    let comp: DataFlowClass;
    let dataFlowServiceStub: SinonStubbedInstance<DataFlowService>;

    beforeEach(() => {
      dataFlowServiceStub = sinon.createStubInstance<DataFlowService>(DataFlowService);

      wrapper = shallowMount<DataFlowClass>(DataFlowDetailComponent, {
        store,
        localVue,
        router,
        provide: { dataFlowService: () => dataFlowServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundDataFlow = { id: 123 };
        dataFlowServiceStub.find.resolves(foundDataFlow);

        // WHEN
        comp.retrieveDataFlow(123);
        await comp.$nextTick();

        // THEN
        expect(comp.dataFlow).toBe(foundDataFlow);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundDataFlow = { id: 123 };
        dataFlowServiceStub.find.resolves(foundDataFlow);

        // WHEN
        comp.beforeRouteEnter({ params: { dataFlowId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.dataFlow).toBe(foundDataFlow);
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
