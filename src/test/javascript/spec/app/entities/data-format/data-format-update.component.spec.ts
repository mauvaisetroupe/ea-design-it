/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';

import * as config from '@/shared/config/config';
import DataFormatUpdateComponent from '@/entities/data-format/data-format-update.vue';
import DataFormatClass from '@/entities/data-format/data-format-update.component';
import DataFormatService from '@/entities/data-format/data-format.service';

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
  describe('DataFormat Management Update Component', () => {
    let wrapper: Wrapper<DataFormatClass>;
    let comp: DataFormatClass;
    let dataFormatServiceStub: SinonStubbedInstance<DataFormatService>;

    beforeEach(() => {
      dataFormatServiceStub = sinon.createStubInstance<DataFormatService>(DataFormatService);

      wrapper = shallowMount<DataFormatClass>(DataFormatUpdateComponent, {
        store,
        localVue,
        router,
        provide: {
          dataFormatService: () => dataFormatServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.dataFormat = entity;
        dataFormatServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(dataFormatServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.dataFormat = entity;
        dataFormatServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(dataFormatServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundDataFormat = { id: 123 };
        dataFormatServiceStub.find.resolves(foundDataFormat);
        dataFormatServiceStub.retrieve.resolves([foundDataFormat]);

        // WHEN
        comp.beforeRouteEnter({ params: { dataFormatId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.dataFormat).toBe(foundDataFormat);
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
