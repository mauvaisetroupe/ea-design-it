/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import OwnerComponent from '@/entities/owner/owner.vue';
import OwnerClass from '@/entities/owner/owner.component';
import OwnerService from '@/entities/owner/owner.service';
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
  describe('Owner Management Component', () => {
    let wrapper: Wrapper<OwnerClass>;
    let comp: OwnerClass;
    let ownerServiceStub: SinonStubbedInstance<OwnerService>;

    beforeEach(() => {
      ownerServiceStub = sinon.createStubInstance<OwnerService>(OwnerService);
      ownerServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<OwnerClass>(OwnerComponent, {
        store,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          ownerService: () => ownerServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      ownerServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllOwners();
      await comp.$nextTick();

      // THEN
      expect(ownerServiceStub.retrieve.called).toBeTruthy();
      expect(comp.owners[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      ownerServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      expect(ownerServiceStub.retrieve.callCount).toEqual(1);

      comp.removeOwner();
      await comp.$nextTick();

      // THEN
      expect(ownerServiceStub.delete.called).toBeTruthy();
      expect(ownerServiceStub.retrieve.callCount).toEqual(2);
    });
  });
});
