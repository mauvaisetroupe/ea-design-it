/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';

import * as config from '@/shared/config/config';
import OwnerUpdateComponent from '@/entities/owner/owner-update.vue';
import OwnerClass from '@/entities/owner/owner-update.component';
import OwnerService from '@/entities/owner/owner.service';

import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
const router = new Router();
localVue.use(Router);
localVue.component('font-awesome-icon', {});
localVue.component('b-input-group', {});
localVue.component('b-input-group-prepend', {});
localVue.component('b-form-datepicker', {});
localVue.component('b-form-input', {});

describe('Component Tests', () => {
  describe('Owner Management Update Component', () => {
    let wrapper: Wrapper<OwnerClass>;
    let comp: OwnerClass;
    let ownerServiceStub: SinonStubbedInstance<OwnerService>;

    beforeEach(() => {
      ownerServiceStub = sinon.createStubInstance<OwnerService>(OwnerService);

      wrapper = shallowMount<OwnerClass>(OwnerUpdateComponent, {
        store,
        localVue,
        router,
        provide: {
          ownerService: () => ownerServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.owner = entity;
        ownerServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(ownerServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.owner = entity;
        ownerServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(ownerServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundOwner = { id: 123 };
        ownerServiceStub.find.resolves(foundOwner);
        ownerServiceStub.retrieve.resolves([foundOwner]);

        // WHEN
        comp.beforeRouteEnter({ params: { ownerId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.owner).toBe(foundOwner);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        comp.previousState();
        await comp.$nextTick();

        expect(comp.$router.currentRoute.fullPath).toContain('/');
      });
    });
  });
});
