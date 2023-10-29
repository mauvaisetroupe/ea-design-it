/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import CapabilityApplicationMappingUpdate from './capability-application-mapping-update.vue';
import CapabilityApplicationMappingService from './capability-application-mapping.service';
import AlertService from '@/shared/alert/alert.service';

import CapabilityService from '@/entities/capability/capability.service';
import ApplicationService from '@/entities/application/application.service';
import LandscapeViewService from '@/entities/landscape-view/landscape-view.service';

type CapabilityApplicationMappingUpdateComponentType = InstanceType<typeof CapabilityApplicationMappingUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const capabilityApplicationMappingSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<CapabilityApplicationMappingUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('CapabilityApplicationMapping Management Update Component', () => {
    let comp: CapabilityApplicationMappingUpdateComponentType;
    let capabilityApplicationMappingServiceStub: SinonStubbedInstance<CapabilityApplicationMappingService>;

    beforeEach(() => {
      route = {};
      capabilityApplicationMappingServiceStub =
        sinon.createStubInstance<CapabilityApplicationMappingService>(CapabilityApplicationMappingService);
      capabilityApplicationMappingServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          capabilityApplicationMappingService: () => capabilityApplicationMappingServiceStub,
          capabilityService: () =>
            sinon.createStubInstance<CapabilityService>(CapabilityService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
          applicationService: () =>
            sinon.createStubInstance<ApplicationService>(ApplicationService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
          landscapeViewService: () =>
            sinon.createStubInstance<LandscapeViewService>(LandscapeViewService, {
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
        const wrapper = shallowMount(CapabilityApplicationMappingUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.capabilityApplicationMapping = capabilityApplicationMappingSample;
        capabilityApplicationMappingServiceStub.update.resolves(capabilityApplicationMappingSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(capabilityApplicationMappingServiceStub.update.calledWith(capabilityApplicationMappingSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        capabilityApplicationMappingServiceStub.create.resolves(entity);
        const wrapper = shallowMount(CapabilityApplicationMappingUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.capabilityApplicationMapping = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(capabilityApplicationMappingServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        capabilityApplicationMappingServiceStub.find.resolves(capabilityApplicationMappingSample);
        capabilityApplicationMappingServiceStub.retrieve.resolves([capabilityApplicationMappingSample]);

        // WHEN
        route = {
          params: {
            capabilityApplicationMappingId: '' + capabilityApplicationMappingSample.id,
          },
        };
        const wrapper = shallowMount(CapabilityApplicationMappingUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.capabilityApplicationMapping).toMatchObject(capabilityApplicationMappingSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        capabilityApplicationMappingServiceStub.find.resolves(capabilityApplicationMappingSample);
        const wrapper = shallowMount(CapabilityApplicationMappingUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
