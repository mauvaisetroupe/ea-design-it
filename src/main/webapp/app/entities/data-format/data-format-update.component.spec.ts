/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import DataFormatUpdate from './data-format-update.vue';
import DataFormatService from './data-format.service';
import AlertService from '@/shared/alert/alert.service';

type DataFormatUpdateComponentType = InstanceType<typeof DataFormatUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const dataFormatSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<DataFormatUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('DataFormat Management Update Component', () => {
    let comp: DataFormatUpdateComponentType;
    let dataFormatServiceStub: SinonStubbedInstance<DataFormatService>;

    beforeEach(() => {
      route = {};
      dataFormatServiceStub = sinon.createStubInstance<DataFormatService>(DataFormatService);
      dataFormatServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          dataFormatService: () => dataFormatServiceStub,
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(DataFormatUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.dataFormat = dataFormatSample;
        dataFormatServiceStub.update.resolves(dataFormatSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(dataFormatServiceStub.update.calledWith(dataFormatSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        dataFormatServiceStub.create.resolves(entity);
        const wrapper = shallowMount(DataFormatUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.dataFormat = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(dataFormatServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        dataFormatServiceStub.find.resolves(dataFormatSample);
        dataFormatServiceStub.retrieve.resolves([dataFormatSample]);

        // WHEN
        route = {
          params: {
            dataFormatId: '' + dataFormatSample.id,
          },
        };
        const wrapper = shallowMount(DataFormatUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.dataFormat).toMatchObject(dataFormatSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        dataFormatServiceStub.find.resolves(dataFormatSample);
        const wrapper = shallowMount(DataFormatUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
