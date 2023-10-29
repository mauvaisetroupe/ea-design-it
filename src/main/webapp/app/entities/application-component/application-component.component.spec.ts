/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import ApplicationComponent from './application-component.vue';
import ApplicationComponentService from './application-component.service';
import AlertService from '@/shared/alert/alert.service';

type ApplicationComponentComponentType = InstanceType<typeof ApplicationComponent>;
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

  describe('ApplicationComponent Management Component', () => {
    let applicationComponentServiceStub: SinonStubbedInstance<ApplicationComponentService>;
    let mountOptions: MountingOptions<ApplicationComponentComponentType>['global'];

    beforeEach(() => {
      applicationComponentServiceStub = sinon.createStubInstance<ApplicationComponentService>(ApplicationComponentService);
      applicationComponentServiceStub.retrieve.resolves({ headers: {} });

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
          applicationComponentService: () => applicationComponentServiceStub,
          accountService,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        applicationComponentServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(ApplicationComponent, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(applicationComponentServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.applicationComponents[0]).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
    describe('Handles', () => {
      let comp: ApplicationComponentComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(ApplicationComponent, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        applicationComponentServiceStub.retrieve.reset();
        applicationComponentServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        applicationComponentServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeApplicationComponent();
        await comp.$nextTick(); // clear components

        // THEN
        expect(applicationComponentServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(applicationComponentServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
