/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import OwnerDetailComponent from '@/entities/owner/owner-details.vue';
import OwnerClass from '@/entities/owner/owner-details.component';
import OwnerService from '@/entities/owner/owner.service';
import router from '@/router';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('Owner Management Detail Component', () => {
    let wrapper: Wrapper<OwnerClass>;
    let comp: OwnerClass;
    let ownerServiceStub: SinonStubbedInstance<OwnerService>;

    beforeEach(() => {
      ownerServiceStub = sinon.createStubInstance<OwnerService>(OwnerService);

      wrapper = shallowMount<OwnerClass>(OwnerDetailComponent, {
        store,
        localVue,
        router,
        provide: { ownerService: () => ownerServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundOwner = { id: 123 };
        ownerServiceStub.find.resolves(foundOwner);

        // WHEN
        comp.retrieveOwner(123);
        await comp.$nextTick();

        // THEN
        expect(comp.owner).toBe(foundOwner);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundOwner = { id: 123 };
        ownerServiceStub.find.resolves(foundOwner);

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
