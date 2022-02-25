/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import DataFlowItemComponent from '@/entities/data-flow-item/data-flow-item.vue';
import DataFlowItemClass from '@/entities/data-flow-item/data-flow-item.component';
import DataFlowItemService from '@/entities/data-flow-item/data-flow-item.service';
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
  describe('DataFlowItem Management Component', () => {
    let wrapper: Wrapper<DataFlowItemClass>;
    let comp: DataFlowItemClass;
    let dataFlowItemServiceStub: SinonStubbedInstance<DataFlowItemService>;

    beforeEach(() => {
      dataFlowItemServiceStub = sinon.createStubInstance<DataFlowItemService>(DataFlowItemService);
      dataFlowItemServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<DataFlowItemClass>(DataFlowItemComponent, {
        store,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          dataFlowItemService: () => dataFlowItemServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      dataFlowItemServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllDataFlowItems();
      await comp.$nextTick();

      // THEN
      expect(dataFlowItemServiceStub.retrieve.called).toBeTruthy();
      expect(comp.dataFlowItems[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      dataFlowItemServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      expect(dataFlowItemServiceStub.retrieve.callCount).toEqual(1);

      comp.removeDataFlowItem();
      await comp.$nextTick();

      // THEN
      expect(dataFlowItemServiceStub.delete.called).toBeTruthy();
      expect(dataFlowItemServiceStub.retrieve.callCount).toEqual(2);
    });
  });
});
