/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import LandscapeView from './landscape-view.vue';
import LandscapeViewService from './landscape-view.service';
import AlertService from '@/shared/alert/alert.service';

type LandscapeViewComponentType = InstanceType<typeof LandscapeView>;
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

  describe('LandscapeView Management Component', () => {
    let landscapeViewServiceStub: SinonStubbedInstance<LandscapeViewService>;
    let mountOptions: MountingOptions<LandscapeViewComponentType>['global'];

    beforeEach(() => {
      landscapeViewServiceStub = sinon.createStubInstance<LandscapeViewService>(LandscapeViewService);
      landscapeViewServiceStub.retrieve.resolves({ headers: {} });

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
          landscapeViewService: () => landscapeViewServiceStub,
          accountService,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        landscapeViewServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(LandscapeView, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(landscapeViewServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.landscapeViews[0]).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
    describe('Handles', () => {
      let comp: LandscapeViewComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(LandscapeView, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        landscapeViewServiceStub.retrieve.reset();
        landscapeViewServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        landscapeViewServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeLandscapeView();
        await comp.$nextTick(); // clear components

        // THEN
        expect(landscapeViewServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(landscapeViewServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
