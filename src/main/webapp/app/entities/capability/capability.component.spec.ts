/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import Capability from './capability.vue';
import CapabilityService from './capability.service';
import AlertService from '@/shared/alert/alert.service';

type CapabilityComponentType = InstanceType<typeof Capability>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('Capability Management Component', () => {
    let capabilityServiceStub: SinonStubbedInstance<CapabilityService>;
    let mountOptions: MountingOptions<CapabilityComponentType>['global'];

    beforeEach(() => {
      capabilityServiceStub = sinon.createStubInstance<CapabilityService>(CapabilityService);
      capabilityServiceStub.retrieve.resolves({ headers: {} });

      alertService = new AlertService({
        bvToast: {
          toast: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          bModal: bModalStub as any,
          'font-awesome-icon': true,
          'b-badge': true,
          'b-button': true,
          'router-link': true,
        },
        directives: {
          'b-modal': {},
        },
        provide: {
          alertService,
          capabilityService: () => capabilityServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        capabilityServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(Capability, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(capabilityServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.capabilities[0]).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
    describe('Handles', () => {
      let comp: CapabilityComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(Capability, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        capabilityServiceStub.retrieve.reset();
        capabilityServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        capabilityServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeCapability();
        await comp.$nextTick(); // clear components

        // THEN
        expect(capabilityServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(capabilityServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
