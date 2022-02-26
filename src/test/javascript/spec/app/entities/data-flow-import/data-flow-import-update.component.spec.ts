/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import DataFlowImportUpdateComponent from '@/entities/data-flow-import/data-flow-import-update.vue';
import DataFlowImportClass from '@/entities/data-flow-import/data-flow-import-update.component';
import DataFlowImportService from '@/entities/data-flow-import/data-flow-import.service';

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
  describe('DataFlowImport Management Update Component', () => {
    let wrapper: Wrapper<DataFlowImportClass>;
    let comp: DataFlowImportClass;
    let dataFlowImportServiceStub: SinonStubbedInstance<DataFlowImportService>;

    beforeEach(() => {
      dataFlowImportServiceStub = sinon.createStubInstance<DataFlowImportService>(DataFlowImportService);

      wrapper = shallowMount<DataFlowImportClass>(DataFlowImportUpdateComponent, {
        store,
        localVue,
        router,
        provide: {
          dataFlowImportService: () => dataFlowImportServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.dataFlowImport = entity;
        dataFlowImportServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(dataFlowImportServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.dataFlowImport = entity;
        dataFlowImportServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(dataFlowImportServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundDataFlowImport = { id: 123 };
        dataFlowImportServiceStub.find.resolves(foundDataFlowImport);
        dataFlowImportServiceStub.retrieve.resolves([foundDataFlowImport]);

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
