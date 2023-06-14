/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import CapabilityApplicationMappingComponent from '@/entities/capability-application-mapping/capability-application-mapping.vue';
import CapabilityApplicationMappingClass from '@/entities/capability-application-mapping/capability-application-mapping.component';
import CapabilityApplicationMappingService from '@/entities/capability-application-mapping/capability-application-mapping.service';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(ToastPlugin);

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('b-badge', {});
localVue.directive('b-modal', {});
localVue.component('b-button', {});
localVue.component('router-link', {});

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  describe('CapabilityApplicationMapping Management Component', () => {
    let wrapper: Wrapper<CapabilityApplicationMappingClass>;
    let comp: CapabilityApplicationMappingClass;
    let capabilityApplicationMappingServiceStub: SinonStubbedInstance<CapabilityApplicationMappingService>;

    beforeEach(() => {
      capabilityApplicationMappingServiceStub =
        sinon.createStubInstance<CapabilityApplicationMappingService>(CapabilityApplicationMappingService);
      capabilityApplicationMappingServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<CapabilityApplicationMappingClass>(CapabilityApplicationMappingComponent, {
        store,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          capabilityApplicationMappingService: () => capabilityApplicationMappingServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      capabilityApplicationMappingServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllCapabilityApplicationMappings();
      await comp.$nextTick();

      // THEN
      expect(capabilityApplicationMappingServiceStub.retrieve.called).toBeTruthy();
      expect(comp.capabilityApplicationMappings[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      capabilityApplicationMappingServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      expect(capabilityApplicationMappingServiceStub.retrieve.callCount).toEqual(1);

      comp.removeCapabilityApplicationMapping();
      await comp.$nextTick();

      // THEN
      expect(capabilityApplicationMappingServiceStub.delete.called).toBeTruthy();
      expect(capabilityApplicationMappingServiceStub.retrieve.callCount).toEqual(2);
    });
  });
});
