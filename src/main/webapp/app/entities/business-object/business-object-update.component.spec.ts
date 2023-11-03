/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import BusinessObjectUpdate from './business-object-update.vue';
import BusinessObjectService from './business-object.service';
import AlertService from '@/shared/alert/alert.service';

import OwnerService from '@/entities/owner/owner.service';

type BusinessObjectUpdateComponentType = InstanceType<typeof BusinessObjectUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const businessObjectSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<BusinessObjectUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('BusinessObject Management Update Component', () => {
    let comp: BusinessObjectUpdateComponentType;
    let businessObjectServiceStub: SinonStubbedInstance<BusinessObjectService>;

    beforeEach(() => {
      route = {};
      businessObjectServiceStub = sinon.createStubInstance<BusinessObjectService>(BusinessObjectService);
      businessObjectServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          businessObjectService: () => businessObjectServiceStub,
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
        const wrapper = shallowMount(BusinessObjectUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.businessObject = businessObjectSample;
        businessObjectServiceStub.update.resolves(businessObjectSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(businessObjectServiceStub.update.calledWith(businessObjectSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        businessObjectServiceStub.create.resolves(entity);
        const wrapper = shallowMount(BusinessObjectUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.businessObject = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(businessObjectServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        businessObjectServiceStub.find.resolves(businessObjectSample);
        businessObjectServiceStub.retrieve.resolves([businessObjectSample]);

        // WHEN
        route = {
          params: {
            businessObjectId: '' + businessObjectSample.id,
          },
        };
        const wrapper = shallowMount(BusinessObjectUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.businessObject).toMatchObject(businessObjectSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        businessObjectServiceStub.find.resolves(businessObjectSample);
        const wrapper = shallowMount(BusinessObjectUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
