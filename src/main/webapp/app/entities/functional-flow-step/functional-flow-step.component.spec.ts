/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import FunctionalFlowStep from './functional-flow-step.vue';
import FunctionalFlowStepService from './functional-flow-step.service';
import AlertService from '@/shared/alert/alert.service';

type FunctionalFlowStepComponentType = InstanceType<typeof FunctionalFlowStep>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('FunctionalFlowStep Management Component', () => {
    let functionalFlowStepServiceStub: SinonStubbedInstance<FunctionalFlowStepService>;
    let mountOptions: MountingOptions<FunctionalFlowStepComponentType>['global'];

    beforeEach(() => {
      functionalFlowStepServiceStub = sinon.createStubInstance<FunctionalFlowStepService>(FunctionalFlowStepService);
      functionalFlowStepServiceStub.retrieve.resolves({ headers: {} });

      alertService = new AlertService({
        bvToast: {
          toast: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          bModal: bModalStub as any,
          'font-awesome-icon': true,
          'b-badge': true,
          'b-button': true,
          'router-link': true,
        },
        directives: {
          'b-modal': {},
        },
        provide: {
          alertService,
          functionalFlowStepService: () => functionalFlowStepServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        functionalFlowStepServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(FunctionalFlowStep, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(functionalFlowStepServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.functionalFlowSteps[0]).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
    describe('Handles', () => {
      let comp: FunctionalFlowStepComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(FunctionalFlowStep, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        functionalFlowStepServiceStub.retrieve.reset();
        functionalFlowStepServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        functionalFlowStepServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeFunctionalFlowStep();
        await comp.$nextTick(); // clear components

        // THEN
        expect(functionalFlowStepServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(functionalFlowStepServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
