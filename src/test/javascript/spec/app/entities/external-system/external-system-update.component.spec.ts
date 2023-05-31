/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import ExternalSystemUpdateComponent from '@/entities/external-system/external-system-update.vue';
import ExternalSystemClass from '@/entities/external-system/external-system-update.component';
import ExternalSystemService from '@/entities/external-system/external-system.service';

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
  describe('ExternalSystem Management Update Component', () => {
    let wrapper: Wrapper<ExternalSystemClass>;
    let comp: ExternalSystemClass;
    let externalSystemServiceStub: SinonStubbedInstance<ExternalSystemService>;

    beforeEach(() => {
      externalSystemServiceStub = sinon.createStubInstance<ExternalSystemService>(ExternalSystemService);

      wrapper = shallowMount<ExternalSystemClass>(ExternalSystemUpdateComponent, {
        store,
        localVue,
        router,
        provide: {
          externalSystemService: () => externalSystemServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.externalSystem = entity;
        externalSystemServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(externalSystemServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.externalSystem = entity;
        externalSystemServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(externalSystemServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundExternalSystem = { id: 123 };
        externalSystemServiceStub.find.resolves(foundExternalSystem);
        externalSystemServiceStub.retrieve.resolves([foundExternalSystem]);

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
