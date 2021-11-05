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
localVue.component('jhi-sort-indicator', {});
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
        stubs: { jhiItemCount: true, bPagination: true, bModal: bModalStub as any },
        provide: {
          applicationService: () => applicationServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      applicationServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 'ABC' }] });

      // WHEN
      comp.retrieveAllApplications();
      await comp.$nextTick();

      // THEN
      expect(applicationServiceStub.retrieve.called).toBeTruthy();
      expect(comp.applications[0]).toEqual(expect.objectContaining({ id: 'ABC' }));
    });

    it('should load a page', async () => {
      // GIVEN
      applicationServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 'ABC' }] });
      comp.previousPage = 1;

      // WHEN
      comp.loadPage(2);
      await comp.$nextTick();

      // THEN
      expect(applicationServiceStub.retrieve.called).toBeTruthy();
      expect(comp.applications[0]).toEqual(expect.objectContaining({ id: 'ABC' }));
    });

    it('should not load a page if the page is the same as the previous page', () => {
      // GIVEN
      applicationServiceStub.retrieve.reset();
      comp.previousPage = 1;

      // WHEN
      comp.loadPage(1);

      // THEN
      expect(applicationServiceStub.retrieve.called).toBeFalsy();
    });

    it('should re-initialize the page', async () => {
      // GIVEN
      applicationServiceStub.retrieve.reset();
      applicationServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 'ABC' }] });

      // WHEN
      comp.loadPage(2);
      await comp.$nextTick();
      comp.clear();
      await comp.$nextTick();

      // THEN
      expect(applicationServiceStub.retrieve.callCount).toEqual(3);
      expect(comp.page).toEqual(1);
      expect(comp.applications[0]).toEqual(expect.objectContaining({ id: 'ABC' }));
    });

    it('should calculate the sort attribute for an id', () => {
      // WHEN
      const result = comp.sort();

      // THEN
      expect(result).toEqual(['id,asc']);
    });

    it('should calculate the sort attribute for a non-id attribute', () => {
      // GIVEN
      comp.propOrder = 'name';

      // WHEN
      const result = comp.sort();

      // THEN
      expect(result).toEqual(['name,asc', 'id']);
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      applicationServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 'ABC' });
      comp.removeApplication();
      await comp.$nextTick();

      // THEN
      expect(applicationServiceStub.delete.called).toBeTruthy();
      expect(applicationServiceStub.retrieve.callCount).toEqual(1);
    });
  });
});
