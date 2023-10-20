/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import DataFlowItem from './data-flow-item.vue';
import DataFlowItemService from './data-flow-item.service';
import AlertService from '@/shared/alert/alert.service';

type DataFlowItemComponentType = InstanceType<typeof DataFlowItem>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('DataFlowItem Management Component', () => {
    let dataFlowItemServiceStub: SinonStubbedInstance<DataFlowItemService>;
    let mountOptions: MountingOptions<DataFlowItemComponentType>['global'];

    beforeEach(() => {
      dataFlowItemServiceStub = sinon.createStubInstance<DataFlowItemService>(DataFlowItemService);
      dataFlowItemServiceStub.retrieve.resolves({ headers: {} });

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
          dataFlowItemService: () => dataFlowItemServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        dataFlowItemServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(DataFlowItem, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(dataFlowItemServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.dataFlowItems[0]).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
    describe('Handles', () => {
      let comp: DataFlowItemComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(DataFlowItem, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        dataFlowItemServiceStub.retrieve.reset();
        dataFlowItemServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        dataFlowItemServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeDataFlowItem();
        await comp.$nextTick(); // clear components

        // THEN
        expect(dataFlowItemServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(dataFlowItemServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
