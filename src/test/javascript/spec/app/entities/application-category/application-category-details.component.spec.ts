/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import ApplicationCategoryDetailComponent from '@/entities/application-category/application-category-details.vue';
import ApplicationCategoryClass from '@/entities/application-category/application-category-details.component';
import ApplicationCategoryService from '@/entities/application-category/application-category.service';
import router from '@/router';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('ApplicationCategory Management Detail Component', () => {
    let wrapper: Wrapper<ApplicationCategoryClass>;
    let comp: ApplicationCategoryClass;
    let applicationCategoryServiceStub: SinonStubbedInstance<ApplicationCategoryService>;

    beforeEach(() => {
      applicationCategoryServiceStub = sinon.createStubInstance<ApplicationCategoryService>(ApplicationCategoryService);

      wrapper = shallowMount<ApplicationCategoryClass>(ApplicationCategoryDetailComponent, {
        store,
        localVue,
        router,
        provide: { applicationCategoryService: () => applicationCategoryServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundApplicationCategory = { id: 123 };
        applicationCategoryServiceStub.find.resolves(foundApplicationCategory);

        // WHEN
        comp.retrieveApplicationCategory(123);
        await comp.$nextTick();

        // THEN
        expect(comp.applicationCategory).toBe(foundApplicationCategory);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundApplicationCategory = { id: 123 };
        applicationCategoryServiceStub.find.resolves(foundApplicationCategory);

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
