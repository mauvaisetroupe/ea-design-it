/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import CapabilityApplicationMappingUpdateComponent from '@/entities/capability-application-mapping/capability-application-mapping-update.vue';
import CapabilityApplicationMappingClass from '@/entities/capability-application-mapping/capability-application-mapping-update.component';
import CapabilityApplicationMappingService from '@/entities/capability-application-mapping/capability-application-mapping.service';

import CapabilityService from '@/entities/capability/capability.service';

import ApplicationService from '@/entities/application/application.service';

import LandscapeViewService from '@/entities/landscape-view/landscape-view.service';
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
  describe('CapabilityApplicationMapping Management Update Component', () => {
    let wrapper: Wrapper<CapabilityApplicationMappingClass>;
    let comp: CapabilityApplicationMappingClass;
    let capabilityApplicationMappingServiceStub: SinonStubbedInstance<CapabilityApplicationMappingService>;

    beforeEach(() => {
      capabilityApplicationMappingServiceStub =
        sinon.createStubInstance<CapabilityApplicationMappingService>(CapabilityApplicationMappingService);

      wrapper = shallowMount<CapabilityApplicationMappingClass>(CapabilityApplicationMappingUpdateComponent, {
        store,
        localVue,
        router,
        provide: {
          capabilityApplicationMappingService: () => capabilityApplicationMappingServiceStub,
          alertService: () => new AlertService(),

          capabilityService: () =>
            sinon.createStubInstance<CapabilityService>(CapabilityService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          applicationService: () =>
            sinon.createStubInstance<ApplicationService>(ApplicationService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          landscapeViewService: () =>
            sinon.createStubInstance<LandscapeViewService>(LandscapeViewService, {
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
        comp.capabilityApplicationMapping = entity;
        capabilityApplicationMappingServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(capabilityApplicationMappingServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.capabilityApplicationMapping = entity;
        capabilityApplicationMappingServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(capabilityApplicationMappingServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundCapabilityApplicationMapping = { id: 123 };
        capabilityApplicationMappingServiceStub.find.resolves(foundCapabilityApplicationMapping);
        capabilityApplicationMappingServiceStub.retrieve.resolves([foundCapabilityApplicationMapping]);

        // WHEN
        comp.beforeRouteEnter({ params: { capabilityApplicationMappingId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.capabilityApplicationMapping).toBe(foundCapabilityApplicationMapping);
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
