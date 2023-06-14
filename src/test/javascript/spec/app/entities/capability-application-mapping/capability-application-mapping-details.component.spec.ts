/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import CapabilityApplicationMappingDetailComponent from '@/entities/capability-application-mapping/capability-application-mapping-details.vue';
import CapabilityApplicationMappingClass from '@/entities/capability-application-mapping/capability-application-mapping-details.component';
import CapabilityApplicationMappingService from '@/entities/capability-application-mapping/capability-application-mapping.service';
import router from '@/router';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('CapabilityApplicationMapping Management Detail Component', () => {
    let wrapper: Wrapper<CapabilityApplicationMappingClass>;
    let comp: CapabilityApplicationMappingClass;
    let capabilityApplicationMappingServiceStub: SinonStubbedInstance<CapabilityApplicationMappingService>;

    beforeEach(() => {
      capabilityApplicationMappingServiceStub =
        sinon.createStubInstance<CapabilityApplicationMappingService>(CapabilityApplicationMappingService);

      wrapper = shallowMount<CapabilityApplicationMappingClass>(CapabilityApplicationMappingDetailComponent, {
        store,
        localVue,
        router,
        provide: {
          capabilityApplicationMappingService: () => capabilityApplicationMappingServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundCapabilityApplicationMapping = { id: 123 };
        capabilityApplicationMappingServiceStub.find.resolves(foundCapabilityApplicationMapping);

        // WHEN
        comp.retrieveCapabilityApplicationMapping(123);
        await comp.$nextTick();

        // THEN
        expect(comp.capabilityApplicationMapping).toBe(foundCapabilityApplicationMapping);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundCapabilityApplicationMapping = { id: 123 };
        capabilityApplicationMappingServiceStub.find.resolves(foundCapabilityApplicationMapping);

        // WHEN
        comp.beforeRouteEnter({ params: { capabilityApplicationMappingId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.capabilityApplicationMapping).toBe(foundCapabilityApplicationMapping);
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
