/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import FlowInterface from './flow-interface.vue';
import FlowInterfaceService from './flow-interface.service';
import AlertService from '@/shared/alert/alert.service';
import { createTestingPinia } from '@pinia/testing';
import { useStore } from '@/store';
import AccountService from '@/account/account.service';

type FlowInterfaceComponentType = InstanceType<typeof FlowInterface>;

const pinia = createTestingPinia();
const store = useStore();
const accountService = new AccountService(store);

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('FlowInterface Management Component', () => {
    let flowInterfaceServiceStub: SinonStubbedInstance<FlowInterfaceService>;
    let mountOptions: MountingOptions<FlowInterfaceComponentType>['global'];

    beforeEach(() => {
      flowInterfaceServiceStub = sinon.createStubInstance<FlowInterfaceService>(FlowInterfaceService);
      flowInterfaceServiceStub.retrieve.resolves({ headers: {} });

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
          flowInterfaceService: () => flowInterfaceServiceStub,
          accountService,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        flowInterfaceServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(FlowInterface, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(flowInterfaceServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.flowInterfaces[0]).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
    describe('Handles', () => {
      let comp: FlowInterfaceComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(FlowInterface, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        flowInterfaceServiceStub.retrieve.reset();
        flowInterfaceServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        flowInterfaceServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeFlowInterface();
        await comp.$nextTick(); // clear components

        // THEN
        expect(flowInterfaceServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(flowInterfaceServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
