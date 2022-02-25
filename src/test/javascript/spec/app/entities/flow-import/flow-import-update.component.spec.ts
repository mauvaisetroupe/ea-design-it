/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';

import * as config from '@/shared/config/config';
import FlowImportUpdateComponent from '@/entities/flow-import/flow-import-update.vue';
import FlowImportClass from '@/entities/flow-import/flow-import-update.component';
import FlowImportService from '@/entities/flow-import/flow-import.service';

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
  describe('FlowImport Management Update Component', () => {
    let wrapper: Wrapper<FlowImportClass>;
    let comp: FlowImportClass;
    let flowImportServiceStub: SinonStubbedInstance<FlowImportService>;

    beforeEach(() => {
      flowImportServiceStub = sinon.createStubInstance<FlowImportService>(FlowImportService);

      wrapper = shallowMount<FlowImportClass>(FlowImportUpdateComponent, {
        store,
        localVue,
        router,
        provide: {
          flowImportService: () => flowImportServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.flowImport = entity;
        flowImportServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(flowImportServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.flowImport = entity;
        flowImportServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(flowImportServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundFlowImport = { id: 123 };
        flowImportServiceStub.find.resolves(foundFlowImport);
        flowImportServiceStub.retrieve.resolves([foundFlowImport]);

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
