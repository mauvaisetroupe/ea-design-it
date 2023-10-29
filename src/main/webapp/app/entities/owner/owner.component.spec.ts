/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import Owner from './owner.vue';
import OwnerService from './owner.service';
import AlertService from '@/shared/alert/alert.service';

type OwnerComponentType = InstanceType<typeof Owner>;
const accountService = { hasAnyAuthorityAndCheckAuth: vitest.fn().mockImplementation(() => Promise.resolve(true)) };

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('Owner Management Component', () => {
    let ownerServiceStub: SinonStubbedInstance<OwnerService>;
    let mountOptions: MountingOptions<OwnerComponentType>['global'];

    beforeEach(() => {
      ownerServiceStub = sinon.createStubInstance<OwnerService>(OwnerService);
      ownerServiceStub.retrieve.resolves({ headers: {} });

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
          ownerService: () => ownerServiceStub,
          accountService,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        ownerServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(Owner, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(ownerServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.owners[0]).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
    describe('Handles', () => {
      let comp: OwnerComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(Owner, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        ownerServiceStub.retrieve.reset();
        ownerServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        ownerServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeOwner();
        await comp.$nextTick(); // clear components

        // THEN
        expect(ownerServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(ownerServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
