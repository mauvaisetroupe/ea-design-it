/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import TechnologyUpdateComponent from '@/entities/technology/technology-update.vue';
import TechnologyClass from '@/entities/technology/technology-update.component';
import TechnologyService from '@/entities/technology/technology.service';

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
  describe('Technology Management Update Component', () => {
    let wrapper: Wrapper<TechnologyClass>;
    let comp: TechnologyClass;
    let technologyServiceStub: SinonStubbedInstance<TechnologyService>;

    beforeEach(() => {
      technologyServiceStub = sinon.createStubInstance<TechnologyService>(TechnologyService);

      wrapper = shallowMount<TechnologyClass>(TechnologyUpdateComponent, {
        store,
        localVue,
        router,
        provide: {
          technologyService: () => technologyServiceStub,
          alertService: () => new AlertService(),

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
        comp.technology = entity;
        technologyServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(technologyServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.technology = entity;
        technologyServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(technologyServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundTechnology = { id: 123 };
        technologyServiceStub.find.resolves(foundTechnology);
        technologyServiceStub.retrieve.resolves([foundTechnology]);

        // WHEN
        comp.beforeRouteEnter({ params: { technologyId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.technology).toBe(foundTechnology);
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
