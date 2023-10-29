/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import Application from './application.vue';
import ApplicationService from './application.service';
import AlertService from '@/shared/alert/alert.service';

type ApplicationComponentType = InstanceType<typeof Application>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('Application Management Component', () => {
    let applicationServiceStub: SinonStubbedInstance<ApplicationService>;
    let mountOptions: MountingOptions<ApplicationComponentType>['global'];

    beforeEach(() => {
      applicationServiceStub = sinon.createStubInstance<ApplicationService>(ApplicationService);
      applicationServiceStub.retrieve.resolves({ headers: {} });

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
          applicationService: () => applicationServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        applicationServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(Application, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(applicationServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.applications[0]).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
    describe('Handles', () => {
      let comp: ApplicationComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(Application, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        applicationServiceStub.retrieve.reset();
        applicationServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('Should call delete service on confirmDelete', async () => {
        //   // GIVEN
        //   applicationServiceStub.delete.resolves({});
        //   // WHEN
        //   comp.prepareRemove({ id: 123 });
        //   comp.removeApplication();
        //   await comp.$nextTick(); // clear components
        //   // THEN
        //   expect(applicationServiceStub.delete.called).toBeTruthy();
        //   // THEN
        //   await comp.$nextTick(); // handle component clear watch
        //   expect(applicationServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
