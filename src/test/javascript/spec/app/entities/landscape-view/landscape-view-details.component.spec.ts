/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import LandscapeViewDetailComponent from '@/entities/landscape-view/landscape-view-details.vue';
import LandscapeViewClass from '@/entities/landscape-view/landscape-view-details.component';
import LandscapeViewService from '@/entities/landscape-view/landscape-view.service';
import router from '@/router';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('LandscapeView Management Detail Component', () => {
    let wrapper: Wrapper<LandscapeViewClass>;
    let comp: LandscapeViewClass;
    let landscapeViewServiceStub: SinonStubbedInstance<LandscapeViewService>;

    beforeEach(() => {
      landscapeViewServiceStub = sinon.createStubInstance<LandscapeViewService>(LandscapeViewService);

      wrapper = shallowMount<LandscapeViewClass>(LandscapeViewDetailComponent, {
        store,
        localVue,
        router,
        provide: { landscapeViewService: () => landscapeViewServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundLandscapeView = { id: 123 };
        landscapeViewServiceStub.find.resolves(foundLandscapeView);

        // WHEN
        comp.retrieveLandscapeView(123);
        await comp.$nextTick();

        // THEN
        expect(comp.landscapeView).toBe(foundLandscapeView);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundLandscapeView = { id: 123 };
        landscapeViewServiceStub.find.resolves(foundLandscapeView);

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
