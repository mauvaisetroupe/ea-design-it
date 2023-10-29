/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import FlowGroup from './flow-group.vue';
import FlowGroupService from './flow-group.service';
import AlertService from '@/shared/alert/alert.service';

type FlowGroupComponentType = InstanceType<typeof FlowGroup>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('FlowGroup Management Component', () => {
    let flowGroupServiceStub: SinonStubbedInstance<FlowGroupService>;
    let mountOptions: MountingOptions<FlowGroupComponentType>['global'];

    beforeEach(() => {
      flowGroupServiceStub = sinon.createStubInstance<FlowGroupService>(FlowGroupService);
      flowGroupServiceStub.retrieve.resolves({ headers: {} });

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
          flowGroupService: () => flowGroupServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        flowGroupServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(FlowGroup, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(flowGroupServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.flowGroups[0]).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
    describe('Handles', () => {
      let comp: FlowGroupComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(FlowGroup, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        flowGroupServiceStub.retrieve.reset();
        flowGroupServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        flowGroupServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeFlowGroup();
        await comp.$nextTick(); // clear components

        // THEN
        expect(flowGroupServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(flowGroupServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
