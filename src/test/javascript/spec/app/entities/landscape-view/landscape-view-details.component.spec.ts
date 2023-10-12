/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import LandscapeViewDetailComponent from '@/entities/landscape-view/landscape-view-details.vue';
import LandscapeViewClass from '@/entities/landscape-view/landscape-view-details.component';
import LandscapeViewService from '@/entities/landscape-view/landscape-view.service';
import router from '@/router';
import AlertService from '@/shared/alert/alert.service';
import { ICapability } from '@/shared/model/capability.model';
import { ILandscapeDTO } from '@/shared/model/landscape-view.model';
import FlowImportService from '@/entities/flow-import/flow-import.service';
import FunctionalFlowStepService from '@/entities/functional-flow-step/functional-flow-step.service';
import FunctionalFlowService from '@/entities/functional-flow/functional-flow.service';
import { ILandscapeView, LandscapeView } from '@/shared/model/landscape-view.model';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('LandscapeView Management Detail Component', () => {
    let wrapper: Wrapper<LandscapeViewClass>;
    let comp: LandscapeViewClass;
    let landscapeViewServiceStub: SinonStubbedInstance<LandscapeViewService>;
    let alertServiceStub: SinonStubbedInstance<AlertService>;
    let flowImportServiceStub: SinonStubbedInstance<FlowImportService>;
    let functionalFlowStepServiceStub: SinonStubbedInstance<FunctionalFlowStepService>;
    let functionalFlowServiceStub: SinonStubbedInstance<FunctionalFlowService>;

    const accountService = { hasAnyAuthorityAndCheckAuth: jest.fn().mockImplementation(() => Promise.resolve(true)) };

    beforeEach(() => {
      landscapeViewServiceStub = sinon.createStubInstance<LandscapeViewService>(LandscapeViewService);
      alertServiceStub = sinon.createStubInstance<AlertService>(AlertService);
      flowImportServiceStub = sinon.createStubInstance<FlowImportService>(FlowImportService);
      functionalFlowStepServiceStub = sinon.createStubInstance<FunctionalFlowStepService>(FunctionalFlowStepService);
      functionalFlowServiceStub = sinon.createStubInstance<FunctionalFlowService>(FunctionalFlowService);

      wrapper = shallowMount<LandscapeViewClass>(LandscapeViewDetailComponent, {
        store,
        localVue,
        router,
        provide: {
          landscapeViewService: () => landscapeViewServiceStub,
          alertService: () => alertServiceStub,
          accountService: () => accountService,
          flowImportService: () => flowImportServiceStub,
          functionalFlowStepService: () => functionalFlowStepServiceStub,
          functionalFlowService: () => functionalFlowServiceStub,
        },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        landscapeViewServiceStub.getPlantUML.resolves({
          svg: '@startuml\nAlice -> Bob: Authentication Request\n@enduml',
          labelsShown: false,
          flows: [],
          interfaces: [],
        });
        const lanscapeDTO: ILandscapeDTO = {
          landscape: { id: 123, flows: [] },
          consolidatedCapability: {},
          applicationsOnlyInCapabilities: [],
          applicationsOnlyInFlows: [],
        };
        landscapeViewServiceStub.find.resolves(lanscapeDTO);

        // WHEN
        comp.retrieveLandscapeView(123);
        await comp.$nextTick();

        // const args = alertServiceStub.showHttpError.getCall(0).args;
        // console.log('XXXXXXXXXXXXXXXXXXXXXXXXX Arguments passed to showHttpError:', args);

        // THEN
        expect(alertServiceStub.showError.callCount).toBe(0);
        expect(alertServiceStub.showHttpError.callCount).toBe(0);
        expect(comp.landscapeView.id).toBe(123);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        landscapeViewServiceStub.getPlantUML.resolves({
          svg: '@startuml\nAlice -> Bob: Authentication Request\n@enduml',
          labelsShown: false,
          flows: [],
          interfaces: [],
        });
        const lanscapeDTO: ILandscapeDTO = {
          landscape: { id: 123, flows: [] },
          consolidatedCapability: {},
          applicationsOnlyInCapabilities: [],
          applicationsOnlyInFlows: [],
        };

        landscapeViewServiceStub.find.resolves(lanscapeDTO);

        // WHEN
        comp.beforeRouteEnter({ params: { landscapeViewId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(alertServiceStub.showError.callCount).toBe(0);
        expect(alertServiceStub.showHttpError.callCount).toBe(0);
        expect(comp.landscapeView.id).toBe(123);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        comp.previousState();
        await comp.$nextTick();

        expect(alertServiceStub.showError.callCount).toBe(0);
        expect(alertServiceStub.showHttpError.callCount).toBe(0);
        expect(comp.$router.currentRoute.fullPath).toContain('/');
      });
    });
  });
});
