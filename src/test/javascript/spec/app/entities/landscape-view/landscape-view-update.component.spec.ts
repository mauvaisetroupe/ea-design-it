/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import LandscapeViewUpdateComponent from '@/entities/landscape-view/landscape-view-update.vue';
import LandscapeViewClass from '@/entities/landscape-view/landscape-view-update.component';
import LandscapeViewService from '@/entities/landscape-view/landscape-view.service';

import OwnerService from '@/entities/owner/owner.service';

import FunctionalFlowService from '@/entities/functional-flow/functional-flow.service';

import CapabilityApplicationMappingService from '@/entities/capability-application-mapping/capability-application-mapping.service';
import AlertService from '@/shared/alert/alert.service';
import { ILandscapeDTO } from '@/shared/model/landscape-view.model';

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
  describe('LandscapeView Management Update Component', () => {
    let wrapper: Wrapper<LandscapeViewClass>;
    let comp: LandscapeViewClass;
    let landscapeViewServiceStub: SinonStubbedInstance<LandscapeViewService>;

    beforeEach(() => {
      landscapeViewServiceStub = sinon.createStubInstance<LandscapeViewService>(LandscapeViewService);

      wrapper = shallowMount<LandscapeViewClass>(LandscapeViewUpdateComponent, {
        store,
        localVue,
        router,
        provide: {
          landscapeViewService: () => landscapeViewServiceStub,
          alertService: () => new AlertService(),

          ownerService: () =>
            sinon.createStubInstance<OwnerService>(OwnerService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          functionalFlowService: () =>
            sinon.createStubInstance<FunctionalFlowService>(FunctionalFlowService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          capabilityApplicationMappingService: () =>
            sinon.createStubInstance<CapabilityApplicationMappingService>(CapabilityApplicationMappingService, {
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
        comp.landscapeView = entity;
        landscapeViewServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(landscapeViewServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.landscapeView = entity;
        landscapeViewServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(landscapeViewServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundLandscapeView = { id: 123 };
        const lanscapeDTO: ILandscapeDTO = {
          landscape: { id: 123 },
          consolidatedCapability: [],
        };
        landscapeViewServiceStub.find.resolves(lanscapeDTO);
        landscapeViewServiceStub.retrieve.resolves([lanscapeDTO]);

        // WHEN
        comp.beforeRouteEnter({ params: { landscapeViewId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.landscapeView).toStrictEqual(foundLandscapeView);
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
