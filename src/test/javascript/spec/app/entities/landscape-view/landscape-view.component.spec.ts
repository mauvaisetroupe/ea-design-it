/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';

import * as config from '@/shared/config/config';
import LandscapeViewComponent from '@/entities/landscape-view/landscape-view.vue';
import LandscapeViewClass from '@/entities/landscape-view/landscape-view.component';
import LandscapeViewService from '@/entities/landscape-view/landscape-view.service';
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
  describe('LandscapeView Management Component', () => {
    let wrapper: Wrapper<LandscapeViewClass>;
    let comp: LandscapeViewClass;
    let landscapeViewServiceStub: SinonStubbedInstance<LandscapeViewService>;

    beforeEach(() => {
      landscapeViewServiceStub = sinon.createStubInstance<LandscapeViewService>(LandscapeViewService);
      landscapeViewServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<LandscapeViewClass>(LandscapeViewComponent, {
        store,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          landscapeViewService: () => landscapeViewServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      landscapeViewServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllLandscapeViews();
      await comp.$nextTick();

      // THEN
      expect(landscapeViewServiceStub.retrieve.called).toBeTruthy();
      expect(comp.landscapeViews[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      landscapeViewServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      comp.removeLandscapeView();
      await comp.$nextTick();

      // THEN
      expect(landscapeViewServiceStub.delete.called).toBeTruthy();
      expect(landscapeViewServiceStub.retrieve.callCount).toEqual(1);
    });
  });
});
