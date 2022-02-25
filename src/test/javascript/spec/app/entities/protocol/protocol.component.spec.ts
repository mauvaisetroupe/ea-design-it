/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import ProtocolComponent from '@/entities/protocol/protocol.vue';
import ProtocolClass from '@/entities/protocol/protocol.component';
import ProtocolService from '@/entities/protocol/protocol.service';
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
  describe('Protocol Management Component', () => {
    let wrapper: Wrapper<ProtocolClass>;
    let comp: ProtocolClass;
    let protocolServiceStub: SinonStubbedInstance<ProtocolService>;

    beforeEach(() => {
      protocolServiceStub = sinon.createStubInstance<ProtocolService>(ProtocolService);
      protocolServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<ProtocolClass>(ProtocolComponent, {
        store,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          protocolService: () => protocolServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      protocolServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllProtocols();
      await comp.$nextTick();

      // THEN
      expect(protocolServiceStub.retrieve.called).toBeTruthy();
      expect(comp.protocols[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      protocolServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      expect(protocolServiceStub.retrieve.callCount).toEqual(1);

      comp.removeProtocol();
      await comp.$nextTick();

      // THEN
      expect(protocolServiceStub.delete.called).toBeTruthy();
      expect(protocolServiceStub.retrieve.callCount).toEqual(2);
    });
  });
});
