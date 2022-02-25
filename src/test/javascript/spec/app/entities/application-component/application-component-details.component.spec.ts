/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import ApplicationComponentDetailComponent from '@/entities/application-component/application-component-details.vue';
import ApplicationComponentClass from '@/entities/application-component/application-component-details.component';
import ApplicationComponentService from '@/entities/application-component/application-component.service';
import router from '@/router';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('ApplicationComponent Management Detail Component', () => {
    let wrapper: Wrapper<ApplicationComponentClass>;
    let comp: ApplicationComponentClass;
    let applicationComponentServiceStub: SinonStubbedInstance<ApplicationComponentService>;

    beforeEach(() => {
      applicationComponentServiceStub = sinon.createStubInstance<ApplicationComponentService>(ApplicationComponentService);

      wrapper = shallowMount<ApplicationComponentClass>(ApplicationComponentDetailComponent, {
        store,
        localVue,
        router,
        provide: { applicationComponentService: () => applicationComponentServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundApplicationComponent = { id: 123 };
        applicationComponentServiceStub.find.resolves(foundApplicationComponent);

        // WHEN
        comp.retrieveApplicationComponent(123);
        await comp.$nextTick();

        // THEN
        expect(comp.applicationComponent).toBe(foundApplicationComponent);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundApplicationComponent = { id: 123 };
        applicationComponentServiceStub.find.resolves(foundApplicationComponent);

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
