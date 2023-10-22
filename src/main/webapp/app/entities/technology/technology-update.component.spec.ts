/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import TechnologyUpdate from './technology-update.vue';
import TechnologyService from './technology.service';
import AlertService from '@/shared/alert/alert.service';

type TechnologyUpdateComponentType = InstanceType<typeof TechnologyUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const technologySample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<TechnologyUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('Technology Management Update Component', () => {
    let comp: TechnologyUpdateComponentType;
    let technologyServiceStub: SinonStubbedInstance<TechnologyService>;

    beforeEach(() => {
      route = {};
      technologyServiceStub = sinon.createStubInstance<TechnologyService>(TechnologyService);
      technologyServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

      alertService = new AlertService({
        bvToast: {
          toast: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          'font-awesome-icon': true,
          'b-input-group': true,
          'b-input-group-prepend': true,
          'b-form-datepicker': true,
          'b-form-input': true,
        },
        provide: {
          alertService,
          technologyService: () => technologyServiceStub,
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(TechnologyUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.technology = technologySample;
        technologyServiceStub.update.resolves(technologySample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(technologyServiceStub.update.calledWith(technologySample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        technologyServiceStub.create.resolves(entity);
        const wrapper = shallowMount(TechnologyUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.technology = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(technologyServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        technologyServiceStub.find.resolves(technologySample);
        technologyServiceStub.retrieve.resolves([technologySample]);

        // WHEN
        route = {
          params: {
            technologyId: '' + technologySample.id,
          },
        };
        const wrapper = shallowMount(TechnologyUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.technology).toMatchObject(technologySample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        technologyServiceStub.find.resolves(technologySample);
        const wrapper = shallowMount(TechnologyUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
