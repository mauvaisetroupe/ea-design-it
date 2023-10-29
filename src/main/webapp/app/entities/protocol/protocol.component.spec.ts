/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import Protocol from './protocol.vue';
import ProtocolService from './protocol.service';
import AlertService from '@/shared/alert/alert.service';
import type AccountService from '@/account/account.service';

type ProtocolComponentType = InstanceType<typeof Protocol>;
const accountService = { hasAnyAuthorityAndCheckAuth: vitest.fn().mockImplementation(() => Promise.resolve(true)) };

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('Protocol Management Component', () => {
    let protocolServiceStub: SinonStubbedInstance<ProtocolService>;
    let mountOptions: MountingOptions<ProtocolComponentType>['global'];

    beforeEach(() => {
      protocolServiceStub = sinon.createStubInstance<ProtocolService>(ProtocolService);
      protocolServiceStub.retrieve.resolves({ headers: {} });

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
          protocolService: () => protocolServiceStub,
          accountService,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        protocolServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(Protocol, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(protocolServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.protocols[0]).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
    describe('Handles', () => {
      let comp: ProtocolComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(Protocol, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        protocolServiceStub.retrieve.reset();
        protocolServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        protocolServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeProtocol();
        await comp.$nextTick(); // clear components

        // THEN
        expect(protocolServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(protocolServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
