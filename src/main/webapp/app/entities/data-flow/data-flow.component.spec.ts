/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import DataFlow from './data-flow.vue';
import DataFlowService from './data-flow.service';
import AlertService from '@/shared/alert/alert.service';

type DataFlowComponentType = InstanceType<typeof DataFlow>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('DataFlow Management Component', () => {
    let dataFlowServiceStub: SinonStubbedInstance<DataFlowService>;
    let mountOptions: MountingOptions<DataFlowComponentType>['global'];

    beforeEach(() => {
      dataFlowServiceStub = sinon.createStubInstance<DataFlowService>(DataFlowService);
      dataFlowServiceStub.retrieve.resolves({ headers: {} });

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
          dataFlowService: () => dataFlowServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        dataFlowServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(DataFlow, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(dataFlowServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.dataFlows[0]).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
    describe('Handles', () => {
      let comp: DataFlowComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(DataFlow, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        dataFlowServiceStub.retrieve.reset();
        dataFlowServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        dataFlowServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeDataFlow();
        await comp.$nextTick(); // clear components

        // THEN
        expect(dataFlowServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(dataFlowServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
