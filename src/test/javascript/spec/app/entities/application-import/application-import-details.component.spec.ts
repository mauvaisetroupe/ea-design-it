/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import ApplicationImportDetailComponent from '@/entities/application-import/application-import-details.vue';
import ApplicationImportClass from '@/entities/application-import/application-import-details.component';
import ApplicationImportService from '@/entities/application-import/application-import.service';
import router from '@/router';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('ApplicationImport Management Detail Component', () => {
    let wrapper: Wrapper<ApplicationImportClass>;
    let comp: ApplicationImportClass;
    let applicationImportServiceStub: SinonStubbedInstance<ApplicationImportService>;

    beforeEach(() => {
      applicationImportServiceStub = sinon.createStubInstance<ApplicationImportService>(ApplicationImportService);

      wrapper = shallowMount<ApplicationImportClass>(ApplicationImportDetailComponent, {
        store,
        localVue,
        router,
        provide: { applicationImportService: () => applicationImportServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundApplicationImport = { id: 123 };
        applicationImportServiceStub.find.resolves(foundApplicationImport);

        // WHEN
        comp.retrieveApplicationImport(123);
        await comp.$nextTick();

        // THEN
        expect(comp.applicationImport).toBe(foundApplicationImport);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundApplicationImport = { id: 123 };
        applicationImportServiceStub.find.resolves(foundApplicationImport);

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
