/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import ApplicationDetailComponent from '@/entities/application/application-details.vue';
import ApplicationClass from '@/entities/application/application-details.component';
import ApplicationService from '@/entities/application/application.service';
import router from '@/router';
import AlertService from '@/shared/alert/alert.service';
import { PlantumlDTO } from '@/shared/model/plantuml-dto';
import { ICapability } from '@/shared/model/capability.model';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('Application Management Detail Component', () => {
    let wrapper: Wrapper<ApplicationClass>;
    let comp: ApplicationClass;
    let applicationServiceStub: SinonStubbedInstance<ApplicationService>;
    let alertServiceStub: SinonStubbedInstance<AlertService>;

    const accountService = { hasAnyAuthorityAndCheckAuth: jest.fn().mockImplementation(() => Promise.resolve(true)) };

    beforeEach(() => {
      applicationServiceStub = sinon.createStubInstance<ApplicationService>(ApplicationService);
      alertServiceStub = sinon.createStubInstance<AlertService>(AlertService);

      wrapper = shallowMount<ApplicationClass>(ApplicationDetailComponent, {
        store,
        localVue,
        router,
        provide: {
          applicationService: () => applicationServiceStub,
          alertService: () => alertServiceStub,
          accountService: () => accountService,
        },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundApplication = { id: 123 };
        applicationServiceStub.find.resolves(foundApplication);
        const plantumldto: PlantumlDTO = {
          svg: 'string',
          interfaces: [],
          flows: [],
          labelsShown: false,
        };
        applicationServiceStub.getPlantUML.resolves(plantumldto);
        applicationServiceStub.getApplicationStructurePlantUML.resolves('<svg></svg>');
        const capabilities: ICapability[] = [{}];
        applicationServiceStub.getCapabilities.resolves(capabilities);

        // WHEN
        comp.retrieveApplication(123);
        await comp.$nextTick();

        // THEN
        expect(comp.application).toBe(foundApplication);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundApplication = { id: 123 };
        applicationServiceStub.find.resolves(foundApplication);
        const plantumldto: PlantumlDTO = {
          svg: 'string',
          interfaces: [],
          flows: [],
          labelsShown: false,
        };
        applicationServiceStub.getPlantUML.resolves(plantumldto);
        applicationServiceStub.getApplicationStructurePlantUML.resolves('<svg></svg>');
        const capabilities: ICapability[] = [{}];
        applicationServiceStub.getCapabilities.resolves(capabilities);

        // WHEN
        comp.beforeRouteEnter({ params: { applicationId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.application).toBe(foundApplication);
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
