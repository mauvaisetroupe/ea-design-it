/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import CapabilityDetailComponent from '@/entities/capability/capability-details.vue';
import CapabilityClass from '@/entities/capability/capability-details.component';
import CapabilityService from '@/entities/capability/capability.service';
import router from '@/router';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('Capability Management Detail Component', () => {
    let wrapper: Wrapper<CapabilityClass>;
    let comp: CapabilityClass;
    let capabilityServiceStub: SinonStubbedInstance<CapabilityService>;

    beforeEach(() => {
      capabilityServiceStub = sinon.createStubInstance<CapabilityService>(CapabilityService);

      wrapper = shallowMount<CapabilityClass>(CapabilityDetailComponent, {
        store,
        localVue,
        router,
        provide: { capabilityService: () => capabilityServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundCapability = { id: 123 };
        capabilityServiceStub.find.resolves(foundCapability);

        // WHEN
        comp.retrieveCapability(123);
        await comp.$nextTick();

        // THEN
        expect(comp.capability).toBe(foundCapability);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundCapability = { id: 123 };
        capabilityServiceStub.find.resolves(foundCapability);

        // WHEN
        comp.beforeRouteEnter({ params: { capabilityId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.capability).toBe(foundCapability);
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
