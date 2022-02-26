/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import ApplicationCategoryComponent from '@/entities/application-category/application-category.vue';
import ApplicationCategoryClass from '@/entities/application-category/application-category.component';
import ApplicationCategoryService from '@/entities/application-category/application-category.service';
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
  describe('ApplicationCategory Management Component', () => {
    let wrapper: Wrapper<ApplicationCategoryClass>;
    let comp: ApplicationCategoryClass;
    let applicationCategoryServiceStub: SinonStubbedInstance<ApplicationCategoryService>;

    beforeEach(() => {
      applicationCategoryServiceStub = sinon.createStubInstance<ApplicationCategoryService>(ApplicationCategoryService);
      applicationCategoryServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<ApplicationCategoryClass>(ApplicationCategoryComponent, {
        store,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          applicationCategoryService: () => applicationCategoryServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      applicationCategoryServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllApplicationCategorys();
      await comp.$nextTick();

      // THEN
      expect(applicationCategoryServiceStub.retrieve.called).toBeTruthy();
      expect(comp.applicationCategories[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      applicationCategoryServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      expect(applicationCategoryServiceStub.retrieve.callCount).toEqual(1);

      comp.removeApplicationCategory();
      await comp.$nextTick();

      // THEN
      expect(applicationCategoryServiceStub.delete.called).toBeTruthy();
      expect(applicationCategoryServiceStub.retrieve.callCount).toEqual(2);
    });
  });
});
