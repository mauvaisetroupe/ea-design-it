/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import ApplicationComponentUpdate from './application-component-update.vue';
import ApplicationComponentService from './application-component.service';
import AlertService from '@/shared/alert/alert.service';

import ApplicationService from '@/entities/application/application.service';
import ApplicationCategoryService from '@/entities/application-category/application-category.service';
import TechnologyService from '@/entities/technology/technology.service';
import ExternalReferenceService from '@/entities/external-reference/external-reference.service';

type ApplicationComponentUpdateComponentType = InstanceType<typeof ApplicationComponentUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const applicationComponentSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<ApplicationComponentUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('ApplicationComponent Management Update Component', () => {
    let comp: ApplicationComponentUpdateComponentType;
    let applicationComponentServiceStub: SinonStubbedInstance<ApplicationComponentService>;

    beforeEach(() => {
      route = {};
      applicationComponentServiceStub = sinon.createStubInstance<ApplicationComponentService>(ApplicationComponentService);
      applicationComponentServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          applicationComponentService: () => applicationComponentServiceStub,
          applicationService: () =>
            sinon.createStubInstance<ApplicationService>(ApplicationService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
          applicationCategoryService: () =>
            sinon.createStubInstance<ApplicationCategoryService>(ApplicationCategoryService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
          technologyService: () =>
            sinon.createStubInstance<TechnologyService>(TechnologyService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
          externalReferenceService: () =>
            sinon.createStubInstance<ExternalReferenceService>(ExternalReferenceService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(ApplicationComponentUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.applicationComponent = applicationComponentSample;
        applicationComponentServiceStub.update.resolves(applicationComponentSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(applicationComponentServiceStub.update.calledWith(applicationComponentSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        applicationComponentServiceStub.create.resolves(entity);
        const wrapper = shallowMount(ApplicationComponentUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.applicationComponent = entity;

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
        applicationComponentServiceStub.find.resolves(applicationComponentSample);
        applicationComponentServiceStub.retrieve.resolves([applicationComponentSample]);

        // WHEN
        route = {
          params: {
            applicationComponentId: '' + applicationComponentSample.id,
          },
        };
        const wrapper = shallowMount(ApplicationComponentUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.applicationComponent).toMatchObject(applicationComponentSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        applicationComponentServiceStub.find.resolves(applicationComponentSample);
        const wrapper = shallowMount(ApplicationComponentUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
