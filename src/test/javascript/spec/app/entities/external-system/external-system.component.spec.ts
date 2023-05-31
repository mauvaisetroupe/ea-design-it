/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import ExternalSystemComponent from '@/entities/external-system/external-system.vue';
import ExternalSystemClass from '@/entities/external-system/external-system.component';
import ExternalSystemService from '@/entities/external-system/external-system.service';
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
  describe('ExternalSystem Management Component', () => {
    let wrapper: Wrapper<ExternalSystemClass>;
    let comp: ExternalSystemClass;
    let externalSystemServiceStub: SinonStubbedInstance<ExternalSystemService>;

    beforeEach(() => {
      externalSystemServiceStub = sinon.createStubInstance<ExternalSystemService>(ExternalSystemService);
      externalSystemServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<ExternalSystemClass>(ExternalSystemComponent, {
        store,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          externalSystemService: () => externalSystemServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      externalSystemServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllExternalSystems();
      await comp.$nextTick();

      // THEN
      expect(externalSystemServiceStub.retrieve.called).toBeTruthy();
      expect(comp.externalSystems[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      externalSystemServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      expect(externalSystemServiceStub.retrieve.callCount).toEqual(1);

      comp.removeExternalSystem();
      await comp.$nextTick();

      // THEN
      expect(externalSystemServiceStub.delete.called).toBeTruthy();
      expect(externalSystemServiceStub.retrieve.callCount).toEqual(2);
    });
  });
});
