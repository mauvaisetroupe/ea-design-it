/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';

import dayjs from 'dayjs';
import { DATE_TIME_LONG_FORMAT } from '@/shared/date/filters';

import * as config from '@/shared/config/config';
import ApplicationImportUpdateComponent from '@/entities/application-import/application-import-update.vue';
import ApplicationImportClass from '@/entities/application-import/application-import-update.component';
import ApplicationImportService from '@/entities/application-import/application-import.service';

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
  describe('ApplicationImport Management Update Component', () => {
    let wrapper: Wrapper<ApplicationImportClass>;
    let comp: ApplicationImportClass;
    let applicationImportServiceStub: SinonStubbedInstance<ApplicationImportService>;

    beforeEach(() => {
      applicationImportServiceStub = sinon.createStubInstance<ApplicationImportService>(ApplicationImportService);

      wrapper = shallowMount<ApplicationImportClass>(ApplicationImportUpdateComponent, {
        store,
        localVue,
        router,
        provide: {
          applicationImportService: () => applicationImportServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    describe('load', () => {
      it('Should convert date from string', () => {
        // GIVEN
        const date = new Date('2019-10-15T11:42:02Z');

        // WHEN
        const convertedDate = comp.convertDateTimeFromServer(date);

        // THEN
        expect(convertedDate).toEqual(dayjs(date).format(DATE_TIME_LONG_FORMAT));
      });

      it('Should not convert date if date is not present', () => {
        expect(comp.convertDateTimeFromServer(null)).toBeNull();
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.applicationImport = entity;
        applicationImportServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(applicationImportServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.applicationImport = entity;
        applicationImportServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(applicationImportServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundApplicationImport = { id: 123 };
        applicationImportServiceStub.find.resolves(foundApplicationImport);
        applicationImportServiceStub.retrieve.resolves([foundApplicationImport]);

        // WHEN
        comp.beforeRouteEnter({ params: { applicationImportId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.applicationImport).toBe(foundApplicationImport);
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
