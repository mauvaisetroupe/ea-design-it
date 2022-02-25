/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import ApplicationImportComponent from '@/entities/application-import/application-import.vue';
import ApplicationImportClass from '@/entities/application-import/application-import.component';
import ApplicationImportService from '@/entities/application-import/application-import.service';
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
  describe('ApplicationImport Management Component', () => {
    let wrapper: Wrapper<ApplicationImportClass>;
    let comp: ApplicationImportClass;
    let applicationImportServiceStub: SinonStubbedInstance<ApplicationImportService>;

    beforeEach(() => {
      applicationImportServiceStub = sinon.createStubInstance<ApplicationImportService>(ApplicationImportService);
      applicationImportServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<ApplicationImportClass>(ApplicationImportComponent, {
        store,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          applicationImportService: () => applicationImportServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      applicationImportServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllApplicationImports();
      await comp.$nextTick();

      // THEN
      expect(applicationImportServiceStub.retrieve.called).toBeTruthy();
      expect(comp.applicationImports[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      applicationImportServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      expect(applicationImportServiceStub.retrieve.callCount).toEqual(1);

      comp.removeApplicationImport();
      await comp.$nextTick();

      // THEN
      expect(applicationImportServiceStub.delete.called).toBeTruthy();
      expect(applicationImportServiceStub.retrieve.callCount).toEqual(2);
    });
  });
});
