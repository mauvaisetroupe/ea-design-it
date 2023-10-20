/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import DataFlowUpdate from './data-flow-update.vue';
import DataFlowService from './data-flow.service';
import AlertService from '@/shared/alert/alert.service';

import DataFormatService from '@/entities/data-format/data-format.service';
import FunctionalFlowService from '@/entities/functional-flow/functional-flow.service';
import FlowInterfaceService from '@/entities/flow-interface/flow-interface.service';

type DataFlowUpdateComponentType = InstanceType<typeof DataFlowUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const dataFlowSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<DataFlowUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('DataFlow Management Update Component', () => {
    let comp: DataFlowUpdateComponentType;
    let dataFlowServiceStub: SinonStubbedInstance<DataFlowService>;

    beforeEach(() => {
      route = {};
      dataFlowServiceStub = sinon.createStubInstance<DataFlowService>(DataFlowService);
      dataFlowServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          dataFlowService: () => dataFlowServiceStub,
          dataFormatService: () =>
            sinon.createStubInstance<DataFormatService>(DataFormatService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
          functionalFlowService: () =>
            sinon.createStubInstance<FunctionalFlowService>(FunctionalFlowService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
          flowInterfaceService: () =>
            sinon.createStubInstance<FlowInterfaceService>(FlowInterfaceService, {
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
        const wrapper = shallowMount(DataFlowUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.dataFlow = dataFlowSample;
        dataFlowServiceStub.update.resolves(dataFlowSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(dataFlowServiceStub.update.calledWith(dataFlowSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        dataFlowServiceStub.create.resolves(entity);
        const wrapper = shallowMount(DataFlowUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.dataFlow = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(dataFlowServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        dataFlowServiceStub.find.resolves(dataFlowSample);
        dataFlowServiceStub.retrieve.resolves([dataFlowSample]);

        // WHEN
        route = {
          params: {
            dataFlowId: '' + dataFlowSample.id,
          },
        };
        const wrapper = shallowMount(DataFlowUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.dataFlow).toMatchObject(dataFlowSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        dataFlowServiceStub.find.resolves(dataFlowSample);
        const wrapper = shallowMount(DataFlowUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
