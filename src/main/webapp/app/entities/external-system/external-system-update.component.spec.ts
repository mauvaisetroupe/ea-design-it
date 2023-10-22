/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import ExternalSystemUpdate from './external-system-update.vue';
import ExternalSystemService from './external-system.service';
import AlertService from '@/shared/alert/alert.service';

type ExternalSystemUpdateComponentType = InstanceType<typeof ExternalSystemUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const externalSystemSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<ExternalSystemUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('ExternalSystem Management Update Component', () => {
    let comp: ExternalSystemUpdateComponentType;
    let externalSystemServiceStub: SinonStubbedInstance<ExternalSystemService>;

    beforeEach(() => {
      route = {};
      externalSystemServiceStub = sinon.createStubInstance<ExternalSystemService>(ExternalSystemService);
      externalSystemServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          externalSystemService: () => externalSystemServiceStub,
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(ExternalSystemUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.externalSystem = externalSystemSample;
        externalSystemServiceStub.update.resolves(externalSystemSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(externalSystemServiceStub.update.calledWith(externalSystemSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        externalSystemServiceStub.create.resolves(entity);
        const wrapper = shallowMount(ExternalSystemUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.externalSystem = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(externalSystemServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        externalSystemServiceStub.find.resolves(externalSystemSample);
        externalSystemServiceStub.retrieve.resolves([externalSystemSample]);

        // WHEN
        route = {
          params: {
            externalSystemId: '' + externalSystemSample.id,
          },
        };
        const wrapper = shallowMount(ExternalSystemUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.externalSystem).toMatchObject(externalSystemSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        externalSystemServiceStub.find.resolves(externalSystemSample);
        const wrapper = shallowMount(ExternalSystemUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
