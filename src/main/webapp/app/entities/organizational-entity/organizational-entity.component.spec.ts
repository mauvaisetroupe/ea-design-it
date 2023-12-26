/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import OrganizationalEntity from './organizational-entity.vue';
import OrganizationalEntityService from './organizational-entity.service';
import AlertService from '@/shared/alert/alert.service';

type OrganizationalEntityComponentType = InstanceType<typeof OrganizationalEntity>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('OrganizationalEntity Management Component', () => {
    let organizationalEntityServiceStub: SinonStubbedInstance<OrganizationalEntityService>;
    let mountOptions: MountingOptions<OrganizationalEntityComponentType>['global'];

    beforeEach(() => {
      organizationalEntityServiceStub = sinon.createStubInstance<OrganizationalEntityService>(OrganizationalEntityService);
      organizationalEntityServiceStub.retrieve.resolves({ headers: {} });

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
          organizationalEntityService: () => organizationalEntityServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        organizationalEntityServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(OrganizationalEntity, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(organizationalEntityServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.organizationalEntities[0]).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
    describe('Handles', () => {
      let comp: OrganizationalEntityComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(OrganizationalEntity, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        organizationalEntityServiceStub.retrieve.reset();
        organizationalEntityServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        organizationalEntityServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeOrganizationalEntity();
        await comp.$nextTick(); // clear components

        // THEN
        expect(organizationalEntityServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(organizationalEntityServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
