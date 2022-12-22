/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import FlowGroupComponent from '@/entities/flow-group/flow-group.vue';
import FlowGroupClass from '@/entities/flow-group/flow-group.component';
import FlowGroupService from '@/entities/flow-group/flow-group.service';
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
  describe('FlowGroup Management Component', () => {
    let wrapper: Wrapper<FlowGroupClass>;
    let comp: FlowGroupClass;
    let flowGroupServiceStub: SinonStubbedInstance<FlowGroupService>;

    beforeEach(() => {
      flowGroupServiceStub = sinon.createStubInstance<FlowGroupService>(FlowGroupService);
      flowGroupServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<FlowGroupClass>(FlowGroupComponent, {
        store,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          flowGroupService: () => flowGroupServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      flowGroupServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllFlowGroups();
      await comp.$nextTick();

      // THEN
      expect(flowGroupServiceStub.retrieve.called).toBeTruthy();
      expect(comp.flowGroups[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      flowGroupServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      expect(flowGroupServiceStub.retrieve.callCount).toEqual(1);

      comp.removeFlowGroup();
      await comp.$nextTick();

      // THEN
      expect(flowGroupServiceStub.delete.called).toBeTruthy();
      expect(flowGroupServiceStub.retrieve.callCount).toEqual(2);
    });
  });
});
