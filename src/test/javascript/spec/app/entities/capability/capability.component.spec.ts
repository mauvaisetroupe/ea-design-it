/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';

import * as config from '@/shared/config/config';
import CapabilityComponent from '@/entities/capability/capability.vue';
import CapabilityClass from '@/entities/capability/capability.component';
import CapabilityService from '@/entities/capability/capability.service';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();

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
  describe('Capability Management Component', () => {
    let wrapper: Wrapper<CapabilityClass>;
    let comp: CapabilityClass;
    let capabilityServiceStub: SinonStubbedInstance<CapabilityService>;

    beforeEach(() => {
      capabilityServiceStub = sinon.createStubInstance<CapabilityService>(CapabilityService);
      capabilityServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<CapabilityClass>(CapabilityComponent, {
        store,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          capabilityService: () => capabilityServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      capabilityServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllCapabilitys();
      await comp.$nextTick();

      // THEN
      expect(capabilityServiceStub.retrieve.called).toBeTruthy();
      expect(comp.capabilities[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      capabilityServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      comp.removeCapability();
      await comp.$nextTick();

      // THEN
      expect(capabilityServiceStub.delete.called).toBeTruthy();
      expect(capabilityServiceStub.retrieve.callCount).toEqual(1);
    });
  });
});
