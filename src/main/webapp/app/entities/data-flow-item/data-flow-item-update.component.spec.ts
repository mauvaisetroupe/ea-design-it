/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import DataFlowItemUpdate from './data-flow-item-update.vue';
import DataFlowItemService from './data-flow-item.service';
import AlertService from '@/shared/alert/alert.service';

import DataFlowService from '@/entities/data-flow/data-flow.service';

type DataFlowItemUpdateComponentType = InstanceType<typeof DataFlowItemUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const dataFlowItemSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<DataFlowItemUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('DataFlowItem Management Update Component', () => {
    let comp: DataFlowItemUpdateComponentType;
    let dataFlowItemServiceStub: SinonStubbedInstance<DataFlowItemService>;

    beforeEach(() => {
      route = {};
      dataFlowItemServiceStub = sinon.createStubInstance<DataFlowItemService>(DataFlowItemService);
      dataFlowItemServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          dataFlowItemService: () => dataFlowItemServiceStub,
          dataFlowService: () =>
            sinon.createStubInstance<DataFlowService>(DataFlowService, {
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
        const wrapper = shallowMount(DataFlowItemUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.dataFlowItem = dataFlowItemSample;
        dataFlowItemServiceStub.update.resolves(dataFlowItemSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(dataFlowItemServiceStub.update.calledWith(dataFlowItemSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        dataFlowItemServiceStub.create.resolves(entity);
        const wrapper = shallowMount(DataFlowItemUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.dataFlowItem = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(dataFlowItemServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        dataFlowItemServiceStub.find.resolves(dataFlowItemSample);
        dataFlowItemServiceStub.retrieve.resolves([dataFlowItemSample]);

        // WHEN
        route = {
          params: {
            dataFlowItemId: '' + dataFlowItemSample.id,
          },
        };
        const wrapper = shallowMount(DataFlowItemUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.dataFlowItem).toMatchObject(dataFlowItemSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        dataFlowItemServiceStub.find.resolves(dataFlowItemSample);
        const wrapper = shallowMount(DataFlowItemUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
