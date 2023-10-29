/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import CapabilityApplicationMapping from './capability-application-mapping.vue';
import CapabilityApplicationMappingService from './capability-application-mapping.service';
import AlertService from '@/shared/alert/alert.service';

type CapabilityApplicationMappingComponentType = InstanceType<typeof CapabilityApplicationMapping>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('CapabilityApplicationMapping Management Component', () => {
    let capabilityApplicationMappingServiceStub: SinonStubbedInstance<CapabilityApplicationMappingService>;
    let mountOptions: MountingOptions<CapabilityApplicationMappingComponentType>['global'];

    beforeEach(() => {
      capabilityApplicationMappingServiceStub =
        sinon.createStubInstance<CapabilityApplicationMappingService>(CapabilityApplicationMappingService);
      capabilityApplicationMappingServiceStub.retrieve.resolves({ headers: {} });

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
          capabilityApplicationMappingService: () => capabilityApplicationMappingServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        capabilityApplicationMappingServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(CapabilityApplicationMapping, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(capabilityApplicationMappingServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.capabilityApplicationMappings[0]).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
    describe('Handles', () => {
      let comp: CapabilityApplicationMappingComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(CapabilityApplicationMapping, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        capabilityApplicationMappingServiceStub.retrieve.reset();
        capabilityApplicationMappingServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        capabilityApplicationMappingServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeCapabilityApplicationMapping();
        await comp.$nextTick(); // clear components

        // THEN
        expect(capabilityApplicationMappingServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(capabilityApplicationMappingServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
