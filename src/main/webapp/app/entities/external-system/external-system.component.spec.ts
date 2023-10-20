/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import ExternalSystem from './external-system.vue';
import ExternalSystemService from './external-system.service';
import AlertService from '@/shared/alert/alert.service';

type ExternalSystemComponentType = InstanceType<typeof ExternalSystem>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('ExternalSystem Management Component', () => {
    let externalSystemServiceStub: SinonStubbedInstance<ExternalSystemService>;
    let mountOptions: MountingOptions<ExternalSystemComponentType>['global'];

    beforeEach(() => {
      externalSystemServiceStub = sinon.createStubInstance<ExternalSystemService>(ExternalSystemService);
      externalSystemServiceStub.retrieve.resolves({ headers: {} });

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
          externalSystemService: () => externalSystemServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        externalSystemServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(ExternalSystem, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(externalSystemServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.externalSystems[0]).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
    describe('Handles', () => {
      let comp: ExternalSystemComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(ExternalSystem, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        externalSystemServiceStub.retrieve.reset();
        externalSystemServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        externalSystemServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeExternalSystem();
        await comp.$nextTick(); // clear components

        // THEN
        expect(externalSystemServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(externalSystemServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
