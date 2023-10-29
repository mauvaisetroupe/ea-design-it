/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import FunctionalFlow from './functional-flow.vue';
import FunctionalFlowService from './functional-flow.service';
import AlertService from '@/shared/alert/alert.service';

type FunctionalFlowComponentType = InstanceType<typeof FunctionalFlow>;
const accountService = { hasAnyAuthorityAndCheckAuth: vitest.fn().mockImplementation(() => Promise.resolve(true)) };

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('FunctionalFlow Management Component', () => {
    let functionalFlowServiceStub: SinonStubbedInstance<FunctionalFlowService>;
    let mountOptions: MountingOptions<FunctionalFlowComponentType>['global'];

    beforeEach(() => {
      functionalFlowServiceStub = sinon.createStubInstance<FunctionalFlowService>(FunctionalFlowService);
      functionalFlowServiceStub.retrieve.resolves({ headers: {} });

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
          functionalFlowService: () => functionalFlowServiceStub,
          accountService,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        functionalFlowServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(FunctionalFlow, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(functionalFlowServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.functionalFlows[0]).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
    describe('Handles', () => {
      let comp: FunctionalFlowComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(FunctionalFlow, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        functionalFlowServiceStub.retrieve.reset();
        functionalFlowServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        functionalFlowServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeFunctionalFlow();
        await comp.$nextTick(); // clear components

        // THEN
        expect(functionalFlowServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(functionalFlowServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
