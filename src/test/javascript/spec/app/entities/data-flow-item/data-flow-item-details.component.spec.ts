/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import DataFlowItemDetailComponent from '@/entities/data-flow-item/data-flow-item-details.vue';
import DataFlowItemClass from '@/entities/data-flow-item/data-flow-item-details.component';
import DataFlowItemService from '@/entities/data-flow-item/data-flow-item.service';
import router from '@/router';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('DataFlowItem Management Detail Component', () => {
    let wrapper: Wrapper<DataFlowItemClass>;
    let comp: DataFlowItemClass;
    let dataFlowItemServiceStub: SinonStubbedInstance<DataFlowItemService>;

    beforeEach(() => {
      dataFlowItemServiceStub = sinon.createStubInstance<DataFlowItemService>(DataFlowItemService);

      wrapper = shallowMount<DataFlowItemClass>(DataFlowItemDetailComponent, {
        store,
        localVue,
        router,
        provide: { dataFlowItemService: () => dataFlowItemServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundDataFlowItem = { id: 123 };
        dataFlowItemServiceStub.find.resolves(foundDataFlowItem);

        // WHEN
        comp.retrieveDataFlowItem(123);
        await comp.$nextTick();

        // THEN
        expect(comp.dataFlowItem).toBe(foundDataFlowItem);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundDataFlowItem = { id: 123 };
        dataFlowItemServiceStub.find.resolves(foundDataFlowItem);

        // WHEN
        comp.beforeRouteEnter({ params: { dataFlowItemId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.dataFlowItem).toBe(foundDataFlowItem);
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
