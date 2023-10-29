/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import ProtocolUpdate from './protocol-update.vue';
import ProtocolService from './protocol.service';
import AlertService from '@/shared/alert/alert.service';

type ProtocolUpdateComponentType = InstanceType<typeof ProtocolUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const protocolSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<ProtocolUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('Protocol Management Update Component', () => {
    let comp: ProtocolUpdateComponentType;
    let protocolServiceStub: SinonStubbedInstance<ProtocolService>;

    beforeEach(() => {
      route = {};
      protocolServiceStub = sinon.createStubInstance<ProtocolService>(ProtocolService);
      protocolServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          protocolService: () => protocolServiceStub,
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(ProtocolUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.protocol = protocolSample;
        protocolServiceStub.update.resolves(protocolSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(protocolServiceStub.update.calledWith(protocolSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        protocolServiceStub.create.resolves(entity);
        const wrapper = shallowMount(ProtocolUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.protocol = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(protocolServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        protocolServiceStub.find.resolves(protocolSample);
        protocolServiceStub.retrieve.resolves([protocolSample]);

        // WHEN
        route = {
          params: {
            protocolId: '' + protocolSample.id,
          },
        };
        const wrapper = shallowMount(ProtocolUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.protocol).toMatchObject(protocolSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        protocolServiceStub.find.resolves(protocolSample);
        const wrapper = shallowMount(ProtocolUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
