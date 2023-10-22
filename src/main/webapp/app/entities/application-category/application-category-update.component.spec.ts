/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import ApplicationCategoryUpdate from './application-category-update.vue';
import ApplicationCategoryService from './application-category.service';
import AlertService from '@/shared/alert/alert.service';

type ApplicationCategoryUpdateComponentType = InstanceType<typeof ApplicationCategoryUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const applicationCategorySample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<ApplicationCategoryUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('ApplicationCategory Management Update Component', () => {
    let comp: ApplicationCategoryUpdateComponentType;
    let applicationCategoryServiceStub: SinonStubbedInstance<ApplicationCategoryService>;

    beforeEach(() => {
      route = {};
      applicationCategoryServiceStub = sinon.createStubInstance<ApplicationCategoryService>(ApplicationCategoryService);
      applicationCategoryServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

      alertService = new AlertService({
        bvToast: {
          toast: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          'font-awesome-icon': true,
          'b-input-group': true,
          'b-input-group-prepend': true,
          'b-form-datepicker': true,
          'b-form-input': true,
        },
        provide: {
          alertService,
          applicationCategoryService: () => applicationCategoryServiceStub,
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(ApplicationCategoryUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.applicationCategory = applicationCategorySample;
        applicationCategoryServiceStub.update.resolves(applicationCategorySample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(applicationCategoryServiceStub.update.calledWith(applicationCategorySample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        applicationCategoryServiceStub.create.resolves(entity);
        const wrapper = shallowMount(ApplicationCategoryUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.applicationCategory = entity;

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
        applicationCategoryServiceStub.find.resolves(applicationCategorySample);
        applicationCategoryServiceStub.retrieve.resolves([applicationCategorySample]);

        // WHEN
        route = {
          params: {
            applicationCategoryId: '' + applicationCategorySample.id,
          },
        };
        const wrapper = shallowMount(ApplicationCategoryUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.applicationCategory).toMatchObject(applicationCategorySample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        applicationCategoryServiceStub.find.resolves(applicationCategorySample);
        const wrapper = shallowMount(ApplicationCategoryUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
