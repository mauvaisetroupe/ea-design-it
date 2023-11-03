/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import DataObjectUpdate from './data-object-update.vue';
import DataObjectService from './data-object.service';
import AlertService from '@/shared/alert/alert.service';

import ApplicationService from '@/entities/application/application.service';
import OwnerService from '@/entities/owner/owner.service';
import TechnologyService from '@/entities/technology/technology.service';
import LandscapeViewService from '@/entities/landscape-view/landscape-view.service';
import BusinessObjectService from '@/entities/business-object/business-object.service';

type DataObjectUpdateComponentType = InstanceType<typeof DataObjectUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const dataObjectSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<DataObjectUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('DataObject Management Update Component', () => {
    let comp: DataObjectUpdateComponentType;
    let dataObjectServiceStub: SinonStubbedInstance<DataObjectService>;

    beforeEach(() => {
      route = {};
      dataObjectServiceStub = sinon.createStubInstance<DataObjectService>(DataObjectService);
      dataObjectServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          dataObjectService: () => dataObjectServiceStub,
          applicationService: () =>
            sinon.createStubInstance<ApplicationService>(ApplicationService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
          ownerService: () =>
            sinon.createStubInstance<OwnerService>(OwnerService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
          technologyService: () =>
            sinon.createStubInstance<TechnologyService>(TechnologyService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
          landscapeViewService: () =>
            sinon.createStubInstance<LandscapeViewService>(LandscapeViewService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
          businessObjectService: () =>
            sinon.createStubInstance<BusinessObjectService>(BusinessObjectService, {
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
        const wrapper = shallowMount(DataObjectUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.dataObject = dataObjectSample;
        dataObjectServiceStub.update.resolves(dataObjectSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(dataObjectServiceStub.update.calledWith(dataObjectSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        dataObjectServiceStub.create.resolves(entity);
        const wrapper = shallowMount(DataObjectUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.dataObject = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(dataObjectServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        dataObjectServiceStub.find.resolves(dataObjectSample);
        dataObjectServiceStub.retrieve.resolves([dataObjectSample]);

        // WHEN
        route = {
          params: {
            dataObjectId: '' + dataObjectSample.id,
          },
        };
        const wrapper = shallowMount(DataObjectUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.dataObject).toMatchObject(dataObjectSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        dataObjectServiceStub.find.resolves(dataObjectSample);
        const wrapper = shallowMount(DataObjectUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
