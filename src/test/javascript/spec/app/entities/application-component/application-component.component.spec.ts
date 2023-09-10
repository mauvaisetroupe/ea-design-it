/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import ApplicationComponentComponent from '@/entities/application-component/application-component.vue';
import ApplicationComponentClass from '@/entities/application-component/application-component.component';
import ApplicationComponentService from '@/entities/application-component/application-component.service';
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
  describe('ApplicationComponent Management Component', () => {
    let wrapper: Wrapper<ApplicationComponentClass>;
    let comp: ApplicationComponentClass;
    let applicationComponentServiceStub: SinonStubbedInstance<ApplicationComponentService>;

    const accountService = { hasAnyAuthorityAndCheckAuth: jest.fn().mockImplementation(() => Promise.resolve(true)) };

    beforeEach(() => {
      applicationComponentServiceStub = sinon.createStubInstance<ApplicationComponentService>(ApplicationComponentService);
      applicationComponentServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<ApplicationComponentClass>(ApplicationComponentComponent, {
        store,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          applicationComponentService: () => applicationComponentServiceStub,
          alertService: () => new AlertService(),
          accountService: () => accountService,
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      applicationComponentServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllApplicationComponents();
      await comp.$nextTick();

      // THEN
      expect(applicationComponentServiceStub.retrieve.called).toBeTruthy();
      expect(comp.applicationComponents[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      applicationComponentServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      expect(applicationComponentServiceStub.retrieve.callCount).toEqual(1);

      comp.removeApplicationComponent();
      await comp.$nextTick();

      // THEN
      expect(applicationComponentServiceStub.delete.called).toBeTruthy();
      expect(applicationComponentServiceStub.retrieve.callCount).toEqual(2);
    });
  });
});
