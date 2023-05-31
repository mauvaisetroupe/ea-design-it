/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import ExternalSystemDetailComponent from '@/entities/external-system/external-system-details.vue';
import ExternalSystemClass from '@/entities/external-system/external-system-details.component';
import ExternalSystemService from '@/entities/external-system/external-system.service';
import router from '@/router';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('ExternalSystem Management Detail Component', () => {
    let wrapper: Wrapper<ExternalSystemClass>;
    let comp: ExternalSystemClass;
    let externalSystemServiceStub: SinonStubbedInstance<ExternalSystemService>;

    beforeEach(() => {
      externalSystemServiceStub = sinon.createStubInstance<ExternalSystemService>(ExternalSystemService);

      wrapper = shallowMount<ExternalSystemClass>(ExternalSystemDetailComponent, {
        store,
        localVue,
        router,
        provide: { externalSystemService: () => externalSystemServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundExternalSystem = { id: 123 };
        externalSystemServiceStub.find.resolves(foundExternalSystem);

        // WHEN
        comp.retrieveExternalSystem(123);
        await comp.$nextTick();

        // THEN
        expect(comp.externalSystem).toBe(foundExternalSystem);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundExternalSystem = { id: 123 };
        externalSystemServiceStub.find.resolves(foundExternalSystem);

        // WHEN
        comp.beforeRouteEnter({ params: { externalSystemId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.externalSystem).toBe(foundExternalSystem);
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
