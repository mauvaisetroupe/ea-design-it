/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import FlowInterfaceUpdate from './flow-interface-update.vue';
import FlowInterfaceService from './flow-interface.service';
import AlertService from '@/shared/alert/alert.service';

import ApplicationService from '@/entities/application/application.service';
import ApplicationComponentService from '@/entities/application-component/application-component.service';
import ProtocolService from '@/entities/protocol/protocol.service';
import OwnerService from '@/entities/owner/owner.service';

type FlowInterfaceUpdateComponentType = InstanceType<typeof FlowInterfaceUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const flowInterfaceSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<FlowInterfaceUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('FlowInterface Management Update Component', () => {
    let comp: FlowInterfaceUpdateComponentType;
    let flowInterfaceServiceStub: SinonStubbedInstance<FlowInterfaceService>;

    beforeEach(() => {
      route = {};
      flowInterfaceServiceStub = sinon.createStubInstance<FlowInterfaceService>(FlowInterfaceService);
      flowInterfaceServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          flowInterfaceService: () => flowInterfaceServiceStub,
          applicationService: () =>
            sinon.createStubInstance<ApplicationService>(ApplicationService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
          applicationComponentService: () =>
            sinon.createStubInstance<ApplicationComponentService>(ApplicationComponentService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
          protocolService: () =>
            sinon.createStubInstance<ProtocolService>(ProtocolService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
          ownerService: () =>
            sinon.createStubInstance<OwnerService>(OwnerService, {
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
        const wrapper = shallowMount(FlowInterfaceUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.flowInterface = flowInterfaceSample;
        flowInterfaceServiceStub.update.resolves(flowInterfaceSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(flowInterfaceServiceStub.update.calledWith(flowInterfaceSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        flowInterfaceServiceStub.create.resolves(entity);
        const wrapper = shallowMount(FlowInterfaceUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.flowInterface = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(flowInterfaceServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        flowInterfaceServiceStub.find.resolves(flowInterfaceSample);
        flowInterfaceServiceStub.retrieve.resolves([flowInterfaceSample]);

        // WHEN
        route = {
          params: {
            flowInterfaceId: '' + flowInterfaceSample.id,
          },
        };
        const wrapper = shallowMount(FlowInterfaceUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.flowInterface).toMatchObject(flowInterfaceSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        flowInterfaceServiceStub.find.resolves(flowInterfaceSample);
        const wrapper = shallowMount(FlowInterfaceUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
