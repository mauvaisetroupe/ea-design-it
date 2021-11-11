/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';

import * as config from '@/shared/config/config';
import LandscapeViewUpdateComponent from '@/entities/landscape-view/landscape-view-update.vue';
import LandscapeViewClass from '@/entities/landscape-view/landscape-view-update.component';
import LandscapeViewService from '@/entities/landscape-view/landscape-view.service';

import OwnerService from '@/entities/owner/owner.service';

import FunctionalFlowService from '@/entities/functional-flow/functional-flow.service';
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

          ownerService: () => new OwnerService(),

          functionalFlowService: () => new FunctionalFlowService(),
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
        landscapeViewServiceStub.find.resolves(foundLandscapeView);
        landscapeViewServiceStub.retrieve.resolves([foundLandscapeView]);

        // WHEN
        comp.beforeRouteEnter({ params: { landscapeViewId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.landscapeView).toBe(foundLandscapeView);
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
