/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';

import * as config from '@/shared/config/config';
import ApplicationComponentUpdateComponent from '@/entities/application-component/application-component-update.vue';
import ApplicationComponentClass from '@/entities/application-component/application-component-update.component';
import ApplicationComponentService from '@/entities/application-component/application-component.service';

import ApplicationService from '@/entities/application/application.service';

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
  describe('ApplicationComponent Management Update Component', () => {
    let wrapper: Wrapper<ApplicationComponentClass>;
    let comp: ApplicationComponentClass;
    let applicationComponentServiceStub: SinonStubbedInstance<ApplicationComponentService>;

    beforeEach(() => {
      applicationComponentServiceStub = sinon.createStubInstance<ApplicationComponentService>(ApplicationComponentService);

      wrapper = shallowMount<ApplicationComponentClass>(ApplicationComponentUpdateComponent, {
        store,
        localVue,
        router,
        provide: {
          applicationComponentService: () => applicationComponentServiceStub,
          alertService: () => new AlertService(),

          applicationService: () => new ApplicationService(),

          applicationCategoryService: () => new ApplicationCategoryService(),
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.applicationComponent = entity;
        applicationComponentServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(applicationComponentServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.applicationComponent = entity;
        applicationComponentServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(applicationComponentServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundApplicationComponent = { id: 123 };
        applicationComponentServiceStub.find.resolves(foundApplicationComponent);
        applicationComponentServiceStub.retrieve.resolves([foundApplicationComponent]);

        // WHEN
        comp.beforeRouteEnter({ params: { applicationComponentId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.applicationComponent).toBe(foundApplicationComponent);
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
