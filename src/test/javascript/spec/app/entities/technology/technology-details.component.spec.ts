/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import TechnologyDetailComponent from '@/entities/technology/technology-details.vue';
import TechnologyClass from '@/entities/technology/technology-details.component';
import TechnologyService from '@/entities/technology/technology.service';
import router from '@/router';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('Technology Management Detail Component', () => {
    let wrapper: Wrapper<TechnologyClass>;
    let comp: TechnologyClass;
    let technologyServiceStub: SinonStubbedInstance<TechnologyService>;

    beforeEach(() => {
      technologyServiceStub = sinon.createStubInstance<TechnologyService>(TechnologyService);

      wrapper = shallowMount<TechnologyClass>(TechnologyDetailComponent, {
        store,
        localVue,
        router,
        provide: { technologyService: () => technologyServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundTechnology = { id: 123 };
        technologyServiceStub.find.resolves(foundTechnology);

        // WHEN
        comp.retrieveTechnology(123);
        await comp.$nextTick();

        // THEN
        expect(comp.technology).toBe(foundTechnology);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundTechnology = { id: 123 };
        technologyServiceStub.find.resolves(foundTechnology);

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
