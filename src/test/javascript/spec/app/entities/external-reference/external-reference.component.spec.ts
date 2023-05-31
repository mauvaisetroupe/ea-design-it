/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import ExternalReferenceComponent from '@/entities/external-reference/external-reference.vue';
import ExternalReferenceClass from '@/entities/external-reference/external-reference.component';
import ExternalReferenceService from '@/entities/external-reference/external-reference.service';
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
  describe('ExternalReference Management Component', () => {
    let wrapper: Wrapper<ExternalReferenceClass>;
    let comp: ExternalReferenceClass;
    let externalReferenceServiceStub: SinonStubbedInstance<ExternalReferenceService>;

    beforeEach(() => {
      externalReferenceServiceStub = sinon.createStubInstance<ExternalReferenceService>(ExternalReferenceService);
      externalReferenceServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<ExternalReferenceClass>(ExternalReferenceComponent, {
        store,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          externalReferenceService: () => externalReferenceServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      externalReferenceServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllExternalReferences();
      await comp.$nextTick();

      // THEN
      expect(externalReferenceServiceStub.retrieve.called).toBeTruthy();
      expect(comp.externalReferences[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      externalReferenceServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      expect(externalReferenceServiceStub.retrieve.callCount).toEqual(1);

      comp.removeExternalReference();
      await comp.$nextTick();

      // THEN
      expect(externalReferenceServiceStub.delete.called).toBeTruthy();
      expect(externalReferenceServiceStub.retrieve.callCount).toEqual(2);
    });
  });
});
