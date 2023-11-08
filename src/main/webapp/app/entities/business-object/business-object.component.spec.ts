/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import BusinessObject from './business-object.vue';
import BusinessObjectService from './business-object.service';
import AlertService from '@/shared/alert/alert.service';

type BusinessObjectComponentType = InstanceType<typeof BusinessObject>;
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

  describe('BusinessObject Management Component', () => {
    let businessObjectServiceStub: SinonStubbedInstance<BusinessObjectService>;
    let mountOptions: MountingOptions<BusinessObjectComponentType>['global'];

    beforeEach(() => {
      businessObjectServiceStub = sinon.createStubInstance<BusinessObjectService>(BusinessObjectService);
      businessObjectServiceStub.retrieve.resolves({ headers: {} });

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
          businessObjectService: () => businessObjectServiceStub,
          accountService,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        businessObjectServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(BusinessObject, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(businessObjectServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.businessObjects[0]).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
    describe('Handles', () => {
      let comp: BusinessObjectComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(BusinessObject, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        businessObjectServiceStub.retrieve.reset();
        businessObjectServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        businessObjectServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeBusinessObject();
        await comp.$nextTick(); // clear components

        // THEN
        expect(businessObjectServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(businessObjectServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
