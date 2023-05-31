/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import ExternalReferenceDetailComponent from '@/entities/external-reference/external-reference-details.vue';
import ExternalReferenceClass from '@/entities/external-reference/external-reference-details.component';
import ExternalReferenceService from '@/entities/external-reference/external-reference.service';
import router from '@/router';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('ExternalReference Management Detail Component', () => {
    let wrapper: Wrapper<ExternalReferenceClass>;
    let comp: ExternalReferenceClass;
    let externalReferenceServiceStub: SinonStubbedInstance<ExternalReferenceService>;

    beforeEach(() => {
      externalReferenceServiceStub = sinon.createStubInstance<ExternalReferenceService>(ExternalReferenceService);

      wrapper = shallowMount<ExternalReferenceClass>(ExternalReferenceDetailComponent, {
        store,
        localVue,
        router,
        provide: { externalReferenceService: () => externalReferenceServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundExternalReference = { id: 123 };
        externalReferenceServiceStub.find.resolves(foundExternalReference);

        // WHEN
        comp.retrieveExternalReference(123);
        await comp.$nextTick();

        // THEN
        expect(comp.externalReference).toBe(foundExternalReference);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundExternalReference = { id: 123 };
        externalReferenceServiceStub.find.resolves(foundExternalReference);

        // WHEN
        comp.beforeRouteEnter({ params: { externalReferenceId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.externalReference).toBe(foundExternalReference);
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
