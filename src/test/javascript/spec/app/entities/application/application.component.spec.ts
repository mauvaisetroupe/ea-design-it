/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';

import * as config from '@/shared/config/config';
import ApplicationComponent from '@/entities/application/application.vue';
import ApplicationClass from '@/entities/application/application.component';
import ApplicationService from '@/entities/application/application.service';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();

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
  describe('Application Management Component', () => {
    let wrapper: Wrapper<ApplicationClass>;
    let comp: ApplicationClass;
    let applicationServiceStub: SinonStubbedInstance<ApplicationService>;

    beforeEach(() => {
      applicationServiceStub = sinon.createStubInstance<ApplicationService>(ApplicationService);
      applicationServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<ApplicationClass>(ApplicationComponent, {
        store,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          applicationService: () => applicationServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      applicationServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllApplications();
      await comp.$nextTick();

      // THEN
      expect(applicationServiceStub.retrieve.called).toBeTruthy();
      expect(comp.applications[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      applicationServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      comp.removeApplication();
      await comp.$nextTick();

      // THEN
      expect(applicationServiceStub.delete.called).toBeTruthy();
      expect(applicationServiceStub.retrieve.callCount).toEqual(1);
    });
  });
});
