/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import Technology from './technology.vue';
import TechnologyService from './technology.service';
import AlertService from '@/shared/alert/alert.service';

type TechnologyComponentType = InstanceType<typeof Technology>;
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

  describe('Technology Management Component', () => {
    let technologyServiceStub: SinonStubbedInstance<TechnologyService>;
    let mountOptions: MountingOptions<TechnologyComponentType>['global'];

    beforeEach(() => {
      technologyServiceStub = sinon.createStubInstance<TechnologyService>(TechnologyService);
      technologyServiceStub.retrieve.resolves({ headers: {} });

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
          technologyService: () => technologyServiceStub,
          accountService,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        technologyServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(Technology, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(technologyServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.technologies[0]).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
    describe('Handles', () => {
      let comp: TechnologyComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(Technology, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        technologyServiceStub.retrieve.reset();
        technologyServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        technologyServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeTechnology();
        await comp.$nextTick(); // clear components

        // THEN
        expect(technologyServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(technologyServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
