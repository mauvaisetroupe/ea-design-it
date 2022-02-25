/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import DataFlowItemUpdateComponent from '@/entities/data-flow-item/data-flow-item-update.vue';
import DataFlowItemClass from '@/entities/data-flow-item/data-flow-item-update.component';
import DataFlowItemService from '@/entities/data-flow-item/data-flow-item.service';

import DataFlowService from '@/entities/data-flow/data-flow.service';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
const router = new Router();
localVue.use(Router);
localVue.use(ToastPlugin);
localVue.component('font-awesome-icon', {});
localVue.component('b-input-group', {});
localVue.component('b-input-group-prepend', {});
localVue.component('b-form-datepicker', {});
localVue.component('b-form-input', {});

describe('Component Tests', () => {
  describe('DataFlowItem Management Update Component', () => {
    let wrapper: Wrapper<DataFlowItemClass>;
    let comp: DataFlowItemClass;
    let dataFlowItemServiceStub: SinonStubbedInstance<DataFlowItemService>;

    beforeEach(() => {
      dataFlowItemServiceStub = sinon.createStubInstance<DataFlowItemService>(DataFlowItemService);

      wrapper = shallowMount<DataFlowItemClass>(DataFlowItemUpdateComponent, {
        store,
        localVue,
        router,
        provide: {
          dataFlowItemService: () => dataFlowItemServiceStub,
          alertService: () => new AlertService(),

          dataFlowService: () =>
            sinon.createStubInstance<DataFlowService>(DataFlowService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.dataFlowItem = entity;
        dataFlowItemServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(dataFlowItemServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.dataFlowItem = entity;
        dataFlowItemServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(dataFlowItemServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundDataFlowItem = { id: 123 };
        dataFlowItemServiceStub.find.resolves(foundDataFlowItem);
        dataFlowItemServiceStub.retrieve.resolves([foundDataFlowItem]);

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
