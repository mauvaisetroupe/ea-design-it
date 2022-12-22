/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import FlowGroupDetailComponent from '@/entities/flow-group/flow-group-details.vue';
import FlowGroupClass from '@/entities/flow-group/flow-group-details.component';
import FlowGroupService from '@/entities/flow-group/flow-group.service';
import router from '@/router';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('FlowGroup Management Detail Component', () => {
    let wrapper: Wrapper<FlowGroupClass>;
    let comp: FlowGroupClass;
    let flowGroupServiceStub: SinonStubbedInstance<FlowGroupService>;

    beforeEach(() => {
      flowGroupServiceStub = sinon.createStubInstance<FlowGroupService>(FlowGroupService);

      wrapper = shallowMount<FlowGroupClass>(FlowGroupDetailComponent, {
        store,
        localVue,
        router,
        provide: { flowGroupService: () => flowGroupServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundFlowGroup = { id: 123 };
        flowGroupServiceStub.find.resolves(foundFlowGroup);

        // WHEN
        comp.retrieveFlowGroup(123);
        await comp.$nextTick();

        // THEN
        expect(comp.flowGroup).toBe(foundFlowGroup);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundFlowGroup = { id: 123 };
        flowGroupServiceStub.find.resolves(foundFlowGroup);

        // WHEN
        comp.beforeRouteEnter({ params: { flowGroupId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.flowGroup).toBe(foundFlowGroup);
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
