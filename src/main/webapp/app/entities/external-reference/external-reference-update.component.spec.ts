/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import ExternalReferenceUpdate from './external-reference-update.vue';
import ExternalReferenceService from './external-reference.service';
import AlertService from '@/shared/alert/alert.service';

import ExternalSystemService from '@/entities/external-system/external-system.service';

type ExternalReferenceUpdateComponentType = InstanceType<typeof ExternalReferenceUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const externalReferenceSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<ExternalReferenceUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('ExternalReference Management Update Component', () => {
    let comp: ExternalReferenceUpdateComponentType;
    let externalReferenceServiceStub: SinonStubbedInstance<ExternalReferenceService>;

    beforeEach(() => {
      route = {};
      externalReferenceServiceStub = sinon.createStubInstance<ExternalReferenceService>(ExternalReferenceService);
      externalReferenceServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          externalReferenceService: () => externalReferenceServiceStub,
          externalSystemService: () =>
            sinon.createStubInstance<ExternalSystemService>(ExternalSystemService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(ExternalReferenceUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.externalReference = externalReferenceSample;
        externalReferenceServiceStub.update.resolves(externalReferenceSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(externalReferenceServiceStub.update.calledWith(externalReferenceSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        externalReferenceServiceStub.create.resolves(entity);
        const wrapper = shallowMount(ExternalReferenceUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.externalReference = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(externalReferenceServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        externalReferenceServiceStub.find.resolves(externalReferenceSample);
        externalReferenceServiceStub.retrieve.resolves([externalReferenceSample]);

        // WHEN
        route = {
          params: {
            externalReferenceId: '' + externalReferenceSample.id,
          },
        };
        const wrapper = shallowMount(ExternalReferenceUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.externalReference).toMatchObject(externalReferenceSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        externalReferenceServiceStub.find.resolves(externalReferenceSample);
        const wrapper = shallowMount(ExternalReferenceUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
