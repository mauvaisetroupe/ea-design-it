/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import FlowInterfaceDetailComponent from '@/entities/flow-interface/flow-interface-details.vue';
import FlowInterfaceClass from '@/entities/flow-interface/flow-interface-details.component';
import FlowInterfaceService from '@/entities/flow-interface/flow-interface.service';
import router from '@/router';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('FlowInterface Management Detail Component', () => {
    let wrapper: Wrapper<FlowInterfaceClass>;
    let comp: FlowInterfaceClass;
    let flowInterfaceServiceStub: SinonStubbedInstance<FlowInterfaceService>;

    beforeEach(() => {
      flowInterfaceServiceStub = sinon.createStubInstance<FlowInterfaceService>(FlowInterfaceService);

      wrapper = shallowMount<FlowInterfaceClass>(FlowInterfaceDetailComponent, {
        store,
        localVue,
        router,
        provide: { flowInterfaceService: () => flowInterfaceServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundFlowInterface = { id: 123 };
        flowInterfaceServiceStub.find.resolves(foundFlowInterface);

        // WHEN
        comp.retrieveFlowInterface(123);
        await comp.$nextTick();

        // THEN
        expect(comp.flowInterface).toBe(foundFlowInterface);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundFlowInterface = { id: 123 };
        flowInterfaceServiceStub.find.resolves(foundFlowInterface);

        // WHEN
        comp.beforeRouteEnter({ params: { flowInterfaceId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.flowInterface).toBe(foundFlowInterface);
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
