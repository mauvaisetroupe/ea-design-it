/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';

import * as config from '@/shared/config/config';
import ApplicationCategoryUpdateComponent from '@/entities/application-category/application-category-update.vue';
import ApplicationCategoryClass from '@/entities/application-category/application-category-update.component';
import ApplicationCategoryService from '@/entities/application-category/application-category.service';

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
  describe('ApplicationCategory Management Update Component', () => {
    let wrapper: Wrapper<ApplicationCategoryClass>;
    let comp: ApplicationCategoryClass;
    let applicationCategoryServiceStub: SinonStubbedInstance<ApplicationCategoryService>;

    beforeEach(() => {
      applicationCategoryServiceStub = sinon.createStubInstance<ApplicationCategoryService>(ApplicationCategoryService);

      wrapper = shallowMount<ApplicationCategoryClass>(ApplicationCategoryUpdateComponent, {
        store,
        localVue,
        router,
        provide: {
          applicationCategoryService: () => applicationCategoryServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.applicationCategory = entity;
        applicationCategoryServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(applicationCategoryServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.applicationCategory = entity;
        applicationCategoryServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(applicationCategoryServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundApplicationCategory = { id: 123 };
        applicationCategoryServiceStub.find.resolves(foundApplicationCategory);
        applicationCategoryServiceStub.retrieve.resolves([foundApplicationCategory]);

        // WHEN
        comp.beforeRouteEnter({ params: { applicationCategoryId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.applicationCategory).toBe(foundApplicationCategory);
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
