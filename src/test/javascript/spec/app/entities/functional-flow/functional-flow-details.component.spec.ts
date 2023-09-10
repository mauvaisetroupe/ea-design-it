/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import FunctionalFlowDetailComponent from '@/entities/functional-flow/functional-flow-details.vue';
import FunctionalFlowClass from '@/entities/functional-flow/functional-flow-details.component';
import FunctionalFlowService from '@/entities/functional-flow/functional-flow.service';
import router from '@/router';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('FunctionalFlow Management Detail Component', () => {
    let wrapper: Wrapper<FunctionalFlowClass>;
    let comp: FunctionalFlowClass;
    let functionalFlowServiceStub: SinonStubbedInstance<FunctionalFlowService>;

    const accountService = { hasAnyAuthorityAndCheckAuth: jest.fn().mockImplementation(() => Promise.resolve(true)) };

    beforeEach(() => {
      functionalFlowServiceStub = sinon.createStubInstance<FunctionalFlowService>(FunctionalFlowService);

      wrapper = shallowMount<FunctionalFlowClass>(FunctionalFlowDetailComponent, {
        store,
        localVue,
        router,
        provide: {
          functionalFlowService: () => functionalFlowServiceStub,
          alertService: () => new AlertService(),
          accountService: () => accountService,
        },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundFunctionalFlow = { id: 123 };
        functionalFlowServiceStub.find.resolves(foundFunctionalFlow);
        functionalFlowServiceStub.getPlantUML.resolves('@startuml\nAlice -> Bob: Authentication Request\n@enduml');

        // WHEN
        comp.retrieveFunctionalFlow(123);
        await comp.$nextTick();

        // THEN
        expect(comp.functionalFlow).toBe(foundFunctionalFlow);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundFunctionalFlow = { id: 123 };
        functionalFlowServiceStub.find.resolves(foundFunctionalFlow);
        functionalFlowServiceStub.getPlantUML.resolves('@startuml\nAlice -> Bob: Authentication Request\n@enduml');

        // WHEN
        comp.beforeRouteEnter({ params: { functionalFlowId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.functionalFlow).toBe(foundFunctionalFlow);
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
