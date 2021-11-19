/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';

import * as config from '@/shared/config/config';
import DataFlowUpdateComponent from '@/entities/data-flow/data-flow-update.vue';
import DataFlowClass from '@/entities/data-flow/data-flow-update.component';
import DataFlowService from '@/entities/data-flow/data-flow.service';

import EventDataService from '@/entities/event-data/event-data.service';

import FunctionalFlowService from '@/entities/functional-flow/functional-flow.service';

import FlowInterfaceService from '@/entities/flow-interface/flow-interface.service';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
const router = new Router();
localVue.use(Router);
localVue.component('font-awesome-icon', {});
localVue.component('b-input-group', {});
localVue.component('b-input-group-prepend', {});
localVue.component('b-form-datepicker', {});
localVue.component('b-form-input', {});

describe('Component Tests', () => {
  describe('DataFlow Management Update Component', () => {
    let wrapper: Wrapper<DataFlowClass>;
    let comp: DataFlowClass;
    let dataFlowServiceStub: SinonStubbedInstance<DataFlowService>;

    beforeEach(() => {
      dataFlowServiceStub = sinon.createStubInstance<DataFlowService>(DataFlowService);

      wrapper = shallowMount<DataFlowClass>(DataFlowUpdateComponent, {
        store,
        localVue,
        router,
        provide: {
          dataFlowService: () => dataFlowServiceStub,
          alertService: () => new AlertService(),

          eventDataService: () => new EventDataService(),

          functionalFlowService: () => new FunctionalFlowService(),

          flowInterfaceService: () => new FlowInterfaceService(),
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.dataFlow = entity;
        dataFlowServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(dataFlowServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.dataFlow = entity;
        dataFlowServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(dataFlowServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundDataFlow = { id: 123 };
        dataFlowServiceStub.find.resolves(foundDataFlow);
        dataFlowServiceStub.retrieve.resolves([foundDataFlow]);

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
