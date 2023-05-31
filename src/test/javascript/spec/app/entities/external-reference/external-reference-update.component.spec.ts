/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import ExternalReferenceUpdateComponent from '@/entities/external-reference/external-reference-update.vue';
import ExternalReferenceClass from '@/entities/external-reference/external-reference-update.component';
import ExternalReferenceService from '@/entities/external-reference/external-reference.service';

import ExternalSystemService from '@/entities/external-system/external-system.service';

import ApplicationService from '@/entities/application/application.service';

import ApplicationComponentService from '@/entities/application-component/application-component.service';
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
  describe('ExternalReference Management Update Component', () => {
    let wrapper: Wrapper<ExternalReferenceClass>;
    let comp: ExternalReferenceClass;
    let externalReferenceServiceStub: SinonStubbedInstance<ExternalReferenceService>;

    beforeEach(() => {
      externalReferenceServiceStub = sinon.createStubInstance<ExternalReferenceService>(ExternalReferenceService);

      wrapper = shallowMount<ExternalReferenceClass>(ExternalReferenceUpdateComponent, {
        store,
        localVue,
        router,
        provide: {
          externalReferenceService: () => externalReferenceServiceStub,
          alertService: () => new AlertService(),

          externalSystemService: () =>
            sinon.createStubInstance<ExternalSystemService>(ExternalSystemService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          applicationService: () =>
            sinon.createStubInstance<ApplicationService>(ApplicationService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          applicationComponentService: () =>
            sinon.createStubInstance<ApplicationComponentService>(ApplicationComponentService, {
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
        comp.externalReference = entity;
        externalReferenceServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(externalReferenceServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.externalReference = entity;
        externalReferenceServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(externalReferenceServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundExternalReference = { id: 123 };
        externalReferenceServiceStub.find.resolves(foundExternalReference);
        externalReferenceServiceStub.retrieve.resolves([foundExternalReference]);

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
